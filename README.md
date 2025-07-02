# Spam Call Filter

Spam Call Filter is an Android application designed to monitor incoming calls and detect potential spam calls. It leverages Android's telephony services to provide real-time call state monitoring and logging.

## Features

- **Spam Call Monitoring**: Detect and log incoming calls for spam detection.
- **Foreground Service**: Runs as a foreground service to ensure continuous monitoring.
- **Permission Handling**: Requests and manages necessary permissions for call monitoring.
- **Call Logs**: Displays a list of detected calls in a user-friendly interface.
- **Clickable Footer Link**: Includes a clickable footer link to the developer's Upwork profile.

## Screenshots

![Screenshot 1.png](screenshot_1.png)

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/AnasLakhani/spam-call-filter.git

   Open the project in Android Studio.
Build the project to download dependencies.
Run the app on an Android device or emulator.
Permissions
The app requires the following permissions to function properly:  
RECEIVE_BOOT_COMPLETED
READ_PHONE_STATE
READ_CALL_LOG
POST_NOTIFICATIONS
READ_PHONE_NUMBERS
FOREGROUND_SERVICE
Ensure these permissions are granted for the app to work as expected.  
Usage
Launch the app.
Grant the required permissions when prompted.
The app will start monitoring calls in the background.
View the call logs in the app's interface.
Code Overview
Main Components
MainActivity: Handles the UI and permission requests.
CallStateService: A foreground service that monitors call states using TelephonyManager.
CallStateListener: Listens for call state changes and logs them.
SpamLogger: Utility class for managing call logs.
