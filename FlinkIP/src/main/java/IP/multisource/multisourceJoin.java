package IP.multisource;

import IP.all_joined;
import IP.customer_order_oncustkey;
import IP.multisource.newsource.n_source;
import IP.result_form;
import IP.sourceAndBasicdataClass.*;
import org.apache.commons.collections.iterators.ObjectArrayIterator;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.ObjectArrayTypeInfo;
import org.apache.flink.calcite.shaded.com.google.common.collect.ObjectArrays;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.util.IntArrayList;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.co.KeyedCoProcessFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.table.data.util.DataFormatConverters;
import org.apache.flink.util.Collector;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

public class multisourceJoin {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        SingleOutputStreamOperator<customer> customerStream = env.addSource(new customerSource())
                .assignTimestampsAndWatermarks(WatermarkStrategy.<customer>forMonotonousTimestamps().withTimestampAssigner(
                        new SerializableTimestampAssigner<customer>() {
                            @Override
                            public long extractTimestamp(customer customer, long l) {
                                return System.currentTimeMillis();
                            }
                        }
                ));
        SingleOutputStreamOperator<order> orderStream = env.addSource(new orderSource())
                .assignTimestampsAndWatermarks(WatermarkStrategy.<order>forMonotonousTimestamps().withTimestampAssigner(
                        new SerializableTimestampAssigner<order>() {
                            @Override
                            public long extractTimestamp(order order, long l) {
                                return System.currentTimeMillis();
                            }
                        }
                ));
        SingleOutputStreamOperator<nation> nationStream = env.addSource(new n_source())
                .assignTimestampsAndWatermarks(WatermarkStrategy.<nation>forMonotonousTimestamps().withTimestampAssigner(
                        new SerializableTimestampAssigner<nation>() {
                            @Override
                            public long extractTimestamp(nation nation, long l) {
                                return System.currentTimeMillis();
                            }
                        }
                ));
        SingleOutputStreamOperator<lineitem> lineitemStream = env.addSource(new lineitemSource())
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


        // get keyby stream
        KeyedStream<customer, String> customer_KeyedStream = customerStream.keyBy(s1 -> s1.c_nationkey);
        KeyedStream<nation, String> nation_KeyedStream = nationStream.keyBy(s1 -> s1.n_nationkey);

        // connect the two stream to share the state
        ConnectedStreams<nation, customer> nation_customer_ConnectedStream = nation_KeyedStream.connect(customer_KeyedStream);
        SingleOutputStreamOperator<joined_on_nationkey> nation_customer_update_result = nation_customer_ConnectedStream.process(new KeyedCoProcessFunction<String, nation, customer, joined_on_nationkey>() {

            private MapState<String, String> itemMap = null;
            private MapState<String, Integer> countMap = null;

            @Override
            public void open(Configuration parameters) throws Exception {
                MapStateDescriptor<String, String> stringStringMapStateDescriptor = new MapStateDescriptor<>(
                        "customer_state",
                        TypeInformation.of(String.class),
                        TypeInformation.of(String.class)
                );
                stringStringMapStateDescriptor.enableTimeToLive(StateTtlConfig.newBuilder(Time.days(2)).build());
                itemMap = getRuntimeContext().getMapState(stringStringMapStateDescriptor);

                MapStateDescriptor<String, Integer> stringIntegerMapStateDescriptor = new MapStateDescriptor<>(
                        "count_state",
                        TypeInformation.of(String.class),
                        TypeInformation.of(Integer.class)
                );
                stringIntegerMapStateDescriptor.enableTimeToLive(StateTtlConfig.newBuilder(Time.days(2)).build());
                countMap = getRuntimeContext().getMapState(stringIntegerMapStateDescriptor);
            }

            @Override
            public void processElement1(nation nation, Context context, Collector<joined_on_nationkey> collector) throws Exception {
                String nationkey = nation.n_nationkey;
                if (countMap.contains(nationkey)){
                    int old_count = countMap.get(nationkey);
                    old_count += 1;
                    countMap.put(nationkey, old_count);
                }else {
                    countMap.put(nationkey, 1);
                }

                if (itemMap.contains(nationkey)){
                    String custlist_string = itemMap.get(nationkey);
                    String[] custlists = custlist_string.split(";");
                    for (String cust_elements : custlists) {
                        String[] cust_element_list = cust_elements.split("`");
                        joined_on_nationkey joined_on_nationkey = new joined_on_nationkey(nation.n_name, cust_element_list[0], cust_element_list[1], cust_element_list[2], cust_element_list[3], cust_element_list[4]);
                        collector.collect(joined_on_nationkey);
                    }
                }
            }

            @Override
            public void processElement2(customer customer, Context context, Collector<joined_on_nationkey> collector) throws Exception {
                String c_nationKey = customer.c_nationkey;
                String content = customer.c_custkey + '`' + customer.c_name + '`' + customer.c_address + '`' + customer.c_phone + '`' + customer.c_acctbal;
                if (!itemMap.contains(c_nationKey)){
                    itemMap.put(c_nationKey, content);
                }
                if (itemMap.contains(c_nationKey)){
                    String old_content = itemMap.get(c_nationKey);
                    String new_content = old_content + ';' + content;
                    itemMap.put(c_nationKey, new_content);
                }
            }

        });

        // (nation + customer) + order connected stream
        KeyedStream<joined_on_nationkey, String> nation_customer_update_KeyedStream = nation_customer_update_result.keyBy(s1 -> s1.c_custkey);
        KeyedStream<order, String> orderStringKeyedStream = order_filteredStream.keyBy(s1 -> s1.o_custkey);
        ConnectedStreams<joined_on_nationkey, order> nation_customer_order_connectedStream = nation_customer_update_KeyedStream.connect(orderStringKeyedStream);
        SingleOutputStreamOperator<n_c_o_JoinedOn_custkey> n_c_o_update_stream = nation_customer_order_connectedStream.process(new KeyedCoProcessFunction<String, joined_on_nationkey, order, n_c_o_JoinedOn_custkey>() {

            private MapState<String, String> orderMap = null;
            private MapState<String, Integer> countMap = null;

            @Override
            public void open(Configuration parameters) throws Exception {
                MapStateDescriptor<String, String> stringStringMapStateDescriptor = new MapStateDescriptor<>(
                        "order_state",
                        TypeInformation.of(String.class),
                        TypeInformation.of(String.class)
                );
                stringStringMapStateDescriptor.enableTimeToLive(StateTtlConfig.newBuilder(Time.days(2)).build());
                orderMap = getRuntimeContext().getMapState(stringStringMapStateDescriptor);

                MapStateDescriptor<String, Integer> stringIntegerMapStateDescriptor = new MapStateDescriptor<>(
                        "count_state",
                        TypeInformation.of(String.class),
                        TypeInformation.of(Integer.class)
                );
                stringIntegerMapStateDescriptor.enableTimeToLive(StateTtlConfig.newBuilder(Time.days(2)).build());
                countMap = getRuntimeContext().getMapState(stringIntegerMapStateDescriptor);
            }

            @Override
            public void processElement1(joined_on_nationkey joined_on_nationkey, Context context, Collector<n_c_o_JoinedOn_custkey> collector) throws Exception {
                String custkey = joined_on_nationkey.c_custkey;
                if (countMap.contains(custkey)){
                    int old_count = countMap.get(custkey);
                    old_count += 1;
                    countMap.put(custkey, old_count);
                }else {
                    countMap.put(custkey, 1);
                }

                if (orderMap.contains(custkey)){
                    String corderlist_string = orderMap.get(custkey);
                    String[] orderlists = corderlist_string.split(";");
                    for (String order_element : orderlists) {
                        n_c_o_JoinedOn_custkey n_c_o_joinedOn_custkey = new n_c_o_JoinedOn_custkey(
                                order_element,
                                joined_on_nationkey.n_name,
                                joined_on_nationkey.c_custkey,
                                joined_on_nationkey.c_name,
                                joined_on_nationkey.c_address,
                                joined_on_nationkey.c_phone,
                                joined_on_nationkey.c_acctbal
                        );
                        collector.collect(n_c_o_joinedOn_custkey);
                    }
                }
            }

            @Override
            public void processElement2(order order, Context context, Collector<n_c_o_JoinedOn_custkey> collector) throws Exception {
                String o_custkey = order.o_custkey;
                String content = order.o_orderkey;
                if (!orderMap.contains(o_custkey)){
                    orderMap.put(o_custkey, content);
                }
                if (orderMap.contains(o_custkey)){
                    String old_content = orderMap.get(o_custkey);
                    String new_content = old_content + ';' + content;
                    orderMap.put(o_custkey, new_content);
                }
            }
        });


        // (nation + customer + order) + lineitem connected stream
        KeyedStream<n_c_o_JoinedOn_custkey, String> n_c_o_joinedOn_custkeyStringKeyedStream = n_c_o_update_stream.keyBy(s1 -> s1.o_orderkey);
        KeyedStream<lineitem, String> lineitem_KeyedStream = lineitem_filteredStream.keyBy(s1 -> s1.l_orderkey);
        ConnectedStreams<n_c_o_JoinedOn_custkey, lineitem> n_c_o_joinedOn_custkeylineitemConnectedStreams = n_c_o_joinedOn_custkeyStringKeyedStream.connect(lineitem_KeyedStream);
        SingleOutputStreamOperator<full_joined> fullJoinedStream = n_c_o_joinedOn_custkeylineitemConnectedStreams.process(new KeyedCoProcessFunction<String, n_c_o_JoinedOn_custkey, lineitem, full_joined>() {

            private MapState<String, String> lineMap = null;
            private MapState<String, Integer> countMap = null;

            @Override
            public void open(Configuration parameters) throws Exception {
                MapStateDescriptor<String, String> stringStringMapStateDescriptor = new MapStateDescriptor<>(
                        "order_state",
                        TypeInformation.of(String.class),
                        TypeInformation.of(String.class)
                );
                stringStringMapStateDescriptor.enableTimeToLive(StateTtlConfig.newBuilder(Time.days(2)).build());
                lineMap = getRuntimeContext().getMapState(stringStringMapStateDescriptor);

                MapStateDescriptor<String, Integer> stringIntegerMapStateDescriptor = new MapStateDescriptor<>(
                        "count_state",
                        TypeInformation.of(String.class),
                        TypeInformation.of(Integer.class)
                );
                stringIntegerMapStateDescriptor.enableTimeToLive(StateTtlConfig.newBuilder(Time.days(2)).build());
                countMap = getRuntimeContext().getMapState(stringIntegerMapStateDescriptor);
            }

            @Override
            public void processElement1(n_c_o_JoinedOn_custkey n_c_o_joinedOn_custkey, Context context, Collector<full_joined> collector) throws Exception {
                String orderkey = n_c_o_joinedOn_custkey.o_orderkey;
                if (countMap.contains(orderkey)){
                    int old_count = countMap.get(orderkey);
                    old_count += 1;
                    countMap.put(orderkey, old_count);
                }else {
                    countMap.put(orderkey, 1);
                }

                if (lineMap.contains(orderkey)){
                    String linelist_string = lineMap.get(orderkey);
                    String[] linelists = linelist_string.split(";");
                    for (String line_elements : linelists) {
                        String[] line_element_list = line_elements.split("`");
                        full_joined full_joined = new full_joined(
                                line_element_list[0],
                                line_element_list[1],
                                n_c_o_joinedOn_custkey.n_name,
                                n_c_o_joinedOn_custkey.c_custkey,
                                n_c_o_joinedOn_custkey.c_name,
                                n_c_o_joinedOn_custkey.c_address,
                                n_c_o_joinedOn_custkey.c_phone,
                                n_c_o_joinedOn_custkey.c_acctbal
                                );
                        collector.collect(full_joined);
                    }
                }

            }

            @Override
            public void processElement2(lineitem lineitem, Context context, Collector<full_joined> collector) throws Exception {
                String o_orderkey = lineitem.l_orderkey;
                String content = lineitem.l_extendedprice + '`' + lineitem.l_discount;
                if (!lineMap.contains(o_orderkey)){
                    lineMap.put(o_orderkey, content);
                }
                if (lineMap.contains(o_orderkey)){
                    String old_content = lineMap.get(o_orderkey);
                    String new_content = old_content + ';' + content;
                    lineMap.put(o_orderkey, new_content);
                }
            }
        });

        SingleOutputStreamOperator<full_joined_c> map_result = fullJoinedStream.map(new MapFunction<full_joined, full_joined_c>() {
            @Override
            public full_joined_c map(full_joined input) throws Exception {
                return new full_joined_c(
                        Float.parseFloat(input.l_extendedprice) * (1 - Float.parseFloat(input.l_discount)),
                        input.n_name,
                        input.c_custkey,
                        input.c_name,
                        input.c_address,
                        input.c_phone,
                        input.c_acctbal
                );
            }
        });

        SingleOutputStreamOperator<full_joined_c> agg_result = map_result.keyBy(
                s1 -> s1.c_custkey + s1.c_name + s1.c_acctbal + s1.n_name + s1.c_address + s1.c_phone
        ).reduce(new ReduceFunction<full_joined_c>() {
            @Override
            public full_joined_c reduce(full_joined_c full_joined_c, full_joined_c t1) throws Exception {
                full_joined_c.revenue = full_joined_c.revenue + t1.revenue;
                return full_joined_c;
            }
        });


        agg_result.print();


        env.execute();


    }
}
