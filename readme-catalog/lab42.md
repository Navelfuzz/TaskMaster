# Lab 42: Monetization And AdMob Ads

### Overview
Today, your app will integrate a few different kinds of ads via Google AdMob.

### Setup
Continue working in your taskmaster repository.

## Resources
[AdMob Banner Ads](https://developers.google.com/admob/android/banner)
[AdMob Interstitial Ads](https://developers.google.com/admob/android/interstitial)
[AdMob Rewarded Ads](https://developers.google.com/admob/android/rewarded)

## Feature Tasks

#### Banner Ad
On the “Main” activity, add a banner ad to the bottom of the page and display a Google test ad there.

#### Interstitial Ad
Add a button to the “Main” activity that allows users to see an interstitial ad. (In a real app, 
you’ll want to show this during natural transitions/pauses in your app, but we don’t really have 
that in this application.)

#### Rewarded Ad
Add a button to the “Main” activity that allows users to see a rewarded ad using a Google test ad. 
When the user clicks the close button, the user should see their reward in a text field next to the button.

## Documentation
Update your daily change log with today’s changes.

### Info for AdMob

Goes into the AndroidManifest.xml file

    <manifest>
      <application>
        <!-- Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713 -->
        <meta-data
            android:name="com.google.android.gms.ads.APPLICATION_ID"
            android:value="ca-app-pub-xxxxxxxxxxxxxxxx~yyyyyyyyyy"/>
      </application>
    </manifest>


Below is the official AdMob account info

        <meta-data
            android:name="com.google.android.gms.ads.APPLICATION_ID"
            android:value="ca-app-pub-5407008914078918~8622600249" />

### Screenshots

<img src="screenshots/lab42/bannerAd.png" alt="banner on main" width="200"/>

<img src="screenshots/lab42/interstitialAd.png" alt="interstitial play" width="200"/>

<img src="screenshots/lab42/rewardAd.png" alt="reward star" width="200"/>