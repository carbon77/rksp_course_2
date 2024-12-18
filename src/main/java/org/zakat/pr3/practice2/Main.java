package org.zakat.pr3.practice2;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        CountDownLatch countDownLatch = new CountDownLatch(3);

        // 2.1.1
        Flowable<Integer> randomSquares = Flowable
                .create(emitter -> {
                    for (int i = 0; i < 10; i++) {
                        Thread.sleep(1000);
                        emitter.onNext(random.nextInt());
                    }
                    emitter.onComplete();
                }, BackpressureStrategy.BUFFER);

        randomSquares
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(
                        x -> System.out.printf("Random square: %d\n", x),
                        err -> System.err.printf("Error: %s\n", err),
                        countDownLatch::countDown
                );

        // 2.2.1
        Observable<Integer> numbersStream = Observable
                .create(emitter -> {
                    for (int i = 0; i < 1000; i++) {
                        emitter.onNext(random.nextInt(10));
                    }
                    emitter.onComplete();
                });

        Observable<String> characterStream = Observable
                .create(emitter -> {
                    for (int i = 0; i < 1000; i++) {
                        char randomChar = (char) ('A' + random.nextInt(26));
                        emitter.onNext(String.valueOf(randomChar));
                    }
                    emitter.onComplete();
                });

        Observable<String> combinedStream = Observable
                .zip(
                        numbersStream,
                        characterStream,
                        (num, ch) -> String.format("%s%d", ch, num)
                );

        combinedStream
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(
                        System.out::println,
                        err -> System.err.printf("Error: %s\n", err),
                        countDownLatch::countDown
                );

        // 2.3.1
        Observable
                .create(emitter -> {
                    for (int i = 0; i < 10; i++) {
                        Thread.sleep(1000);
                        emitter.onNext(random.nextInt(100));
                    }
                    emitter.onComplete();
                })
                .skip(3)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(
                        System.out::println,
                        err -> System.err.printf("Error: %s\n", err),
                        countDownLatch::countDown
                );

        countDownLatch.await();
    }
}
