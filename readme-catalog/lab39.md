# Lab 39: Location

## Overview
Today, your app will add the user’s location to a task automatically when that task is created.

### Setup
Continue working in your taskmaster repository.

## Feature Tasks

#### Location
When the user adds a task, their location should be retrieved and included as part of the saved Task.

#### Displaying Location
On the Task Detail activity, the location of a Task should be displayed if it exists.

### Documentation
Update your daily change log with today’s changes.

### Screenshots

[//]: # (<img src="../screenshots/labXX/XX.png" alt="XX" width="200"/>)

[//]: # (<img src="../screenshots/labXX/XX.png" alt="XX" width="200"/>)

[//]: # (<img src="../screenshots/labXX/XX.png" alt="XX" width="200"/>)

### Invision Notes

**Lab 39: Location**

1. Location
    * On task save, get the user's current location
    * Add new properties to Task schema to hold the location
      * Hint: Use Strings
2. Displaying Location
    * Display the task location on the TaskDetailsActivity
    * 1 - Add Intent Extras from Main Activity for Lat/Long Strings
    * 2 - Query for Task in Detail Activity given its ID
3. Stretch
    * AWS Amplify doesn't recognize BigDecimal or double
       * Use String (not float!)
    * State/City/Country properties may have weird naming conventions
    * Extra Stretch Goal: Show Lat/Long on an actual map


#### Warm-up

   * If you wanted to store a user's location, what format might you use?
     * Latitude and Longitude
       * Handling N/S/E/W??
         * `+ / -`
       * Use Geocoder in order to convert lat/long to an actual address
     * User address: City, state, zipcode, etc.
       * Addressing in different countries is complicated

#### Location Services

1. Recording user's latitude/longitude
   * getLastLocation()
     * get quick location estimate and minimize battery usage
     * info could be out of date, if no other client has actively used location recently
   * getCurrentLocation()
     * gets fresher, more accurate location more consistently
     * can cause active location computation to occur on the device
   * FusedLocationProviderClient
     * Google Play Services way of grabbing location
2. Permissions
   * [Approximate vs Precise Location](https://developer.android.com/training/location/permissions#accuracy)
     * ACCESS_COARSE_LOCATION
       * Provides a device location estimate via FusedLocationProvider. Generally accurate within about 1.2 square miles
     * ACCESS_FINE_LOCATION
       * Provides location estimate that is as accurate as possible through the FusedLocationProvider. Generally accurate within within 10-160 feet or better.
   * Other permissions you may encounter
     * READ_EXTERNAL_STORAGE
     * ACCESS_BACKGROUND_LOCATION
     * ACCESS_NETWORK_STATE
     * INTERNET
   * **NOTE: When changing permissions, uninstall and reinstall the app!**
3. requestLocationUpdates()
   * [warning from Google's docs](https://developer.android.com/training/location/retrieve-current#BestEstimate)

