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
import com.skyracle.QuickParcels.Adapter.DropAddressAdapter;
import com.skyracle.QuickParcels.Model.DropAddress;
import com.skyracle.QuickParcels.Model.ManageAddress;
import com.skyracle.QuickParcels.Services.WebServiceClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DropLocation extends AppCompatActivity {

    WebServiceClass webServiceClass=new WebServiceClass();
    Button buttondcountinue;
    TextView addnew;
    ImageView backimg;
    RecyclerView recyclerView;
    DropAddressAdapter bAdapter;
    private ArrayList<DropAddress> newAddresses;

    String pickup;

    public static String droplocationname="droplocationname";
    public static String droplocationmobile="droplocationmobile";
    public static String droplocationflat="droplocationflat";
    public static String droplocationstreet="droplocationstreet";
    public static String droplocationnearby="droplocationnearby";
    public static String droplocationcity="droplocationcity";
    public static String droplocationstate="droplocationstate";
    public static String droplocationpin="droplocationpin";
    public static String droplocationadd="droplocationadd";

   String Checked="false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_location);

        pickup=getIntent().getStringExtra("PickupOrder");

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
                Utils.savePreferences(DropLocation.this,droplocationname,intent.getStringExtra("name"));
                Utils.savePreferences(DropLocation.this,droplocationmobile,intent.getStringExtra("mobile"));
                Utils.savePreferences(DropLocation.this,droplocationflat,intent.getStringExtra("street"));
                Utils.savePreferences(DropLocation.this,droplocationstreet,intent.getStringExtra("locality"));
                Utils.savePreferences(DropLocation.this,droplocationstate,intent.getStringExtra("state"));
                Utils.savePreferences(DropLocation.this,droplocationcity,intent.getStringExtra("city"));
                Utils.savePreferences(DropLocation.this,droplocationnearby,intent.getStringExtra("nearby"));
                Utils.savePreferences(DropLocation.this,droplocationpin,intent.getStringExtra("pincode"));
                Utils.savePreferences(DropLocation.this,droplocationadd,intent.getStringExtra("address"));





            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(aMessageReceiver, new IntentFilter("custom4"));


        BroadcastReceiver bMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String value=intent.getStringExtra("value");
                String addressid=intent.getStringExtra("mobile");

                if (value.equals("true"))
                {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(DropLocation.this,R.style.MyDialogTheme);
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

        buttondcountinue=findViewById(R.id.btnnext);
        buttondcountinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Checked.equals("false"))
                {
                    Toast.makeText(DropLocation.this, "Please Select Address", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent=new Intent(DropLocation.this,ParcelWeightMgmt.class);
                    intent.putExtra("PickupOrder",pickup);
                    intent.putExtra("drop","drop2");
                    startActivity(intent);
                }

            }
        });

        addnew=findViewById(R.id.AddAddress);
        addnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DropLocation.this,Drop.class);
                intent.putExtra("PickupOrder",pickup);
                startActivity(intent);
            }
        });
    }

    private void loadAddressinfo() {

        final ProgressDialog pd = new ProgressDialog(DropLocation.this);
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        pd.setContentView(R.layout.progress);

        String userid= Utils.getSavedPreferences(DropLocation.this ,Hom.userid,"");
        String Urls=webServiceClass.Url+webServiceClass.getMethodShowaddress();
        StringRequest request = new StringRequest(Request.Method.POST,Urls,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            pd.dismiss();
                            JSONArray array1 = new JSONArray(response);
                            for (int i=0;i<array1.length();i++){
                                JSONObject object=array1.getJSONObject(i);
                                DropAddress mycreditList = new DropAddress(
                                        object.getString("user_name"),
                                        object.getString("user_mobile_no"),
                                        object.getString("flat_number"),
                                        object.getString("street"),
                                        object.getString("near_by_place"),
                                        object.getString("city"),
                                        object.getString("state"),
                                        object.getString("address_type"),
                                        object.getString("pincode"),
                                        object.getString("address_id"),
                                        pickup.toString()
                                );
                                newAddresses.add(mycreditList);

                            }

                            bAdapter = new DropAddressAdapter(DropLocation.this, newAddresses);
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
                params.put(" user_id",userid);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(DropLocation.this);
        requestQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void back() {

        Intent i=new Intent(DropLocation.this,PickUpLocation.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra("PickupOrder",pickup);
        startActivity(i);
    }


}
