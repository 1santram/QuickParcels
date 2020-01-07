package com.skyracle.QuickParcels;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OrderSuccess extends AppCompatActivity {

    WebServiceClass webServiceClass=new WebServiceClass();
    TextView bookingid,parcelprice,totaldiscount,totalprice,pickupcharge;
    ImageView homess;

    Button btncalcel;
    String msg="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        homess=findViewById(R.id.homess);
        homess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        bookingid=findViewById(R.id.bookingid);
        parcelprice=findViewById(R.id.price);
        totaldiscount=findViewById(R.id.savevisit);
        totalprice=findViewById(R.id.total);
        pickupcharge=findViewById(R.id.pickupcharge);

        pickupcharge.setText("Rs. "+Utils.getSavedPreferences(OrderSuccess.this,OrderPlace.parcelpickup,""));
        bookingid.setText("#"+Utils.getSavedPreferences(OrderSuccess.this,OrderPlace.parcelorderid,""));
        Float price=Float.parseFloat( Utils.getSavedPreferences(OrderSuccess.this,OrderPlace.parcelprice,""));
        Float distance = 0.0f;
        try {
             distance = Float.parseFloat(Utils.getSavedPreferences(OrderSuccess.this, OrderPlace.parceldistance, ""));
        }catch (Exception e){
            
        }
        parcelprice.setText("Rs. "+String.format("%.2f",(price*distance)));
        totaldiscount.setText("Rs. "+Utils.getSavedPreferences(OrderSuccess.this,OrderPlace.parceldiscount,""));
        totalprice.setText(Utils.getSavedPreferences(OrderSuccess.this,OrderPlace.parceltotalprice,""));


        btncalcel=findViewById(R.id.btncancle);
        btncalcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Ordercancel();

            }
        });

    }

    private void Ordercancel() {

        AlertDialog.Builder builder = new AlertDialog.Builder(OrderSuccess.this);
        View view = LayoutInflater.from(OrderSuccess.this).inflate(R.layout.layout_cancelmsg, null);
        builder.setView(view);
        RadioGroup group = (RadioGroup) view.findViewById(R.id.radiogroup);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.r1) {
                    msg = "Delivery partner denied duty";
                } else if (checkedId == R.id.r2) {
                    msg = "The address booked is incorrect";
                } else if (checkedId == R.id.r3) {
                    msg = "My reason is not listed";
                } else if (checkedId == R.id.r4) {
                    msg = "Unable to contact";
                }
            }

        });
        AlertDialog alertDialog = builder.create();

        ImageView imageclose = (ImageView) view.findViewById(R.id.closebtn);
        imageclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        Button btnapply = (Button) view.findViewById(R.id.buttonapply);
        btnapply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msg.equals("")) {
                    Toast.makeText(OrderSuccess.this, "Please select reason?", Toast.LENGTH_SHORT).show();
                } else {
                    final ProgressDialog pd = new ProgressDialog(OrderSuccess.this);
                    pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    pd.setCancelable(false);
                    pd.setCanceledOnTouchOutside(false);
                    pd.show();
                    pd.setContentView(R.layout.progress);
                    String userid = Utils.getSavedPreferences(OrderSuccess.this, Hom.userid, "");
                    String Urls = webServiceClass.getUrl() + webServiceClass.getMethodCancelorder();
                    StringRequest request = new StringRequest(Request.Method.POST, Urls,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        JSONObject object = new JSONObject(response);
                                        pd.dismiss();
                                        Intent intent = new Intent(OrderSuccess.this, Hom.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);

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
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            String userid = Utils.getSavedPreferences(OrderSuccess.this, Hom.userid, "");
                            String orderid = Utils.getSavedPreferences(OrderSuccess.this, OrderPlace.parcelorderid, "");
                            params.put("user_id", userid);
                            params.put("order_id", orderid);
                            params.put("message_order", msg);

                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(OrderSuccess.this);
                    requestQueue.add(request);
                }
            }
        });

        alertDialog.setCancelable(false);
        alertDialog.show();

    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        Intent intent=new Intent(OrderSuccess.this,Hom.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onResume() {

            final android.app.AlertDialog.Builder al=new  android.app.AlertDialog.Builder(OrderSuccess.this,R.style.MyDialogTheme);
            al.setCancelable(false);
            al.setTitle("Alert Message!");
            al.setMessage("Your order will be automatically cancelled, " +
                    "if we didn't find any nearby partner for your courier delivery.");
            al.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
            android.app.AlertDialog alert=al.create();
            alert.show();
            super.onResume();

    }
}
