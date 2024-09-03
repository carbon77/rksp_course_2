package org.zakat.pr2;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Checksum {
    public static void main(String[] args) throws IOException {
        String filePath = "file.txt";
        short checkSum = findFileChecksum(filePath);
        String text = String.join("\n", Files.readAllLines(Paths.get(filePath)));
        System.out.printf("16-bit checksum of %s: %d\n", filePath, checkSum);
        System.out.printf("16-bit checksum of text %s: %d", filePath, findChecksum(text));
    }

    public static int findChecksum(String text) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(text.getBytes(StandardCharsets.UTF_8));

        int checksum = 0;

        while (byteBuffer.remaining() > 0) {
            if (byteBuffer.remaining() >= 2) {
                short value = byteBuffer.getShort();
                checksum ^= value;
            } else {
                checksum ^= byteBuffer.get();
            }
        }

        // Reduce to 16 bits
        checksum = (checksum & 0xFFFF) + (checksum >> 16);
        return checksum & 0xFFFF;
    }

    public static short findFileChecksum(String filePath) {
        try (FileChannel in = new FileInputStream(filePath).getChannel()) {
            ByteBuffer buf = ByteBuffer.allocate(2);
            short checkSum = 0;

            while (in.read(buf) != -1) {
                buf.flip();

                while (buf.remaining() == 2) {
                    checkSum ^= buf.getShort();
                }

                buf.clear();
            }

            return checkSum;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
