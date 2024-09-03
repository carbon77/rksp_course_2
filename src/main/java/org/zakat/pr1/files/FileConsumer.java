package org.zakat.pr1.files;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class FileConsumer implements Runnable {
    private final BlockingQueue<File> queue;
    private final FileType type;

    public FileConsumer(BlockingQueue<File> queue, FileType type) {
        this.queue = queue;
        this.type = type;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                File file = queue.poll(1, TimeUnit.SECONDS);
                if (file == null) {
                    continue;
                }

                if (file.type() == type) {
                    int size = file.size() * 7;
                    TimeUnit.MILLISECONDS.sleep(size);
                    System.out.printf("Handled %s\n", file);
                    continue;
                }

                queue.put(file);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
