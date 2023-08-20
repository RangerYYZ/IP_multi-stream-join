package IP;

import IP.sourceAndBasicdataClass.*;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

public class original_JOIN {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        SingleOutputStreamOperator<customer> customerStream = env.addSource(new customerSource2())
                .assignTimestampsAndWatermarks(WatermarkStrategy.<customer>forMonotonousTimestamps().withTimestampAssigner(
                        new SerializableTimestampAssigner<customer>() {
                            @Override
                            public long extractTimestamp(customer customer, long l) {
                                return System.currentTimeMillis();
                            }
                        }
                ));

        SingleOutputStreamOperator<order> orderStream = env.addSource(new orderSource2())
                .assignTimestampsAndWatermarks(WatermarkStrategy.<order>forMonotonousTimestamps().withTimestampAssigner(
                        new SerializableTimestampAssigner<order>() {
                            @Override
                            public long extractTimestamp(order order, long l) {
                                return System.currentTimeMillis();
                            }
                        }
                ));

        SingleOutputStreamOperator<nation> nationStream = env.addSource(new nationSource2())
                .assignTimestampsAndWatermarks(WatermarkStrategy.<nation>forMonotonousTimestamps().withTimestampAssigner(
                        new SerializableTimestampAssigner<nation>() {
                            @Override
                            public long extractTimestamp(nation nation, long l) {
                                return System.currentTimeMillis();
                            }
                        }
                ));
        SingleOutputStreamOperator<lineitem> lineitemStream = env.addSource(new lineitemSource2())
                .assignTimestampsAndWatermarks(WatermarkStrategy.<lineitem>forMonotonousTimestamps().withTimestampAssigner(
                        new SerializableTimestampAssigner<lineitem>() {
                            @Override
                            public long extractTimestamp(lineitem lineitem, long l) {
                                return System.currentTimeMillis();
                            }
                        }
                ));


        Calendar calendar = Calendar.getInstance();
        calendar.set(1993,Calendar.NOVEMBER,6);
        Date order_startDate = calendar.getTime();
        calendar.add(Calendar.MONTH, 3);
        Date order_endDate = calendar.getTime();

        SingleOutputStreamOperator<order> order_filteredStream = orderStream.filter(new FilterFunction<order>() {
            @Override
            public boolean filter(order order) throws Exception {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date orderDate = sdf.parse(order.o_orderdate);
                return orderDate.compareTo(order_startDate) >= 0 && orderDate.compareTo(order_endDate) < 0 && Integer.parseInt(order.o_orderkey) < 1000000;
            }
        });

        SingleOutputStreamOperator<lineitem> lineitem_filteredStream = lineitemStream.filter(new FilterFunction<lineitem>() {
            @Override
            public boolean filter(lineitem lineitem) throws Exception {
                return lineitem.l_returnflag.equals("R") && Integer.parseInt(lineitem.l_orderkey) < 1000000;
            }
        });

        DataStream<customer_order_oncustkey> customer_order_joinedStream = customerStream.join(order_filteredStream)
                .where(s1 -> s1.c_custkey)
                .equalTo(s2 -> s2.o_custkey)
                .window(TumblingEventTimeWindows.of(Time.seconds(5000)))
                .apply(new JoinFunction<customer, order, customer_order_oncustkey>() {
                    @Override
                    public customer_order_oncustkey join(customer customer, order order) throws Exception {
                        return new customer_order_oncustkey(customer, order);
                    }
                });


        DataStream<customer_order_oncustkey> cust_order_nation_joinedStream = customer_order_joinedStream.join(nationStream)
                .where(s1 -> s1.c_nationkey)
                .equalTo(s2 -> s2.n_nationkey)
                .window(TumblingEventTimeWindows.of(Time.seconds(5000)))
                .apply(new JoinFunction<customer_order_oncustkey, nation, customer_order_oncustkey>() {
                    @Override
                    public customer_order_oncustkey join(customer_order_oncustkey customer_order_oncustkey, nation nation) throws Exception {
                        return new customer_order_oncustkey(customer_order_oncustkey, nation);
                    }
                });


        DataStream<all_joined> all_joinedDataStream = cust_order_nation_joinedStream.join(lineitem_filteredStream)
                .where(s1 -> s1.o_orderkey)
                .equalTo(s2 -> s2.l_orderkey)
                .window(TumblingEventTimeWindows.of(Time.seconds(5000)))
                .apply(new JoinFunction<customer_order_oncustkey, lineitem, all_joined>() {
                    @Override
                    public all_joined join(customer_order_oncustkey customer_order_oncustkey, lineitem lineitem) throws Exception {
                        return new all_joined(customer_order_oncustkey, lineitem);
                    }
                });


        SingleOutputStreamOperator<result_form> alljoined_Calculated = all_joinedDataStream.map(new MapFunction<all_joined, result_form>() {
            @Override
            public result_form map(all_joined all_joined) throws Exception {
                return new result_form(all_joined,
                        Float.parseFloat(all_joined.l_extendedprice) * (1 - Float.parseFloat(all_joined.l_discount)));
            }
        });

        SingleOutputStreamOperator<result_form> reduce = alljoined_Calculated.keyBy(
                s1 -> s1.c_custkey + s1.c_name + s1.c_acctbal + s1.n_name + s1.c_address + s1.c_phone
        ).reduce(new ReduceFunction<result_form>() {
            @Override
            public result_form reduce(result_form result_form, result_form t1) throws Exception {
                result_form.revenue = result_form.revenue + t1.revenue;
                return result_form;
            }
        });


        reduce.print();

        env.execute();


    }
}
