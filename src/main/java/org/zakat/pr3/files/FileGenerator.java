package org.zakat.pr3.files;

import io.reactivex.rxjava3.core.Observable;
import org.zakat.pr1.files.File;
import org.zakat.pr1.files.FileType;

import java.util.Random;

public class FileGenerator {
    private final Random random;
    private final FileType[] fileTypes = new FileType[]{
            FileType.XML,
            FileType.XLS,
            FileType.JSON
    };

    public FileGenerator() {
        this.random = new Random();
    }

    public Observable<File> generateFiles() {
        return Observable
                .create(emitter -> {
                    while (!emitter.isDisposed()) {
                        Thread.sleep(1000);
                        FileType type = fileTypes[random.nextInt(fileTypes.length)];
                        File file = new File(type, random.nextInt(10, 101));
                        emitter.onNext(file);
                    }
                    emitter.onComplete();
                });
    }
}
