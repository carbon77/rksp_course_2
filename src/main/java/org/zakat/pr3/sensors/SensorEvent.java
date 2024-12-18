package org.zakat.pr3.sensors;

public record SensorEvent<T>(
        SensorEventType type,
        T payload
) {
}
