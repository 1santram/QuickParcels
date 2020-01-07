package com.skyracle.QuickParcels;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PickUp extends AppCompatActivity {

    WebServiceClass webServiceClass=new WebServiceClass();
    Button addpickup;
    EditText username,usermobile,userflat,userstreet,usernearby,usercity,userstate,userpincode;
    RadioGroup radioGroup;
    RadioButton radioButton1,radioButton2;
    String home="Home";

    Spinner spinnerstate,spinnercity;


    ImageView backimg;

    public static String pickupusernames="pickupusernames";
    public static String pickupusermobiles="pickupusermobiles";
    public static String pickupuserflats="pickupuserflats";
    public static String pickupuserstreets="pickupuserstreets";
    public static String pickupusernearbyp="pickupusernearbyp";
    public static String pickupusercitys="pickupusercitys";
    public static String pickupuserstates="pickupuserstates";
    public static String pickupuserpincodes="pickupuserpincodes";
    public static String pickupuseraddresstype="pickupuseraddresstype";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_up);

        backimg=(ImageView)findViewById(R.id.back_img);
        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               back();
            }
        });


        spinnerstate=findViewById(R.id.spinnerstate);
        spinnercity=findViewById(R.id.spinnercity);

        ArrayList<String> arraystate=new ArrayList<>();
        ArrayList<String> arraycity=new ArrayList<>();

        arraystate.add("Select State");
        arraystate.add("Uttar Pradesh");

        arraycity.add("Select City");
        arraycity.add("Lucknow");

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, arraystate);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinnerstate.setAdapter(adapter);

        ArrayAdapter<String> adapter1 =
                new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, arraycity);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinnercity.setAdapter(adapter1);

        spinnerstate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) view).setTextColor(Color.GRAY);
                ((TextView) view).setTextSize(14);
                String name=spinnerstate.getSelectedItem().toString();

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        spinnercity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) view).setTextColor(Color.GRAY);
                ((TextView) view).setTextSize(14);
                String name=spinnercity.getSelectedItem().toString();

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        usermobile=findViewById(R.id.mobile);
        username=findViewById(R.id.username);
        userflat=findViewById(R.id.userflat);
        userstreet=findViewById(R.id.userstreet);
        usernearby=findViewById(R.id.usernearby);
        /*usercity=findViewById(R.id.usercity);
        userstate=findViewById(R.id.userstate);*/
        userpincode=findViewById(R.id.userpicode);

        radioGroup=findViewById(R.id.radiogroup);
        radioButton1=findViewById(R.id.radio4);
        radioButton1=findViewById(R.id.radio5);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radio4) {
                    home="Home";
                } else  {
                    home="Office";
                }
            }
        });


        addpickup=findViewById(R.id.buttonaddpick);
        addpickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (TextUtils.isEmpty(usermobile.getText())&& usermobile.getText().length()!=10){
                    usermobile.setError("Check mobile number!");
                }else if( TextUtils.isEmpty(username.getText())){
                    username.setError( "First name is required!" );
                }else if( TextUtils.isEmpty(userflat.getText())) {
                    userflat.setError("Flat No is required!");
                } else if( TextUtils.isEmpty(userstreet.getText())) {
                    userstreet.setError("Locality  is required!");
                } else  if (spinnerstate.getSelectedItem().equals("Select State")) {
                    Toast.makeText(PickUp.this, "Select State", Toast.LENGTH_SHORT).show();
                }else  if (spinnercity.getSelectedItem().equals("Select City")) {
                    Toast.makeText(PickUp.this, "Select City", Toast.LENGTH_SHORT).show();
                }

                /*else if( TextUtils.isEmpty(usercity.getText())) {
                    usercity.setError("City  is required!");
                }*/else if (TextUtils.isEmpty(usernearby.getText())){
                    usernearby.setError("Nearby place is required!");
                }else if (TextUtils.isEmpty(userpincode.getText())&& userpincode.getText().length()!=6){
                    userpincode.setError("Check pincode!");
                }else{
                    Utils.savePreferences(PickUp.this,pickupusernames,username.getText().toString());
                    Utils.savePreferences(PickUp.this,pickupusermobiles,usermobile.getText().toString());
                    Utils.savePreferences(PickUp.this,pickupuserflats,userflat.getText().toString());
                    Utils.savePreferences(PickUp.this,pickupuserstates,userstreet.getText().toString());
                    Utils.savePreferences(PickUp.this,pickupusercitys,spinnercity.getSelectedItem().toString());
                    Utils.savePreferences(PickUp.this,pickupuserstates,spinnerstate.getSelectedItem().toString());
                    Utils.savePreferences(PickUp.this,pickupusernearbyp,usernearby.getText().toString());
                    Utils.savePreferences(PickUp.this,pickupuserpincodes,userpincode.getText().toString());
                    Utils.savePreferences(PickUp.this,pickupuseraddresstype,home.toString());

                    InsertAddress();

                }


            }
        });
    }

    private void InsertAddress() {

        final ProgressDialog pd = new ProgressDialog(PickUp.this);
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        pd.setContentView(R.layout.progress);

        String Urls=webServiceClass.getUrl()+webServiceClass.getMethodInsertaddress();
        StringRequest request = new StringRequest(Request.Method.POST,Urls,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            pd.dismiss();
                            JSONObject object=new JSONObject(response);
                            String msg=object.getString("MESSAGE");
                            if (msg.equals("DATA INSERT SUCCESSFULLY")){
                                Intent intent=new Intent(PickUp.this,DropLocation.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("PickupOrder","pickup1");
                                startActivity(intent);
                            }else{
                                Toast.makeText(PickUp.this, "Server Problem", Toast.LENGTH_SHORT).show();
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
        }) {
            protected Map<String,String> getParams()
            {
                Map<String,String> params=new HashMap<String, String>();
                String userid=Utils.getSavedPreferences(PickUp.this,Hom.userid,"");
                params.put("user_id",userid);
                params.put("u_user_mobile_no",usermobile.getText().toString());
                params.put("u_user_name",username.getText().toString());
                params.put("u_flat_number",userflat.getText().toString());
                params.put("u_street",userstreet.getText().toString());
                params.put("u_near_by_place",usernearby.getText().toString());
                params.put("u_city",spinnercity.getSelectedItem().toString());
                params.put("u_state",spinnerstate.getSelectedItem().toString());
                params.put("u_address_type",home.toString());
                params.put("u_pincode",userpincode.getText().toString());

                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(PickUp.this);
        requestQueue.add(request);
    }

    @Override
    public void onBackPressed() {
       super.onBackPressed();
    }

    private void back() {
        Intent i=new Intent(PickUp.this,PickUpLocation.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
