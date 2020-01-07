package com.skyracle.QuickParcels.Fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.skyracle.QuickParcels.Adapter.ManageAddressAdapter;
import com.skyracle.QuickParcels.Hom;
import com.skyracle.QuickParcels.Model.ManageAddress;
import com.skyracle.QuickParcels.R;
import com.skyracle.QuickParcels.Services.WebServiceClass;
import com.skyracle.QuickParcels.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class fragment_manageaddress  extends Fragment {

    WebServiceClass webServiceClass=new WebServiceClass();
    View view;

    RecyclerView recyclerView;
    ManageAddressAdapter bAdapter;
    private ArrayList<ManageAddress> newAddresses;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.layout_manageaddress, container, false);

        recyclerView=view.findViewById(R.id.recyclermanage);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        newAddresses = new ArrayList<>();

        loadAddressinfo();

        BroadcastReceiver bMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String value=intent.getStringExtra("value");
                String addressid=intent.getStringExtra("mobile");

                if (value.equals("true"))
                {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.MyDialogTheme);
                    builder.setCancelable(false);
                    builder.setTitle("DELETE");
                    builder.setMessage("Are you sure to delete this address?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            final ProgressDialog pd = new ProgressDialog(getActivity());
                            pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            pd.setCancelable(false);
                            pd.setCanceledOnTouchOutside(false);
                            pd.show();
                            pd.setContentView(R.layout.progress);

                            String Url=webServiceClass.getUrl()+webServiceClass.getMethodDeleteaddress();
                            final StringRequest request = new StringRequest(Request.Method.POST, Url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            // progressDialog.dismiss();
                                             pd.dismiss();
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
                                  //  pd.dismiss();
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
                    builder.create().show();
                }
            }
        };
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(bMessageReceiver, new IntentFilter("custom1"));



        return view;
    }

    private void loadAddressinfo() {

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        pd.setContentView(R.layout.progress);

        String userid= Utils.getSavedPreferences(getActivity(), Hom.userid,"");
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
                               ManageAddress mycreditList = new ManageAddress(
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

                            bAdapter = new ManageAddressAdapter(getActivity(), newAddresses);
                            recyclerView.setAdapter(bAdapter);
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
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }
}