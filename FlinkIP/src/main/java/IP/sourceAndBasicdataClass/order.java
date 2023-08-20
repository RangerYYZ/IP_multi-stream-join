package IP.sourceAndBasicdataClass;

public class order extends record{
    public String o_orderkey;
    public String o_custkey;
    public String o_orderdate;
//    public String primaryKey;
//    public String foreignKey;
//    public String t_name;

    public order() {
    }

    public order(String o_orderkey, String o_custkey, String o_orderdate) {
        this.o_orderkey = o_orderkey;
        this.o_custkey = o_custkey;
        this.o_orderdate = o_orderdate;
        this.primaryKey = o_orderkey;
        this.foreignKey = o_custkey;
        this.t_name = "order";
    }

    public String getO_orderkey() {
        return o_orderkey;
    }

    public void setO_orderkey(String o_orderkey) {
        this.o_orderkey = o_orderkey;
    }

    public String getO_custkey() {
        return o_custkey;
    }

    public void setO_custkey(String o_custkey) {
        this.o_custkey = o_custkey;
    }

    public String getO_orderdate() {
        return o_orderdate;
    }

    public void setO_orderdate(String o_orderdate) {
        this.o_orderdate = o_orderdate;
    }

    @Override
    public String toString() {
        return "order{" +
                "o_orderkey='" + o_orderkey + '\'' +
                ", o_custkey='" + o_custkey + '\'' +
                ", o_orderdate='" + o_orderdate + '\'' +
                '}';
    }
}
