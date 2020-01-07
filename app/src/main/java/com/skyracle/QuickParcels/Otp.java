package com.skyracle.QuickParcels;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class Otp extends AppCompatActivity {

    WebServiceClass webServiceClass1 = new WebServiceClass();
    Pinview pinview;
    Button button;
    TextView mobilenumber, resendotp, timer, otpmsg;
    CountDownTimer countDownTimer;

    String otp, number;

    ImageView backimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        backimg=(ImageView)findViewById(R.id.back_img);
        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });

        Intent i = getIntent();
        number = i.getStringExtra("mobile");
        otp = i.getStringExtra("OTP");
        otpmsg = (TextView) findViewById(R.id.otpmsg);
        resendotp = (TextView) findViewById(R.id.resendotp);
        timer = (TextView) findViewById(R.id.timer);
        mobilenumber = (TextView) findViewById(R.id.number);
        mobilenumber.setText(number);

        StartCountDown();
        countDownTimer.start();


        pinview = findViewById(R.id.pinview);

        resendotp.setVisibility(View.GONE);
        resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                countDownTimer.cancel();
                final ProgressDialog pd = new ProgressDialog(Otp.this);
                pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                pd.setCancelable(false);
                pd.setCanceledOnTouchOutside(false);
                pd.show();
                pd.setContentView(R.layout.progress);

                otpmsg.setVisibility(View.GONE);
                String Url = webServiceClass1.getUrl() + webServiceClass1.getMethodGetotp()+"/"+number;
                StringRequest request = new StringRequest(Request.Method.POST, Url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    pd.dismiss();
                                    JSONArray array = new JSONArray(response);
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject object = array.getJSONObject(i);
                                        String msg = object.getString("MESSAGE");
                                        otp = object.getString("OTP");
                                        resendotp.setEnabled(false);
                                        resendotp.setVisibility(View.GONE);
                                        StartCountDown();
                                        countDownTimer.start();

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        // Toast.makeText(OTPActivity.this, "Connection Problem ", Toast.LENGTH_SHORT).show();
                    }
                }) /*{
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("user_mobile_number",number);
                        return params;
                    }
                }*/;
                RequestQueue requestQueue = Volley.newRequestQueue(Otp.this);
                requestQueue.add(request);

            }
        });
    }

    private void StartCountDown() {

        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText(millisUntilFinished / 1000 + " sec");

                pinview.setPinViewEventListener(new Pinview.PinViewEventListener() {
                    @Override
                    public void onDataEntered(Pinview pinview, boolean fromUser) {

                        if (pinview.getValue().toString().equals(otp)) {
                            Intent intent = new Intent(Otp.this, UserRegistration.class);
                            intent.putExtra("number", number);
                            startActivity(intent);
                        } else {
                            Toast.makeText(Otp.this, "Invalid Otp", Toast.LENGTH_SHORT).show();
                            pinview.clearValue();
                            otpmsg.setVisibility(View.VISIBLE);
                            resendotp.setVisibility(View.VISIBLE);
                        }
                    }
                });

            }

            @Override
            public void onFinish() {
                timer.setText("");
                resendotp.setFocusable(true);
                resendotp.setEnabled(true);
                resendotp.setVisibility(View.VISIBLE);

            }
        };

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}