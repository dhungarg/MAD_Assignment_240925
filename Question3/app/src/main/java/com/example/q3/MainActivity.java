package com.example.q3;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accel, light, proximity;

    private TextView tvAccel, tvLight, tvProx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvAccel = findViewById(R.id.tvAccel);
        tvLight = findViewById(R.id.tvLight);
        tvProx = findViewById(R.id.tvProx);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if (accel == null) tvAccel.setText("Accelerometer: Not available");
        if (light == null) tvLight.setText("Light: Not available");
        if (proximity == null) tvProx.setText("Proximity: Not available");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accel != null) sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_UI);
        if (light != null) sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_UI);
        if (proximity != null) sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int type = event.sensor.getType();

        if (type == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            tvAccel.setText(String.format(Locale.US,
                    "Accelerometer (m/s²)\nX: %.2f  Y: %.2f  Z: %.2f", x, y, z));
        } else if (type == Sensor.TYPE_LIGHT) {
            float lux = event.values[0];
            tvLight.setText(String.format(Locale.US, "Light (lux)\n%.2f", lux));
        } else if (type == Sensor.TYPE_PROXIMITY) {
            float d = event.values[0];
            tvProx.setText(String.format(Locale.US, "Proximity\n%.2f", d));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }
}