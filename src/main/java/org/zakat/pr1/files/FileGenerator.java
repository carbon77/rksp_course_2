package org.zakat.pr1.files;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class FileGenerator implements Runnable {
    private final BlockingQueue<File> queue;
    private final Random random = new Random();
    private final FileType[] types = new FileType[] {
      FileType.XML, FileType.JSON, FileType.XLS
    };

    public FileGenerator(BlockingQueue<File> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                FileType type = types[random.nextInt(types.length)];
                int size = 10 + random.nextInt(91);
                File file = new File(type, size);

                TimeUnit.MILLISECONDS.sleep(100 + random.nextInt(901));

                queue.put(file);
                System.out.printf("Generated %s\n", file);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
