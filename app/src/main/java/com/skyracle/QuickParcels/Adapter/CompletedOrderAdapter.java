package com.skyracle.QuickParcels.Adapter;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.skyracle.QuickParcels.Hom;
import com.skyracle.QuickParcels.Model.CompletedOrder;
import com.skyracle.QuickParcels.OrderDetails;
import com.skyracle.QuickParcels.R;
import com.skyracle.QuickParcels.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

public class CompletedOrderAdapter  extends RecyclerView.Adapter<CompletedOrderAdapter.MyViewHolder> {

    Context context;


    private List<CompletedOrder> OfferList;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView time,date,pickup,drop,bookind,distance,downloadurl;
        ImageView download;
        RelativeLayout review;


        public MyViewHolder(View view) {
            super(view);

            review=view.findViewById(R.id.review);
            downloadurl=view.findViewById(R.id.downloadpdf);
            download=view.findViewById(R.id.download);
            time=view.findViewById(R.id.time);
            date=view.findViewById(R.id.date);
            bookind=view.findViewById(R.id.bookingid);
            pickup=view.findViewById(R.id.pickuplocation);
            drop=view.findViewById(R.id.droplocation);
           // distance=view.findViewById(R.id.distance);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context.getApplicationContext(), OrderDetails.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    intent.putExtra("bookingid",bookind.getText().toString());
                    intent.putExtra("status","4");
                    context.getApplicationContext().startActivity(intent);
                }
            });

            review.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialogs = new Dialog(context);
                    dialogs.setContentView(R.layout.dialogreview);
                    ImageView imgclose=(ImageView)dialogs.findViewById(R.id.close_img);
                    imgclose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogs.dismiss();
                        }
                    });
                    RatingBar ratingBar = (RatingBar) dialogs.findViewById(R.id.rating_bar);
                    LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
                    stars.getDrawable(2).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                    stars.getDrawable(0).setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);

                    EditText editText=(EditText)dialogs.findViewById(R.id.inputText);
                    Button btn=(Button)dialogs.findViewById(R.id.submit_feedback);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Float getrating=0.0f;
                            String order=bookind.getText().toString();
                            order=order.replace("#","");

                            String comt=  editText.getText().toString();
                            if (comt.equalsIgnoreCase("")){
                                comt="No%20Message";
                                comt.replace(" ","%20");
                            }
                           comt= comt.replace(" ","%20");
                            int noofstars = ratingBar.getNumStars();
                            getrating = ratingBar.getRating();
                            if (getrating.equals(0.0f)){
                                Toast.makeText(context, "Please Select Rating...", Toast.LENGTH_SHORT).show();
                            }else{

                                String userid= Utils.getSavedPreferences(context, Hom.userid,"");
                                String Urls="http://quickparcels.in/app/qp/rest_server/isrt_usr_msg"+"/"+userid+"/"+order+"/"+getrating+"/"+comt;
                                StringRequest request = new StringRequest(Request.Method.POST,Urls,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONArray array=new JSONArray(response);
                                                    for (int i=0;i<array.length();i++){
                                                        JSONObject object=array.getJSONObject(i);
                                                        String msg=object.getString("MESSAGE");
                                                        if (msg.equalsIgnoreCase("")){
                                                            dialogs.dismiss();
                                                            Toast.makeText(context, "Already Reviewed!", Toast.LENGTH_SHORT).show();
                                                        }else if (msg.equalsIgnoreCase("DATA NOT INSERT")){
                                                            dialogs.dismiss();
                                                            Toast.makeText(context, "Try After Some Time!", Toast.LENGTH_SHORT).show();
                                                        } else{
                                                            dialogs.dismiss();
                                                            Toast.makeText(context, "Successfully submitted", Toast.LENGTH_SHORT).show();
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
                                });
                                RequestQueue requestQueue= Volley.newRequestQueue(context);
                                requestQueue.add(request);
                            }

                        }
                    });

                    dialogs.show();
                }
            });

            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String ids=bookind.getText().toString();
                    ids=ids.replace("#","");
                    String path=String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+"/"+ids+".pdf"));
                    File file=new File(path);
                    if (file.isFile()) {
                        final Dialog dialogs = new Dialog(context, android.R.style.Theme_DeviceDefault_NoActionBar_TranslucentDecor);
                        dialogs.setContentView(R.layout.dialogpdfviewer);
                        ImageView imgclose=(ImageView)dialogs.findViewById(R.id.close_img);
                        imgclose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogs.dismiss();
                            }
                        });

                        // File file1=getContext().getFilesDir().getAbsolutePath("sann.pdf");
                        PDFView pdfView=(PDFView)dialogs.findViewById(R.id.pdfView);
                        pdfView.fromFile(file)
                                .enableAnnotationRendering(true)
                                .scrollHandle(new DefaultScrollHandle(context))
                                .spacing(10)
                                .load();

                        dialogs.show();

                    }else {
                        try {
                            String url = "http://quickparcels.in/app/qp/pdf/" + downloadurl.getText().toString();
                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                            request.setTitle("Quick Parcels");
                            request.setDescription("Invoice downloading...");
                            request.allowScanningByMediaScanner();
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            String id = bookind.getText().toString();
                            id = id.replace("#", "");
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, id + ".pdf");

                            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                            manager.enqueue(request);
                        }catch (Exception e){
                            Toast.makeText(context, "You have to give storage permission", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });
        }

    }

    public CompletedOrderAdapter(Context context, List<CompletedOrder> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public CompletedOrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_completed, parent, false);


        return new CompletedOrderAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull CompletedOrderAdapter.MyViewHolder holder, final int position) {
        final CompletedOrder lists = OfferList.get(position);
        holder.date.setText(lists.getDate());
        String times=lists.getTime();
        times=times.replace("-"," ");
        holder.time.setText(times);
        holder.pickup.setText(lists.getPickup());
        holder.drop.setText(lists.getDrop());
        holder.bookind.setText("#"+lists.getOrderid());
        holder.downloadurl.setText(lists.getDistance());

    }


    @Override
    public int getItemCount() {
        return OfferList.size();

    }

}


