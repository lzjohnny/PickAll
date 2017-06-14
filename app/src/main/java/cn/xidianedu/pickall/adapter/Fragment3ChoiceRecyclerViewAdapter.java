package cn.xidianedu.pickall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.xidianedu.pickall.R;

/**
 * Created by ShiningForever on 2016/8/8.
 */
public class Fragment3ChoiceRecyclerViewAdapter extends RecyclerView.Adapter<Fragment3ChoiceRecyclerViewAdapter.ViewHolder> implements View.OnClickListener{

    private int[] mChoiceItemImg;
    private String[] mChoiceItemText;

    private OnRecyclerViewItemClickListener mListener = null;
    private Context mContext;


    public Fragment3ChoiceRecyclerViewAdapter(Context context, int[] choiceItemImg, String[] choiceItemText) {
        mContext = context;
        mChoiceItemImg = choiceItemImg;
        mChoiceItemText = choiceItemText;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // ViewGroup不设置成null会崩溃
        // The specified child already has a parent. You must call removeView() on the child's parent first.
//        View choiceItemView = View.inflate(mContext, R.layout.choice_item, null);

        // item内容宽度匹配父元素
        View choiceItemView = LayoutInflater.from(mContext).inflate(R.layout.choice_item, parent, false);
        Fragment3ChoiceRecyclerViewAdapter.ViewHolder holder =  new Fragment3ChoiceRecyclerViewAdapter.ViewHolder(choiceItemView);
        choiceItemView.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.choiceItemImg.setImageResource(mChoiceItemImg[position]);
        holder.choiceItemText.setText(mChoiceItemText[position]);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mChoiceItemText.length;
    }

    @Override
    public void onClick(View view) {
        if (mListener != null) {
            mListener.onItemClick(view, (Integer) view.getTag());
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView choiceItemImg;
        TextView choiceItemText;

        ViewHolder(View itemView) {
            super(itemView);
            choiceItemImg = (ImageView) itemView.findViewById(R.id.choice_item_img);
            choiceItemText = (TextView) itemView.findViewById(R.id.choice_item_text);
        }
    }

    public interface OnRecyclerViewItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mListener = listener;
    }
}
