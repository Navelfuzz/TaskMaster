# Lab 37: S3 Uploads

## Overview
Today, your app will allow users to upload images related to tasks, like screenshots.

#### Setup
Continue working in your taskmaster repository.

### Resources
[Amplify Getting Started](https://aws-amplify.github.io/docs/)
[Android File Picker](https://developer.android.com/guide/topics/providers/document-provider)
[Amplify S3](https://docs.amplify.aws/lib/storage/getting-started/q/platform/android/)

## Feature Tasks

### Uploads
On the “Add a Task” activity, allow users to optionally select an image to attach to that task. If a user attaches an image to a task, that image should be uploaded to S3, and associated with that task.

### Displaying Files
On the Task detail activity, if there is a file that is an image associated with a particular Task, that image should be displayed within that activity.

### Documentation
Update your daily change log with today’s changes.

### Screenshots

[//]: # (<img src="../screenshots/labXX/XXX.png" alt="XXX" width="200"/>)
[//]: # (<img src="../screenshots/labXX/XXX.png" alt="XXX" width="200"/>)
[//]: # (<img src="../screenshots/labXX/XXX.png" alt="XXX" width="200"/>)



#### Submission Instructions
* Continue working in your taskmaster repo.
* Work on a non-master branch and make commits appropriately.
* Update your README with your changes for today and screenshot of your work.
* Create a pull request to your master branch with your work for this lab.
* Submit the link to that pull request on Canvas. Add a comment with the amount of time you spent on this assignment.

### Notes/Info

*build.gradle(:app) dependencies*
```groovy
dependencies {
implementation 'com.amplifyframework:aws-storage-s3:2.13.2'
implementation 'com.amplifyframework:aws-auth-cognito:2.13.2'
}
```

*MainActivity imports*
```java
import android.util.Log;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;
```

*Plugins can be used anywhere, we're building in MainActivity first* 
```java	
Amplify.addPlugin(new AWSCognitoAuthPlugin());
Amplify.addPlugin(new AWSS3StoragePlugin());
```


*MainActivity onCreate() plugins*
```java
public class MyAmplifyApp extends Application {
    	@Override
   	public void onCreate() {
        super.onCreate();

        try {
            // Add these lines to add the AWSCognitoAuthPlugin and AWSS3StoragePlugin plugins
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSS3StoragePlugin());
            Amplify.configure(getApplicationContext());

            Log.i("MyAmplifyApp", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }
    }
}
```
	

