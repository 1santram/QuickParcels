package com.skyracle.QuickParcels;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.skyracle.QuickParcels.Fragment.fragment_about;
import com.skyracle.QuickParcels.Fragment.fragment_canceled;
import com.skyracle.QuickParcels.Fragment.fragment_changemobile;
import com.skyracle.QuickParcels.Fragment.fragment_changepassword;
import com.skyracle.QuickParcels.Fragment.fragment_completed;
import com.skyracle.QuickParcels.Fragment.fragment_home;
import com.skyracle.QuickParcels.Fragment.fragment_inProgress;
import com.skyracle.QuickParcels.Fragment.fragment_manageaddress;
import com.skyracle.QuickParcels.Fragment.fragment_neworders;
import com.skyracle.QuickParcels.Fragment.fragment_pickup;
import com.skyracle.QuickParcels.Fragment.fragment_terms;
import com.skyracle.QuickParcels.Model.ChildModel;
import com.skyracle.QuickParcels.Model.HeaderModel;
import com.skyracle.QuickParcels.View.ExpandableNavigationListView;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.CALL_PHONE;

public class Hom extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public static String mobile="mobile";
    public static String userid="userid";

    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;
    Toolbar toolbar;
    TextView editprodile;

    private ExpandableNavigationListView navigationExpandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hom);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

      /*  //Check permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndGrantPermissions();
        }*/


        navigationExpandableListView = (ExpandableNavigationListView) findViewById(R.id.expandable_navigation);

        //redirect to home page
        Intent i = getIntent();
        String data = i.getStringExtra("FromReservation");

        if (data != null && data.contentEquals("0")) {

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.homepage, new fragment_home(),"MYFRAGMENT");
            fragmentTransaction.addToBackStack("MYFRAGMENT").commit();


        }else if (data != null && data.contentEquals("1")) {

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.homepage, new fragment_neworders());
            toolbar.setTitle("Recent Request");
            fragmentTransaction.addToBackStack(null).commit();


        }else if (data != null && data.contentEquals("2")) {

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.homepage, new fragment_inProgress());
            toolbar.setTitle("InProgress Request");
            fragmentTransaction.addToBackStack(null).commit();

        }else if (data != null && data.contentEquals("3")) {

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.homepage, new fragment_completed());
            toolbar.setTitle("Delivered Request");
            fragmentTransaction.addToBackStack(null).commit();


        }else if (data != null && data.contentEquals("4")) {

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.homepage, new fragment_canceled());
            toolbar.setTitle("Canceled Request");
            fragmentTransaction.addToBackStack(null).commit();


        }else if (data != null && data.contentEquals("5")) {

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.homepage, new fragment_manageaddress());
            toolbar.setTitle("Manage Address");
            fragmentTransaction.addToBackStack(null).commit();


        }else if (data != null && data.contentEquals("6")) {

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.homepage, new fragment_pickup());
            toolbar.setTitle("Accepted Request");
            fragmentTransaction.addToBackStack(null).commit();

        }else{
            Fragment fragment=new fragment_home();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.homepage, fragment,"MYFRAGMENT");
            transaction.addToBackStack("MYFRAGMENT").commit();
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layouts);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        navigationExpandableListView
                .init(this)
                .addHeaderModel(new HeaderModel("Home"))
                .addHeaderModel(new HeaderModel("Place Parcel Order"))
                .addHeaderModel(
                        new HeaderModel("Your Parcel Order", R.drawable.add, true)
                                .addChildModel(new ChildModel("Pending"))
                                .addChildModel(new ChildModel("Accepted"))
                                .addChildModel(new ChildModel("In Progress"))
                                .addChildModel(new ChildModel("Delivered"))
                                .addChildModel(new ChildModel("Canceled"))
                )
                .addHeaderModel( new HeaderModel("Profile", R.drawable.add, true)
                        .addChildModel(new ChildModel("Edit Profile"))
                        .addChildModel(new ChildModel("Change Password"))
                        .addChildModel(new ChildModel("Change Mobile Number"))
                        .addChildModel(new ChildModel("Manage Address")))
                .addHeaderModel( new HeaderModel("Communication", R.drawable.add, true)
                        .addChildModel(new ChildModel("Share"))
                        .addChildModel(new ChildModel("About Us"))
                        .addChildModel(new ChildModel("Terms & Condition"))
                        .addChildModel(new ChildModel("Contact Us")))
                .addHeaderModel(new HeaderModel("Sign Out"))
                .build()
                .addOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                        navigationExpandableListView.setSelected(groupPosition);

                        //drawer.closeDrawer(GravityCompat.START);
                        if (id == 0) {
                            toolbar.setTitle("Home");
                            Fragment fragment=new fragment_home();
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.homepage, fragment,"MYFRAGMENT");
                            transaction.addToBackStack("MYFRAGMENT").commit();
                            drawer.closeDrawer(GravityCompat.START);
                        } else if (id == 1) {
                            Intent intent=new Intent(Hom.this,PickUpLocation.class);
                            startActivity(intent);
                        } else if (id == 2) {

                        } else if (id == 3) {

                        }else if (id == 4) {

                        }else if (id == 5) {
                            final AlertDialog.Builder al=new  AlertDialog.Builder(Hom.this,R.style.MyDialogTheme);
                            al.setCancelable(false);
                            al.setMessage("Do you to Log Out? ");
                            al.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ConnectionHelper con = new ConnectionHelper(Hom.this);
                                    SQLiteDatabase db = con.getWritableDatabase();
                                    String query = "delete from userinfo";
                                    db.execSQL(query);
                                    db.close();
                                    con.close();
                                    Intent i=new Intent(Hom.this, SplashScreen.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);

                                }
                            });
                            al.setNegativeButton("no", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog alert=al.create();
                            alert.show();
                            drawer.closeDrawer(GravityCompat.START);
                        }
                        return false;
                    }
                })
                .addOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                        navigationExpandableListView.setSelected(groupPosition, childPosition);

                        if (groupPosition==2){
                            if (id == 0) {
                                toolbar.setTitle("Pending Request");
                                Fragment fragment=new fragment_neworders();
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.homepage, fragment);
                                transaction.addToBackStack(null).commit();
                                drawer.closeDrawer(GravityCompat.START);
                            } else if (id == 1) {
                                toolbar.setTitle("Accepted Request");
                                Fragment fragment=new fragment_pickup();
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.homepage, fragment);
                                transaction.addToBackStack(null).commit();
                                drawer.closeDrawer(GravityCompat.START);
                            } else if (id == 2) {
                                toolbar.setTitle("InProgress Request");
                                Fragment fragment=new fragment_inProgress();
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.homepage, fragment);
                                transaction.addToBackStack(null).commit();
                                drawer.closeDrawer(GravityCompat.START);
                            } else if (id == 3) {
                                toolbar.setTitle("Delivered Request");
                                Fragment fragment=new fragment_completed();
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.homepage, fragment);
                                transaction.addToBackStack(null).commit();
                            }else if (id == 4) {
                                toolbar.setTitle("Cancel Request");
                                Fragment fragment=new fragment_canceled();
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.homepage, fragment);
                                transaction.addToBackStack(null).commit();
                                drawer.closeDrawer(GravityCompat.START);
                            }
                        }else if (groupPosition==3){
                            if (id == 0) {
                                Intent intent=new Intent(Hom.this,UserProfile.class);
                                startActivity(intent);
                            } else if (id == 1) {
                                toolbar.setTitle("Change Password");
                                Fragment fragment=new fragment_changepassword();
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.homepage, fragment);
                                transaction.addToBackStack(null).commit();
                                drawer.closeDrawer(GravityCompat.START);

                            } else if (id == 2) {
                                toolbar.setTitle("Change Contact Number");
                                Fragment fragment=new fragment_changemobile();
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.homepage, fragment);
                                transaction.addToBackStack(null).commit();
                            }else if (id == 3) {
                                toolbar.setTitle("Manage Address");
                                Fragment fragment=new fragment_manageaddress();
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.homepage, fragment);
                                transaction.addToBackStack(null).commit();
                            }
                        }else if (groupPosition==4){
                            if (id == 0) {
                               // Toast.makeText(Hom.this, "Share is Clicked", Toast.LENGTH_SHORT).show();
                                drawer.closeDrawer(GravityCompat.START);
                                ShareCompat.IntentBuilder.from(Hom.this)
                                        .setType("text/plain")
                                        .setChooserTitle("Share application")
                                        .setText("http://play.google.com/store/apps/details?id=" +getPackageName())
                                        .startChooser();
                            } else if (id == 1) {
                                drawer.closeDrawer(GravityCompat.START);
                                toolbar.setTitle("About Us");
                                Fragment fragment=new fragment_about();
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.homepage, fragment);
                                transaction.addToBackStack(null).commit();
                            }else if (id==2){
                                drawer.closeDrawer(GravityCompat.START);
                                toolbar.setTitle("Terms & Condition");
                                Fragment fragment=new fragment_terms();
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.homepage, fragment);
                                transaction.addToBackStack(null).commit();
                            }else if (id==3){
                                drawer.closeDrawer(GravityCompat.START);
                                ViewGroup viewGroup = findViewById(android.R.id.content);
                                View dialogView = LayoutInflater.from(Hom.this).inflate(R.layout.my_dialog, viewGroup, false);
                                AlertDialog.Builder builder = new AlertDialog.Builder(Hom.this);
                                builder.setView(dialogView);
                                AlertDialog alertDialog = builder.create();
                                alertDialog.setCancelable(false);
                                alertDialog.show();
                                TextView contact=(TextView)alertDialog.findViewById(R.id.contact);
                                contact.setOnClickListener(new View.OnClickListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.M)
                                    @Override
                                    public void onClick(View v) {

                                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                                        callIntent.setData(Uri.parse("tel:" +contact.getText().toString() ));
                                        if (ContextCompat.checkSelfPermission(Hom.this, CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                            startActivity(callIntent);
                                        } else {
                                            requestPermissions(new String[]{CALL_PHONE}, 1);
                                        }
                                    }
                                });
                                TextView gmail=(TextView)alertDialog.findViewById(R.id.emailid);
                                gmail.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        final Intent intent = new Intent(Intent.ACTION_SEND);
                                        intent.setType("plain/text");
                                        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{gmail.getText().toString()});
                                        startActivity(intent);
                                    }
                                });
                                Button button=(Button)alertDialog.findViewById(R.id.buttonOk);
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertDialog.dismiss();
                                    }
                                });
                            }
                        }


                        drawer.closeDrawer(GravityCompat.START);
                        return false;
                    }
                });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layouts);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layouts);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            fragment_home fragment;
            fragment = (fragment_home) getSupportFragmentManager().findFragmentByTag("MYFRAGMENT");
            if (fragment==null) {
                toolbar.setTitle("Home");
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.homepage, new fragment_home(),"MYFRAGMENT");
                fragmentTransaction.addToBackStack("MYFRAGMENT").commit();
            }else{
                if ( fragment.isVisible()) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyDialogTheme);
                    builder.setMessage("Confirm to exit.")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent a = new Intent(Intent.ACTION_MAIN);
                                    a.addCategory(Intent.CATEGORY_HOME);
                                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(a);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }else{
                    toolbar.setTitle("Home");
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.homepage, new fragment_home(),"MYFRAGMENT");
                    fragmentTransaction.addToBackStack("MYFRAGMENT").commit();
                }
            }



        }
    }

    @Override
    protected void onStart() {
        try {
            ConnectionHelper cone = new ConnectionHelper(Hom.this);
            SQLiteDatabase dbs = cone.getWritableDatabase();
            String querys = "select * from userinfo";
            Cursor cs = dbs.rawQuery(querys, null);
            cs.moveToLast();
            try {
                String numb = cs.getString(0);
                String user=cs.getString(2);
                Utils.savePreferences(Hom.this, mobile, numb);
                Utils.savePreferences(Hom.this,userid,user);
                cs.close();
            } catch (Exception e) {
            }

            dbs.close();
            cone.close();
        }catch (Exception e){
        }
        super.onStart();
    }

  /*  @TargetApi(Build.VERSION_CODES.M)
    private void checkAndGrantPermissions() {

        int stor = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);

        List<String> permissions = new ArrayList<String>();


        if (stor == PackageManager.PERMISSION_DENIED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }



        if (!permissions.isEmpty()) {
            requestPermissions(permissions.toArray(new String[permissions.size()]), 200);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 200) {
            if ((grantResults[0] == PackageManager.PERMISSION_DENIED ) ) {

                checkAndGrantPermissions();
                finish();

            }
        }
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                //denied
                Log.e("denied", permission);
            } else {
                if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                    //allowed
                    Log.e("allowed", permission);
                    statusCheck();

                } else {
                    //set to never ask again
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", Hom.this.getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
            }
        }
    }

    private void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }else{
         //  finish();
        }
    }

    private void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyDialogTheme);
        builder.setMessage("Your GPS seems to be disabled,do you want to enable  it ?")
                .setCancelable(false)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }*/

}
