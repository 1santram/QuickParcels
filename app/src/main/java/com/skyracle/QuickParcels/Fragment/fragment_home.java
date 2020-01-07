package com.skyracle.QuickParcels.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.skyracle.QuickParcels.Hom;
import com.skyracle.QuickParcels.PickUpLocation;
import com.skyracle.QuickParcels.R;
import com.skyracle.QuickParcels.Services.WebServiceClass;
import com.skyracle.QuickParcels.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;

public class fragment_home extends Fragment {

    WebServiceClass webServiceClass=new WebServiceClass();
    CardView card2,card3,card4,card5;
    TextView countrecent,countinprogress,countcompleted,countcancled,countaccept;
    Button buttonpickup;
    private View view;

    int recent=0;
    int inprogress=0;
    int completed=0;
    int canceled=0;
    int accept=0;

    ViewPager viewPager;
    List<Integer> colorName;
    TabLayout indicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.layout_home, container, false);
      //  card1=view.findViewById(R.id.cart1);
        card2=view.findViewById(R.id.cart2);
        card3=view.findViewById(R.id.cart3);
        card4=view.findViewById(R.id.cart4);
        card5=view.findViewById(R.id.cart5);

      //  viewPager=view.findViewById(R.id.viewPager);
      //  indicator=(TabLayout)view.findViewById(R.id.indicator);
        Integer[] items = {R.drawable.saaa,R.drawable.saaa};
        colorName= Arrays.asList(items);


      //  viewPager.setAdapter(new SliderAdapter(getActivity(),colorName));
      //  indicator.setupWithViewPager(viewPager, true);

     //   Timer timer = new Timer();
     //   timer.scheduleAtFixedRate(new SliderTimer(), 3000, 5000);

      //  countrecent=view.findViewById(R.id.countrecent);
        countcancled=view.findViewById(R.id.countcancele);
        countcompleted=view.findViewById(R.id.countcompleted);
        countinprogress=view.findViewById(R.id.countinprogress);
        countaccept=view.findViewById(R.id.accept);




        buttonpickup=view.findViewById(R.id.buttonpickup);

        loadAllORder();

        buttonpickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), PickUpLocation.class);
                startActivity(intent);




            }
        });

        /*card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Hom.class);
                intent.putExtra("FromReservation", "1");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });*/
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Hom.class);
                intent.putExtra("FromReservation", "2");
                startActivity(intent);
            }
        });
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Hom.class);
                intent.putExtra("FromReservation", "3");
                startActivity(intent);
            }
        });
        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Hom.class);
                intent.putExtra("FromReservation", "4");
                startActivity(intent);
            }
        });

        card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Hom.class);
                intent.putExtra("FromReservation", "6");
                startActivity(intent);
            }
        });



        return view;
    }

    private void loadAllORder() {

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(true);
        pd.show();
        pd.setContentView(R.layout.progress);
        String userid=Utils.getSavedPreferences(getActivity(), Hom.userid,"");
        String Urls=webServiceClass.getUrl()+webServiceClass.getMethodGetallorder()+"/"+userid;
        StringRequest request = new StringRequest(Request.Method.POST,Urls,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            pd.dismiss();
                            JSONArray array=new JSONArray(response);
                            for (int i=0;i<array.length();i++){
                                JSONObject object=array.getJSONObject(i);
                                String status=object.getString("status");
                                /*if (status.equals("1")){
                                    recent++;
                                    countrecent.setText(String.valueOf(recent));
                                }else*/ if (status.equals("2")){
                                    accept++;
                                    countaccept.setText(String.valueOf(accept));
                                } else if (status.equals("3")){
                                    inprogress++;
                                    countinprogress.setText(String.valueOf(inprogress));
                                }else if (status.equals("4")){
                                    completed++;
                                    countcompleted.setText(String.valueOf(completed));
                                }else if (status.equals("5")){
                                    canceled++;
                                    countcancled.setText(String.valueOf(canceled));
                                }else{
                                }
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

    @Override
    public void onResume() {
       // Toast.makeText(getActivity(), "Getting record please wait...", Toast.LENGTH_SHORT).show();
        super.onResume();
    }

    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            try {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (viewPager.getCurrentItem() < colorName.size() - 1) {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        } else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }catch (Exception e){
            }
        }
    }
}