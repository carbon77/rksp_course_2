package org.zakat.pr1;

import java.util.Scanner;
import java.util.concurrent.*;

public class SquareNumbers {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        Scanner in = new Scanner(System.in);
        runLoop(pool, in);

        in.close();
        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.MINUTES);
    }

    private static void runLoop(ExecutorService executor, Scanner in) {
        while (true) {
            System.out.print(">> ");
            String input = in.nextLine();

            if (input.equals("exit")) {
                break;
            }

            int num = Integer.parseInt(input);
            runTask(executor, num)
                    .thenAccept(result -> {
                        System.out.printf("Result: %d, thread: %s\n", result, Thread.currentThread().getName());
                        System.out.print(">> ");
                    });
        }
    }

    private static CompletableFuture<Integer> runTask(ExecutorService executor, int num) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return num * num;
        }, executor);
    }
}
