package org.zakat.pr2;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CopyFile {
    public static void main(String[] args) {
        File source = new File("source.bin");
        File dest = new File("dest.bin");

        measurePerformance(() -> copyUsingStream(source, dest), "Stream");
        measurePerformance(() -> copyUsingFileChannel(source, dest), "File channel");
        measurePerformance(() -> copyUsingCommonsIO(source, dest), "Commons IO");

        Path sourcePath = Paths.get("source.bin");
        Path destPath = Paths.get("dest1.bin");
        measurePerformance(() -> copyUsingFiles(sourcePath, destPath), "Files class");
    }

    public static void measurePerformance(Runnable runnable, String method) {
        System.out.printf("Copying with %s... ", method);
        long before = System.currentTimeMillis();
        runnable.run();
        long after = System.currentTimeMillis();
        System.out.printf("Time taken: %d ms\n", after - before);
    }

    public static void copyUsingStream(File source, File dest) {
        try (InputStream in = new FileInputStream(source);
             OutputStream out = new FileOutputStream(dest)) {
            byte[] buffer = new byte[1024];
            int length;

            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void copyUsingFileChannel(File source, File dest) {
        try (FileChannel in = new FileInputStream(source).getChannel();
             FileChannel out = new FileOutputStream(dest).getChannel()) {
            in.transferTo(0, in.size(), out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void copyUsingCommonsIO(File source, File dest) {
        try {
            FileUtils.copyFile(source, dest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void copyUsingFiles(Path source, Path dest) {
        try {
            Files.copy(source, dest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
