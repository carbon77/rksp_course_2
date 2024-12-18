package org.zakat.pr3.sensors;


import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class Alarm implements Observer<SensorEvent<?>> {
    private static final int TEMPERATURE_LIMIT = 25;
    private static final int CO2_LIMIT = 70;

    private boolean temperatureStatus;
    private boolean co2Status;

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        System.out.printf("%d has subscribed\n", d.hashCode());
    }

    @Override
    public void onNext(@NonNull SensorEvent<?> event) {
        SensorEventType type = event.type();
        checkStatus();

        switch (type) {
            case TEMPERATURE -> {
                int val = (int) event.payload();
                temperatureStatus = val > TEMPERATURE_LIMIT;
                System.out.printf("Temperature: %d\n", val);
            }
            case CO2 -> {
                int val = (int) event.payload();
                co2Status = val > CO2_LIMIT;
                System.out.printf("CO2: %d\n", val);
            }
        }
    }

    private void checkStatus() {
        if (temperatureStatus && co2Status) {
            System.out.println("ALARM!");
        } else if (temperatureStatus) {
            System.out.println("Temperature is too high!");
        } else if (co2Status) {
            System.out.println("CO2 is too high!");
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {
        System.out.printf("Error: %s\n", e.getMessage());
    }

    @Override
    public void onComplete() {

    }
}
