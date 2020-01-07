package com.skyracle.QuickParcels.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skyracle.QuickParcels.Model.PickupOrder;
import com.skyracle.QuickParcels.OrderDetails;
import com.skyracle.QuickParcels.R;

import java.util.List;

public class PickupAdapter  extends RecyclerView.Adapter<PickupAdapter.MyViewHolder> {

    Context context;


    private List<PickupOrder> OfferList;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView time,date,pickup,drop,bookind,distance;


        public MyViewHolder(View view) {
            super(view);

            time=view.findViewById(R.id.time);
            date=view.findViewById(R.id.date);
            bookind=view.findViewById(R.id.bookingid);
            pickup=view.findViewById(R.id.pickuplocation);
            drop=view.findViewById(R.id.droplocation);
            distance=view.findViewById(R.id.distance);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context.getApplicationContext(), OrderDetails.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    intent.putExtra("bookingid",bookind.getText().toString());
                    intent.putExtra("status","2");
                    context.getApplicationContext().startActivity(intent);
                }
            });
        }

    }
    public PickupAdapter(Context context, List<PickupOrder> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public PickupAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pickup, parent, false);


        return new PickupAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull PickupAdapter.MyViewHolder holder, final int position) {
        final PickupOrder lists = OfferList.get(position);
        holder.date.setText(lists.getDate());
        String times=lists.getTime();
        times=times.replace("-"," ");
        holder.time.setText(times);
        holder.pickup.setText(lists.getPickup());
        holder.drop.setText(lists.getDrop());
        holder.bookind.setText("#"+lists.getOrderid());
        holder.distance.setText(lists.getDistance()+"KM");

    }


    @Override
    public int getItemCount() {
        return OfferList.size();

    }

}


