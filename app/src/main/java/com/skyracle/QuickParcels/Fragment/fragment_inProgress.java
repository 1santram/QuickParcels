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
import com.skyracle.QuickParcels.Adapter.InProgressOrderAdapter;
import com.skyracle.QuickParcels.Hom;
import com.skyracle.QuickParcels.Model.InProgressOrder;
import com.skyracle.QuickParcels.R;
import com.skyracle.QuickParcels.Services.WebServiceClass;
import com.skyracle.QuickParcels.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class fragment_inProgress  extends Fragment {
    WebServiceClass webServiceClass=new WebServiceClass();
    private View view;
    RecyclerView recyclerView;
    InProgressOrderAdapter adapter;
    private ArrayList<InProgressOrder> homeCategoryModelClasses;

    int currentItems,totalItems,scrooledOutItem;
    int q,r;
    LinearLayoutManager manager;
    boolean isScrolling=false;

    ProgressBar progressBar;

    TextView txtmsg;

    int list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.layout_inprogress, container, false);

        progressBar=view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.recyclerinprogress);
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
                            JSONArray array=new JSONArray(response);
                            list=array.length();
                            for (int i=array.length()-1;i>=0;i--){
                                JSONObject object=array.getJSONObject(i);
                                String status=object.getString("status");
                                if (status.equals("3")){
                                    String pickup=object.getString("street1")+" "+object.getString("locality1")+"  "+object.getString("city1");
                                    String drop=object.getString("street")+" "+object.getString("locality")+"  "+object.getString("city");
                                    InProgressOrder mylist=new InProgressOrder(
                                            object.getString("date"),
                                            object.getString("time"),
                                            pickup,
                                            drop,
                                            object.getString("courier_distance"),
                                            object.getString("order_id"),
                                            object.getString("weight")

                                    );
                                    homeCategoryModelClasses.add(mylist);
                                    if (homeCategoryModelClasses.size()>0) {
                                        txtmsg.setVisibility(View.GONE);
                                        adapter = new InProgressOrderAdapter(getActivity(), homeCategoryModelClasses);
                                        recyclerView.setAdapter(adapter);
                                    }else{
                                      //  Toast.makeText(getActivity(), "No Record Found", Toast.LENGTH_SHORT).show();
                                        txtmsg.setVisibility(View.VISIBLE);
                                    }
                                }else{
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
        }) ;
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(request);


    }

   /* @Override
    public void onStart() {
        super.onStart();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int a=recyclerView.getAdapter().getItemCount();
                if (list<recyclerView.getAdapter().getItemCount()){
                    isScrolling=false;
                }else{
                    isScrolling=true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems=manager.getChildCount();
                totalItems=manager.getItemCount();
                scrooledOutItem=manager.findFirstVisibleItemPosition();
                if (isScrolling && (currentItems+scrooledOutItem==totalItems)){
                    isScrolling=false;
                    q=q+15;
                    r=q+15;
                    if (r<=list) {
                        progressBar.setVisibility(View.VISIBLE);
                        loadMore();
                        progressBar.setVisibility(View.GONE);
                    }
                    // loadMore();
                }

            }
        });
    }

    private void loadMore(){

        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //  progressBar.setVisibility(View.VISIBLE);
                    for (int i = q; i < r; i++) {
                        String userid= Utils.getSavedPreferences(getActivity(), Hom.userid,"");
                        String Urls=webServiceClass.getUrl()+webServiceClass.getMethodGetallorder()+"/"+userid;
                        StringRequest request = new StringRequest(Request.Method.POST,Urls,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONArray array=new JSONArray(response);
                                            for (int i=q;i<r;i++){
                                                JSONObject object=array.getJSONObject(i);
                                                String status=object.getString("status");
                                                if (status.equals("3")){
                                                    String pickup=object.getString("locality1")+" , "+object.getString("city1");
                                                    String drop=object.getString("locality")+" , "+object.getString("city");
                                                    InProgressOrder mylist=new InProgressOrder(
                                                            object.getString("date"),
                                                            object.getString("time"),
                                                            pickup,
                                                            drop,
                                                            object.getString("courier_distance"),
                                                            object.getString("order_id")

                                                    );
                                                    homeCategoryModelClasses.add(mylist);
                                                    adapter = new InProgressOrderAdapter(getActivity(),homeCategoryModelClasses);
                                                    adapter.notifyDataSetChanged();
                                                    recyclerView.setAdapter(adapter);
                                                    //  progressBar.setVisibility(View.GONE);
                                                }else{
                                                }


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
                        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
                        requestQueue.add(request);
                    }
                }
            }, 0);

        }catch (Exception e){
        }

    }*/

}