# SensorPRINT

## Description
It is a small app to record sensor values of the built-in accelerometer and gyroscope in android devices.  
This project was made to complement my bachelors thesis [AndroGUARD](https://github.com/KGeri201/AndroGUARD).  
It does record the original unchanged values from the sensors and also modified ones, where the patch got applied to.  
This way the values before and after the patch can be compared more easily.  

## Usage
1. Download the latest apk from the [release page](https://github.com/KGeri201/SensorPRINT/releases/latest) and install it on an android device.  
2. Check the **Apply Patch** box to record also the modified values for later comparasion.
3. Start the recording by pressing **Record**.  
It will record the sensor values for over a minute, in the default intervalls of 250ms.
4. Use the CSVs under *Android/data/com.sensorprint/files* to compare the results.

## Credits
[KGeri201](https://github.com/KGeri201)

## License
[MIT License](LICENSE)
