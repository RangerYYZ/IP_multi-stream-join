package IP.vertex_datastructure;

import IP.sourceAndBasicdataClass.record;

import java.util.ArrayList;

public class I_subtable {
    public ArrayList<I_atom> subtable;

    public I_subtable() {
        this.subtable = new ArrayList<>();
    }

    // add a new tuple into the subtable as the given active value
    public void add(record t, boolean active){
        I_atom ti_atom = new I_atom(t, active);
        subtable.add(ti_atom);
    }

    // getter and setter
    public ArrayList<I_atom> getSubtable() {
        return subtable;
    }

    public void setSubtable(ArrayList<I_atom> subtable) {
        this.subtable = subtable;
    }

}
