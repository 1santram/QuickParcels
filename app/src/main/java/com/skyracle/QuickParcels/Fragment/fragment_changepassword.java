package com.skyracle.QuickParcels.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.skyracle.QuickParcels.Hom;
import com.skyracle.QuickParcels.R;
import com.skyracle.QuickParcels.Services.WebServiceClass;
import com.skyracle.QuickParcels.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class fragment_changepassword  extends Fragment {

    WebServiceClass webServiceClass=new WebServiceClass();
    EditText edtnewpass,edtcnfnewpass;
    Button buttonupdatepass;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.layout_changepassword, container, false);

        edtnewpass=view.findViewById(R.id.newpass);
        edtcnfnewpass=view.findViewById(R.id.cnfpass);
        buttonupdatepass=view.findViewById(R.id.changepass);
        buttonupdatepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtnewpass.getText().toString())) {
                    edtnewpass.setError("Enter New Password");
                } else if (TextUtils.isEmpty(edtcnfnewpass.getText().toString())) {
                    edtcnfnewpass.setError("Enter Confirm Password");
                } else if (edtnewpass.getText().toString() == edtcnfnewpass.getText().toString()) {
                    Toast.makeText(getActivity(), "Password Not Matched", Toast.LENGTH_SHORT).show();
                } else {

                    final ProgressDialog pd = new ProgressDialog(getActivity());
                    pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    pd.setCancelable(false);
                    pd.setCanceledOnTouchOutside(false);
                    pd.show();
                    pd.setContentView(R.layout.progress);
                    String Mobile = Utils.getSavedPreferences(getActivity(), Hom.mobile, "");
                    String Url = webServiceClass.getUrl() + webServiceClass.getMethodUpdatepassword()+"/"+Mobile+"/"+edtnewpass.getText().toString().trim();
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
                                            if (msg.equals("PASSWORD UPDATE SUCCESSFULLY")){
                                                Toast.makeText(getActivity(), "Password updated successfully.", Toast.LENGTH_SHORT).show();

                                                Intent intent=new Intent(getActivity(), Hom.class);
                                                startActivity(intent);

                                            }else{
                                                pd.dismiss();
                                                Toast.makeText(getActivity(), "Try again", Toast.LENGTH_SHORT).show();
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
                            // Toast.makeText(OTPActivity.this, "Connection Problem ", Toast.LENGTH_SHORT).show();
                        }
                    }) /*{
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            String Mobile = Utils.getSavedPreferences(getActivity(), Hom.mobile, "");
                            params.put("u_password", edtnewpass.getText().toString());
                            params.put("u_mobile_number ",Mobile );
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
}
