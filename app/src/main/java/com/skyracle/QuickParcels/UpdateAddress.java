package com.skyracle.QuickParcels;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.skyracle.QuickParcels.Services.WebServiceClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdateAddress extends AppCompatActivity {

    WebServiceClass webServiceClass = new WebServiceClass();
    Button updateaddress;
    EditText username, usermobile, userflat, userstreet, usernearby, usercity, userstate, userpincode;
    RadioGroup radioGroup;
    RadioButton radioButton1, radioButton2;
    String home = "Home";

    ImageView backimg;

    String addressid;
    String layout;

    String pickup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_address);

        pickup=getIntent().getStringExtra("PickupOrder");
        addressid=getIntent().getStringExtra("addressid");
        layout=getIntent().getStringExtra("layout");
        backimg = (ImageView) findViewById(R.id.back_img);
        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        usermobile = findViewById(R.id.mobile);
        username = findViewById(R.id.username);
        userflat = findViewById(R.id.userflat);
        userstreet = findViewById(R.id.userstreet);
        usernearby = findViewById(R.id.usernearby);
        usercity = findViewById(R.id.usercity);
        userstate = findViewById(R.id.userstate);
        userpincode = findViewById(R.id.userpicode);

        radioGroup = findViewById(R.id.radiogroup);
        radioButton1 = findViewById(R.id.radio4);
        radioButton2 = findViewById(R.id.radio5);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio4) {
                    home = "Home";
                } else {
                    home = "Office";
                }
            }
        });

        showAddressInfo();


        updateaddress = findViewById(R.id.buttonaddpick);
        updateaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (TextUtils.isEmpty(usermobile.getText()) && usermobile.getText().length() != 10) {
                    usermobile.setError("Check mobile number!");
                } else if (TextUtils.isEmpty(username.getText())) {
                    username.setError("First name is required!");
                } else if (TextUtils.isEmpty(userflat.getText())) {
                    userflat.setError("Flat No is required!");
                } else if (TextUtils.isEmpty(userstreet.getText())) {
                    userstreet.setError("Locality  is required!");
                } else if (TextUtils.isEmpty(usercity.getText())) {
                    usercity.setError("City  is required!");
                } else if (TextUtils.isEmpty(usernearby.getText())) {
                    usernearby.setError("Nearby place is required!");
                } else if (TextUtils.isEmpty(userpincode.getText()) && userpincode.getText().length() != 6) {
                    userpincode.setError("Check pincode!");
                } else {


                    upadateAddress();

                }


            }
        });
    }

    private void showAddressInfo() {

        String Urls=webServiceClass.getUrl()+webServiceClass.getMethodAddressdetails();
        StringRequest request = new StringRequest(Request.Method.POST,Urls,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array=new JSONArray(response);
                            for (int i=0;i<array.length();i++){
                                JSONObject object=array.getJSONObject(i);

                                username.setText(object.getString("user_name"));
                                usermobile.setText(object.getString("user_mobile_no"));
                                userflat.setText(object.getString("flat_number"));
                                userstreet.setText(object.getString("street"));
                                usernearby.setText(object.getString("near_by_place"));
                                usercity.setText(object.getString("city"));
                                userstate.setText(object.getString("state"));
                                userpincode.setText(object.getString("pincode"));
                                String add=object.getString("address_type");
                                if (add.equals("Home")){
                                    radioButton1.setChecked(true);
                                }else{
                                    radioButton2.setChecked(true);
                                }

                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            protected Map<String,String> getParams()
            {
                Map<String,String> params=new HashMap<String, String>();
                String userid=Utils.getSavedPreferences(UpdateAddress.this,Hom.userid,"");
                params.put("user_id",userid);
                params.put("address_id",addressid);


                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(UpdateAddress.this);
        requestQueue.add(request);


    }

    private void upadateAddress() {
        String Urls=webServiceClass.getUrl()+webServiceClass.getMethodUpdateperadd();
        StringRequest request = new StringRequest(Request.Method.POST,Urls,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject object=new JSONObject(response);
                            String msg=object.getString("MESSAGE");
                            if (msg.equals("DATA UPDATE SUCCESSFULLY")){
                                if (layout.equals("pickup")){
                                    Intent intent=new Intent(UpdateAddress.this,PickUpLocation.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }else if (layout.equals("drop")){
                                    Intent intent=new Intent(UpdateAddress.this,DropLocation.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("PickupOrder",pickup);
                                    startActivity(intent);
                                }

                            }else{
                                Toast.makeText(UpdateAddress.this, "Server Problem", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            protected Map<String,String> getParams()
            {
                Map<String,String> params=new HashMap<String, String>();
                String userid=Utils.getSavedPreferences(UpdateAddress.this,Hom.userid,"");
                params.put("user_id",userid);
                params.put("address_id",addressid);
                params.put("u_user_mobile_no",usermobile.getText().toString());
                params.put("u_user_name",username.getText().toString());
                params.put("u_flat_number",userflat.getText().toString());
                params.put("u_street",userstreet.getText().toString());
                params.put("u_near_by_place",usernearby.getText().toString());
                params.put("u_city",usercity.getText().toString());
                params.put("u_state",userstate.getText().toString());
                params.put("u_address_type",home.toString());
                params.put("u_pincode",userpincode.getText().toString());

                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(UpdateAddress.this);
        requestQueue.add(request);
    }
}