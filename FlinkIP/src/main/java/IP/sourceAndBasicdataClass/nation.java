package IP.sourceAndBasicdataClass;

public class nation extends record{
    public String n_nationkey;
    public String n_name;
//    public String primaryKey;
//    public String foreignKey;
//    public String t_name;

    public nation() {
    }

    public nation(String n_nationkey, String n_name) {
        this.n_nationkey = n_nationkey;
        this.n_name = n_name;
        this.primaryKey = n_nationkey;
        this.foreignKey = "foreign";
        this.t_name = "nation";
    }

    public String getN_nationkey() {
        return n_nationkey;
    }

    public void setN_nationkey(String n_nationkey) {
        this.n_nationkey = n_nationkey;
    }

    public String getN_name() {
        return n_name;
    }

    public void setN_name(String n_name) {
        this.n_name = n_name;
    }

    @Override
    public String toString() {
        return "nation{" +
                "n_nationkey='" + n_nationkey + '\'' +
                ", n_name='" + n_name + '\'' +
                '}';
    }
}
