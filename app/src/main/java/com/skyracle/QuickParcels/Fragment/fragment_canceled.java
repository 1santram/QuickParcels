package com.skyracle.QuickParcels.Fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.skyracle.QuickParcels.Adapter.CancelOrderAdapter;
import com.skyracle.QuickParcels.Hom;
import com.skyracle.QuickParcels.Model.CancelOrder;
import com.skyracle.QuickParcels.R;
import com.skyracle.QuickParcels.Services.WebServiceClass;
import com.skyracle.QuickParcels.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class fragment_canceled extends Fragment{

    WebServiceClass webServiceClass=new WebServiceClass();
        private View view;
        RecyclerView recyclerView;
        CancelOrderAdapter adapter;
        private ArrayList<CancelOrder> homeCategoryModelClasses;

        TextView txtmsg;

    int currentItems,totalItems,scrooledOutItem;
    int q,r;
    LinearLayoutManager manager;
    boolean isScrolling=false;

    int list;

    ProgressBar progressBar;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.layout_canceled, container, false);

            progressBar=view.findViewById(R.id.progressBar);
            recyclerView = view.findViewById(R.id.recyclercancels);
            manager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
            recyclerView.setLayoutManager(manager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            txtmsg=view.findViewById(R.id.txtmsg);
            homeCategoryModelClasses = new ArrayList<>();
            homeCategoryModelClasses.clear();
            loadData();

            return view;
        }

    private void loadData() {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        pd.setContentView(R.layout.progress);
        String userid= Utils.getSavedPreferences(getActivity(), Hom.userid,"");
        String Urls=webServiceClass.getUrl()+webServiceClass.getMethodGetallorder()+"/"+userid;
        StringRequest request = new StringRequest(Request.Method.POST,Urls,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            pd.dismiss();
                            txtmsg.setVisibility(View.GONE);
                            JSONArray array=new JSONArray(response);
                            for (int i=0;i<array.length();i++){
                                JSONObject object=array.getJSONObject(i);
                                String status=object.getString("status");
                                if (status.equals("5")){
                                    String pickup=object.getString("street1")+" "+object.getString("locality1")+"  "+object.getString("city1");
                                    String drop=object.getString("street")+" "+object.getString("locality")+"  "+object.getString("city");
                                    CancelOrder mylist=new CancelOrder(
                                            object.getString("date"),
                                            object.getString("time"),
                                            pickup,
                                            drop,
                                            object.getString("courier_distance"),
                                            object.getString("order_id"),
                                            object.getString("message_order"),
                                            object.getString("weight")
                                    );
                                    homeCategoryModelClasses.add(mylist);

                                }else{
                                }
                            }
                            if (homeCategoryModelClasses.size()>0) {
                                adapter = new CancelOrderAdapter(getActivity(), homeCategoryModelClasses);
                                recyclerView.setAdapter(adapter);
                            }else{
                              //  Toast.makeText(getActivity(), "No Record Found", Toast.LENGTH_SHORT).show();
                                txtmsg.setVisibility(View.VISIBLE);
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
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(request);


    }


}