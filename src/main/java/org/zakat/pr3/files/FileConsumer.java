package org.zakat.pr3.files;

import org.zakat.pr1.files.File;
import org.zakat.pr1.files.FileType;

import java.util.concurrent.TimeUnit;

public class FileConsumer {
    private final FileType type;

    public FileConsumer(FileType type) {
        this.type = type;
    }

    public void processFile(File file) throws InterruptedException {
        if (file.type() == type) {
            int processingTime = file.size() * 7; // Время обработки в мс
            TimeUnit.MILLISECONDS.sleep(processingTime);
            System.out.println("Processed: " + file);
        } else {
            System.out.println("Cannot process file of type: " + file.type());
        }
    }
}
