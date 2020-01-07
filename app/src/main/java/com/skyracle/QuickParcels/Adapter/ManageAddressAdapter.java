package com.skyracle.QuickParcels.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skyracle.QuickParcels.EditAddedAddress;
import com.skyracle.QuickParcels.Model.ManageAddress;
import com.skyracle.QuickParcels.R;

import java.util.List;

public class ManageAddressAdapter extends RecyclerView.Adapter<ManageAddressAdapter.MyViewHolder> {

    Context context;
    private List<ManageAddress> OfferList;
    private int lastSelectedPosition = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView btnedit,btndelete;
        TextView addresstype,name,street,locality,city,mobile,state,addressid,pincode;
        LinearLayout linearLayout;


        public MyViewHolder(View view) {
            super(view);


            linearLayout=(LinearLayout)view.findViewById(R.id.line1) ;
            addresstype=(TextView)view.findViewById(R.id.addresstype);
            name=(TextView)view.findViewById(R.id.name);
            street=(TextView)view.findViewById(R.id.street);
            locality=(TextView)view.findViewById(R.id.locality);
            city=(TextView)view.findViewById(R.id.city);
            mobile=(TextView)view.findViewById(R.id.mobile);
            state=(TextView)view.findViewById(R.id.state);
            pincode=(TextView)view.findViewById(R.id.pincode);
            addressid=(TextView)view.findViewById(R.id.addressid);
            btnedit=(ImageView) view.findViewById(R.id.edit);
            btndelete=(ImageView) view.findViewById(R.id.delete);



            if (OfferList.size()==1)
            {

                btndelete.setVisibility(View.GONE);

            }
            else{
                btndelete.setVisibility(View.VISIBLE);
            }

            btndelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent("custom1");
                    intent.putExtra("mobile", addressid.getText().toString());
                    intent.putExtra("value", "true");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            });

            btnedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(context.getApplicationContext(), EditAddedAddress.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    intent.putExtra("addressid",addressid.getText().toString());
                    context.getApplicationContext().startActivity(intent);
                }
            });


        }

    }


    public ManageAddressAdapter(Context context, List<ManageAddress> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public ManageAddressAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_manageaddress, parent, false);


        return new ManageAddressAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull ManageAddressAdapter.MyViewHolder holder, final int position) {
        final ManageAddress lists = OfferList.get(position);

        holder.addresstype.setText(lists.getAddresstype());
        holder.mobile.setText(lists.getUsermobile());
        holder.name.setText(lists.getUsername());
        holder.locality.setText(lists.getFlat());
        holder.city.setText(lists.getCity());
        holder.street.setText(lists.getStreet());
        holder.addressid.setText(lists.getAddressid());
        holder.state.setText(lists.getState());
        holder.pincode.setText(lists.getPincode());
    }

    @Override
    public int getItemCount() {
        return OfferList.size();

    }

}

