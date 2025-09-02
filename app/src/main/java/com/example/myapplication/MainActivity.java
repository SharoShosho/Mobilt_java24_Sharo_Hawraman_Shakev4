package com.example.myapplication;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView accelXTextView, accelYTextView, accelZTextView, maxValuesTextView;
    private ProgressBar xProgressBar, yProgressBar, zProgressBar;
    private SeekBar thresholdSeekBar;
    private Button resetButton;

    private float maxX = 0, maxY = 0, maxZ = 0;
    private float threshold = 12.0f;
    private long lastShakeTime = 0;
    private static final int SHAKE_COOLDOWN = 1000; // 1 second cooldown between shakes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        accelXTextView = findViewById(R.id.accelXTextView);
        accelYTextView = findViewById(R.id.accelYTextView);
        accelZTextView = findViewById(R.id.accelZTextView);
        maxValuesTextView = findViewById(R.id.maxValuesTextView);
        xProgressBar = findViewById(R.id.xProgressBar);
        yProgressBar = findViewById(R.id.yProgressBar);
        zProgressBar = findViewById(R.id.zProgressBar);
        thresholdSeekBar = findViewById(R.id.thresholdSeekBar);
        resetButton = findViewById(R.id.resetButton);

        // Setup sensor manager and accelerometer
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        // Set up threshold seekbar
        thresholdSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                threshold = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(MainActivity.this, "Threshold set to: " + threshold, Toast.LENGTH_SHORT).show();
            }
        });

        // Set up reset button
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maxX = 0;
                maxY = 0;
                maxZ = 0;
                updateMaxValuesText();
                Toast.makeText(MainActivity.this, "Max values reset", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (accelerometer != null) {
            sensorManager.unregisterListener(this);
        }
    }



    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Update UI with current values
            accelXTextView.setText(String.format("X: %.2f m/s²", x));
            accelYTextView.setText(String.format("Y: %.2f m/s²", y));
            accelZTextView.setText(String.format("Z: %.2f m/s²", z));

            // Update progress bars for all three axes
            updateProgressBar(xProgressBar, x);
            updateProgressBar(yProgressBar, y);
            updateProgressBar(zProgressBar, z);

            // Update max values
            if (Math.abs(x) > maxX) maxX = Math.abs(x);
            if (Math.abs(y) > maxY) maxY = Math.abs(y);
            if (Math.abs(z) > maxZ) maxZ = Math.abs(z);
            updateMaxValuesText();

            // Calculate acceleration magnitude
            double acceleration = Math.sqrt(x * x + y * y + z * z);

            // Check for shake
            if (acceleration > threshold) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastShakeTime > SHAKE_COOLDOWN) {
                    lastShakeTime = currentTime;
                    onShakeDetected();
                }
            }
        }
    }

    private void updateProgressBar(ProgressBar progressBar, float value) {
        int progressValue = (int) (Math.abs(value) * 2);
        progressBar.setProgress(progressValue);
    }

    private void updateMaxValuesText() {
        maxValuesTextView.setText(String.format("Max values: X=%.2f, Y=%.2f, Z=%.2f", maxX, maxY, maxZ));
    }

    private void onShakeDetected() {
        // Log the shake event
        Log.d("ShakeApp", "Shake detected with threshold: " + threshold);

        // Show toast notification
        Toast.makeText(this, "Shake detected!", Toast.LENGTH_SHORT).show();

        // Vibrate for feedback
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not needed for this app
    }
}