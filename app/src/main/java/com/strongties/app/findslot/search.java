package com.strongties.app.findslot;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
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
import com.strongties.app.findslot.subclasses.VCenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class search extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ProgressBar progressBar = findViewById(R.id.search_progressbar);
        EditText et_pincode = findViewById(R.id.search_by_pin);
        MaterialButton search_btn = findViewById(R.id.search_btn);

        ImageView iv_img = findViewById(R.id.search_img);
        ImageView iv_notif_stop = findViewById(R.id.notif_stop);


        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                Glide.with(this)
                        .load(R.drawable.ic_screen_bg_dark)
                        .centerCrop()
                        .into(iv_img);
                Glide.with(this)
                        .load(R.drawable.ic_notification_stop_white)
                        .centerCrop()
                        .into(iv_notif_stop);
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                Glide.with(this)
                        .load(R.drawable.ic_screen_bg)
                        .centerCrop()
                        .into(iv_img);
                Glide.with(this)
                        .load(R.drawable.ic_notification_stop)
                        .centerCrop()
                        .into(iv_notif_stop);
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                break;
        }


        //Dev Dialog
        TextView tv_dev = findViewById(R.id.footer_dev);
        tv_dev.setOnClickListener(view -> {
            Dialog dev_dialog = new Dialog(this);
            dev_dialog.setContentView(R.layout.dialog_contact_us);
            Objects.requireNonNull(dev_dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dev_dialog.show();
        });

        //TOS
        TextView tv_tos = findViewById(R.id.footer_tos);
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


        //Stop Notifications
        iv_notif_stop.setOnClickListener(view -> {
            SharedPreferences.Editor editor = getSharedPreferences("slot_prefs", MODE_PRIVATE).edit();
            editor.putString("pincode", "5");
            editor.putBoolean("notifshow", false);
            editor.apply();

            Intent myService = new Intent(search.this, NotificationService.class);
            stopService(myService);

            Toast.makeText(this, "Stopped Alerts", Toast.LENGTH_SHORT).show();
        });






        ArrayList<VCenter> centersList = new ArrayList<>();



        search_btn.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);

            String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            String pincode = et_pincode.getText().toString().trim();


            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);
            String url ="https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode="+pincode+"&date="+date;
            JsonObjectRequest request = new JsonObjectRequest(url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (null != response) {
                                //handle your response
                                try {
                                    //Log.d("Output", String.valueOf(response.length()));
                                    //Log.d("Output", String.valueOf(response));
                                    for (int i=0; i<response.length(); i++){
                                        // Log.d("Output", String.valueOf(response.getJSONArray("centers")));
                                        JSONArray centers= response.getJSONArray("centers");
                                        for (int j=0; j<centers.length(); j++){
                                            //Log.d("Output", String.valueOf(centers.getJSONObject(j)));
                                            //Individual Center
                                            JSONObject JSONcenter = centers.getJSONObject(j);
                                            VCenter center = new VCenter();
                                            center.setCenter_id(String.valueOf(JSONcenter.get("center_id")));
                                            center.setName(String.valueOf(JSONcenter.get("name")));
                                            center.setAddress(String.valueOf(JSONcenter.get("address")));
                                            center.setState(String.valueOf(JSONcenter.get("state_name")));
                                            center.setDistrict(String.valueOf(JSONcenter.get("district_name")));
                                            center.setBlock(String.valueOf(JSONcenter.get("block_name")));
                                            center.setPincode(String.valueOf(JSONcenter.get("pincode")));
                                            center.setOp_time_from(String.valueOf(JSONcenter.get("from")));
                                            center.setOp_time_to(String.valueOf(JSONcenter.get("to")));
                                            center.setFee_type(String.valueOf(JSONcenter.get("fee_type")));
                                            center.setSessions(JSONcenter.getJSONArray("sessions"));

                                            centersList.add(center);
                                            //Log.d("Output", center.toString());
                                        }
                                       // Log.d("Output", String.valueOf(centersList));
                                        progressBar.setVisibility(View.INVISIBLE);

                                        if(centersList.size()>0) {

                                            Intent intent = new Intent(search.this, searchResult.class);
                                            Bundle args = new Bundle();
                                            //  args.putSerializable("ARRAYLIST",(Serializable)centersList);
                                            intent.putExtra("centers", centersList);
                                            startActivity(intent);
                                            finish();

                                        }else{
                                            Toast.makeText(getBaseContext(), "No Vaccination Center Found!", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(search.this, "Incorrect Pincode", Toast.LENGTH_SHORT).show();

                }
            });
            queue.add(request);

        });

    }
}