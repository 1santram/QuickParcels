package com.skyracle.QuickParcels;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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

public class UserProfile extends AppCompatActivity {

    WebServiceClass webServiceClass=new WebServiceClass();
    TextView usernumber;
    EditText username,useremail,userflat,userstreet,usercity,userstate,userpincode;
    RadioGroup radiogender,radioGroup;
    RadioButton radio1,radio2,radio4,radio5;
    Button buttonregister;

    String home="Home";
    String gender="Male";
    String password;
    String mobile,email;

    ImageView backimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        backimg=(ImageView)findViewById(R.id.back_img);
        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        usernumber=findViewById(R.id.usermobile);
        username=findViewById(R.id.username);
        useremail=findViewById(R.id.email);
        userflat=findViewById(R.id.userflat);
        userstreet=findViewById(R.id.userlocality);
        usercity=findViewById(R.id.usercity);
        userstate=findViewById(R.id.userstate);
        userpincode=findViewById(R.id.userpicode);
        radio1=findViewById(R.id.radio1);
        radio2=findViewById(R.id.radio2);
        radio4=findViewById(R.id.radio4);
        radio5=findViewById(R.id.radio5);


        loadUserData();

        radioGroup=(RadioGroup)findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.radio4) {
                    home="Home";
                } else  {
                    home="Office";
                }
            }

        });

        radiogender=findViewById(R.id.radiogender);
        radiogender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.radio1) {
                    gender="Male";
                } else  {
                    gender="Female";
                }
            }

        });


        buttonregister=findViewById(R.id.buttonupdate);
        buttonregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(username.getText())) {
                    username.setError("First name is required!");
                } else if (TextUtils.isEmpty(userflat.getText())) {
                    userflat.setError("Flat No is required!");
                } else if (TextUtils.isEmpty(userstreet.getText())) {
                    userstreet.setError("Locality  is required!");
                } else if (TextUtils.isEmpty(usercity.getText())) {
                    usercity.setError("City  is required!");
                } else if (TextUtils.isEmpty(userstate.getText())) {
                    userstate.setError("State is required!");
                } else if (TextUtils.isEmpty(userpincode.getText())) {
                    userpincode.setError("Pincode is required!");
                } else if (userpincode.length() < 6) {
                    userpincode.setError("Check Pincode");
                } else {
                    String emailInput = useremail.getText().toString().trim();

                    if (emailInput.length() > 1) {
                        if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                            useremail.setError("Please Enter Valid Email Address!!!");
                        } else {
                            updateUser();
                        }
                    } else {
                        updateUser();

                    }

                }
            }
        });
    }

    private void loadUserData() {
        String userid=Utils.getSavedPreferences(UserProfile.this,Hom.userid,"");
        String Urls=webServiceClass.Url+webServiceClass.getMethodGetuser();
        StringRequest request = new StringRequest(Request.Method.POST,Urls,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array1 = new JSONArray(response);
                            for (int i=0;i<array1.length();i++){
                                JSONObject object=array1.getJSONObject(i);
                              mobile= (object.getString("user_mobile_no"));
                                username.setText(object.getString("user_name"));
                                String email=object.getString("email_id");
                                if (email.length()>4){
                                    useremail.setText(email);
                                }else{
                                    useremail.setText("");
                                }


                                userflat.setText(object.getString("street"));
                                userstreet.setText(object.getString("locality"));
                                usercity.setText(object.getString("city"));
                                userstate.setText(object.getString("state"));
                                userpincode.setText(object.getString("pincode"));
                                password=object.getString("password");
                                String address=object.getString("address_type");
                                if (address.equals("Home")){
                                    radio4.setChecked(true);
                                }else{
                                    radio5.setChecked(true);
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
                params.put(" user_id",userid);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(UserProfile.this);
        requestQueue.add(request);

    }

    private void updateUser() {

        String userid=Utils.getSavedPreferences(UserProfile.this,Hom.userid,"");
        String Urls=webServiceClass.getUrl()+webServiceClass.getMethodUpdateuser();
        StringRequest request = new StringRequest(Request.Method.POST,Urls,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject array1 = new JSONObject(response);
                            String msg=  array1.getString("MESSAGE");
                            if (msg.equals("DATA UPDATE SUCCESSFULLY")){
                                Intent intent=new Intent(UserProfile.this,Hom.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }else {
                                Toast.makeText(UserProfile.this, "Try after some time", Toast.LENGTH_SHORT).show();
                              //  onBackPressed();
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
                params.put(" user_id",userid);
                params.put("u_name",username.getText().toString());
                params.put("u_mobile_number",mobile);
                params.put("u_email_id",useremail.getText().toString());
                params.put("u_password",password);
                params.put("u_street",userflat.getText().toString());
                params.put("u_locality",userstreet.getText().toString());
                params.put("u_city",usercity.getText().toString());
                params.put("u_state",userstate.getText().toString());
                params.put("u_pincode",userpincode.getText().toString());
                params.put("u_address_type",home.toString());
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(UserProfile.this);
        requestQueue.add(request);


    }

}

