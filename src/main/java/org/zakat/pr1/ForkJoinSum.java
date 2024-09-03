package org.zakat.pr1;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinSum extends RecursiveTask<Long> {
    private static final int THRESHOLD = 5000;
    private final int[] array;
    private final int start;
    private final int end;

    ForkJoinSum(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        if (end - start <= THRESHOLD) {
            long sum = 0;
            for (int i = start; i < end; i++) {
                sum += array[i];
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            return sum;
        }

        int mid = (start + end) / 2;
        ForkJoinSum leftTask = new ForkJoinSum(array, start, mid);
        ForkJoinSum rightTask = new ForkJoinSum(array, mid, end);
        leftTask.fork();
        return rightTask.compute() + leftTask.join();
    }

    public static long sum(int[] array) {
        try (ForkJoinPool pool = new ForkJoinPool()) {
            return pool.invoke(new ForkJoinSum(array, 0, array.length));
        }
    }
}
