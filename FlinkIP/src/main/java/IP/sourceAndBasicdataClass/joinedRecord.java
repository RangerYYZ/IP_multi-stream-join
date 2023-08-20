package IP.sourceAndBasicdataClass;

import java.util.HashMap;

public class joinedRecord {
    public HashMap<String, record> content;

    public joinedRecord() {
        this.content = new HashMap<>();
    }

    public joinedRecord(joinedRecord joinedRecord){
        this.content = new HashMap<>(joinedRecord.content);
    }
}
