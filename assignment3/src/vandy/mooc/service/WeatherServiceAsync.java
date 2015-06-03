package vandy.mooc.service;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import vandy.mooc.aidl.WeatherRequest;
import vandy.mooc.aidl.WeatherResults;

/**
 * Created by Kirill Feoktistov on 01.06.15
 */

public class WeatherServiceAsync extends AbstractWeatherService {

    WeatherRequest.Stub mWeatherRequestImpl = new WeatherRequest.Stub() {
        @Override
        public void getCurrentWeather(String location, WeatherResults results) throws RemoteException {
            results.sendResults(getResults(location));
        }
    };

    public static Intent makeIntent(Context context) {
        return new Intent(context, WeatherServiceAsync.class);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mWeatherRequestImpl;
    }
}
