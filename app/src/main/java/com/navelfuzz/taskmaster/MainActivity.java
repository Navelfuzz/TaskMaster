package com.navelfuzz.taskmaster;

import static com.navelfuzz.taskmaster.activities.SettingsActivity.USERNAME_TAG;
import static com.navelfuzz.taskmaster.activities.SettingsActivity.TEAM_NAME_TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.preference.PreferenceManager;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.Manifest;

import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult;
import com.amplifyframework.auth.options.AuthSignOutOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.navelfuzz.taskmaster.activities.AddTaskActivity;
import com.navelfuzz.taskmaster.activities.AllTasksActivity;
import com.navelfuzz.taskmaster.activities.SettingsActivity;
import com.navelfuzz.taskmaster.adapters.ViewAdapter;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;

import java.util.Date;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    public static final String TASK_NAME_TAG = "taskName";
    public static final String TASK_DESC_TAG = "taskDesc";
    public static final String TASK_STATUS_TAG = "taskStatus";
    public static final String TASK_ID_EXTRA_TAG = "taskId";
    public static final String TASK_LATITUDE_EXTRA_TAG = "taskLat";
    public static final String TASK_LONGITUDE_EXTRA_TAG = "taskLon";
    public static final String TASK_ADDRESS_EXTRA_TAG = "taskAddress";

    List<Task> tasks = new ArrayList<>();
    ViewAdapter adapter;
    SharedPreferences preferences;

    // TODO: add the private variables for banner ads and interstitial ads here
    private AdRequest bannerAdRequest;
    private AdRequest interstitialAdRequest;
    private AdRequest rewardedAdRequest;
    private InterstitialAd mInterstitialAd;
    private RewardedAd rewardedAd;

    private AdView bannerAdView;
    private Button rewardAdButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: add the views for the ads here
        bannerAdView = findViewById(R.id.bannerAdView);

        logAppStartup();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        //createTeamInstances();
        setupSettingsButton();
        setupAddTaskButton();
        setupAllTasksButton();
        updateTasksListFromDatabase();
        setupRecyclerView();
        //manualS3FileUpload();

        // TODO: call the functions for the banner ads and interstitial ads here
        initializeMobileAds();
        setupBannerAd();
        setupInterstitialAd();
        setupInterstitialAdButton();
        setupRewardedAd();
        setupRewardAdButton();

    }

    // TODO: add necessary function definitions for ads

    // TODO: initializeMobileAds()
    private void initializeMobileAds() {
        MobileAds.initialize(this, new OnInitializationCompleteListener(){
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus){
            }
        });
    }
    // TODO: setupBannerAd()
    private void setupBannerAd(){
        bannerAdRequest = new AdRequest.Builder().build();
        bannerAdView.loadAd(bannerAdRequest);
    }

    // TODO: setupInterstitialAd() -> connect this to the existing add task button
    private void setupInterstitialAd() {
        interstitialAdRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", interstitialAdRequest,
            new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    mInterstitialAd = interstitialAd;
                    Log.i(TAG, "interstitial ad loaded");

                    // on successful ad load, define the full screen callback
                    // define a callback for the ad, if we have one
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                        @Override
                        public void onAdClicked() {
                            // Called when a click is recorded for an ad.
                            Log.d(TAG, "Ad was clicked.");
                        }

                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Called when ad is dismissed.
                            // Set the ad reference to null so you don't show the ad a second time.
                            Log.d(TAG, "Ad dismissed fullscreen content.");
                            mInterstitialAd = null;

                            // After the ad is dismissed, navigate to AddTaskActivity
//                            Intent goToAddTasksIntent = new Intent(MainActivity.this, AddTaskActivity.class);
//                            startActivity(goToAddTasksIntent);

                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            // Called when ad fails to show.
                            Log.e(TAG, "Ad failed to show fullscreen content.");
                            mInterstitialAd = null;
                        }

                        @Override
                        public void onAdImpression() {
                            // Called when an impression is recorded for an ad.
                            Log.d(TAG, "Ad recorded an impression.");
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            // Called when ad is shown.
                            Log.d(TAG, "Ad showed fullscreen content.");
                        }
                    });
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    // Handle the error
                    Log.d(TAG, loadAdError.toString());
                    mInterstitialAd = null;
                }
            });
    }
    // TODO: interstitialAdButton()
    private void setupInterstitialAdButton() {
        ImageView interstitialAdButton = findViewById(R.id.MainActivityInterstitialAdButton);

        interstitialAdButton.setOnClickListener(v -> {
            if (mInterstitialAd != null) {
                mInterstitialAd.show(MainActivity.this);
            } else {
                Log.d("TAG", "The interstitial ad wasn't ready yet.");
            }
        });
    }

    // TODO: setupRewardedAd()
    private void setupRewardedAd() {
        rewardedAdRequest = new AdRequest.Builder().build();

        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
            rewardedAdRequest, new RewardedAdLoadCallback() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    // Handle the error.
                    Log.d(TAG, loadAdError.toString());
                    rewardedAd = null;
                }

                @Override
                public void onAdLoaded(@NonNull RewardedAd ad) {
                    rewardedAd = ad;
                    Log.d(TAG, "Rewarded ad was loaded.");

                    // On successful ad load, define the full screen callback
                    rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdClicked() {
                            // Called when a click is recorded for an ad.
                            Log.d(TAG, "Rewarded ad was clicked.");
                        }

                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Called when ad is dismissed.
                            // Set the ad reference to null so you don't show the ad a second time.
                            Log.d(TAG, "Rewarded ad dismissed fullscreen content.");
                            rewardedAd = null;
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            // Called when ad fails to show.
                            Log.e(TAG, "Rewarded ad failed to show fullscreen content.");
                            rewardedAd = null;
                        }

                        @Override
                        public void onAdImpression() {
                            // Called when an impression is recorded for an ad.
                            Log.d(TAG, "Rewarded ad recorded an impression.");
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            // Called when ad is shown.
                            Log.d(TAG, "Rewarded ad showed fullscreen content.");
                        }
                    });
                }
            });
    }
    // TODO: setupRewardedAdButton()
    private void setupRewardAdButton() {
        ImageView rewardButton = findViewById(R.id.MainActivityRewardButton);

        rewardButton.setOnClickListener(view -> {
            if (rewardedAd != null) {
                Activity activityContext = MainActivity.this;
                rewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        // Handle the reward.
                        int rewardAmount = rewardItem.getAmount();
                        String rewardType = rewardItem.getType();
                        Log.d(TAG, "The user earned the reward. Reward Amount: " + rewardAmount + " Reward Type: " + rewardType);
                    }
                });
            } else {
                Log.d(TAG, "The rewarded ad wasn't ready yet.");
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        setupUsernameTextView();

        updateTasksListFromDatabase();

    }

    void setupAddTaskButton() {
        Button addTaskButton = findViewById(R.id.MainActivityAddTaskButton);
        addTaskButton.setOnClickListener(view -> {
            Intent goToAddTasksIntent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(goToAddTasksIntent);
        });
    }


    void setupAllTasksButton() {
        Button allTasksButton = findViewById(R.id.MainActivityAllTasksButton);
        allTasksButton.setOnClickListener(view -> {
            Intent goToAllTasksIntent = new Intent(MainActivity.this, AllTasksActivity.class);
            startActivity(goToAllTasksIntent);
        });
    }

    void setupSettingsButton() {
        ((ImageView) findViewById(R.id.MainActivitySettingsButton)).setOnClickListener(view -> {
            Intent gotToSettingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(gotToSettingsIntent);
        });
    }

    void setupUsernameTextView() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userName = preferences.getString(USERNAME_TAG, "No Username");
        String teamName = preferences.getString(TEAM_NAME_TAG, "All Teams");
        TextView usernameTextView = findViewById(R.id.MainActivityTextView);
        usernameTextView.setText(userName + "'s Tasks");
        TextView teamTextView = findViewById(R.id.MainActivityTeamNameLabel);
        teamTextView.setText(teamName + " View");
    }

    void setupRecyclerView() {
        RecyclerView taskListRecyclerView = (RecyclerView) findViewById(R.id.MainActivityTaskRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        taskListRecyclerView.setLayoutManager(layoutManager);

        int spaceInPixels = getResources().getDimensionPixelSize(R.dimen.task_fragment_spacing);
        taskListRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = spaceInPixels;

                if (parent.getChildAdapterPosition(view) == tasks.size() - 1) {
                    outRect.bottom = 0;
                }
            }
        });

        adapter = new ViewAdapter(tasks, this);
        taskListRecyclerView.setAdapter(adapter);
    }


    void updateTasksListFromDatabase() {
        Amplify.API.query(
            ModelQuery.list(Task.class),
            success -> {
                Log.i(TAG, "Read tasks successfully.");
                String teamName = preferences.getString(TEAM_NAME_TAG, null);
                tasks.clear();
                if (teamName == null) {
                    for (Task databaseTask : success.getData()) {
                        tasks.add(databaseTask);
                    }
                } else {
                    for (Task databaseTask : success.getData()) {
                        if (databaseTask.getTeam().getTeamName().equals(teamName)) {
                            tasks.add(databaseTask);
                        }
                    }
                }
                runOnUiThread(() -> {
                    adapter.notifyDataSetChanged();
                });
            },
            failure -> Log.i(TAG, "Did not read tasks successfully.")
        );
    }

    void logAppStartup() {
        AnalyticsEvent event = AnalyticsEvent.builder()
            .name("appOpened")
            .addProperty("time", Long.toString(new Date().getTime()))
            .addProperty("trackingEvent", "MainActivity opened")
            .build();

        Amplify.Analytics.recordEvent(event);
    }

}
