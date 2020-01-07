package com.skyracle.QuickParcels;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
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
import com.skyracle.QuickParcels.Adapter.PickUpAddressAdapter;
import com.skyracle.QuickParcels.Model.PickupAddress;
import com.skyracle.QuickParcels.Services.WebServiceClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PickUpLocation extends AppCompatActivity {

    WebServiceClass webServiceClass=new WebServiceClass();
    ImageView backimg;
    Button buttoncontinue;
    TextView addnewaddress;

    RecyclerView recyclerView;
    PickUpAddressAdapter bAdapter;
    private ArrayList<PickupAddress> newAddresses;

    public static String pickuplocationname="pickuplocationname";
    public static String pickuplocationmobile="pickuplocationmobile";
    public static String pickuplocationflat="pickuplocationflat";
    public static String pickuplocationstreet="pickuplocationstreet";
    public static String pickuplocationnearby="pickuplocationnearby";
    public static String pickuplocationcity="pickuplocationcity";
    public static String pickuplocationstate="pickuplocationstate";
    public static String pickuplocationpin="pickuplocationpin";
    public static String getPickuplocationadd="getPickuplocationadd";

    String Checked="false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_up_location);


        recyclerView=findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        newAddresses = new ArrayList<>();

        loadAddressinfo();


        //recieve adapter Click
        BroadcastReceiver aMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                Checked=intent.getStringExtra("value");
                Utils.savePreferences(PickUpLocation.this,pickuplocationname,intent.getStringExtra("name"));
                Utils.savePreferences(PickUpLocation.this,pickuplocationmobile,intent.getStringExtra("mobile"));
                Utils.savePreferences(PickUpLocation.this,pickuplocationflat,intent.getStringExtra("street"));
                Utils.savePreferences(PickUpLocation.this,pickuplocationstreet,intent.getStringExtra("locality"));
                Utils.savePreferences(PickUpLocation.this,pickuplocationstate,intent.getStringExtra("state"));
                Utils.savePreferences(PickUpLocation.this,pickuplocationcity,intent.getStringExtra("city"));
                Utils.savePreferences(PickUpLocation.this,pickuplocationnearby,intent.getStringExtra("nearby"));
                Utils.savePreferences(PickUpLocation.this,pickuplocationpin,intent.getStringExtra("pincode"));
                Utils.savePreferences(PickUpLocation.this,getPickuplocationadd,intent.getStringExtra("address"));





            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(aMessageReceiver, new IntentFilter("custom"));



        BroadcastReceiver bMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String value=intent.getStringExtra("value");
                String addressid=intent.getStringExtra("mobile");

                if (value.equals("true"))
                {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(PickUpLocation.this,R.style.MyDialogTheme);
                    builder.setCancelable(false);
                    builder.setTitle("DELETE");
                    builder.setMessage("Are you sure to delete this address?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            String Url=webServiceClass.getUrl()+webServiceClass.getMethodDeleteaddress();
                            final StringRequest request = new StringRequest(Request.Method.POST, Url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            try {

                                                JSONObject object =new JSONObject(response);
                                                String msg=object.getString("MESSAGE");
                                                if (msg.equals("DATA DELETE SUCCESSFULLY")){
                                                    newAddresses.clear();
                                                    loadAddressinfo();

                                                }else{
                                                    Toast.makeText(context, "Server Problem", Toast.LENGTH_SHORT).show();
                                                }


                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }){
                                protected Map<String,String> getParams()
                                {
                                    Map<String,String> params=new HashMap<String, String>();
                                    params.put(" user_id",addressid);
                                    return params;
                                }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(context);
                            requestQueue.add(request);


                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    try {
                        builder.create().show();
                    }
                    catch (WindowManager.BadTokenException e) {
                    }
                }
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(bMessageReceiver, new IntentFilter("custom1"));



        backimg=(ImageView)findViewById(R.id.back_img);
        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        buttoncontinue=findViewById(R.id.btnnext);
        buttoncontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Checked.equals("false"))
                {
                    Toast.makeText(PickUpLocation.this, "Please Select Address", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent=new Intent(PickUpLocation.this,DropLocation.class);
                    intent.putExtra("PickupOrder","pickup2");
                    startActivity(intent);
                }
            }
        });

        addnewaddress=findViewById(R.id.AddAddress);
        addnewaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PickUpLocation.this,PickUp.class);
                startActivity(intent);
            }
        });
    }

    private void loadAddressinfo() {

        final ProgressDialog pd = new ProgressDialog(PickUpLocation.this);
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        pd.setContentView(R.layout.progress);

        String userid= Utils.getSavedPreferences(PickUpLocation.this, Hom.userid,"");
        String Urls=webServiceClass.Url+webServiceClass.getMethodShowaddress();
        StringRequest request = new StringRequest(Request.Method.POST,Urls,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array1 = new JSONArray(response);
                            pd.dismiss();
                            for (int i=0;i<array1.length();i++){
                                JSONObject object=array1.getJSONObject(i);
                                PickupAddress mycreditList = new PickupAddress(
                                        object.getString("user_name"),
                                        object.getString("user_mobile_no"),
                                        object.getString("flat_number"),
                                        object.getString("street"),
                                        object.getString("near_by_place"),
                                        object.getString("city"),
                                        object.getString("state"),
                                        object.getString("address_type"),
                                        object.getString("pincode"),
                                        object.getString("address_id")
                                );
                                newAddresses.add(mycreditList);
                            }

                            bAdapter = new PickUpAddressAdapter(PickUpLocation.this, newAddresses);
                            recyclerView.setAdapter(bAdapter);
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
        }) {
            protected Map<String,String> getParams()
            {
                Map<String,String> params=new HashMap<String, String>();
                params.put("user_id",userid);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(PickUpLocation.this);
        requestQueue.add(request);
    }


    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {

        Intent i=new Intent(PickUpLocation.this,Hom.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }


}
