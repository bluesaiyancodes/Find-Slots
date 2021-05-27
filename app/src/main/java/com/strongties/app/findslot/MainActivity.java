package com.strongties.app.findslot;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.NotificationCompat;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.strongties.app.findslot.subclasses.NotificationService;
import com.strongties.app.findslot.subclasses.OnSwipeTouchListener;
import com.strongties.app.findslot.subclasses.Restarter;
import com.strongties.app.findslot.subclasses.VCenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private int notificationId = 1;

    OnSwipeTouchListener onSwipeTouchListener;

    Intent mServiceIntent;
    private NotificationService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Start Service
        mService = new NotificationService();
        mServiceIntent = new Intent(this, mService.getClass());
        if (!isMyServiceRunning(mService.getClass())) {
            startService(mServiceIntent);
        }

        //Version Check
        DocumentReference docRef = FirebaseFirestore.getInstance()
                .collection("maintainance")
                .document("versioncheck");

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot doc = task.getResult();
                assert doc != null;
                int versionCode = BuildConfig.VERSION_CODE;
                int cloudVersion = Objects.requireNonNull(doc.getLong("version")).intValue();
                Log.d("Firebase", String.valueOf(cloudVersion));

                if (cloudVersion > versionCode) {


                    Dialog outdated_dialog = new Dialog(this);
                    outdated_dialog.setContentView(R.layout.dialog_outdated);
                    Objects.requireNonNull(outdated_dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    ImageView iv_down = outdated_dialog.findViewById(R.id.dg_download);
                    iv_down.setOnClickListener(view -> {
                        Uri uri = Uri.parse(Objects.requireNonNull(doc.getString("downloadurl")));
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    });
                    outdated_dialog.show();

                }
            }
        });

        SharedPreferences pref = getApplicationContext().getSharedPreferences("slot_prefs", MODE_PRIVATE);
        boolean firstrun = pref.getBoolean("firstrun", true);

        if (firstrun) {
            SharedPreferences.Editor editor = getSharedPreferences("slot_prefs", MODE_PRIVATE).edit();
            editor.putBoolean("firstrun", false);
            editor.apply();

            Dialog fr_dialog = new Dialog(this);
            fr_dialog.setContentView(R.layout.dialog_guide);
            Objects.requireNonNull(fr_dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            ImageView iv_notifOn = fr_dialog.findViewById(R.id.dg_notif_on);
            ImageView iv_notifOff = fr_dialog.findViewById(R.id.dg_notif_off);
            int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            switch (nightModeFlags) {
                case Configuration.UI_MODE_NIGHT_YES:
                    Glide.with(this)
                            .load(R.drawable.ic_notification_white)
                            .centerCrop()
                            .into(iv_notifOn);
                    Glide.with(this)
                            .load(R.drawable.ic_notification_stop_white)
                            .centerCrop()
                            .into(iv_notifOff);
                    break;

                case Configuration.UI_MODE_NIGHT_NO:
                    Glide.with(this)
                            .load(R.drawable.ic_notification_black)
                            .centerCrop()
                            .into(iv_notifOn);
                    Glide.with(this)
                            .load(R.drawable.ic_notification_stop)
                            .centerCrop()
                            .into(iv_notifOff);
                    break;

                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    break;
            }

            fr_dialog.show();
        }


        onSwipeTouchListener = new OnSwipeTouchListener(this, findViewById(R.id.main_relative));

        //blink text
        TextView swp_up = findViewById(R.id.main_screen_tail1);

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(600);
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        swp_up.startAnimation(anim);

        ImageView iv_top = findViewById(R.id.im_top);
        ImageView iv_bottom = findViewById(R.id.im_bottom);

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                Glide.with(this)
                        .load(R.drawable.ic_covid19_vaccination_dark)
                        .centerCrop()
                        .into(iv_top);

                Glide.with(this)
                        .load(R.drawable.ic_wave_screen1_dark)
                        .fitCenter()
                        .into(iv_bottom);
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                Glide.with(this)
                        .load(R.drawable.ic_covid19_vaccination)
                        .centerCrop()
                        .into(iv_top);

                Glide.with(this)
                        .load(R.drawable.ic_wave_screen1)
                        .centerCrop()
                        .into(iv_bottom);
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                break;
        }


        //for(int i=0; i<centersList.size(); i++)

        MaterialButton btn = findViewById(R.id.btn_notif);
        btn.setOnClickListener(view -> {

            send_notification("Test", "Testing notification");

        });


    }

    void send_notification(String title, String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "Default";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("Service status", "Running");
                return true;
            }
        }
        Log.i("Service status", "Not running");
        return false;
    }

    @Override
    protected void onDestroy() {
        //stopService(mServiceIntent);
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
        super.onDestroy();
    }


}