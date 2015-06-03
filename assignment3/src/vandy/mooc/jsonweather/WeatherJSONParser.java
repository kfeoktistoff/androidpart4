package vandy.mooc.jsonweather;

import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses the Json weather data returned from the Weather Services API
 * and returns a List of JsonWeather objects that contain this data.
 */
public class WeatherJSONParser {
    /**
     * Used for logging purposes.
     */
    private final String TAG =
            this.getClass().getCanonicalName();

    /**
     * Parse the @a inputStream and convert it into a List of JsonWeather
     * objects.
     */
    public List<JsonWeather> parseJsonStream(InputStream inputStream)
            throws IOException {
        try (JsonReader reader =
                     new JsonReader(new InputStreamReader(inputStream,
                             "UTF-8"))) {
            return parseJsonWeatherArray(reader);
        }
    }

    /**
     * Parse a Json stream and convert it into a List of JsonWeather
     * objects.
     */
    public List<JsonWeather> parseJsonWeatherArray(JsonReader reader)
            throws IOException {

        List<JsonWeather> jsonWeathers = new ArrayList<>();
        jsonWeathers.add(parseJsonWeather(reader));

        return jsonWeathers;
    }

    /**
     * Parse a Json stream and return a JsonWeather object.
     */
    public JsonWeather parseJsonWeather(JsonReader reader)
            throws IOException {

        JsonWeather jsonWeather = new JsonWeather();

        reader.beginObject();

        if (reader.peek() == JsonToken.END_OBJECT) {
            return null;
        }

        while (reader.hasNext()) {
            String name = reader.nextName();

            switch (name) {
                case JsonWeather.name_JSON:
                    jsonWeather.setName(reader.nextString());
                    break;
                case JsonWeather.sys_JSON:
                    jsonWeather.setSys(parseSys(reader));
                    break;
                case JsonWeather.weather_JSON:
                    jsonWeather.setWeather(parseWeathers(reader));
                    break;
                case JsonWeather.main_JSON:
                    jsonWeather.setMain(parseMain(reader));
                    break;
                case JsonWeather.wind_JSON:
                    jsonWeather.setWind(parseWind(reader));
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }

        reader.endObject();

        return jsonWeather;
    }

    /**
     * Parse a Json stream and return a List of Weather objects.
     */
    public List<Weather> parseWeathers(JsonReader reader) throws IOException {
        List<Weather> weathers = new ArrayList<>();


        reader.beginArray();

        while (reader.hasNext() && reader.peek() != JsonToken.END_ARRAY) {
            weathers.add(parseWeather(reader));
        }

        reader.endArray();

        return weathers;
    }

    /**
     * Parse a Json stream and return a Weather object.
     */
    public Weather parseWeather(JsonReader reader) throws IOException {
        Weather weather = new Weather();

        reader.beginObject();

        while (reader.hasNext()) {
            String name = reader.nextName();

            switch (name) {
                case Weather.id_JSON:
                    weather.setId(reader.nextLong());
                    break;
                case Weather.main_JSON:
                    weather.setMain(reader.nextString());
                    break;
                case Weather.description_JSON:
                    weather.setDescription(reader.nextString());
                    break;
                case Weather.icon_JSON:
                    weather.setIcon(reader.nextString());
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }

        reader.endObject();

        return weather;
    }

    /**
     * Parse a Json stream and return a Main Object.
     */
    public Main parseMain(JsonReader reader)
            throws IOException {

        Main main = new Main();

        reader.beginObject();

        while (reader.hasNext()) {
            String name = reader.nextName();

            switch (name) {
                case Main.grndLevel_JSON:
                    main.setGrndLevel(reader.nextDouble());
                    break;
                case Main.humidity_JSON:
                    main.setHumidity(reader.nextLong());
                    break;
                case Main.pressure_JSON:
                    main.setPressure(reader.nextDouble());
                    break;
                case Main.seaLevel_JSON:
                    main.setSeaLevel(reader.nextDouble());
                    break;
                case Main.temp_JSON:
                    main.setTemp(reader.nextDouble());
                    break;
                case Main.tempMax_JSON:
                    main.setTempMax(reader.nextDouble());
                    break;
                case Main.tempMin_JSON:
                    main.setTempMin(reader.nextDouble());
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }

        reader.endObject();

        return main;
    }

    /**
     * Parse a Json stream and return a Wind Object.
     */
    public Wind parseWind(JsonReader reader) throws IOException {
        Wind wind = new Wind();

        reader.beginObject();

        while (reader.hasNext()) {
            String name = reader.nextName();

            switch (name) {
                case Wind.deg_JSON:
                    wind.setDeg(reader.nextDouble());
                    break;
                case Wind.speed_JSON:
                    wind.setSpeed(reader.nextDouble());
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }

        reader.endObject();

        return wind;
    }

    /**
     * Parse a Json stream and return a Sys Object.
     */
    public Sys parseSys(JsonReader reader) throws IOException {
        Sys sys = new Sys();

        reader.beginObject();

        while (reader.hasNext()) {
            String name = reader.nextName();

            switch (name) {
                case Sys.message_JSON:
                    sys.setMessage(reader.nextDouble());
                    break;
                case Sys.country_JSON:
                    sys.setCountry(reader.nextString());
                    break;
                case Sys.sunrise_JSON:
                    sys.setSunrise(reader.nextLong());
                    break;
                case Sys.sunset_JSON:
                    sys.setSunset(reader.nextLong());
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }

        reader.endObject();

        return sys;
    }
}
