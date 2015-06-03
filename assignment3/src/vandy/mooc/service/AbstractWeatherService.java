package vandy.mooc.service;

import android.app.Service;
import vandy.mooc.aidl.WeatherData;
import vandy.mooc.utils.CacheManager;
import vandy.mooc.utils.Utils;

import java.util.List;

/**
 * Created by Kirill Feoktistov on 04.06.15
 */

public abstract class AbstractWeatherService extends Service {
    private CacheManager cacheManager = CacheManager.instance();


    public List<WeatherData> getResults(String location) {
        List<WeatherData> results = cacheManager.get(location);

        if (results.isEmpty()) {
            results = Utils.getWeatherData(location);

            if (!results.isEmpty()) {
                cacheManager.put(location, results.get(0));
            }
        }

        return results;
    }
}
