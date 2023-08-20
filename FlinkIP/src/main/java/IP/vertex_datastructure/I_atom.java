package IP.vertex_datastructure;

import IP.sourceAndBasicdataClass.record;

import java.util.ArrayList;

public class I_atom {
    public record record;
    public boolean active;

    //constructor
    public I_atom(record record, boolean active) {
        this.record = record;
        this.active = active;
    }


    // getter and setter
    public record getSubtable() {
        return record;
    }

    public void setSubtable(record subtable) {
        record = subtable;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
