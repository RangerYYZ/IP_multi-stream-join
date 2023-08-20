package IP.sourceAndBasicdataClass;

public class lineitem extends record{

    public String l_orderkey;
    public String l_extendedprice;
    public String l_discount;
    public String l_returnflag;
    // public String primaryKey;
    // public String foreignKey;
    // public String t_name;

    public lineitem() {
    }

    public lineitem(String l_orderkey, String l_extendedprice, String l_discount, String l_returnflag) {
        this.l_orderkey = l_orderkey;
        this.l_extendedprice = l_extendedprice;
        this.l_discount = l_discount;
        this.l_returnflag = l_returnflag;
        this.primaryKey = "primary";
        this.foreignKey = l_orderkey;
        this.t_name = "lineitem";
    }

    public String getL_orderkey() {
        return l_orderkey;
    }

    public void setL_orderkey(String l_orderkey) {
        this.l_orderkey = l_orderkey;
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

    public String getL_returnflag() {
        return l_returnflag;
    }

    public void setL_returnflag(String l_returnflag) {
        this.l_returnflag = l_returnflag;
    }

    @Override
    public String toString() {
        return "lineitem{" +
                "l_orderkey='" + l_orderkey + '\'' +
                ", l_extendedprice='" + l_extendedprice + '\'' +
                ", l_discount='" + l_discount + '\'' +
                ", l_returnflag='" + l_returnflag + '\'' +
                '}';
    }
}
