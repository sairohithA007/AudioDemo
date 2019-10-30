package com.starter.code.audiorecorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private MediaRecorder recorder;
    private MediaPlayer player;
    private static String fileName = null;
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Location to store audio file
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        fileName = filePath + "/audioFile.3gp";

        // Initializing the recorder and player
        player = new MediaPlayer();
        recorder = new MediaRecorder();
    }


    // Code snippet to stop playing the recording
    public void stopPlaying(View view){
        // Stopping the player and releasing the player
        player.stop();
        player.release();

        // Re-initializing player to use it again
        player = new MediaPlayer();

        Toast.makeText(getApplicationContext(),"Stopped playing", Toast.LENGTH_SHORT).show();
    }

    public void stopRecording(View view){
        // Stopping the recorder and releasing the recorder
        recorder.stop();
        recorder.release();

        // Re-initializing recorder to use it again
        recorder = new MediaRecorder();

        Toast.makeText(getApplicationContext(), "Stopped recording", Toast.LENGTH_LONG).show();
    }

    public void playRecording(View view){

        try {
            // Loading the audio file from location
            player.setDataSource(fileName);
            // Preparing the player
            // Prepares the player for playback, synchronously.
            player.prepare();
            // Starting the player
            player.start();

            Toast.makeText(getApplicationContext(), "Playing the recording", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("Playing", "Exception while playing the recording");
        }
    }

    public void startRecording(View view){
        // Checking if all the necessary permission are approved or not
        if(CheckPermissions()) {
            // Setting the audio source to mic
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);

            // Setting the audio format
            // you can use other formats like ogg, mpeg4 etc..
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

            // Setting the encoder
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            // setting the output file to start the audio stream
            recorder.setOutputFile(fileName);
            try {
                // Preparing the recorder
                // Prepares the recorder to begin capturing and encoding data.
                recorder.prepare();
            } catch (IOException e) {
                Log.e("Recorder", "Exception while recording");
            }
            // Starting the recorder
            recorder.start();
            Toast.makeText(getApplicationContext(), "Started Recording", Toast.LENGTH_LONG).show();
        }
        else
        {
            // Requesting permissions
            Toast.makeText(getApplicationContext(), "Set the permissions and try again", Toast.LENGTH_LONG).show();
            RequestPermissions();
        }
    }

    public boolean CheckPermissions() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }

}
