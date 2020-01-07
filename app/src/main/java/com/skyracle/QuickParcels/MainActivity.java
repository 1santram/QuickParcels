package com.skyracle.QuickParcels;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.skyracle.QuickParcels.Services.WebServiceClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    WebServiceClass webServiceClass=new WebServiceClass();
    Button button;
    EditText userno;
    TextView usersignin;

    TextView terms,privacy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userno=findViewById(R.id.userno);


        terms=findViewById(R.id.terms);
        privacy=findViewById(R.id.privacy);

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialogs = new Dialog(MainActivity.this,android.R.style.Theme_DeviceDefault_NoActionBar_TranslucentDecor);
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
                final Dialog dialogs = new Dialog(MainActivity.this,android.R.style.Theme_DeviceDefault_NoActionBar_TranslucentDecor);
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

        usersignin=findViewById(R.id.signin);
        usersignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        button=findViewById(R.id.buttonsont);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(userno.getText().toString())){
                    userno.setError("Enter mobile number");
                }else{

                    final ProgressDialog pd = new ProgressDialog(MainActivity.this);
                    pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    pd.setCancelable(false);
                    pd.setCanceledOnTouchOutside(false);
                    pd.show();
                    pd.setContentView(R.layout.progress);

                    String Urls=webServiceClass.getUrl()+webServiceClass.getMethodGetotp()+"/"+userno.getText().toString();
                    StringRequest request = new StringRequest(Request.Method.POST,Urls,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        pd.dismiss();
                                        JSONArray array1 = new JSONArray(response);
                                        for (int i=0;i<array1.length();i++){
                                            JSONObject object=array1.getJSONObject(i);
                                            String msg=object.getString("MESSAGE");
                                            if (msg.equals("OTP SEND SUCCESSFULL")){

                                                String otp=object.getString("OTP");
                                                Intent intent=new Intent(MainActivity.this,Otp.class);
                                                intent.putExtra("mobile",userno.getText().toString());
                                                intent.putExtra("OTP",otp);
                                                startActivity(intent);

                                            }else{
                                                Toast.makeText(MainActivity.this, "Mobile Number Already Exist", Toast.LENGTH_SHORT).show();
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
                        }
                    }) /*{
                        protected Map<String,String> getParams()
                        {
                            Map<String,String> params=new HashMap<String, String>();
                            params.put("user_mobile_number",userno.getText().toString());
                            return params;
                        }
                    }*/;
                    RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);
                    requestQueue.add(request);
                }
            }
        });

    }
}
