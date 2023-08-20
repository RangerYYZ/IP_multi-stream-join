package IP.vertex_datastructure;

import IP.sourceAndBasicdataClass.joinedRecord;
import IP.sourceAndBasicdataClass.record;
import org.apache.flink.api.java.tuple.Tuple2;
import scala.Int;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class DAGVertex implements java.io.Serializable{
    public HashMap<String, I_subtable> I_table;
    public HashMap<String, Integer> Ic_table;
    public boolean isleaf;
    public boolean isroot;
    public DAGVertex child;
    public DAGVertex parent;
    public HashMap<String, ArrayList<joinedRecord>> joinresult;

    public DAGVertex(boolean isleaf, boolean isroot) {
        this.isleaf = isleaf;
        this.isroot = isroot;
        this.I_table = new HashMap<>();
        this.Ic_table = new HashMap<>();
        this.joinresult = new HashMap<>();
    }

    public void add_to_I_table(String foreignKey, record t, boolean active) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (I_table.containsKey(foreignKey)){
            I_subtable I_subtable = I_table.get(foreignKey);
            I_subtable.add(t, active);
            I_table.put(foreignKey, I_subtable);
        }else {
            I_subtable newI_subtable = new I_subtable();
            newI_subtable.add(t, active);
            I_table.put(foreignKey, newI_subtable);
        }
    }

    public void set_I_ctable(String foreignKey, Integer st){
        Ic_table.put(foreignKey, st);
    }

    public void delete_from_I_table(){};



    public void insert(record t) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String fk = t.foreignKey;
        String pk = t.primaryKey;

        // for non leaf element, check active
        if (!this.isleaf) {
            if (Ic_table.containsKey(fk)) {
                if (Ic_table.get(fk) == 1) {
                    // if active , insert a new active element and update to parent
                    add_to_I_table(fk, t, true);

                    if(this.child.joinresult.get(fk) != null) {
                        ArrayList<joinedRecord> joinedRecords = new ArrayList<>(this.child.joinresult.get(fk));
                        //////////////////////////////////////////////
                        for (joinedRecord joinedRecord : joinedRecords) {
                            joinedRecord.content.put(t.t_name, t);
                            add_to_joinresult(joinedRecord, pk);
                            InsertUpdate(this, pk, joinedRecord);
                        }
                    }
                } else {
                    // not active, simply add the element as non-active and quit
                    add_to_I_table(fk, t, false);
                }
            }else{
                add_to_I_table(fk, t, false);
            }
        }

        //for leaf vertex, set active and update
        if (this.isleaf){
            add_to_I_table("leaf", t, true);
            joinedRecord joinedRecord = new joinedRecord();
            joinedRecord.content.put(t.t_name, t);
            this.add_to_joinresult(joinedRecord, pk);
            InsertUpdate(this, pk, joinedRecord);
        }
    }


    public void InsertUpdate(DAGVertex v, String pk, joinedRecord joinedtuple){

        if (v.isroot){
            // pop the result and quit
            // get joined result
//            joinedRecord joinedRecord = new joinedRecord(joinedtuple);
//            this.add_to_joinresult(joinedRecord, pk);
        }else{
            //update the result

            //1.update the ic table of the target(parent)
            DAGVertex target = v.parent;
            target.Ic_table.put(pk, 1);

            //2.subtable
            if (target.I_table.containsKey(pk)) {
                for (I_atom i_atom : target.I_table.get(pk).subtable) {
                    // set active for all element in subtable
                    i_atom.active = true;
                    // get joined result
                    joinedRecord joinedRecord = new joinedRecord(joinedtuple);
                    joinedRecord.content.put(i_atom.record.t_name, i_atom.record);
                    // for all element in subtable, continue to set active for the parent
                    String pk2 = i_atom.record.primaryKey;
                    target.add_to_joinresult(joinedRecord, pk2);
                    InsertUpdate(target, pk2, joinedRecord);
                }
            }

        }
    }

    public void add_to_joinresult(joinedRecord joinedRecord, String pk){
        if (!joinresult.containsKey(pk)) {
            ArrayList<joinedRecord> joinedRecords = new ArrayList<>();
            joinedRecords.add(joinedRecord);
            joinresult.put(pk, joinedRecords);
        }else{
            ArrayList<joinedRecord> joinedRecords = joinresult.get(pk);
            joinedRecords.add(joinedRecord);
            joinresult.put(pk, joinedRecords);
        }
    }



}
