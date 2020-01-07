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
import android.widget.RadioButton;
import android.widget.TextView;

import com.skyracle.QuickParcels.Model.DropAddress;
import com.skyracle.QuickParcels.R;
import com.skyracle.QuickParcels.UpdateAddress;

import java.util.List;

public class DropAddressAdapter extends RecyclerView.Adapter<DropAddressAdapter.MyViewHolder> {

    Context context;
    private List<DropAddress> OfferList;
    private int lastSelectedPosition = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView btnedit,btndelete;
        TextView addresstype,name,flat,street,city,mobile,state,addressid,pincode,nearby,pick;
        LinearLayout linearLayout;
        RadioButton radioButton;


        public MyViewHolder(View view) {
            super(view);


            linearLayout=(LinearLayout)view.findViewById(R.id.line1) ;
            addresstype=(TextView)view.findViewById(R.id.addresstype);
            name=(TextView)view.findViewById(R.id.name);
            flat=(TextView)view.findViewById(R.id.flat);
            street=(TextView)view.findViewById(R.id.street);
            city=(TextView)view.findViewById(R.id.city);
            mobile=(TextView)view.findViewById(R.id.mobile);
            state=(TextView)view.findViewById(R.id.state);
            pincode=(TextView)view.findViewById(R.id.pincode);
            addressid=(TextView)view.findViewById(R.id.addressid);
            radioButton=(RadioButton)view.findViewById(R.id.radio);
            nearby=(TextView) view.findViewById(R.id.nearby);
            btndelete=(ImageView) view.findViewById(R.id.delete);
            btnedit=(ImageView)view.findViewById(R.id.edit);
            pick=(TextView)view.findViewById(R.id.pick);



            /*if (OfferList.size()==1)
            {

                btndelete.setVisibility(View.GONE);

            }
            else{
                btndelete.setVisibility(View.VISIBLE);
            }*/

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

                    Intent intent=new Intent(context.getApplicationContext(), UpdateAddress.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    intent.putExtra("addressid",addressid.getText().toString());
                    intent.putExtra("layout","drop");
                    intent.putExtra("PickupOrder",pick.getText().toString());
                    context.getApplicationContext().startActivity(intent);
                }
            });

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();

                    Intent intent = new Intent("custom4");
                    intent.putExtra("mobile", mobile.getText().toString());
                    intent.putExtra("city", city.getText().toString());
                    intent.putExtra("address",addresstype.getText().toString());
                    intent.putExtra("locality",street.getText().toString());
                    intent.putExtra("street",flat.getText().toString());
                    intent.putExtra("name",name.getText().toString());
                    intent.putExtra("state",state.getText().toString());
                    intent.putExtra("pincode",pincode.getText().toString());
                    intent.putExtra("nearby",nearby.getText().toString());
                    intent.putExtra("value", "true");
                    intent.putExtra("count",getItemCount());
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                }
            });

        }

    }


    public DropAddressAdapter(Context context, List<DropAddress> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public DropAddressAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_addedaddress, parent, false);


        return new DropAddressAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull DropAddressAdapter.MyViewHolder holder, final int position) {
        final DropAddress lists = OfferList.get(position);

        holder.addresstype.setText(lists.getAddresstype());
        holder.mobile.setText(lists.getUsermobile());
        holder.name.setText(lists.getUsername());
        holder.flat.setText(lists.getFlat());
        holder.city.setText(lists.getCity());
        holder.street.setText(lists.getStreet());
        holder.addressid.setText(lists.getAddressid());
        holder.state.setText(lists.getState());
        holder.pincode.setText(lists.getPincode());
        holder.nearby.setText(lists.getNearby());
        holder.pick.setText(lists.getPickup());
        holder.radioButton.setChecked(lastSelectedPosition==position);


    }

    @Override
    public int getItemCount() {
        return OfferList.size();

    }

}

