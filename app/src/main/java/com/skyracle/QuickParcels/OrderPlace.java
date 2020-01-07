package com.skyracle.QuickParcels;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.skyracle.QuickParcels.Adapter.AllCouponAdapterextends;
import com.skyracle.QuickParcels.Model.CouponModelClass;
import com.skyracle.QuickParcels.Services.WebServiceClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class OrderPlace extends AppCompatActivity  {

     ProgressDialog pd;

    private Location location;
    private GoogleApiClient googleApiClient;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private LocationRequest locationRequest;
    private static final long UPDATE_INTERVAL = 3000, FASTEST_INTERVAL = 3000;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private static final int ALL_PERMISSIONS_RESULT = 1011;
    public String lat="lat";
    public String lag="lg";

    public static String parceltotalprice="totalprice";
    public static String parcelorderid="parcelorderid";
    public static String parceltoken="parceltoken";
    public static String parceldistance="parceldistance";
    public static String parcelprice="parcelprice";
    public static String parceldiscount="parceldiscount";
    public static String parcelpickup="parcelpickup";

    WebServiceClass webServiceClass=new WebServiceClass();
    Button buttonback,buttonplace;
    TextView txtpickup,txtdrop,txtweight,txtsize,txtpricekg,txtdistance,txttotalprice,txtpickupcharge,txtdiscount;
    LinearLayout disc;

    ImageView cancelcoupon;

    String pickupname,pickupmobile,pickupflat,pickupstreet,pickupcity,pickupnearby,pickupstate,pickuppincode,pickaddresstype;
    String dropname,dropmobile,dropflat,dropstreet,dropcity,dropnearby,dropstate,droppincode,dropaddresstype;
    String pickupcharges,parcelweight,parcellength,parcelheight,parcelwidth,parcelpriceperkm,pickupradius,admincomission;
    ImageView backimg;

    CardView cardOfferView;

    final String[] parsedDistance = new String[1];
    final String[] response = new String[1];

    RequestQueue requestQueue;
    String Currentdate,Currenttime;

    RadioButton btncash,btnonline;
    String paymenttype="";

    String coupon="",couponname,coupontype;
    Float Distance;

    String orderid;

    AllCouponAdapterextends allCouponAdapter;
    private List<CouponModelClass> mlist;

    String pick,drop;

    Float total;
    Float ammount=0f;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_place);

        pick=getIntent().getStringExtra("PickupOrder");
        drop=getIntent().getStringExtra("drop");

        backimg=(ImageView)findViewById(R.id.back_img);
        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyyy");
        Currentdate = mdformat.format(calendar.getTime());

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("hh:mm-a");
        Currenttime = sdf.format(new Date());


        pickupcharges=Utils.getSavedPreferences(OrderPlace.this,ParcelWeightMgmt.pickup,"");
        parcelweight=Utils.getSavedPreferences(OrderPlace.this,ParcelWeightMgmt.weights,"");
        parcellength=Utils.getSavedPreferences(OrderPlace.this,ParcelWeightMgmt.lengths,"");
        parcelheight=Utils.getSavedPreferences(OrderPlace.this,ParcelWeightMgmt.heights,"");
        parcelwidth=Utils.getSavedPreferences(OrderPlace.this,ParcelWeightMgmt.widths,"");
        parcelpriceperkm=Utils.getSavedPreferences(OrderPlace.this,ParcelWeightMgmt.pricekm,"");

        pickupradius=Utils.getSavedPreferences(OrderPlace.this,ParcelWeightMgmt.radius,"");
        admincomission=Utils.getSavedPreferences(OrderPlace.this,ParcelWeightMgmt.admin,"");





       if (pick.equals("pickup1")){
            pickupname=Utils.getSavedPreferences(OrderPlace.this,PickUp.pickupusernames,"");
            pickupmobile=Utils.getSavedPreferences(OrderPlace.this,PickUp.pickupusermobiles,"");
            pickupflat=Utils.getSavedPreferences(OrderPlace.this,PickUp.pickupuserflats,"");
            pickupstreet=Utils.getSavedPreferences(OrderPlace.this,PickUp.pickupuserstreets,"");
            pickupcity=Utils.getSavedPreferences(OrderPlace.this,PickUp.pickupusercitys,"");
            pickupnearby=Utils.getSavedPreferences(OrderPlace.this,PickUp.pickupusernearbyp,"");
            pickupstate=Utils.getSavedPreferences(OrderPlace.this,PickUp.pickupuserstates,"");
            pickuppincode=Utils.getSavedPreferences(OrderPlace.this,PickUp.pickupuserpincodes,"");
            pickaddresstype=Utils.getSavedPreferences(OrderPlace.this,PickUp.pickupuseraddresstype,"");
        }else{
                pickupname=Utils.getSavedPreferences(OrderPlace.this,PickUpLocation.pickuplocationname,"");
                pickupmobile=Utils.getSavedPreferences(OrderPlace.this,PickUpLocation.pickuplocationmobile,"");
                pickupflat=Utils.getSavedPreferences(OrderPlace.this,PickUpLocation.pickuplocationflat,"");
                pickupstreet=Utils.getSavedPreferences(OrderPlace.this,PickUpLocation.pickuplocationstreet,"");
                pickupcity=Utils.getSavedPreferences(OrderPlace.this,PickUpLocation.pickuplocationcity,"");
                pickupnearby=Utils.getSavedPreferences(OrderPlace.this,PickUpLocation.pickuplocationnearby,"");
                pickupstate=Utils.getSavedPreferences(OrderPlace.this,PickUpLocation.pickuplocationstate,"");
                pickuppincode=Utils.getSavedPreferences(OrderPlace.this,PickUpLocation.pickuplocationpin,"");
                pickaddresstype=Utils.getSavedPreferences(OrderPlace.this,PickUpLocation.getPickuplocationadd,"");
        }



        if (drop.equals("drop1")){
            dropname=Utils.getSavedPreferences(OrderPlace.this,Drop.dropusernames,"");
            dropmobile=Utils.getSavedPreferences(OrderPlace.this,Drop.dropusermobiles,"");
            dropflat=Utils.getSavedPreferences(OrderPlace.this,Drop.dropuserflats,"");
            dropstreet=Utils.getSavedPreferences(OrderPlace.this,Drop.dropuserstreets,"");
            dropcity=Utils.getSavedPreferences(OrderPlace.this,Drop.dropusercitys,"");
            dropnearby=Utils.getSavedPreferences(OrderPlace.this,Drop.dropusernearbyp,"");
            dropstate=Utils.getSavedPreferences(OrderPlace.this,Drop.dropuserstates,"");
            droppincode=Utils.getSavedPreferences(OrderPlace.this,Drop.dropuserpincodes,"");
            dropaddresstype=Utils.getSavedPreferences(OrderPlace.this,Drop.dropuseraddresstype,"");
        }else{
            dropname=Utils.getSavedPreferences(OrderPlace.this,DropLocation.droplocationname,"");
            dropmobile=Utils.getSavedPreferences(OrderPlace.this,DropLocation.droplocationmobile,"");
            dropflat=Utils.getSavedPreferences(OrderPlace.this,DropLocation.droplocationflat,"");
            dropstreet=Utils.getSavedPreferences(OrderPlace.this,DropLocation.droplocationstreet,"");
            dropcity=Utils.getSavedPreferences(OrderPlace.this,DropLocation.droplocationcity,"");
            dropnearby=Utils.getSavedPreferences(OrderPlace.this,DropLocation.droplocationnearby,"");
            dropstate=Utils.getSavedPreferences(OrderPlace.this,DropLocation.droplocationstate,"");
            droppincode=Utils.getSavedPreferences(OrderPlace.this,DropLocation.droplocationpin,"");
            dropaddresstype=Utils.getSavedPreferences(OrderPlace.this,DropLocation.droplocationadd,"");
        }


        btncash=findViewById(R.id.cash);
        btnonline=findViewById(R.id.online);
        txtpickup=findViewById(R.id.pickuplocation);
        txtdrop=findViewById(R.id.droplocation);
        txtweight=findViewById(R.id.productweight);
       // txtsize=findViewById(R.id.productsize);
        txtdiscount=findViewById(R.id.productdiscount);
        txtpricekg=findViewById(R.id.productcharge);
        txtdistance=findViewById(R.id.totaldistance);
        txttotalprice=findViewById(R.id.totalestimate);
        txtpickupcharge=findViewById(R.id.pickupcharge);
        disc=findViewById(R.id.disc);

        cancelcoupon=findViewById(R.id.removecoupon);
        cancelcoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final android.app.AlertDialog.Builder al=new  android.app.AlertDialog.Builder(OrderPlace.this,R.style.MyDialogTheme);
                al.setCancelable(false);
                al.setTitle("Alert");
                al.setMessage("Confirm to remove applied coupon ");
                al.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        disc.setVisibility(View.GONE);
                        coupon="";
                        ammount=0f;
                        txttotalprice.setText(String.valueOf("Rs."+Math.round(Integer.parseInt(pickupcharges)+total)));
                        dialog.dismiss();
                    }
                });
                al.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                android.app.AlertDialog alert=al.create();
                alert.show();

            }
        });

        txtweight.setText(parcelweight);
       // txtsize.setText("Height="+parcelheight+", Width="+parcelwidth+",Length="+parcellength);
        txtpricekg.setText("Rs."+parcelpriceperkm);
        txtpickupcharge.setText("Rs."+pickupcharges);




        txtpickup.setText(pickupflat+" "+pickupstreet+" "+pickupnearby+" "+pickupcity+" "+pickupstate+" "+pickuppincode);
        txtdrop.setText(dropflat+" "+dropstreet+" "+dropnearby+" "+dropcity+" "+dropstate+" "+droppincode);
  //getTotal Distance B/w Address;
        loadDistance();

        buttonback=findViewById(R.id.buttonback);
        buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OrderPlace.this,ParcelWeightMgmt.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        btncash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnonline.setChecked(false);
                paymenttype="Cash";
            }
        });

        btnonline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btncash.setChecked(false);
                paymenttype="Online";
            }
        });

        buttonplace=findViewById(R.id.buttonbook);
        buttonplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (paymenttype.equals(""))
                {
                    Toast.makeText(OrderPlace.this, "Select Payment Type", Toast.LENGTH_SHORT).show();
                }
                else {
                    placeOrder();
                }

            }
        });




        cardOfferView=findViewById(R.id.CardViewOffer);
        cardOfferView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Url = webServiceClass.getUrl() + webServiceClass.getMETHOD_ALlCOUPONS();
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderPlace.this);
                View view = LayoutInflater.from(OrderPlace.this).inflate(R.layout.layout_applycoupon, null);
                RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclercoupon);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(OrderPlace.this, 1);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                mlist = new ArrayList<>();
                mlist.clear();
//progressbar
                final ProgressDialog pd = new ProgressDialog(OrderPlace.this);
                pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                pd.setCancelable(false);
                pd.setCanceledOnTouchOutside(false);
                pd.show();
                pd.setContentView(R.layout.progress);

                final StringRequest request = new StringRequest(Request.Method.GET, Url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray array = new JSONArray(response);
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject object = array.getJSONObject(i);
                                        String status = object.getString("status");
                                        if (status.equals("1")) {
                                            String startdate = object.getString("end_date");
                                            String CurrentDate = new java.text.SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                                            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                            Date date1 = formatter.parse(startdate);
                                            Date date2 = formatter.parse(CurrentDate);



                                            if (date1.compareTo(date2)>= 0 ) {

                                                CouponModelClass couponModelClass = new CouponModelClass(
                                                        object.getString("coupan_id"),
                                                        object.getString("amount"),
                                                        object.getString("end_date"),
                                                        object.getString("minimum_amount"),
                                                        object.getString("coupan_type"),
                                                        object.getString("coupan_type"),
                                                        "11:59:00 PM");
                                                mlist.add(couponModelClass);
                                            }
                                        }
                                    }
                                    pd.dismiss();
                                    allCouponAdapter = new AllCouponAdapterextends(OrderPlace.this, mlist);
                                    recyclerView.setAdapter(allCouponAdapter);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        //  Toast.makeText(ViewCartDetail.this, "  ", Toast.LENGTH_SHORT).show();
                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(OrderPlace.this);
                requestQueue.add(request);

                builder.setView(view);
                AlertDialog alertDialog = builder.create();
                EditText editText=(EditText)view.findViewById(R.id.couponcode);

                // get adapter Token Value
                BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String   name3= intent.getStringExtra("state");
                        editText.setText(name3);
                    }
                };
                LocalBroadcastManager.getInstance(OrderPlace.this).registerReceiver(mMessageReceiver, new IntentFilter("custom-time"));

                ImageView imageclose=(ImageView)view.findViewById(R.id.closebtn);
                imageclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                Button btnapply=(Button)view.findViewById(R.id.buttonapply);
                btnapply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if( TextUtils.isEmpty(editText.getText())){
                            pd.dismiss();
                            editText.setError( "Enter Coupon Code" );
                        }
                        else
                        {
                            alertDialog.dismiss();
                            coupon=editText.getText().toString().trim();

                            final ProgressDialog pds = new ProgressDialog(OrderPlace.this);
                            pds.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            pds.setCancelable(false);
                            pds.setCanceledOnTouchOutside(false);
                            pds.show();
                            pds.setContentView(R.layout.progress);
//enable when wright coupon
                            String Urls=webServiceClass.getUrl()+webServiceClass.getMethodGetpercoupon()+"/"+coupon;
                            final StringRequest request = new StringRequest(Request.Method.POST,Urls,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONArray array1=new JSONArray(response);
                                                for (int i=0;i<array1.length();i++){
                                                    JSONObject object=array1.getJSONObject(i);
                                                    String msg = object.getString("MESSAGE");
                                                    if (msg.equals("COUPON NOT FOUND")) {
                                                        pds.dismiss();
                                                        Toast.makeText(OrderPlace.this, "Invalid Coupon Code", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        couponname=object.getString("coupan_name");
                                                        coupontype=object.getString("coupan_type");
                                                        alertDialog.dismiss();
                                                        Float allprice = Float.parseFloat(String.format("%.2f",total));
                                                        Float maxprice = Float.parseFloat(object.getString("minimum_amount"));
                                                        if (allprice > maxprice) {
                                                            //dismiss progress
                                                            pds.dismiss();
                                                            ammount=Float.parseFloat(object.getString("amount"));
                                                            Float pricess=allprice-ammount;
                                                            pricess=pricess+ Float.parseFloat(pickupcharges);
                                                            txttotalprice.setText("Rs."+String.format("%.2f",(pricess)));
                                                            txtdiscount.setText("Rs."+object.getString("amount"));
                                                            disc.setVisibility(View.VISIBLE);


                                                        }else{
                                                            pds.dismiss();

                                                            final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(OrderPlace.this,R.style.MyDialogTheme);
                                                            builder.setTitle("Coupon Alert!")
                                                                    .setMessage("Courier charge is less then coupon minimum amount. ")
                                                                    .setCancelable(false)
                                                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                            dialog.dismiss();
                                                                            coupon="";

                                                                        }
                                                                    });

                                                            final android.app.AlertDialog alertDialog = builder.create();
                                                            alertDialog.show();


                                                           // Toast.makeText(OrderPlace.this, "Coupon will apply on courier charge...", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    alertDialog.dismiss();
                                    pds.dismiss();
                                    pd.dismiss();
                                    // Toast.makeText(ViewCartDetail.this, " Connection Problem ", Toast.LENGTH_SHORT).show();
                                }
                            });
                            RequestQueue requestQueue= Volley.newRequestQueue(OrderPlace.this);
                            requestQueue.add(request);
                        }
                    }
                });


                alertDialog.show();
            }
        });


    }

    private void placeOrder() {

        String pickcharge=pickupcharges.toString();
        pickcharge=pickcharge.replace(" ","%20");

        String parcelweights=parcelweight.toString();
        parcelweights=parcelweights.replace(" ","%20");

        String perkmprice=parcelpriceperkm.toString();
        perkmprice=perkmprice.replace(" ","%20");

        String parcellengths=parcellength.replace(" ","%20");
        String parcelwidths=parcellength.replace(" ","%20");
        String parcelheights=parcellength.replace(" ","%20");

        String distances=txtdistance.getText().toString();
        distances=distances.replace(" ","%20");
        distances=distances.replace("KM","");

        String totals=txttotalprice.getText().toString().trim();
        totals=totals.trim();
        totals=totals.replace("Rs. ","");



        pd = new ProgressDialog(OrderPlace.this);
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        pd.setContentView(R.layout.progress);

        String userid=Utils.getSavedPreferences(OrderPlace.this, Hom.userid,"");
        String Urls=webServiceClass.getUrl()+webServiceClass.getMethodInsertorder()+"/"+userid+"/"+pickcharge+"/"+
               totals+"/"+parcelweights+"/"+perkmprice+"/"+parcellengths+"/"+parcelwidths+"/"+parcelheights+"/"+distances+"/"
                +Currentdate+"/"+Currenttime+"/"+paymenttype;

        String finalParcelweights = parcelweights;
        String finalPerkmprice = perkmprice;

        StringRequest request = new StringRequest(Request.Method.POST,Urls,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject object=new JSONObject(response);
                            String msg=object.getString("MESSAGE");
                            if (msg.equals("DATA INSERT SUCCESSFULLY")){
                                 orderid=object.getString("ORDER ID");

                                String Urlss=webServiceClass.getUrl()+webServiceClass.getMethodInsertcouriercharge()+"/"+userid+"/"
                                +orderid+"/"+finalParcelweights+"/"+pickupcharges+"/"+ finalPerkmprice+"/"+admincomission+"/"+pickupradius;
                                StringRequest request2 = new StringRequest(Request.Method.POST,Urlss,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject object2=new JSONObject(response);
                                                    String msg=object2.getString("MESSAGE");

                                                    if (coupon.equals("")){
                                                        InsertLocation();
                                                    }else{

                                                        String Urlsss=webServiceClass.getUrl()+webServiceClass.getMethodApplycoupon()+"/"+userid+"/"+orderid+"/"
                                                                +coupon+"/"+couponname+"/"+coupontype+"/"+ammount;
                                                        StringRequest request3 = new StringRequest(Request.Method.POST,Urlsss,
                                                                new Response.Listener<String>() {
                                                                    @Override
                                                                    public void onResponse(String response) {
                                                                        try {
                                                                            JSONObject object2=new JSONObject(response);
                                                                            String msg=object2.getString("MESSAGE");

                                                                            InsertLocation();

                                                                        } catch (JSONException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    }
                                                                }, new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {
                                                                pd.dismiss();
                                                            }
                                                        });
                                                        requestQueue= Volley.newRequestQueue(OrderPlace.this);
                                                        requestQueue.add(request3);
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
                                });
                                requestQueue= Volley.newRequestQueue(OrderPlace.this);
                                requestQueue.add(request2);

                            }else{
                                pd.dismiss();
                                Toast.makeText(OrderPlace.this, "Try Again", Toast.LENGTH_SHORT).show();
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
        requestQueue= Volley.newRequestQueue(OrderPlace.this);
        requestQueue.add(request);

    }

    private void InsertLocation() {


        String Urlss=webServiceClass.getUrl()+webServiceClass.getMethodOrderpickup();
        StringRequest request2 = new StringRequest(Request.Method.POST,Urlss,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject object1=new JSONObject(response);

                            String msg=object1.getString("MESSAGE");

                            String dropnames=dropname.replace(" ","%20");
                            String dropmobiles=dropmobile.replace(" ","%20");
                            String dropnearbys=dropnearby.replace(" ","%20");
                            dropnearbys=dropnearbys.replace("/","-");

                            String dropflats=dropflat.replace(" ","%20");
                            dropflats=dropflats.replace("/","-");
                            String dropstreets=dropstreet.replace(" ","%20");
                            dropstreets=dropstreets.replace("/","-");
                            String dropcitys=dropcity.replace(" ","%20");
                            dropcitys=dropcitys.replace("/","-");
                            String dropstates=dropstate.replace(" ","%20");
                            dropstates=dropstates.replace("/","-");

                            String user=Utils.getSavedPreferences(OrderPlace.this, Hom.userid,"");
                            String Urlsss=webServiceClass.getUrl()+webServiceClass.getMethodOrderdrop()+"/"+user+"/"+orderid+"/"
                                    +dropnames+"/"+dropmobiles+"/"+dropnearbys+"/"+dropflats+"/"+dropstreets+"/"+dropcitys+"/"
                                    +dropstates+"/"+droppincode+"/"+dropaddresstype;
                            StringRequest request3 = new StringRequest(Request.Method.POST,Urlsss,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                pd.dismiss();
                                                JSONObject object2=new JSONObject(response);
                                                String msg=object2.getString("MESSAGE");


                                                Utils.savePreferences(OrderPlace.this,parceltotalprice,txttotalprice.getText().toString());
                                                Utils.savePreferences(OrderPlace.this,parceldistance,String.valueOf(Distance));
                                                Utils.savePreferences(OrderPlace.this,parceldiscount,String.valueOf(Math.round(ammount)));
                                                Utils.savePreferences(OrderPlace.this,parcelorderid,orderid);
                                                Utils.savePreferences(OrderPlace.this,parcelprice,parcelpriceperkm);
                                                Utils.savePreferences(OrderPlace.this,parcelpickup,pickupcharges);

                                                Intent intent=new Intent(OrderPlace.this,OrderSuccess.class);
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
                            }) ;
                            requestQueue= Volley.newRequestQueue(OrderPlace.this);
                            requestQueue.add(request3);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            protected Map<String,String> getParams()
            {
                Map<String,String> params=new HashMap<String, String>();
                String userid=Utils.getSavedPreferences(OrderPlace.this, Hom.userid,"");
                params.put("user_id",userid);
                params.put("order_id",orderid);
                params.put("u_address_name",pickupname);
                params.put("u_address_mobile_number",pickupmobile);
                params.put("u_address_near_by",pickupnearby);
                params.put("u_address_street",pickupflat);
                params.put("u_address_locality",pickupstreet);
                params.put("u_address_city",pickupcity);
                params.put("u_address_state",pickupstate);
                params.put("u_address_pincode",pickuppincode);
                params.put("u_address_address_type",pickaddresstype);
                params.put("u_address_latitude",Utils.getSavedPreferences(OrderPlace.this,lat,""));
                params.put("u_address_longitude",Utils.getSavedPreferences(OrderPlace.this,lag,""));
                return params;
            }
        };
        requestQueue= Volley.newRequestQueue(OrderPlace.this);
        requestQueue.add(request2);
    }

    private void loadDistance() {


       // String pickup=txtpickup.getText().toString();
        String pickup=pickupstreet+" "+pickupnearby+" "+pickupcity;
        List<Address> addresspickup = null;
        Geocoder geocoder = new Geocoder(this);
        try {
            addresspickup = geocoder.getFromLocationName(pickup, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //String drop=txtdrop.getText().toString();
       String drop=dropstreet+" "+dropnearby+" "+dropcity;
        List<Address> addressdrop = null;
        Geocoder geocoders = new Geocoder(this);
        try {
            addressdrop = geocoders.getFromLocationName(drop, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresspickup != null && !addresspickup.isEmpty()) {
            Address address1 = addresspickup.get(addresspickup.size() - 1);
            if (addressdrop != null && !addressdrop.isEmpty()) {
                Address address2 = addressdrop.get(addressdrop.size() - 1);

                Utils.savePreferences(OrderPlace.this,lat,String.valueOf(address1.getLatitude()));
                Utils.savePreferences(OrderPlace.this,lag,String.valueOf(address1.getLongitude()));


              /*  Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL("https://maps.googleapis.com/maps/api/directions/json?origin=" +address1.getLatitude() + "," + address1.getLongitude() + "&destination=" + address2.getLatitude() + "," + address2.getLongitude() + "&sensor=false&units=metric&mode=driving&"+"key="+getResources().getString(R.string.google_maps_key));
                            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("POST");
                            InputStream in = new BufferedInputStream(conn.getInputStream());
                            response[0] = org.apache.commons.io.IOUtils.toString(in, "UTF-8");

                            JSONObject jsonObject = new JSONObject(response[0]);
                            JSONArray array = jsonObject.getJSONArray("routes");
                            JSONObject routes = array.getJSONObject(0);
                            JSONArray legs = routes.getJSONArray("legs");
                            JSONObject steps = legs.getJSONObject(0);
                            JSONObject distance = steps.getJSONObject("distance");
                            parsedDistance[0] =distance.getString("text");

                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/


             /*   Distance = Float.parseFloat(parsedDistance[0]);

                String dis=String.format("%.1f",Distance);
                if (dis.equals("0.0")){
                    final android.app.AlertDialog.Builder al=new  android.app.AlertDialog.Builder(OrderPlace.this,R.style.MyDialogTheme);
                    al.setCancelable(false);
                    al.setTitle("Alert");
                    al.setMessage("You can not proceed with the same Pick Up and Drop location.");
                    al.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent i=new Intent(OrderPlace.this, PickUpLocation.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }
                    });
                    android.app.AlertDialog alert=al.create();
                    alert.show();


                }*/


                float[] result = new float[1];
                Location.distanceBetween(address1.getLatitude(), address1.getLongitude(), address2.getLatitude(), address2.getLongitude(), result);
                Distance = result[0] / 1000;

                String dis=String.format("%.1f",Distance);
                if (dis.equals("0.0")){
                    pd.dismiss();
                    final android.app.AlertDialog.Builder al=new  android.app.AlertDialog.Builder(OrderPlace.this,R.style.MyDialogTheme);
                    al.setCancelable(false);
                    al.setTitle("Alert");
                    al.setMessage("You can not proceed with the same Pick Up and Drop location.");
                    al.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent i=new Intent(OrderPlace.this, PickUpLocation.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }
                    });
                    android.app.AlertDialog alert=al.create();
                    alert.show();


                }else{

                    String distan=String.format("%.1f",Distance);
                    txtdistance.setText(String.format("%.1f",Distance)+" KM");

                    total=(Float.parseFloat(parcelpriceperkm)*Float.parseFloat(distan));
                    txtpricekg.setText("Rs. "+String.format("%.2f",total));
                    txttotalprice.setText(String.valueOf("Rs. "+String.format("%.2f",Integer.parseInt(pickupcharges)+total)));
                }


            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    private void back() {

        Intent i=new Intent(OrderPlace.this,ParcelWeightMgmt.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra("PickupOrder",pick);
        i.putExtra("drop",drop);
        startActivity(i);
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (!checkPlayServices()) {
            // locationTv.setText("You need to install Google Play Services to use the App properly");
        }
    }
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                finish();
            }

            return false;
        }

        return true;
    }

 /*public String getDistance(final double lat1, final double lon1, final double lat2, final double lon2){
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    URL url = new URL("http://maps.googleapis.com/maps/api/directions/json?origin=" + lat1 + "," + lon1 + "&destination=" + lat2 + "," + lon2 + "&sensor=false&units=metric&mode=driving");
                    final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    response[0] = org.apache.commons.io.IOUtils.toString(in, "UTF-8");

                    JSONObject jsonObject = new JSONObject(response[0]);
                    JSONArray array = jsonObject.getJSONArray("routes");
                    JSONObject routes = array.getJSONObject(0);
                    JSONArray legs = routes.getJSONArray("legs");
                    JSONObject steps = legs.getJSONObject(0);
                    JSONObject distance = steps.getJSONObject("distance");
                    parsedDistance[0] =distance.getString("text");

                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return parsedDistance[0];
    }
*/

}
