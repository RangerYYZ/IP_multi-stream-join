package IP.sourceAndBasicdataClass;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class orderSource extends RichSourceFunction<order>{
    PreparedStatement ps;
    private Connection connection;

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        connection = getConnection();
        String sql = "select * from orders";
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
    public void run(SourceContext<order> sourceContext) throws Exception {
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()){
            if(Integer.parseInt(resultSet.getString("O_ORDERKEY")) < 1000000) {
                order order = new order(
                        resultSet.getString("O_ORDERKEY"),
                        resultSet.getString("O_CUSTKEY"),
                        resultSet.getString("O_ORDERDATE")
                );
                sourceContext.collect(order);
                Thread.sleep(2);
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
