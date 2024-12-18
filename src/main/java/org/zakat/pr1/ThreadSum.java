package org.zakat.pr1;

public class ThreadSum implements Runnable {
    private final int[] array;
    private final int start;
    private final int end;
    private long sum;

    public ThreadSum(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    public long getSum() {
        return sum;
    }

    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            try {
                Thread.sleep(1);
                sum += array[i];
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static long sum(int[] array, int threadCount) throws InterruptedException {
        long sum = 0;
        int chunkSize = array.length / threadCount;
        Thread[] threads = new Thread[threadCount];
        ThreadSum[] tasks = new ThreadSum[threadCount];
        for (int i = 0; i < threadCount; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, array.length);
            tasks[i] = new ThreadSum(array, start, end);
            threads[i] = new Thread(tasks[i]);
            threads[i].start();
        }
        for (int i = 0; i < threadCount; i++) {
            threads[i].join();
            sum += tasks[i].getSum();
        }
        return sum;
    }
}