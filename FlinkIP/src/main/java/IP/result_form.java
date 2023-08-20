package IP;

public class result_form {
    public String c_custkey;
    public String c_name;
    public Float revenue;
    public String c_acctbal;
    public String n_name;
    public String c_address;
    public String c_phone;
    public String c_comment;

    public result_form() {
    }

    public result_form(String c_custkey, String c_name, Float revenue, String c_acctbal, String n_name, String c_address, String c_phone, String c_comment) {
        this.c_custkey = c_custkey;
        this.c_name = c_name;
        this.revenue = revenue;
        this.c_acctbal = c_acctbal;
        this.n_name = n_name;
        this.c_address = c_address;
        this.c_phone = c_phone;
        this.c_comment = c_comment;
    }

    public result_form(all_joined all_joined, Float revenue) {
        this.c_custkey = all_joined.c_custkey;
        this.c_name = all_joined.c_name;
        this.revenue = revenue;
        this.c_acctbal = all_joined.c_acctbal;
        this.n_name = all_joined.n_name;
        this.c_address = all_joined.c_address;
        this.c_phone = all_joined.c_phone;
        this.c_comment = all_joined.c_comment;
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

    public Float getRevenue() {
        return revenue;
    }

    public void setRevenue(Float revenue) {
        this.revenue = revenue;
    }

    public String getC_acctbal() {
        return c_acctbal;
    }

    public void setC_acctbal(String c_acctbal) {
        this.c_acctbal = c_acctbal;
    }

    public String getN_name() {
        return n_name;
    }

    public void setN_name(String n_name) {
        this.n_name = n_name;
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

    @Override
    public String toString() {
        return "result_form{" +
                "c_custkey='" + c_custkey + '\'' +
                ", c_name='" + c_name + '\'' +
                ", revenue='" + revenue.toString() + '\'' +
                ", c_acctbal='" + c_acctbal + '\'' +
                ", n_name='" + n_name + '\'' +
                ", c_address='" + c_address + '\'' +
                ", c_phone='" + c_phone + '\'' +
                ", c_comment='" + '\'' +
                '}';
    }
}
