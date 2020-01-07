package com.skyracle.QuickParcels;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.goodiebag.pinview.Pinview;
import com.skyracle.QuickParcels.Services.WebServiceClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Login extends AppCompatActivity {

    WebServiceClass webServiceClass=new WebServiceClass();
    Button button;
    TextView signup,forgotpassword;
    EditText usermobile,userpwd;
    TextView terms,privacy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Check permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndGrantPermissions();
        }

        terms=findViewById(R.id.terms);
        privacy=findViewById(R.id.privacy);

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialogs = new Dialog(Login.this,android.R.style.Theme_DeviceDefault_NoActionBar_TranslucentDecor);
                dialogs.setContentView(R.layout.terms);

                ImageView img=(ImageView)dialogs.findViewById(R.id.close_img) ;
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogs.dismiss();
                    }
                });

                dialogs.show();
            }
        });

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialogs = new Dialog(Login.this,android.R.style.Theme_DeviceDefault_NoActionBar_TranslucentDecor);
                dialogs.setContentView(R.layout.privacy);
                ImageView img=(ImageView)dialogs.findViewById(R.id.close_img) ;
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogs.dismiss();
                    }
                });

                dialogs.show();
            }
        });

        usermobile=(EditText)findViewById(R.id.usermobile);
        userpwd=(EditText)findViewById(R.id.userpassword);


        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(usermobile.getText())){
                    usermobile.setError("Enter Registerd Mobile");
                }else if (TextUtils.isEmpty(userpwd.getText())){
                    userpwd.setError("Enter Password");
                }else
                {
                   /* Intent intent=new Intent(Login.this,HomePage.class);
                    startActivity(intent);*/
                    LoginUser();
                }

            }
        });

        forgotpassword=findViewById(R.id.forgotpassword);
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgot();
            }
        });

        signup=findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,MainActivity.class);
                startActivity(intent);
            }
        });


    }



    private void forgot() {

        final AlertDialog dialogBuilder = new AlertDialog.Builder(Login.this,R.style.MyDialogTheme).create();
        dialogBuilder.setTitle("Forgot Password");
        LayoutInflater inflater = Login.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custondialog, null);
        final EditText editText = (EditText) dialogView.findViewById(R.id.edt_comment);
        Button button1 = (Button) dialogView.findViewById(R.id.buttoncancel);
        Button button2 = (Button) dialogView.findViewById(R.id.buttonsubmit);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String numb = editText.getText().toString();
                final ProgressDialog pd = new ProgressDialog(Login.this);
                pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                pd.setCancelable(false);
                pd.setCanceledOnTouchOutside(false);
                pd.show();
                pd.setContentView(R.layout.progress);


                String Url = webServiceClass.getUrl() + webServiceClass.getMethodForgotpass()+"/"+numb;
                StringRequest request = new StringRequest(Request.Method.POST, Url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {

                                    JSONArray array = new JSONArray(response);
                                    for (int i = 0; i < array.length(); i++) {
                                        dialogBuilder.dismiss();
                                        pd.dismiss();
                                        JSONObject object = array.getJSONObject(i);
                                        String msg = object.getString("MESSAGE");
                                        if (msg.equalsIgnoreCase("MOBILE NUMBER NOT EXISTS")) {
                                            Toast.makeText(Login.this, "Mobile Number Not Exist", Toast.LENGTH_SHORT).show();
                                        } else {
                                            String user=object.getString("USER ID");
                                            String otp = object.getString("OTP");
                                            final Dialog dialogs = new Dialog(Login.this, android.R.style.Theme_DeviceDefault_NoActionBar_TranslucentDecor);
                                            dialogs.setTitle("Enter Otp Here..");
                                            dialogs.setContentView(R.layout.complete_otp);

                                            TextView textView = (TextView) dialogs.findViewById(R.id.numbers);
                                            textView.setText(numb);
                                            ImageView img = (ImageView) dialogs.findViewById(R.id.close_img);
                                            img.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialogs.dismiss();
                                                }
                                            });
                                            Pinview pinview = (Pinview) dialogs.findViewById(R.id.pinviews);
                                            pinview.setPinViewEventListener(new Pinview.PinViewEventListener() {
                                                @Override
                                                public void onDataEntered(Pinview pinview, boolean fromUser) {

                                                    dialogs.dismiss();
                                                    if (pinview.getValue().toString().equals(otp)) {

                                                        final ProgressDialog pd = new ProgressDialog(Login.this);
                                                        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                        pd.setCancelable(false);
                                                        pd.setCanceledOnTouchOutside(false);
                                                        pd.show();
                                                        pd.setContentView(R.layout.progress);

                                                        //update User login password
                                                        String Urls = webServiceClass.getUrl() + webServiceClass.getMethodGetuser();
                                                        StringRequest request1 = new StringRequest(Request.Method.POST, Urls,
                                                                new Response.Listener<String>() {
                                                                    @Override
                                                                    public void onResponse(String response) {
                                                                        try {
                                                                            JSONArray array = new JSONArray(response);
                                                                            for (int i = 0; i < array.length(); i++) {
                                                                                pd.dismiss();
                                                                                JSONObject object = array.getJSONObject(i);

                                                                                String pass =object.getString("password");

                                                                                ConnectionHelper con = new ConnectionHelper(Login.this);
                                                                                SQLiteDatabase dt = con.getWritableDatabase();
                                                                                String q = "insert into userinfo(number,userpwd,userid) values('" + numb + "','" + pass + "','" + user +"')";
                                                                                dt.execSQL(q);
                                                                                con.close();

                                                                                Intent intent = new Intent(Login.this, Hom.class);
                                                                                startActivity(intent);
                                                                            }
                                                                        } catch (JSONException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    }
                                                                }, new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {
                                                                pd.dismiss();
                                                                // Toast.makeText(UserLogin.this, "Connection Problem", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }){protected Map<String,String> getParams()
                                                        {
                                                            Map<String,String> params=new HashMap<String, String>();
                                                            params.put(" user_id",user);

                                                            return params;
                                                        }
                                                    };
                                                        RequestQueue requestQueue1 = Volley.newRequestQueue(Login.this);
                                                        requestQueue1.add(request1);

                                                    } else {
                                                        pd.dismiss();
                                                        Toast.makeText(Login.this, "Invalid Otp", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                            dialogs.show();
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        //  Toast.makeText(UserLogin.this, "Connection Problem ", Toast.LENGTH_SHORT).show();
                    }
                })/*{
                    protected Map<String,String> getParams()
                    {
                        Map<String,String> params=new HashMap<String, String>();
                        params.put("user_mobile_no",numb);

                        return params;
                    }
                }*/;
                RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
                requestQueue.add(request);

            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    private void LoginUser() {

        final ProgressDialog pd = new ProgressDialog(Login.this);
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        pd.setContentView(R.layout.progress);

        String Urls=webServiceClass.getUrl()+webServiceClass.METHOD_USERLOGIN;
        StringRequest request = new StringRequest(Request.Method.POST,Urls,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            pd.dismiss();
                            JSONObject array1 = new JSONObject(response);
                            String msg=array1.getString("MESSAGE");
                            if (msg.equals("LOGIN SUCCESSFULLY")){

                                String userid=array1.getString("USER_ID");
                                ConnectionHelper con = new ConnectionHelper(Login.this);
                                SQLiteDatabase dt = con.getWritableDatabase();
                                String q = "insert into userinfo(number,userpwd,userid) values('" + usermobile.getText().toString() + "','" + userpwd.getText().toString() + "','" + userid +"')";
                                dt.execSQL(q);
                                dt.close();
                                con.close();
                                Intent intent=new Intent(Login.this,Hom.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(Login.this, "Invalid Login", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(Login.this, "No Internet", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String,String> getParams()
            {
                Map<String,String> params=new HashMap<String, String>();
                params.put("user_mobile_number",usermobile.getText().toString());
                params.put("user_password",userpwd.getText().toString());

                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(Login.this);
        requestQueue.add(request);
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void checkAndGrantPermissions() {

        int location = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> permissions = new ArrayList<String>();



        if (location == PackageManager.PERMISSION_DENIED) {
            permissions.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
                    Uri uri = Uri.fromParts("package", Login.this.getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}

