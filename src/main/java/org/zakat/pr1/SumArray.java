package org.zakat.pr1;

import java.util.Random;

public class SumArray {
    public static void main(String[] args) throws InterruptedException {
        int n = 10_000;
        int[] array = new int[n];
        Random random = new Random();

        System.out.println("Generating array...");
        for (int i = 0; i < n; i++) {
            array[i] = random.nextInt();
        }
        System.out.println("Array generated");

        Long beforeSequentialSum = System.currentTimeMillis();
        long sequentialSum = SequentialSum.sum(array);
        Long afterSequentialSum = System.currentTimeMillis();
        System.out.printf("Sequential sum: %d, time: %d ms\n", sequentialSum, afterSequentialSum - beforeSequentialSum);

        int threadCount = 5;
        Long beforeThreadSum = System.currentTimeMillis();
        long threadSum = ThreadSum.sum(array, threadCount);
        Long afterThreadSum = System.currentTimeMillis();
        System.out.printf("Thread sum: %d, thread count: %d, time: %d ms\n",
                threadSum, threadCount, afterThreadSum - beforeThreadSum);

        Long beforeForkJoinSum = System.currentTimeMillis();
        long forkJoinSum = ForkJoinSum.sum(array);
        Long afterForkJoinSum = System.currentTimeMillis();
        System.out.printf("ForkJoin sum: %d, time: %d ms\n", forkJoinSum, afterForkJoinSum - beforeForkJoinSum);
    }
}


