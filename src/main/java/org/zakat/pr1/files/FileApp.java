package org.zakat.pr1.files;

import java.util.concurrent.*;

public class FileApp {
    public static void main(String[] args) {
        BlockingQueue<File> queue = new LinkedBlockingQueue<>();

        ExecutorService generatorExecutor = Executors.newSingleThreadExecutor();
        ExecutorService consumerExecutor = Executors.newFixedThreadPool(3);

        FileGenerator fileGenerator = new FileGenerator(queue);
        generatorExecutor.submit(fileGenerator);

        FileConsumer xmlConsumer = new FileConsumer(queue, FileType.XML);
        FileConsumer jsonConsumer = new FileConsumer(queue, FileType.JSON);
        FileConsumer xlsConsumer = new FileConsumer(queue, FileType.XLS);

        consumerExecutor.submit(xmlConsumer);
        consumerExecutor.submit(jsonConsumer);
        consumerExecutor.submit(xlsConsumer);

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(1));
            generatorExecutor.shutdown();
            consumerExecutor.shutdown();
            generatorExecutor.awaitTermination(1, TimeUnit.SECONDS);
            consumerExecutor.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
