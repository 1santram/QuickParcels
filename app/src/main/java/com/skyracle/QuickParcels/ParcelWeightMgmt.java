package com.skyracle.QuickParcels;

import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.skyracle.QuickParcels.Services.WebServiceClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParcelWeightMgmt extends AppCompatActivity {

    WebServiceClass webServiceClass=new WebServiceClass();
    Button buttonplace;
    Spinner spinnerweight,spinnerlenght,spinnerwidth,spinnerheight;
    TextView priceperkm,pickups,rs1,rs2;
    EditText edtdesc;
    ImageView backimg;

    public static String weights="weights";
    public static String widths="width";
    public static String heights="height";
    public static String lengths="lenght";
    public static String desc="desc";
    public static String pricekm="prices/km";
    public static String pickup="PickupOrder";
    public static String admin="admin";
    public static String radius="radius";

    ArrayList<String> arrayweight=new ArrayList<>();
    ArrayList<String> arrayprice=new ArrayList<>();
    ArrayList<String> arraypickup=new ArrayList<>();
    ArrayList<String> arraylength=new ArrayList<>();
    ArrayList<String> arraywidth=new ArrayList<>();
    ArrayList<String> arrayheight=new ArrayList<>();
    ArrayList<String> arrayadmin=new ArrayList<>();
    ArrayList<String> arrayradius=new ArrayList<>();

    String pick,drop;
    String pickupradius,admincommision;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcel_weight_mgmt);

        pick=getIntent().getStringExtra("PickupOrder");
        drop=getIntent().getStringExtra("drop");

        backimg=(ImageView)findViewById(R.id.back_img);
        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        spinnerweight=findViewById(R.id.spinnerweight);
        spinnerheight=findViewById(R.id.spinnerheight);
        spinnerwidth=findViewById(R.id.spinnerwidth);
        spinnerlenght=findViewById(R.id.spinnerlength);
        priceperkm=findViewById(R.id.price);
        edtdesc=findViewById(R.id.edtdescriptio);
        pickups=findViewById(R.id.pickup);
        rs1=findViewById(R.id.rs1);
        rs2=findViewById(R.id.rs2);

        arraylength.add("Select Length");
        arraylength.add("1-15   cm");
        arraylength.add("16-30  cm");
        arraylength.add("31-50  cm");
        arraylength.add("50-100 cm");

        arraywidth.add("Select Width");
        arraywidth.add("1-15   cm");
        arraywidth.add("16-30  cm");
        arraywidth.add("31-50  cm");
        arraywidth.add("50-100 cm");

        arrayheight.add("Select Height");
        arrayheight.add("1-15   cm");
        arrayheight.add("16-30  cm");
        arrayheight.add("31-50  cm");
        arrayheight.add("50-100 cm");



        loadCourierCharge();

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, arrayweight);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

        spinnerweight.setAdapter(adapter);
        spinnerweight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) view).setTextColor(Color.GRAY);
                ((TextView) view).setTextSize(14);
                String name=spinnerweight.getSelectedItem().toString();
                int number=spinnerweight.getSelectedItemPosition();
                priceperkm.setText(arrayprice.get(number));
                String price=arraypickup.get(number);
                admincommision=arrayadmin.get(number);
                pickupradius=arrayradius.get(number);
                if (number>0){
                    rs1.setVisibility(View.VISIBLE);
                    rs2.setVisibility(View.VISIBLE);
                }
                if (price.equals("")){
                    pickups.setText("0");
                }else{
                    pickups.setText(price);
                }


            }
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, arraylength);
        arrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> arrayAdapter1 =
                new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, arraywidth);
        arrayAdapter1.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> arrayAdapter2 =
                new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, arrayheight);
        arrayAdapter2.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

        spinnerheight.setAdapter(arrayAdapter);
        spinnerheight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) view).setTextColor(Color.GRAY);
                ((TextView) view).setTextSize(14);
                String name=spinnerheight.getSelectedItem().toString();

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
        spinnerwidth.setAdapter(arrayAdapter1);
        spinnerwidth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) view).setTextColor(Color.GRAY);
                ((TextView) view).setTextSize(14);
                String name=spinnerwidth.getSelectedItem().toString();

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
        spinnerlenght.setAdapter(arrayAdapter2);
        spinnerlenght.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) view).setTextColor(Color.GRAY);
                ((TextView) view).setTextSize(14);
                String name=spinnerlenght.getSelectedItem().toString();
                // Toast.makeText(SignUp.this, name+"", Toast.LENGTH_SHORT).show();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });




        buttonplace=findViewById(R.id.buttoncoun);
        buttonplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinnerweight.getSelectedItem().equals("Select Weight")) {
                    Toast.makeText(ParcelWeightMgmt.this, "Select Courier Weight", Toast.LENGTH_SHORT).show();
                }else{

                    Utils.savePreferences(ParcelWeightMgmt.this,heights,spinnerheight.getSelectedItem().toString());
                    Utils.savePreferences(ParcelWeightMgmt.this,widths,spinnerwidth.getSelectedItem().toString());
                    Utils.savePreferences(ParcelWeightMgmt.this,lengths,spinnerlenght.getSelectedItem().toString());
                    Utils.savePreferences(ParcelWeightMgmt.this,weights,spinnerweight.getSelectedItem().toString());
                    Utils.savePreferences(ParcelWeightMgmt.this,pricekm,priceperkm.getText().toString());
                    Utils.savePreferences(ParcelWeightMgmt.this,desc,edtdesc.getText().toString());
                    Utils.savePreferences(ParcelWeightMgmt.this,pickup,pickups.getText().toString());
                    Utils.savePreferences(ParcelWeightMgmt.this,admin,admincommision);
                    Utils.savePreferences(ParcelWeightMgmt.this,radius,pickupradius);

                    Intent intent=new Intent(ParcelWeightMgmt.this,OrderPlace.class);
                    intent.putExtra("PickupOrder",pick);
                    intent.putExtra("drop",drop);
                    startActivity(intent);
                }

            }
        });
    }

    private void loadCourierCharge() {

        arrayweight.clear();
        arrayprice.clear();
        arraypickup.clear();
        arrayadmin.clear();
        arrayradius.clear();
        arrayweight.add("Select Weight");
        arrayprice.add("Courier Charge");
        arraypickup.add("Pickup Charge");
        arrayadmin.add("Select AdminCharge");
        arrayradius.add("Select Radius");


        String Urls=webServiceClass.Url+webServiceClass.METHOD_COURIERCHARGE;
        StringRequest request = new StringRequest(Request.Method.POST,Urls,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray array1 = new JSONArray(response);
                            for (int i=0;i<array1.length();i++){
                                JSONObject object=array1.getJSONObject(i);
                                String status=object.getString("status");
                                if (status.equals("1")){
                                    arrayweight.add(object.getString("courier_weight"));
                                    arrayprice.add(object.getString("price"));
                                    arraypickup.add(object.getString("base_price"));
                                    arrayadmin.add(object.getString("admin_commission"));
                                    arrayradius.add(object.getString("pickup_radius"));
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
        RequestQueue requestQueue= Volley.newRequestQueue(ParcelWeightMgmt.this);
        requestQueue.add(request);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void back() {

        Intent i=new Intent(ParcelWeightMgmt.this,DropLocation.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra("PickupOrder",pick);
        startActivity(i);
    }
}
