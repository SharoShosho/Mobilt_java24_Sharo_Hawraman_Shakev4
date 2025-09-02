# Shake App - Accelerometer Application

## Overview

Shake App is an Android application that demonstrates the use of the device's accelerometer sensor. It displays real-time motion data and provides visual feedback when the device is shaken.

## Features

- Real-time display of accelerometer data for X, Y, and Z axes
- Visual progress bars for each axis showing movement intensity
- Adjustable shake sensitivity using a seek bar
- Shake detection with visual and haptic feedback
- Maximum value tracking for each axis
- Resource-efficient sensor management

## Technical Implementation

### Sensors Used
- TYPE_ACCELEROMETER: Measures acceleration forces in m/s² along three axes

### Main Components
- MainActivity: Primary activity implementing SensorEventListener
- ProgressBars: Visual indicators for each axis's movement intensity
- SeekBar: Adjusts the shake detection threshold
- Button: Resets maximum recorded values
- TextViews: Display current and maximum sensor values

### Key Methods
- onSensorChanged(): Processes accelerometer data updates
- onShakeDetected(): Handles shake detection events
- updateProgressBar(): Updates visual indicators
- onPause()/onDestroy(): Manages sensor resource cleanup

## Usage

1. Launch the application on an Android device with an accelerometer
2. View real-time accelerometer data for all three axes
3. Adjust shake sensitivity using the seek bar
4. Shake the device to trigger detection feedback
5. Reset maximum values using the reset button

## Requirements

- Android device with accelerometer sensor
- Android API level 21 or higher
- VIBRATE permission for haptic feedback

## Code Structure

The application follows a single-activity architecture with proper lifecycle management to ensure efficient sensor usage and battery conservation.
