package com.skyracle.QuickParcels;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OrderDetails extends AppCompatActivity {

    WebServiceClass webServiceClass=new WebServiceClass();
    TextView bookingid,parcelprice,totaldiscount,totalprice,pickupcharge,name,mobiles;
    Button btncalcel;
    String msg="";

    ImageView imgback,partnerimage;
    CardView partnerdetails;

    LinearLayout close,accept;
    TextView text;

    ImageView imgcall;

    String orderid,status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        status=getIntent().getStringExtra("status");
        String ord=getIntent().getStringExtra("bookingid");
        ord=ord.replace("#","");
        orderid=ord;

        imgback=findViewById(R.id.back_img);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        bookingid=findViewById(R.id.bookingid);
                                        bookingid.setText("#"+orderid);
        parcelprice=findViewById(R.id.price);
        totaldiscount=findViewById(R.id.savevisit);
        totalprice=findViewById(R.id.total);
        pickupcharge=findViewById(R.id.pickupcharge);
        name=findViewById(R.id.name);
        mobiles=findViewById(R.id.mobile);
        partnerimage=findViewById(R.id.imageviews);
        partnerdetails=findViewById(R.id.partners);
        close=findViewById(R.id.close);
        accept=findViewById(R.id.accept);
        text=findViewById(R.id.text);

        imgcall=findViewById(R.id.call);

        imgcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+mobiles.getText().toString()));
                startActivity(intent);
            }
        });

        loadOrderDetails();

        btncalcel=findViewById(R.id.btncancle);


      if (status.equalsIgnoreCase("0")){
          btncalcel.setVisibility(View.VISIBLE);
          partnerdetails.setVisibility(View.GONE);
      }else if (status.equalsIgnoreCase("1")){
          btncalcel.setVisibility(View.GONE);
          partnerdetails.setVisibility(View.GONE);
      }else if (status.equalsIgnoreCase("2")){
          text.setText("Booking Accepted");
          btncalcel.setVisibility(View.GONE);
          partnerdetails.setVisibility(View.VISIBLE);
          loadpartnerdetails();
      }else if (status.equalsIgnoreCase("3")){
          text.setText("Booking Inprogress");
          btncalcel.setVisibility(View.GONE);
          loadpartnerdetails();
          partnerdetails.setVisibility(View.VISIBLE);
          loadpartnerdetails();
      }else if (status.equalsIgnoreCase("4")){
          text.setText("Booking Completed");
          btncalcel.setVisibility(View.GONE);
          partnerdetails.setVisibility(View.VISIBLE);
          loadpartnerdetails();
      }else {
          text.setText("Booking Cancled");
          text.setTextColor(Color.RED);
          accept.setVisibility(View.GONE);
          close.setVisibility(View.VISIBLE);
          btncalcel.setVisibility(View.GONE);
          partnerdetails.setVisibility(View.GONE);
      }



        btncalcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Ordercancel();

            }
        });

    }

    private void loadOrderDetails() {

        final ProgressDialog pd = new ProgressDialog(OrderDetails.this);
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        pd.setContentView(R.layout.progress);
        String userid= Utils.getSavedPreferences(OrderDetails.this, Hom.userid,"");
        String Urls=webServiceClass.getUrl()+webServiceClass.getMethodGetsinglwlorder()+"/"+userid+"/"+orderid;
        StringRequest request = new StringRequest(Request.Method.POST,Urls,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            pd.dismiss();
                            JSONArray array=new JSONArray(response);
                            for (int i=0;i<array.length();i++){
                                JSONObject object=array.getJSONObject(i);
                                String amount=object.getString("amount");
                                if (amount.equals("null")){
                                    String tot=object.getString("total_price");
                                    String pick=object.getString("price");
                                    parcelprice.setText("Rs. "+String.valueOf(Float.parseFloat(tot)-Float.parseFloat(pick)));
                                    totaldiscount.setText("Rs. 0");
                                }else{
                                    Float dis=Float.parseFloat(amount);
                                    String totals=object.getString("total_price");
                                    String pick=object.getString("price");
                                    Float total=Float.parseFloat(totals)-Float.parseFloat(pick);
                                    parcelprice.setText("Rs. "+String.valueOf((total+dis)));
                                    totaldiscount.setText("Rs. "+String.valueOf((Float.parseFloat(object.getString("amount")))));
                                }

                                String tot=object.getString("total_price");
                                totalprice.setText("Rs. "+tot);
                                String pick=object.getString("price");
                                pick=pick.replace("Rs.","");
                                pickupcharge.setText("Rs. "+pick);

                            }
                            pd.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
            }
        }) ;
        RequestQueue requestQueue= Volley.newRequestQueue(OrderDetails.this);
        requestQueue.add(request);

    }

    private void loadpartnerdetails() {

        String userid= Utils.getSavedPreferences(OrderDetails.this, Hom.userid,"");
        String Urls=webServiceClass.getUrl()+webServiceClass. getMethodShowpartner()+"/"+userid+"/"+orderid+"/"+status;
        StringRequest request = new StringRequest(Request.Method.POST,Urls,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array=new JSONArray(response);
                            if (array.length()>0){
                                for (int i=0;i<array.length();i++){
                                    JSONObject object=array.getJSONObject(i);
                                    Picasso.with(OrderDetails.this)
                                            .load("http://quickparcels.in/app/qp/assets/"+object.getString("photo")).placeholder(R.drawable.picture).error(R.drawable.picture).into(partnerimage);
                                    name.setText(object.getString("name"));
                                    mobiles.setText(object.getString("mobile_nomber"));
                                }

                            }else{
                                partnerdetails.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) ;
        RequestQueue requestQueue= Volley.newRequestQueue(OrderDetails.this);
        requestQueue.add(request);

    }

    private void Ordercancel() {

        AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetails.this);
        View view = LayoutInflater.from(OrderDetails.this).inflate(R.layout.layout_cancelmsg, null);
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
                    Toast.makeText(OrderDetails.this, "Please select reason?", Toast.LENGTH_SHORT).show();
                } else {
                    final ProgressDialog pd = new ProgressDialog(OrderDetails.this);
                    pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    pd.setCancelable(false);
                    pd.setCanceledOnTouchOutside(false);
                    pd.show();
                    pd.setContentView(R.layout.progress);
                    String userid = Utils.getSavedPreferences(OrderDetails.this, Hom.userid, "");
                    String Urls = webServiceClass.getUrl() + webServiceClass.getMethodCancelorder();
                    StringRequest request = new StringRequest(Request.Method.POST, Urls,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject object=new JSONObject(response);
                                        pd.dismiss();
                                        Intent intent = new Intent(OrderDetails.this, Hom.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("FromReservation", "4");
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
                            String userid = Utils.getSavedPreferences(OrderDetails.this, Hom.userid, "");
                            String orderid = Utils.getSavedPreferences(OrderDetails.this, OrderPlace.parcelorderid, "");
                            params.put("user_id", userid);
                            params.put("order_id", orderid);
                            params.put("message_order", msg);

                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(OrderDetails.this);
                    requestQueue.add(request);
                }
            }
        });

        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}
