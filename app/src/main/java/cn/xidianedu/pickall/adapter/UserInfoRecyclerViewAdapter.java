package cn.xidianedu.pickall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.xidianedu.pickall.R;

/**
 * Created by ShiningForever on 2016/8/14.
 */
public class UserInfoRecyclerViewAdapter extends RecyclerView.Adapter<UserInfoRecyclerViewAdapter.ViewHolder> implements View.OnClickListener{
    private OnRecyclerViewItemClickListener listener = null;
    List<String> titleList;
    List<String> contentList;
    Context context;

    public UserInfoRecyclerViewAdapter(Context context, List<String> contentList) {
        this.context = context;
        this.contentList = contentList;

        this.titleList = new ArrayList<>();
        titleList.add("用户名");
        titleList.add("昵称");
        titleList.add("性别");
        titleList.add("个人简介");
        titleList.add("年级");
        titleList.add("课程数量");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.user_info_item, parent, false);
//        View itemView = View.inflate(context, R.layout.user_info_item, null);
        itemView.setOnClickListener(this);
        return new UserInfoRecyclerViewAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvTitle.setText(titleList.get(position));
        holder.tvContent.setText(contentList.get(position));
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return titleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvContent;
        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.user_info_item_title);
            tvContent = (TextView) itemView.findViewById(R.id.user_info_item_content);
        }
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onItemClick(view, (Integer) view.getTag());
        }
    }

    public interface OnRecyclerViewItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }
}
