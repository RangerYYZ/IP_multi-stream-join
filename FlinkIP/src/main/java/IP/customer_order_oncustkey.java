package IP;

import IP.sourceAndBasicdataClass.customer;
import IP.sourceAndBasicdataClass.nation;
import IP.sourceAndBasicdataClass.order;

public class customer_order_oncustkey {

    public String c_custkey;
    public String c_name;
    public String c_address;
    public String c_nationkey;
    public String c_phone;
    public String c_acctbal;
    public String c_comment;
    public String o_orderkey;
    public String n_name;

    public customer_order_oncustkey() {
    }

    public customer_order_oncustkey(customer customer, order order) {
        this.c_custkey = customer.c_custkey;
        this.c_name = customer.c_name;
        this.c_address = customer.c_address;
        this.c_nationkey = customer.c_nationkey;
        this.c_phone = customer.c_phone;
        this.c_acctbal = customer.c_acctbal;
        this.c_comment = customer.c_comment;
        this.o_orderkey = order.o_orderkey;
        this.n_name = null;
    }

    public customer_order_oncustkey(customer customer, order order, nation nation) {
        this.c_custkey = customer.c_custkey;
        this.c_name = customer.c_name;
        this.c_address = customer.c_address;
        this.c_nationkey = customer.c_nationkey;
        this.c_phone = customer.c_phone;
        this.c_acctbal = customer.c_acctbal;
        this.c_comment = customer.c_comment;
        this.o_orderkey = order.o_orderkey;
        this.n_name = nation.n_name;
    }

    public customer_order_oncustkey(customer_order_oncustkey customer, nation nation) {
        this.c_custkey = customer.c_custkey;
        this.c_name = customer.c_name;
        this.c_address = customer.c_address;
        this.c_nationkey = customer.c_nationkey;
        this.c_phone = customer.c_phone;
        this.c_acctbal = customer.c_acctbal;
        this.c_comment = customer.c_comment;
        this.o_orderkey = customer.o_orderkey;
        this.n_name = nation.n_name;
    }

    public String getN_name() {
        return n_name;
    }

    public void setN_name(String n_name) {
        this.n_name = n_name;
    }

    public String getC_custkey() {
        return c_custkey;
    }

    public void setC_custkey(String c_custkey) {
        this.c_custkey = c_custkey;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getC_address() {
        return c_address;
    }

    public void setC_address(String c_address) {
        this.c_address = c_address;
    }

    public String getC_nationkey() {
        return c_nationkey;
    }

    public void setC_nationkey(String c_nationkey) {
        this.c_nationkey = c_nationkey;
    }

    public String getC_phone() {
        return c_phone;
    }

    public void setC_phone(String c_phone) {
        this.c_phone = c_phone;
    }

    public String getC_acctbal() {
        return c_acctbal;
    }

    public void setC_acctbal(String c_acctbal) {
        this.c_acctbal = c_acctbal;
    }

    public String getC_comment() {
        return c_comment;
    }

    public void setC_comment(String c_comment) {
        this.c_comment = c_comment;
    }

    public String getO_orderkey() {
        return o_orderkey;
    }

    public void setO_orderkey(String o_orderkey) {
        this.o_orderkey = o_orderkey;
    }

    @Override
    public String toString() {
        return "customer_order_oncustkey{" +
                "c_custkey='" + c_custkey + '\'' +
                ", c_name='" + c_name + '\'' +
                ", c_address='" + c_address + '\'' +
                ", c_nationkey='" + c_nationkey + '\'' +
                ", c_phone='" + c_phone + '\'' +
                ", c_acctbal='" + c_acctbal + '\'' +
                ", c_comment='" + '\'' +
                ", o_orderkey='" + o_orderkey + '\'' +
                ", n_name='" + n_name + '\'' +
                '}';
    }
}
