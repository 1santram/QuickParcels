package com.skyracle.QuickParcels.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.skyracle.QuickParcels.ConnectionHelper;
import com.skyracle.QuickParcels.Hom;
import com.skyracle.QuickParcels.R;
import com.skyracle.QuickParcels.Services.WebServiceClass;
import com.skyracle.QuickParcels.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class fragment_changemobile extends Fragment {

    WebServiceClass webServiceClass = new WebServiceClass();
    EditText edtnewmobile, edtcnfnewmobile;
    Button buttonupdatemobile;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.layout_changemobile, container, false);

        edtnewmobile = view.findViewById(R.id.newmobile);
        edtcnfnewmobile = view.findViewById(R.id.cnfnewmobile);
        buttonupdatemobile = view.findViewById(R.id.updatemobile);
        buttonupdatemobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(edtnewmobile.getText().toString())) {
                    edtnewmobile.setError("Enter Mobile Number");
                } else if (TextUtils.isEmpty(edtcnfnewmobile.getText().toString())) {
                    edtcnfnewmobile.setError("Enter Confirm Mobile Number");
                } else if (edtnewmobile.getText().toString() == edtcnfnewmobile.getText().toString()) {
                    Toast.makeText(getActivity(), "Mobile Number Not Matched", Toast.LENGTH_SHORT).show();
                } else {
                    String userid=Utils.getSavedPreferences(getActivity(), Hom.userid,"");
                    String Url = webServiceClass.getUrl() + webServiceClass.getMethodGetregotp()+"/"+userid+"/"+edtnewmobile.getText().toString();
                    StringRequest request = new StringRequest(Request.Method.POST, Url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONArray array = new JSONArray(response);
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject object = array.getJSONObject(i);
                                            String msg = object.getString("MESSAGE");
                                            if (msg.equals("OTP SEND SUCCESSFULL")) {
                                                String otp = object.getString("OTP");

                                                //open alertdialog match otp

                                                final Dialog dialogs = new Dialog(getActivity(),android.R.style.Theme_DeviceDefault_NoActionBar_TranslucentDecor);
                                                dialogs.setTitle("Enter Otp Here..");
                                                dialogs.setContentView(R.layout.complete_otp);

                                                TextView textView=(TextView)dialogs.findViewById(R.id.numbers);
                                                textView.setText(edtnewmobile.getText().toString());
                                                ImageView img=(ImageView)dialogs.findViewById(R.id.close_img) ;
                                                img.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialogs.dismiss();
                                                    }
                                                });
                                                Pinview pinview=(Pinview)dialogs.findViewById(R.id.pinviews);
                                                pinview.setPinViewEventListener(new Pinview.PinViewEventListener() {
                                                                                    @Override
                                                                                    public void onDataEntered(Pinview pinview, boolean fromUser) {

                                                                                        if (pinview.getValue().toString().equals(otp)) {

                                                                                            dialogs.dismiss();
                                                                                            updatemobile();
                                                                                        }else{
                                                                                            Toast.makeText(getActivity(), "Incorrect Otp", Toast.LENGTH_SHORT).show();
                                                                                            dialogs.dismiss();
                                                                                        }
                                                                                    }
                                                                                });


                                            dialogs.show();

                                            } else {
                                                Toast.makeText(getActivity(), "Mobile Number Already Exist", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Toast.makeText(OTPActivity.this, "Connection Problem ", Toast.LENGTH_SHORT).show();
                        }
                    })/* {
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            String userid=Utils.getSavedPreferences(getActivity(), Hom.userid,"");
                            params.put("user_mobile_number",edtnewmobile.getText().toString());
                            params.put("user_id ", userid);
                            return params;
                        }
                    }*/;
                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    requestQueue.add(request);
                }
            }
        });

        return view;
    }
//update mobile
    private void updatemobile() {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        pd.setContentView(R.layout.progress);

        String Url=webServiceClass.getUrl()+webServiceClass.getMethodUpdatemobile();
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
                                String userid=Utils.getSavedPreferences(getActivity(), Hom.userid,"");
                                ConnectionHelper con = new ConnectionHelper(getActivity());
                                SQLiteDatabase dt = con.getWritableDatabase();
                                String q = "insert into userinfo(number,userpwd,userid) values('" + edtnewmobile.getText().toString() + "','" + "" + "','" + userid +"')";
                                dt.execSQL(q);
                                dt.close();
                                con.close();

                                Intent intent=new Intent(getActivity(),Hom.class);
                                startActivity(intent);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pd.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                // Toast.makeText(OTPActivity.this, "Connection Problem ", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                String userid=Utils.getSavedPreferences(getActivity(), Hom.userid,"");
                params.put("user_id ",userid);
                params.put("u_mobile_number",edtnewmobile.getText().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }
}