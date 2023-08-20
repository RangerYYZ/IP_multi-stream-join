package IP.sourceAndBasicdataClass;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class lineitemSource2 extends RichSourceFunction<lineitem>{
    PreparedStatement ps;
    private Connection connection;

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        connection = getConnection();
        String sql = "select L_ORDERKEY,L_EXTENDEDPRICE,L_DISCOUNT,L_RETURNFLAG from lineitem";
        ps = this.connection.prepareStatement(sql);
    }

    @Override
    public void close() throws Exception {
        super.close();
        if (connection != null){
            connection.close();
        }
        if (ps != null){
            ps.close();
        }
    }

    @Override
    public void run(SourceContext<lineitem> sourceContext) throws Exception {
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()){
            if(Integer.parseInt(resultSet.getString("L_ORDERKEY")) < 1000000){
                lineitem lineitem = new lineitem(
                        resultSet.getString("L_ORDERKEY"),
                        resultSet.getString("L_EXTENDEDPRICE"),
                        resultSet.getString("L_DISCOUNT"),
                        resultSet.getString("L_RETURNFLAG")
                );
                sourceContext.collect(lineitem);
            }
        }
    }

    @Override
    public void cancel() {

    }

    public static Connection getConnection(){
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tpch", "root", "yyzmysql");
        } catch (Exception e) {
            System.out.println("-----------mysql get connection has exception , msg = "+ e.getMessage());
        }
        return con;
    }

}
