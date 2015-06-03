package vandy.mooc.utils;

import android.util.Log;
import vandy.mooc.aidl.WeatherData;
import vandy.mooc.jsonweather.JsonWeather;
import vandy.mooc.jsonweather.WeatherJSONParser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kirill Feoktistov on 02.06.15
 */

public class Utils {
    private static final String TAG = Utils.class.getCanonicalName();
    private final static String OPENWEATHERMAP_BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";

    private Utils() {}

    public static List<WeatherData> getWeatherData(String location) {
        Log.d(TAG, "Retrieving weather data from the cloud");
        List<WeatherData> weatherDataList = new ArrayList<>();

        try {
            URL url = new URL(OPENWEATHERMAP_BASE_URL + location);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try (InputStream in = new BufferedInputStream(urlConnection.getInputStream())) {
                WeatherJSONParser parser = new WeatherJSONParser();
                List<JsonWeather> jsonWeathers = parser.parseJsonStream(in);

                if (!jsonWeathers.isEmpty()) {
                    JsonWeather jsonWeather = jsonWeathers.get(0);
                    WeatherData weatherData = new WeatherData(
                            jsonWeather.getName(),
                            jsonWeather.getWind().getSpeed(),
                            jsonWeather.getWind().getDeg(),
                            jsonWeather.getMain().getTemp(),
                            jsonWeather.getMain().getHumidity(),
                            jsonWeather.getSys().getSunrise(),
                            jsonWeather.getSys().getSunset()
                    );

                    weatherDataList.add(weatherData);
                }

            } finally {
                urlConnection.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return weatherDataList;
    }
}
