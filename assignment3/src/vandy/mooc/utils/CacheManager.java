package vandy.mooc.utils;

import android.util.Log;
import vandy.mooc.aidl.WeatherData;

import java.util.*;

/**
 * Created by Kirill Feoktistov on 03.06.15
 */

public class CacheManager {
    private final String tag = getClass().getSimpleName();
    private static volatile CacheManager instance;
    private Map<String, WeatherDataCached> cache = new HashMap<>();
    public static final long TEN_SECONDS_IN_MILLISEC = 10 * 1000;

    private CacheManager() {}

    public static CacheManager instance() {
        if (instance == null) {
            instance = new CacheManager();
        }

        return instance;
    }

    public synchronized void put(String location, WeatherData weatherData) {
        Log.d(tag, "Caching location " + location);
        cache.put(location, new WeatherDataCached(weatherData, new Date().getTime()));
    }

    public synchronized List<WeatherData> get(String location) {
        List<WeatherData> result = new ArrayList<>();
        WeatherDataCached weatherDataCached = cache.get(location);

        if (weatherDataCached != null) {
            if ((new Date().getTime() - weatherDataCached.getTimestamp()) < TEN_SECONDS_IN_MILLISEC) {
                Log.d(tag, "Using weather data from cache");
                result.add(weatherDataCached.get());
            } else {
                Log.d(tag, "Removing location from cache " + location);
                cache.remove(location);
            }
        }

        return result;
    }
}
