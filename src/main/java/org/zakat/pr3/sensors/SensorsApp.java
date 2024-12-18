package org.zakat.pr3.sensors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SensorsApp {
    public static void main(String[] args) {
        Alarm alarm = new Alarm();

        TemperatureSensor temperatureSensor = new TemperatureSensor();
        temperatureSensor.subscribe(alarm);

        Co2Sensor co2Sensor = new Co2Sensor();
        co2Sensor.subscribe(alarm);

        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(temperatureSensor);
        executor.submit(co2Sensor);

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(5));
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
