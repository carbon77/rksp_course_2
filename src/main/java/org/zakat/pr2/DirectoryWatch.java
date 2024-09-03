package org.zakat.pr2;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Map;

public class DirectoryWatch {
    private final Map<Path, List<String>> fileContents;
    private WatchService watchService;
    private Path dir;

    public DirectoryWatch(Map<Path, List<String>> fileContents) {
        this.fileContents = fileContents;
    }

    public void watchDir(String dirPath) throws IOException, InterruptedException {
        watchService = FileSystems.getDefault().newWatchService();
        dir = Paths.get(dirPath);

        dir.register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_DELETE);

        System.out.printf("Monitoring directory: %s\n", dir);

        WatchKey key;
        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                runListener(event);
            }
            key.reset();
        }
    }

    public void runListener(WatchEvent<?> event) {
        WatchEvent.Kind<?> kind = event.kind();
        Path file = (Path) event.context();
        Path fullPath = dir.resolve(file);

        if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
            onEntryCreate(event, fullPath);
        } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
            onEntryModify(event, fullPath);
        } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
            onEntryDelete(event, fullPath);
        }
    }

    private void onEntryDelete(WatchEvent<?> event, Path fullPath) {
        System.out.printf("File deleted: %s\n", fullPath);
        String fileContent = String.join("\n", fileContents.get(fullPath));
        int checkSum = Checksum.findChecksum(fileContent);
        System.out.printf("File check sum: %d\n", checkSum);
    }

    private void onEntryModify(WatchEvent<?> event, Path fullPath) {

    }

    private void onEntryCreate(WatchEvent<?> event, Path fullPath) {
        System.out.printf("File created: %s\n", fullPath.toString());
        try {
            fileContents.put(fullPath, Files.readAllLines(fullPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path dirPath = Paths.get("watchDir");

        dirPath.register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_DELETE);

        System.out.printf("Monitoring directory: %s\n", dirPath);

        WatchKey key;
        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                Path fileName = (Path) event.context();
                Path fullPath = dirPath.resolve(fileName);

                if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                    System.out.printf("File created: %s\n", fullPath);
                } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                    File f = fullPath.toFile();
                    System.out.printf("File deleted: %s\n", fullPath);
                    System.out.printf("Size: %d\n", f.exists() ? f.length() : 0);
                    System.out.printf("Checksum: %s\n", Checksum.findChecksum(fullPath.toString()));
                }
            }
            key.reset();
        }
    }
}
