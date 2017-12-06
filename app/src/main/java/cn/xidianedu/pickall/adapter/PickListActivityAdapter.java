package cn.xidianedu.pickall.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.xidianedu.pickall.R;
import cn.xidianedu.pickall.activity.PickDetailActivity;
import cn.xidianedu.pickall.bean.PickParkBean;

/**
 * Created by ShiningForever on 2017/5/10.
 */

public class PickListActivityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<PickParkBean> beanList = new ArrayList<>();
    private Context context;

    public PickListActivityAdapter(Context context) {
        super();
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pick_list_item, parent, false);
        final ItemViewHolder holder = new ItemViewHolder(view);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("------posi", holder.getAdapterPosition() + "");
//                String oid = beanList.get(holder.getAdapterPosition()).getObjectId();
                Intent itemIntent = new Intent(context, PickDetailActivity.class);
//                itemIntent.putExtra("oid", oid);
                context.startActivity(itemIntent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PickParkBean pickParkBean = beanList.get(position);
        ((ItemViewHolder) holder).title.setText(pickParkBean.getTitle());
        ((ItemViewHolder) holder).ratingBar.setRating(pickParkBean.getRating());
        ((ItemViewHolder) holder).ratingText.setText(String.format("%.1f", pickParkBean.getRating()));
        ((ItemViewHolder) holder).price.setText(String.format("%s", pickParkBean.getPrice()));
        ((ItemViewHolder) holder).location.setText(pickParkBean.getLocation());
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public void addSrcAndNotify(List<PickParkBean> list) {
        this.beanList = list;
        this.notifyDataSetChanged();
    }

    // 列表点击事件
//    @Override
//    public void onClick(View view) {
//        Log.d("----listposi-----", holder.getAdapterPosition() + "");
//        String oid = beanList.get(holder.getAdapterPosition()).getObjectId();
//        Intent itemIntent = new Intent(context, PickDetailActivity.class);
//        itemIntent.putExtra("oid", oid);
//        context.startActivity(itemIntent);
//    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        View view;
        ImageView imageView;
        TextView title;
        RatingBar ratingBar;
        TextView ratingText;
        TextView price;
        TextView location;
        
        ItemViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.pick_list_item_img);
            title = (TextView) itemView.findViewById(R.id.pick_list_item_title);
            ratingBar = (RatingBar) itemView.findViewById(R.id.pick_list_item_ratingbar);
            ratingText = (TextView) itemView.findViewById(R.id.pick_list_item_rating_text);
            price = (TextView) itemView.findViewById(R.id.pick_list_item_price);
            location = (TextView) itemView.findViewById(R.id.pick_list_item_location);
        }
    }
}
