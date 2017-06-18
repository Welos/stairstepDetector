# Napp
Application using GPS and machine learning to determine user travelling patterns. This will be used for notifications when destination bus/tram stop is near.

# Export SampleCollector database to PC
1. Add location: "C:\Users\{USERNAME}\AppData\Local\Android\sdk\platform-tools" to PATH variable.
2. Install SampleCollector.
3. Collect Location and Acceleration samples (using background service).
4. Open SampleCollector application.
5. Click on export database button.
6. Run in cmd: "adb pull /sdcard/samplecollectordb".
