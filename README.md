# PharmTechApp

## Installation
Clone this repository and import into **Android Studio**
```bash
git clone git@github.com:phamumb/Phamrtechapp.git
```

## Configuration
### Keystores:
Create `app/keystore.gradle` with the following info:
```gradle
ext.key_alias='...'
ext.key_password='...'
ext.store_password='...'
```
And place both keystores under `app/keystores/` directory:
- `playstore.keystore`
- `stage.keystore`


## Build variants
Use the Android Studio *Build Variants* button to choose between **production** and **staging** flavors combined with debug and release build types


## Generating signed APK
From Android Studio:
1. ***Build*** menu
2. ***Generate Signed APK...***
3. Fill in the keystore information *(you only need to do this once manually and then let Android Studio remember it)*

## Maintainers
This project is mantained by:
* [Luat Pham](http://github.com/phamumb)


## App Demo
![App](/images/1.png)
![App](/images/2.png)
![App](/images/3.png)
![App](/images/4.png)
![App](/images/5.png)
![App](/images/6.png)
![App](/images/7.png)
![App](/images/8.png)
