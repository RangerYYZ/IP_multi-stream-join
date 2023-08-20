package IP;

import IP.sourceAndBasicdataClass.lineitem;

public class all_joined {
    public String c_custkey;
    public String c_name;
    public String c_address;
    public String c_nationkey;
    public String c_phone;
    public String c_acctbal;
    public String c_comment;
    public String o_orderkey;
    public String n_name;
    public String l_extendedprice;
    public String l_discount;

    public all_joined() {
    }

    public all_joined(customer_order_oncustkey customer_order_oncustkey, lineitem lineitem) {
        this.c_custkey = customer_order_oncustkey.c_custkey;
        this.c_name = customer_order_oncustkey.c_name;
        this.c_address = customer_order_oncustkey.c_address;
        this.c_nationkey = customer_order_oncustkey.c_nationkey;
        this.c_phone = customer_order_oncustkey.c_phone;
        this.c_acctbal = customer_order_oncustkey.c_acctbal;
        this.c_comment = customer_order_oncustkey.c_comment;
        this.o_orderkey = customer_order_oncustkey.o_orderkey;
        this.n_name = customer_order_oncustkey.n_name;
        this.l_extendedprice = lineitem.l_extendedprice;
        this.l_discount = lineitem.l_discount;
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

    public String getN_name() {
        return n_name;
    }

    public void setN_name(String n_name) {
        this.n_name = n_name;
    }

    public String getL_extendedprice() {
        return l_extendedprice;
    }

    public void setL_extendedprice(String l_extendedprice) {
        this.l_extendedprice = l_extendedprice;
    }

    public String getL_discount() {
        return l_discount;
    }

    public void setL_discount(String l_discount) {
        this.l_discount = l_discount;
    }

    @Override
    public String toString() {
        return "all_joined{" +
                "c_custkey='" + c_custkey + '\'' +
                ", c_name='" + c_name + '\'' +
                ", c_address='" + c_address + '\'' +
                ", c_phone='" + c_phone + '\'' +
                ", c_acctbal='" + c_acctbal + '\'' +
                ", o_orderkey='" + o_orderkey + '\'' +
                ", n_name='" + n_name + '\'' +
                ", l_extendedprice='" + l_extendedprice + '\'' +
                ", l_discount='" + l_discount + '\'' +
                '}';
    }
}
