package cn.xidianedu.pickall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.xidianedu.pickall.R;
import cn.xidianedu.pickall.bean.ForumItem;

/**
 * Created by ShiningForever on 2016/8/29.
 */
public class ForumFragment1RecyclerViewAdapter extends RecyclerView.Adapter<ForumFragment1RecyclerViewAdapter.ViewHolder> {
    private List<ForumItem> forumItemList;
    private Context context;


    public ForumFragment1RecyclerViewAdapter(List<ForumItem> forumItemList, Context context) {
        this.forumItemList = forumItemList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = View.inflate(context, R.layout.forum_1_item, parent);
        View view = LayoutInflater.from(context).inflate(R.layout.forum_1_item, parent, false);
        return new ForumFragment1RecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textViewTitle.setText(forumItemList.get(position).getTitle());
        holder.textViewTag.setText(forumItemList.get(position).getTag());
        holder.textViewName.setText(forumItemList.get(position).getName());
        holder.textViewTime.setText(forumItemList.get(position).getTime());
        holder.textViewNum.setText(forumItemList.get(position).getNum());
    }

    @Override
    public int getItemCount() {
        return forumItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewTitle;
        TextView textViewTag;
        TextView textViewName;
        TextView textViewTime;
        TextView textViewNum;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.forum_1_item_iv);
            textViewTitle = (TextView) itemView.findViewById(R.id.forum_1_item_title_tv);
            textViewTag = (TextView) itemView.findViewById(R.id.forum_1_item_tag_tv);
            textViewName = (TextView) itemView.findViewById(R.id.forum_1_item_reply_name_tv);
            textViewTime = (TextView) itemView.findViewById(R.id.forum_1_item_reply_time_tv);
            textViewNum = (TextView) itemView.findViewById(R.id.forum_1_item_reply_num_tv);
        }
    }
}
