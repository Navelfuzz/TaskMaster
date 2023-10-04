package com.navelfuzz.taskmaster;

import android.app.Application;
import android.util.Log;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.analytics.pinpoint.AWSPinpointAnalyticsPlugin;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;
import com.amplifyframework.predictions.aws.AWSPredictionsPlugin;



public class TaskMasterAmplifyApplication extends Application {
  public static final String TAG = "taskmasterapplication";

  @Override
  public void onCreate(){
    super.onCreate();

    try {
      Amplify.addPlugin(new AWSApiPlugin());
      Amplify.addPlugin(new AWSCognitoAuthPlugin());
      Amplify.addPlugin(new AWSPinpointAnalyticsPlugin());
      Amplify.addPlugin(new AWSPredictionsPlugin());
      Amplify.addPlugin(new AWSS3StoragePlugin());
      Amplify.configure(getApplicationContext());
    } catch(AmplifyException ae){
      Log.e(TAG, "Error Initializing Amplify " + ae.getMessage(), ae);
    }
  }
}