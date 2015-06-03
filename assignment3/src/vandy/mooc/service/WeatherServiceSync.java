package vandy.mooc.service;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import vandy.mooc.aidl.WeatherCall;
import vandy.mooc.aidl.WeatherData;

import java.util.List;

/**
 * Created by Kirill Feoktistov on 01.06.15
 */

public class WeatherServiceSync extends AbstractWeatherService {

    WeatherCall.Stub mWeatherCallImpl = new WeatherCall.Stub() {
        @Override
        public List<WeatherData> getCurrentWeather(String location) {
            return getResults(location);
        }
    };

    public static Intent makeIntent(Context context) {
        return new Intent(context, WeatherServiceSync.class);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mWeatherCallImpl;
    }
}
