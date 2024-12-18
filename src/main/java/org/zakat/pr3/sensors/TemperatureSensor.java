package org.zakat.pr3.sensors;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.subjects.PublishSubject;

import java.util.Random;
import java.util.concurrent.TimeUnit;


public class TemperatureSensor extends Observable<SensorEvent<Integer>> implements Runnable {
    private final PublishSubject<SensorEvent<Integer>> subject;
    private final Random random;

    public TemperatureSensor() {
        this.subject = PublishSubject.create();
        this.random = new Random();
    }

    @Override
    protected void subscribeActual(@NonNull Observer<? super SensorEvent<Integer>> observer) {
        subject.subscribe(observer);
    }

    public void emitValue() {
        var event = new SensorEvent<>(SensorEventType.TEMPERATURE, random.nextInt(15, 31));
        subject.onNext(event);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(1));
                emitValue();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
