package vandy.mooc.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.*;
import vandy.mooc.R;
import vandy.mooc.aidl.WeatherCall;
import vandy.mooc.aidl.WeatherData;
import vandy.mooc.aidl.WeatherRequest;
import vandy.mooc.aidl.WeatherResults;
import vandy.mooc.service.WeatherServiceAsync;
import vandy.mooc.service.WeatherServiceSync;
import vandy.mooc.utils.RetainedFragmentManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeatherActivity extends Activity {
    public static final String FRAGMENT_TAG = "FRAGMENT";
    public static final String WEATHER_DATA_TAG = "WEATHER_DATA";

    protected final RetainedFragmentManager mRetainedFragmentManager =
            new RetainedFragmentManager(this.getFragmentManager(), FRAGMENT_TAG);

    private WeatherCall mWeatherCall;
    private WeatherRequest mWeatherRequest;

    private EditText mCity;
    private Button mSyncLoad;
    private Button mAsyncLoad;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm z", Locale.US);
    private TextView mResultName;
    private TextView mResultSpeed;
    private TextView mResultDeg;
    private TextView mResultTemp;
    private TextView mResultHumidity;
    private TextView mResultSunrise;
    private TextView mResultSunset;

    private ServiceConnection mWeatherServiceSync = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mWeatherCall = WeatherCall.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mWeatherCall = null;
        }
    };

    private ServiceConnection mWeatherServiceAsync = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mWeatherRequest = WeatherRequest.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mWeatherRequest = null;
        }
    };

    private WeatherResults.Stub mWeatherResults = new WeatherResults.Stub() {
        @Override
        public void sendResults(final List<WeatherData> results) throws RemoteException {
            if (!results.isEmpty()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRetainedFragmentManager.put(WEATHER_DATA_TAG, results.get(0));
                        displayWeather(results.get(0));
                    }
                });
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        handleConfigurationChanges();
    }

    protected void handleConfigurationChanges() {
        initUI();
        initListeners();

        if (!mRetainedFragmentManager.firstTimeIn()) {
            WeatherData weatherData = mRetainedFragmentManager.get("WEATHER_DATA");
            if (weatherData != null) {
                displayWeather(weatherData);
            }
        }
    }

    private void initUI() {
        mCity = (EditText) findViewById(R.id.city);
        mSyncLoad = (Button) findViewById(R.id.sync);
        mAsyncLoad = (Button) findViewById(R.id.async);
        mResultName = (TextView) findViewById(R.id.result_name);
        mResultSpeed = (TextView) findViewById(R.id.result_speed);
        mResultDeg = (TextView) findViewById(R.id.result_deg);
        mResultTemp = (TextView) findViewById(R.id.result_temp);
        mResultHumidity = (TextView) findViewById(R.id.result_humidity);
        mResultSunrise = (TextView) findViewById(R.id.result_sunrise);
        mResultSunset = (TextView) findViewById(R.id.result_sunset);
    }

    private void initListeners() {
        mSyncLoad.setOnClickListener(createSyncButtonListener());

        mAsyncLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetUI();

                if (mWeatherRequest != null) {
                    try {
                        mWeatherRequest.getCurrentWeather(getDesiredCity(), mWeatherResults);
                    } catch (RemoteException e) {
                        Toast.makeText(WeatherActivity.this, "Error loading weather", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private View.OnClickListener createSyncButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetUI();
                new AsyncTask<Void, Void, WeatherData>() {
                    @Override
                    protected WeatherData doInBackground(Void... params) {
                        List<WeatherData> weatherData = new ArrayList<>();

                        try {
                            weatherData = mWeatherCall.getCurrentWeather(getDesiredCity());
                        } catch (RemoteException e) {
                            Toast.makeText(WeatherActivity.this, "Error loading weather", Toast.LENGTH_SHORT).show();
                        }

                        return weatherData.get(0);
                    }

                    @Override
                    protected void onPostExecute(WeatherData result) {
                        mRetainedFragmentManager.put("WEATHER_DATA", result);
                        displayWeather(result);
                    }
                }.execute();
            }
        };
    }

    private void resetUI() {
        mResultSpeed.setText(null);
        mResultDeg.setText(null);
        mResultTemp.setText(null);
        mResultHumidity.setText(null);
        mResultSunrise.setText(null);
        mResultSunset.setText(null);
    }

    private void displayWeather(WeatherData result) {
        mResultName.setText(result.getmName());
        mResultSpeed.setText(Double.toString(result.getmSpeed()));
        mResultDeg.setText(Double.toString(result.getmDeg()));
        mResultTemp.setText(Double.toString(result.getmTemp()));
        mResultHumidity.setText(Double.toString(result.getmHumidity()));
        mResultSunrise.setText(dateFormat.format(new Date(result.getmSunrise() * 1000)));
        mResultSunset.setText(dateFormat.format(new Date(result.getmSunset() * 1000)));
    }

    private String getDesiredCity() {
        return mCity.getText().toString();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mWeatherCall == null) {
            bindService(WeatherServiceSync.makeIntent(this), mWeatherServiceSync, BIND_AUTO_CREATE);
        }

        if (mWeatherRequest == null) {
            bindService(WeatherServiceAsync.makeIntent(this), mWeatherServiceAsync, BIND_AUTO_CREATE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mWeatherCall != null) {
            unbindService(mWeatherServiceSync);
        }

        if (mWeatherRequest != null) {
            unbindService(mWeatherServiceAsync);
        }
    }
}
