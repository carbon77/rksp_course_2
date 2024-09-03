package org.zakat.pr1;

public class SequentialSum {
    public static long sum(int[] array) {
        long sum = 0;

        for (int i = 0; i < array.length; i++) {
            try {
                Thread.sleep(1);
                sum += array[i];
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return sum;
    }
}
