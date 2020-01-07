package com.skyracle.QuickParcels.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.skyracle.QuickParcels.Model.CouponModelClass;
import com.skyracle.QuickParcels.R;

import java.util.List;

public class AllCouponAdapterextends extends RecyclerView.Adapter<AllCouponAdapterextends.MyViewHolder> {

        Context context;


private List<CouponModelClass> OfferList;


public class MyViewHolder extends RecyclerView.ViewHolder {



    TextView code,prices,date,minimumammount;
    LinearLayout linear;


    public MyViewHolder(View view) {
        super(view);

        code=view.findViewById(R.id.code);
        prices=view.findViewById(R.id.prices);
        date=view.findViewById(R.id.dates);
        minimumammount=view.findViewById(R.id.minimumpurchage);
    }

}


    public AllCouponAdapterextends(Context context, List<CouponModelClass> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public AllCouponAdapterextends.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemcoupon, parent, false);

        return new AllCouponAdapterextends.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull AllCouponAdapterextends.MyViewHolder holder, final int position) {
        final CouponModelClass lists = OfferList.get(position);

        holder.code.setText(lists.getId());
        String dates=lists.getLastDate();
        String times=lists.getTime();
        holder.date.setText(" "+dates+" | "+times);
        holder.minimumammount.setText(" "+lists.getMinimum()+" Only");
        String coupontype=lists.getCoupontype();
        if (coupontype.equals("Percentage"))
        {
            holder.prices.setText(" "+lists.getPrice()+" %");
        }else{
            holder.prices.setText(" Rs. "+lists.getPrice());
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent("custom-time");
                intent.putExtra("state",holder.code.getText().toString());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });



    }


    @Override
    public int getItemCount() {
        return OfferList.size();

    }







































    /*BaseAdapter {

    Context mContext;
    private List<CouponModelClass> mlist;

    public AllCouponAdapter(Context mContext, List<CouponModelClass> mlist) {
        this.mContext = mContext;
        this.mlist = mlist;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v=View.inflate(mContext, R.layout.itemcoupon,null);
        TextView name=(TextView)v.findViewById(R.id.code);
        TextView price=(TextView)v.findViewById(R.id.price);
        TextView dates=(TextView)v.findViewById(R.id.dates);


        name.setText(mlist.get(position).getId());
        price.setText(mlist.get(position).getPrice());
        dates.setText(mlist.get(position).getLastDate());
        return v;
    }*/
}
