package com.strongties.app.findslot.subclasses;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.strongties.app.findslot.MainActivity;
import com.strongties.app.findslot.search;
import com.strongties.app.findslot.searchResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {
    private int notificationId = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "strongties.findslot.servicenotif";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("Searching for available vaccines.")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        //startTimer();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("slot_prefs", MODE_PRIVATE);
        String pincode = pref.getString("pincode", "5");
        String notiftype = pref.getString("notiftype", "all");
        if (!pincode.equals("5")) {
            startTimer(pincode, notiftype);
        }
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
         stoptimertask();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("slot_prefs", MODE_PRIVATE);
        boolean notif = pref.getBoolean("notifshow", true);
        if (notif) {
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("restartservice");
            broadcastIntent.setClass(this, Restarter.class);
            this.sendBroadcast(broadcastIntent);
        }
    }


    void pintask(String pincode, String notiftype) {


        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());


        ArrayList<VCenter> vCenters = new ArrayList<>();
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode=" + pincode + "&date=" + date;
        JsonObjectRequest request = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (null != response) {
                            //handle your response
                            try {
                                //Log.d("Output", String.valueOf(response.length()));
                                //Log.d("Output", String.valueOf(response));
                                int count_18 = 0;
                                int count_45 = 0;
                                int count_free_18 = 0;

                                for (int i = 0; i < response.length(); i++) {
                                    // Log.d("Output", String.valueOf(response.getJSONArray("centers")));
                                    JSONArray centers = response.getJSONArray("centers");
                                    for (int j = 0; j < centers.length(); j++) {
                                        //Log.d("Output", String.valueOf(centers.getJSONObject(j)));
                                        //Individual Center
                                        JSONObject JSONcenter = centers.getJSONObject(j);
                                        VCenter center = new VCenter();
                                        center.setPincode(String.valueOf(JSONcenter.get("pincode")));
                                        center.setFee_type(String.valueOf(JSONcenter.get("fee_type")));
                                        center.setSessions(JSONcenter.getJSONArray("sessions"));

                                        vCenters.add(center);
                                        ArrayList<VSession> sess = center.getSessions();
                                        for (int k = 0; k < sess.size(); k++) {
                                            if (sess.get(k).getMin_age_limit().equals("18")) {
                                                count_18 += Integer.parseInt(sess.get(k).getAvl_capacity());
                                                if (center.getFee_type().equals("Free")){
                                                    count_free_18 += 1;
                                                }
                                            } else {
                                                count_45 += Integer.parseInt(sess.get(k).getAvl_capacity());
                                            }
                                        }
                                    }
                                }
                              //  Log.d("Output", String.valueOf(count_18));
                              //  Log.d("Output", String.valueOf(count_45));
                                String msg = "";
                                if (notiftype.equals("all")){
                                    msg = pincode+": "+" 18+ vaccines -> "+String.valueOf(count_18)+",  45+ vaccines -> "+String.valueOf(count_45);
                                }else if(notiftype.equals("18")){
                                    msg = pincode+": "+" 18+ vaccines -> Free - "+String.valueOf(count_free_18)+",  Paid - "+String.valueOf(count_18 - count_free_18);
                                }else{
                                    msg = pincode+": " + " 45+ vaccines -> " + String.valueOf(count_45);
                                }

                                send_notification("Available Vaccines Alert", msg);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);


    }

    private Timer timer;
    private TimerTask timerTask;

    public void startTimer(String pincode, String notiftype) {
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                pintask(pincode, notiftype);
            }
        };
        timer.schedule(timerTask, 1000, 1000 * 60 * 60); //run every hour
    }

    public void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    void send_notification(String title, String message){
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


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}