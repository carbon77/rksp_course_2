package org.zakat.pr3.files;
    
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.zakat.pr1.files.File;
import org.zakat.pr1.files.FileType;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class FileApp {
    public static void main(String[] args) {
        BlockingQueue<File> queue = new LinkedBlockingQueue<>();
        FileGenerator generator = new FileGenerator();
        FileConsumer xmlConsumer = new FileConsumer(FileType.XML);
        FileConsumer xlsConsumer = new FileConsumer(FileType.XLS);
        FileConsumer jsonConsumer = new FileConsumer(FileType.JSON);

        generator
                .generateFiles()
                .subscribeOn(Schedulers.newThread())
                .doOnNext(file -> {
                    System.out.println("Added to queue: " + file);
                    queue.add(file);
                })
                .subscribe();

        Observable
                .interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .doOnNext(tick -> {
                    try {
                        File file = queue.poll(1, TimeUnit.SECONDS);

                        if (file != null) {
                            switch (Objects.requireNonNull(file).type()) {
                                case XML -> xmlConsumer.processFile(file);
                                case JSON -> jsonConsumer.processFile(file);
                                case XLS -> xlsConsumer.processFile(file);
                            }
                        }

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                })
                .subscribe();

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(30));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
