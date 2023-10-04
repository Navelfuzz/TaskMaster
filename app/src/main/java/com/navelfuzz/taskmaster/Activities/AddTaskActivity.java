package com.navelfuzz.taskmaster.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ImageView;
import android.location.Address;
import android.location.Geocoder;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskStatusEnum;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;
import com.navelfuzz.taskmaster.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class AddTaskActivity extends AppCompatActivity {
    private final String TAG = "AddTaskActivity";
    private String s3ImageKey = "";
    private String selectedTeam;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Geocoder geocoder;
    CompletableFuture<List<Team>> teamsFuture = null;
    ImageView taskImageView;
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        taskImageView = findViewById(R.id.AddTaskActivityImageView);
        activityResultLauncher = getImagePickingActivityResultLauncher();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        teamsFuture = new CompletableFuture<>();
        setupTaskImageView();
        setupTeamsRadioButtons();
        setupAddTaskButton();
    }

    void setupTaskImageView(){
        taskImageView.setOnClickListener(view -> {
            launchImageSelectionIntent();
        });
    }

    void setupTeamsRadioButtons(){
        Amplify.API.query(
                ModelQuery.list(Team.class),
                success -> {
                    Log.i(TAG, "Read teams Successfully");
                    ArrayList<Team> teams = new ArrayList<>();
                    for(Team team : success.getData()){
                        teams.add(team);
                    }
                    teamsFuture.complete(teams);
                },
                failure -> {
                    teamsFuture.complete(null);
                    Log.i(TAG, "Did not read teams successfully");
                }
        );
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        int id = view.getId();
        if (id == R.id.radio_alpha) {
            if (checked) {
                selectedTeam = "Alpha Team";
            }
        } else if (id == R.id.radio_bravo) {
            if (checked) {
                selectedTeam = "Bravo Team";
            }
        } else if (id == R.id.radio_delta) {
            if (checked) {
                selectedTeam = "Delta Team";
            }
        }
    }

    void setupAddTaskButton(){
        Button addTaskButton = findViewById(R.id.AddTaskInputButton);
        String[] locationData = getUsersLastLocation();
        addTaskButton.setOnClickListener(view -> {

            if (selectedTeam == null) {

                Snackbar.make(findViewById(android.R.id.content), "Please select a team", Snackbar.LENGTH_SHORT).show();
                return;
            }
            List<Team> teams = null;
            try {
                teams = teamsFuture.get();
            } catch (InterruptedException ie) {
                Log.e(TAG, "InterruptedException while getting teams.");
                Thread.currentThread().interrupt();
            } catch (ExecutionException ee){
                Log.e(TAG, "ExecutionException while getting teams");
            }
            assert teams != null;
            Team teamToInput = teams.stream().filter(t -> t.getTeamName().equals(selectedTeam)).findAny().orElseThrow(RuntimeException::new);
            EditText taskInputEditText = findViewById(R.id.AddTaskInputField);
            EditText descriptionInputEditText = findViewById(R.id.AddTaskDescriptionInputField);
            Log.i(TAG, "CURRENT LAT HERE: " + locationData[0]);
            Task taskToSave = Task.builder()
                    .title(taskInputEditText.getText().toString())
                    .body(descriptionInputEditText.getText().toString())
                    .dateCreated(new Temporal.DateTime(new Date(), 0))
                    .status(TaskStatusEnum.New)
                    .team(teamToInput)
                        .taskImageS3Key(s3ImageKey)
                            .latitude(locationData[0])
                                .longitude(locationData[1])
                                    .address(locationData[2])
                                        .build();

            Amplify.API.mutate(
                ModelMutation.create(taskToSave),
                successResponse -> {
                    Log.i(TAG, "AddTaskActivity.setupAddTaskButton(): made task successfully");
                    taskInputEditText.setText("");
                    descriptionInputEditText.setText("");
                    Snackbar.make(findViewById(android.R.id.content), "Task Saved!", Snackbar.LENGTH_SHORT).show();
                },
                failureResponse -> Log.i(TAG, "AddTaskActivity.setupAddTaskButton: failed with this response" + failureResponse)
            );

            taskInputEditText.setText("");
            descriptionInputEditText.setText("");
            Snackbar.make(findViewById(android.R.id.content), "Task Saved!", Snackbar.LENGTH_SHORT).show();
        });
    }

    void launchImageSelectionIntent(){
        Intent imageFilePickingIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageFilePickingIntent.setType("*/*");
        imageFilePickingIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpeg", "image/png"});

        activityResultLauncher.launch(imageFilePickingIntent);
    }

    ActivityResultLauncher<Intent> getImagePickingActivityResultLauncher() {
        ActivityResultLauncher<Intent> imagePickingActivityResultLauncher =
            registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Uri pickedImageFileUri = result.getData().getData();
                    try {
                        InputStream pickedImageInputStream = getContentResolver().openInputStream(pickedImageFileUri);
                        String pickedImageFilename = getFileNameFromUri(pickedImageFileUri);
                        Log.i(TAG, "Succeeded in getting input stream from file on phone! Filename is: " + pickedImageFilename);
                        uploadInputStreamToS3(pickedImageInputStream, pickedImageFilename, pickedImageFileUri);
                    } catch (FileNotFoundException e) {
                        Log.e(TAG, "Could not get file from file picker!");
                    }
                }
            );
        return imagePickingActivityResultLauncher;
    }

    void uploadInputStreamToS3(InputStream pickedImageInputStream, String pickedImageFilename, Uri pickedImageFileUri) {

        Amplify.Storage.uploadInputStream(
            pickedImageFilename, // S3 key
            pickedImageInputStream,
            success -> {
                Log.i(TAG, "Succeeded in getting file uploaded to S3! Key is: " + success.getKey());
                s3ImageKey = success.getKey();
                InputStream pickedImageInputStreamCopy = null;
                try {
                    pickedImageInputStreamCopy = getContentResolver().openInputStream(pickedImageFileUri);
                } catch (FileNotFoundException e) {
                    Log.e(TAG, "Could not get file stream from URI. ");
                }
                taskImageView.setImageBitmap(BitmapFactory.decodeStream(pickedImageInputStreamCopy));
            },
            failure -> {
                Log.e(TAG, "Failure in uploading file to S3. ");
            }
        );
    }


    // Taken from class demo, sourced from StackOverflow
    @SuppressLint("Range")
    public String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    String[] getUsersLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            String[] output = {"","",""};
            return output;
        }
        String[] output = new String[3];
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if (location == null) {
                Log.e(TAG, "Location callback was null. ");
            }
            String currentLatitude = Double.toString(location.getLatitude());
            String currentLongitude = Double.toString(location.getLongitude());
            String currentAddress="";

            try {
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (!addresses.isEmpty()) {
                    currentAddress = addresses.get(0).getAddressLine(0);
                }
            } catch (IOException ioe) {
                Log.e(TAG, "Could not get subscribed location.");
            }

            Log.i(TAG, "Users last latitude: " + currentLatitude);
            Log.i(TAG, "Users last longitude: " + currentLongitude);
            Log.i(TAG, "Users last location: " + currentAddress);
            output[0] = currentLatitude;
            output[1] = currentLongitude;
            output[2] = currentAddress;
        });
        return output;
    }

}
