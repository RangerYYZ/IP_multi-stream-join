package IP.multisource;

public class n_c_o_JoinedOn_custkey {
    public String o_orderkey;
    public String n_name;
    public String c_custkey;
    public String c_name;
    public String c_address;
    public String c_phone;
    public String c_acctbal;

    public n_c_o_JoinedOn_custkey() {
    }

    public n_c_o_JoinedOn_custkey(String o_orderkey, String n_name, String c_custkey, String c_name, String c_address, String c_phone, String c_acctbal) {
        this.o_orderkey = o_orderkey;
        this.n_name = n_name;
        this.c_custkey = c_custkey;
        this.c_name = c_name;
        this.c_address = c_address;
        this.c_phone = c_phone;
        this.c_acctbal = c_acctbal;
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

    @Override
    public String toString() {
        return "n_c_o_JoinedOn_custkey{" +
                "o_orderkey='" + o_orderkey + '\'' +
                ", n_name='" + n_name + '\'' +
                ", c_custkey='" + c_custkey + '\'' +
                ", c_name='" + c_name + '\'' +
                ", c_address='" + c_address + '\'' +
                ", c_phone='" + c_phone + '\'' +
                ", c_acctbal='" + c_acctbal + '\'' +
                '}';
    }
}
