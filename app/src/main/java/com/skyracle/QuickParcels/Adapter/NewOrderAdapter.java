package com.skyracle.QuickParcels.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skyracle.QuickParcels.Model.NewOrder;
import com.skyracle.QuickParcels.OrderDetails;
import com.skyracle.QuickParcels.R;

import java.util.List;

public class NewOrderAdapter extends RecyclerView.Adapter<NewOrderAdapter.MyViewHolder> {

    Context context;


    private List<NewOrder> OfferList;


    public class MyViewHolder extends RecyclerView.ViewHolder {

      TextView time,date,pickup,drop,bookind,distance,weight;


        public MyViewHolder(View view) {
            super(view);

            time=view.findViewById(R.id.time);
            date=view.findViewById(R.id.date);
            bookind=view.findViewById(R.id.bookingid);
            pickup=view.findViewById(R.id.pickuplocation);
            drop=view.findViewById(R.id.droplocation);
            distance=view.findViewById(R.id.distance);
            weight=view.findViewById(R.id.weight);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context.getApplicationContext(), OrderDetails.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    intent.putExtra("bookingid",bookind.getText().toString());
                    intent.putExtra("status","0");
                    context.getApplicationContext().startActivity(intent);
                }
            });
        }

    }


    public NewOrderAdapter(Context context, List<NewOrder> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public NewOrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_neworder, parent, false);


        return new NewOrderAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull NewOrderAdapter.MyViewHolder holder, final int position) {
        final NewOrder lists = OfferList.get(position);

        holder.date.setText(lists.getDate());
        String times=lists.getTime();
        times=times.replace("-"," ");
        holder.time.setText(times);
        holder.pickup.setText(lists.getPickup());
        holder.drop.setText(lists.getDrop());
        holder.bookind.setText("#"+lists.getOrderid());
        holder.distance.setText(lists.getDistance()+"KM");
        holder.weight.setText(lists.getWeight());



    }

    @Override
    public int getItemCount() {
        return OfferList.size();

    }

}


