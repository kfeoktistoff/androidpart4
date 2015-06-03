package vandy.mooc.utils;

import vandy.mooc.aidl.WeatherData;

/**
 * Created by Kirill Feoktistov on 03.06.15
 */

public class WeatherDataCached {
    private WeatherData weatherData;
    private long timestamp;

    public WeatherDataCached(WeatherData weatherData, long timestamp) {
        this.weatherData = weatherData;
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public WeatherData get() {
        return weatherData;
    }
}
