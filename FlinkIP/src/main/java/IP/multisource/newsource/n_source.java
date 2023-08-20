package IP.multisource.newsource;

import IP.sourceAndBasicdataClass.nation;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Random;

public class n_source implements SourceFunction<nation> {
    private Boolean running = true;
    @Override
    public void run(SourceContext<nation> sourceContext) throws Exception {
        String[] n_nationkeys = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17"};
        String[] n_names = {"a", "b", "c", "d"};
        Random random = new Random();
        while (running) {
            sourceContext.collect(new nation(n_nationkeys[random.nextInt(n_nationkeys.length)], n_names[random.nextInt(n_names.length)]));
            Thread.sleep(500);
        }
    }

    @Override
    public void cancel() {
        running = false;
    }
}
