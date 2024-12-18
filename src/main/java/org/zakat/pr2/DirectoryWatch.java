package org.zakat.pr2;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

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
        try {
            HashSet<String> newLines = new HashSet<>(Files.readAllLines(fullPath));
            HashSet<String> oldLines = new HashSet<>(fileContents.getOrDefault(fullPath, new ArrayList<>()));

            List<String> addedLines = new ArrayList<>(newLines);
            addedLines.removeAll(oldLines);

            List<String> deletedLines = new ArrayList<>(oldLines);
            deletedLines.removeAll(newLines);

            System.out.println("==MODIFY==");
            System.out.println("\nAdded lines:");
            for (var line : addedLines) {
                System.out.println(line);
            }

            System.out.println("\nDeleted lines:");
            for (var line : deletedLines) {
                System.out.println(line);
            }
            fileContents.put(fullPath, new ArrayList<>(newLines));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        String dirPath = "./watchDir";
        DirectoryWatch watch = new DirectoryWatch(new HashMap<>());
        watch.watchDir(dirPath);
    }
}
