# Android-WireGuard

## !!! JUST A PROTOTYPE, NOT EVEN ALPHA VERSION !!!

Android WireGuard implementation (rootless, using VpnApi).

Build system based on gradle + gradle experimental android plugin (supporting ndk). For build you need both sdk & ndk.
Create local.properties file in root dir, it should be looks like that:
```
ndk.dir=/home/viper/Android/Sdk/ndk-bundle
sdk.dir=/home/viper/Android/Sdk
```
build:
```
./gradlew assembleDebug
```
app/build/outputs/apk/app-debug.apk is your apk.
