package IP;

import IP.sourceAndBasicdataClass.customer;
import IP.sourceAndBasicdataClass.lineitem;
import IP.sourceAndBasicdataClass.nation;
import IP.sourceAndBasicdataClass.order;
import IP.vertex_datastructure.DAGVertex;

import java.lang.reflect.InvocationTargetException;

public class test2 {
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        // build the DAG
        DAGVertex lineitemV = new DAGVertex(false, true);
        DAGVertex orderV = new DAGVertex(false, false);
        DAGVertex customerV = new DAGVertex(false,false);
        DAGVertex nationV = new DAGVertex(true,false);

        lineitemV.child = orderV;
        orderV.child = customerV;
        customerV.child = nationV;

        nationV.parent = customerV;
        customerV.parent = orderV;
        orderV.parent = lineitemV;
        // graph is: lineitem(root) -> order -> customer -> nation

        lineitem testlineitem = new lineitem("orderkey", "exprice", "discount", "r");
        order testorder = new order("orderkey","custkey","orderdate");
        customer testcustomer = new customer("custkey","name","address","nationkey","","phone","comment");
        nation testnation = new nation("nationkey","name");

        lineitem testlineitem2 = new lineitem("orderkey2", "exprice2", "discount2", "r2");
        order testorder2 = new order("orderkey2","custkey2","orderdate2");
        customer testcustomer2 = new customer("custkey2","name2","address2","nationkey2","","phone2","comment2");
        nation testnation2 = new nation("nationkey2","name2");

        lineitemV.insert(testlineitem);
        orderV.insert(testorder);
        nationV.insert(testnation);
        customerV.insert(testcustomer);

        customerV.insert(testcustomer2);



        //lineitemV.insert(testlineitem);
        System.out.println(lineitemV.joinresult.get("primary").toString());

    }
}
