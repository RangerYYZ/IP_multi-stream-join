package IP.sourceAndBasicdataClass;

public class customer extends record{

    public String c_custkey;
    public String c_name;
    public String c_address;
    public String c_nationkey;
    public String c_phone;
    public String c_acctbal;
    public String c_comment;
//    public String primaryKey;
//    public String foreignKey;
//    public String t_name;

    public customer() {
    }


    public customer(String c_custkey, String c_name, String c_address, String c_nationkey, String c_phone, String c_acctbal, String c_comment) {
        this.c_custkey = c_custkey;
        this.c_name = c_name;
        this.c_address = c_address;
        this.c_nationkey = c_nationkey;
        this.c_phone = c_phone;
        this.c_acctbal = c_acctbal;
        this.c_comment = c_comment;
        this.primaryKey = c_custkey;
        this.foreignKey = c_nationkey;
        this.t_name = "customer";
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

    public String getC_acctbal() {
        return c_acctbal;
    }

    public void setC_acctbal(String c_acctbal) {
        this.c_acctbal = c_acctbal;
    }

    public String getC_address() {
        return c_address;
    }

    public void setC_address(String c_address) {
        this.c_address = c_address;
    }

    public String getC_phone() {
        return c_phone;
    }

    public void setC_phone(String c_phone) {
        this.c_phone = c_phone;
    }

    public String getC_comment() {
        return c_comment;
    }

    public void setC_comment(String c_comment) {
        this.c_comment = c_comment;
    }

    public String getC_nationkey() {
        return c_nationkey;
    }

    public void setC_nationkey(String c_nationkey) {
        this.c_nationkey = c_nationkey;
    }

    @Override
    public String toString() {
        return "customer{" +
                "custkey='" + c_custkey + '\'' +
                ", name='" + c_name + '\'' +
                ", address='" + c_address + '\'' +
                ", nationkey='" + c_nationkey + '\'' +
                ", phone='" + c_phone + '\'' +
                ", acctbal='" + c_acctbal + '\'' +
                ", comment='" + c_comment;

    }
}
