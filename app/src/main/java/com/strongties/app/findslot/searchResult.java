package com.strongties.app.findslot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.strongties.app.findslot.subclasses.NotificationService;
import com.strongties.app.findslot.subclasses.RecyclerViewAdaptor_VCenters;
import com.strongties.app.findslot.subclasses.VCenter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Objects;

public class searchResult extends AppCompatActivity {
    Context mContext;

    RecyclerView rv_vcenters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        mContext = getApplicationContext();

        ArrayList<VCenter> centersList = new ArrayList<>();
        centersList = (ArrayList<VCenter>)getIntent().getSerializableExtra("centers");
        Log.d("Output", String.valueOf(centersList));

        rv_vcenters = findViewById(R.id.rv_search_results);

        LinearLayoutManager layoutManagerhot = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rv_vcenters.setLayoutManager(layoutManagerhot);
        RecyclerViewAdaptor_VCenters adapterhot = new RecyclerViewAdaptor_VCenters(this, centersList);
        rv_vcenters.setAdapter(adapterhot);


        TextView tv_pincode = findViewById(R.id.rv_search_result_pin);
        TextView tv_addr = findViewById(R.id.rv_search_result_addr);
        ImageView imageView = findViewById(R.id.rv_notif_bell);
        RelativeLayout notif_rl = findViewById(R.id.sr_notif_rl);
        ImageView iv_18 = findViewById(R.id.rv_notif_18);
        ImageView iv_45 = findViewById(R.id.rv_notif_45);
        ImageView iv_all = findViewById(R.id.rv_notif_all);

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                Glide.with(this)
                        .load(R.drawable.ic_notification_white)
                        .fitCenter()
                        .into(imageView);
                Glide.with(this)
                        .load(R.drawable.ic__eighteen_white)
                        .fitCenter()
                        .into(iv_18);
                Glide.with(this)
                        .load(R.drawable.ic__fourtyfive_white)
                        .fitCenter()
                        .into(iv_45);
                Glide.with(this)
                        .load(R.drawable.ic_all_white)
                        .fitCenter()
                        .into(iv_all);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                Glide.with(this)
                        .load(R.drawable.ic_notification_black)
                        .fitCenter()
                        .into(imageView);
                Glide.with(this)
                        .load(R.drawable.ic__eighteen)
                        .fitCenter()
                        .into(iv_18);
                Glide.with(this)
                        .load(R.drawable.ic__fourtyfive)
                        .fitCenter()
                        .into(iv_45);
                Glide.with(this)
                        .load(R.drawable.ic_all)
                        .fitCenter()
                        .into(iv_all);
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                break;
        }




        String pincode = centersList.get(0).getPincode();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("slot_prefs", MODE_PRIVATE);
        String savedpincode = pref.getString("pincode", "5");
        if (savedpincode.equals(pincode)){
            Glide.with(this)
                    .load(R.drawable.ic_notification)
                    .fitCenter()
                    .into(imageView);
        }


        // Set Pincode and Address into view
        tv_pincode.setText(pincode);
        String addr = centersList.get(0).getBlock() + ", " + centersList.get(0).getDistrict() + ", " + centersList.get(0).getState();
        tv_addr.setText(addr);


        //notification bell click
        imageView.setOnClickListener(view -> {
            notif_rl.setVisibility(View.VISIBLE);
        });

        //notification 18 click
        iv_18.setOnClickListener(view -> {
            notif_rl.setVisibility(View.GONE);
            Glide.with(this)
                    .load(R.drawable.ic_notification)
                    .fitCenter()
                    .into(imageView);

            SharedPreferences.Editor editor = getSharedPreferences("slot_prefs", MODE_PRIVATE).edit();
            editor.putString("pincode", pincode);
            editor.putString("notiftype", "18");
            editor.putBoolean("notifshow", true);
            editor.apply();

            Intent myService = new Intent(searchResult.this, NotificationService.class);
            stopService(myService);
        });

        //notification 45 click
        iv_45.setOnClickListener(view -> {
            notif_rl.setVisibility(View.GONE);
            Glide.with(this)
                    .load(R.drawable.ic_notification)
                    .fitCenter()
                    .into(imageView);

            SharedPreferences.Editor editor = getSharedPreferences("slot_prefs", MODE_PRIVATE).edit();
            editor.putString("pincode", pincode);
            editor.putString("notiftype", "45");
            editor.putBoolean("notifshow", true);
            editor.apply();

            Intent myService = new Intent(searchResult.this, NotificationService.class);
            stopService(myService);
        });

        //notification all click
        iv_all.setOnClickListener(view -> {
            notif_rl.setVisibility(View.GONE);
            Glide.with(this)
                    .load(R.drawable.ic_notification)
                    .fitCenter()
                    .into(imageView);

            SharedPreferences.Editor editor = getSharedPreferences("slot_prefs", MODE_PRIVATE).edit();
            editor.putString("pincode", pincode);
            editor.putString("notiftype", "all");
            editor.putBoolean("notifshow", true);
            editor.apply();

            Intent myService = new Intent(searchResult.this, NotificationService.class);
            stopService(myService);
        });


        //Dev Dialog
        TextView tv_dev = findViewById(R.id.sr_footer_dev);
        tv_dev.setOnClickListener(view -> {
            Dialog dev_dialog = new Dialog(this);
            dev_dialog.setContentView(R.layout.dialog_contact_us);
            Objects.requireNonNull(dev_dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dev_dialog.show();
        });

        //TOS
        TextView tv_tos = findViewById(R.id.sr_footer_tos);
        tv_tos.setOnClickListener(view -> {
            DocumentReference docRef = FirebaseFirestore.getInstance()
                    .collection("maintainance")
                    .document("tos");

            docRef.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    DocumentSnapshot doc =task.getResult();
                    assert doc != null;

                    Uri uri = Uri.parse(Objects.requireNonNull(doc.getString("url")));
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, search.class);
        startActivity(intent);
        this.finish();
    }
}