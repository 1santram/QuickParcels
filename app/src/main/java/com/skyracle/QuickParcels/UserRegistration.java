package com.skyracle.QuickParcels;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.skyracle.QuickParcels.Services.WebServiceClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRegistration extends AppCompatActivity {

    WebServiceClass webServiceClass=new WebServiceClass();
    TextView usernumber;
    EditText username,useremail,userpassword,usercnfrmpassword,userflat,userstreet,usercity,userstate,userpincode;
    RadioGroup radiogender,radioGroup;
    RadioButton radio1,radio2,radio4,radio5;
    Button buttonregister;
    ImageView viewimg,hideimg;

    Spinner spinnerstate,spinnercity;

    String home="Home";
    String gender="Male";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        //Check permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndGrantPermissions();
        }

        spinnerstate=findViewById(R.id.spinnerstate);
        spinnercity=findViewById(R.id.spinnercity);

        ArrayList<String> arraystate=new ArrayList<>();
        ArrayList<String> arraycity=new ArrayList<>();

        arraystate.add("Select State");
        arraystate.add("Uttar Pradesh");

        arraycity.add("Select City");
        arraycity.add("Lucknow");

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, arraystate);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinnerstate.setAdapter(adapter);

        ArrayAdapter<String> adapter1 =
                new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, arraycity);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinnercity.setAdapter(adapter1);

        spinnerstate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) view).setTextColor(Color.GRAY);
                ((TextView) view).setTextSize(14);
                String name=spinnerstate.getSelectedItem().toString();

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        spinnercity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) view).setTextColor(Color.GRAY);
                ((TextView) view).setTextSize(14);
                String name=spinnercity.getSelectedItem().toString();

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });





        usernumber=findViewById(R.id.usermobile);
        String number=getIntent().getStringExtra("number");
        usernumber.setText(number);

        username=findViewById(R.id.username);
        useremail=findViewById(R.id.useremail);
        userpassword=findViewById(R.id.userpassword);
        usercnfrmpassword=findViewById(R.id.usercnfpassword);
        userflat=findViewById(R.id.userflat);
        userstreet=findViewById(R.id.userlocality);
       // usercity=findViewById(R.id.usercity);
       // userstate=findViewById(R.id.userstate);
        userpincode=findViewById(R.id.userpicode);
        radio1=findViewById(R.id.radio1);
        radio2=findViewById(R.id.radio2);
        radio4=findViewById(R.id.radio4);
        radio5=findViewById(R.id.radio5);

        viewimg=findViewById(R.id.view);
        hideimg=findViewById(R.id.hide);

        viewimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                usercnfrmpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                viewimg.setVisibility(View.GONE);
                hideimg.setVisibility(View.VISIBLE);
            }
        });

        hideimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                userpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                usercnfrmpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

                hideimg.setVisibility(View.GONE);
                viewimg.setVisibility(View.VISIBLE);
            }
        });

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

        buttonregister=findViewById(R.id.buttonregister);
        buttonregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(username.getText())) {
                    username.setError("First name is required!");
                } else if (TextUtils.isEmpty(userflat.getText())) {
                    userflat.setError("Flat No is required!");
                } else if (TextUtils.isEmpty(userstreet.getText())) {
                    userstreet.setError("Locality  is required!");
                } /*else if (TextUtils.isEmpty(usercity.getText())) {
                    usercity.setError("City  is required!");
                } else if (TextUtils.isEmpty(userstate.getText())) {
                    userstate.setError("State is required!");
                }*/
                else  if (spinnerstate.getSelectedItem().equals("Select State")) {
                    Toast.makeText(UserRegistration.this, "Select State", Toast.LENGTH_SHORT).show();
                }else  if (spinnercity.getSelectedItem().equals("Select City")) {
                    Toast.makeText(UserRegistration.this, "Select City", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(userpincode.getText())) {
                    userpincode.setError("Pincode is required!");
                } else if (userpincode.length() < 6) {
                    userpincode.setError("Check Pincode");
                } else if (TextUtils.isEmpty(userpassword.getText())) {
                    userpassword.setError("Password is required");
                    usercnfrmpassword.setError(("required"));
                } else if (userpassword.length() < 8) {
                    userpassword.setError("Minimun 8 characters");
                    userpassword.requestFocus();
                } else if (!userpassword.getText().toString().equals(usercnfrmpassword.getText().toString())) {
                    Toast.makeText(UserRegistration.this, "Check Password", Toast.LENGTH_SHORT).show();
                } else {
                    String emailInput = useremail.getText().toString().trim();

                    if (emailInput.length() > 1) {
                        if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                            useremail.setError("Please Enter Valid Email Address!!!");
                        } else {
                            registerUser();
                        }
                    } else {
                        registerUser();

                    }

                }
            }
        });
    }

    private void registerUser() {

        final ProgressDialog pd = new ProgressDialog(UserRegistration.this);
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        pd.setContentView(R.layout.progress);

        String Urls=webServiceClass.getUrl()+webServiceClass.getMethodUserregistration();
        StringRequest request = new StringRequest(Request.Method.POST,Urls,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            pd.dismiss();
                            JSONObject array1 = new JSONObject(response);
                            String msg=  array1.getString("MESSAGE");
                            if (msg.equals("DATA INSERT SUCCESSFULLY")){
                                String userid=array1.getString("USER ID");
                                try {
                                    String umobile=usernumber.getText().toString();
                                    String password=usercnfrmpassword.getText().toString();
                                    ConnectionHelper con = new ConnectionHelper(UserRegistration.this);
                                    SQLiteDatabase dt = con.getWritableDatabase();
                                    String q = "insert into userinfo(number,userpwd,userid) values('" + umobile + "','" + password + "','" + userid +"')";
                                    dt.execSQL(q);
                                    dt.close();
                                    con.close();
                                }catch (Exception e){
                                }

                                Intent intent=new Intent(UserRegistration.this,Hom.class);
                                startActivity(intent);

                            }else {
                                Toast.makeText(UserRegistration.this, "Try after some time", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
            }
        }) {
            protected Map<String,String> getParams()
            {
                Map<String,String> params=new HashMap<String, String>();
                params.put("u_name",username.getText().toString());
                params.put("u_mobile_number",usernumber.getText().toString());
                params.put("u_email_id",useremail.getText().toString());
                params.put("u_password",usercnfrmpassword.getText().toString());
                params.put("u_street",userflat.getText().toString());
                params.put("u_locality",userstreet.getText().toString());
                params.put("u_city",spinnercity.getSelectedItem().toString());
                params.put("u_state",spinnerstate.getSelectedItem().toString());
                params.put("u_pincode",userpincode.getText().toString());
                params.put("u_address_type",home.toString());
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(UserRegistration.this);
        requestQueue.add(request);

    }


    @TargetApi(Build.VERSION_CODES.M)
    private void checkAndGrantPermissions() {

        int location = checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> permissions = new ArrayList<String>();



        if (location == PackageManager.PERMISSION_DENIED) {
            permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!permissions.isEmpty()) {
            requestPermissions(permissions.toArray(new String[permissions.size()]), 200);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 200) {
            if ((grantResults[0] == PackageManager.PERMISSION_DENIED) ) {
                finish();

            }

        }
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                //denied
                Log.e("denied", permission);
            } else {
                if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                    //allowed
                    Log.e("allowed", permission);
                } else {
                    //set to never ask again
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", UserRegistration.this.getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
            }
        }
    }

}
