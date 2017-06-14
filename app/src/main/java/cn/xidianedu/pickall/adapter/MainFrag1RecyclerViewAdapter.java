package cn.xidianedu.pickall.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jude.rollviewpager.RollPagerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cn.xidianedu.pickall.R;
import cn.xidianedu.pickall.activity.LoginActivity;
import cn.xidianedu.pickall.activity.PickDetailActivity;
import cn.xidianedu.pickall.activity.PickListActivity;
import cn.xidianedu.pickall.activity.RecomListActivity;
import cn.xidianedu.pickall.activity.UploadPickPark;
import cn.xidianedu.pickall.bean.MainHeadData;
import cn.xidianedu.pickall.bean.PickParkBean;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ShiningForever on 2017/2/6.
 */

public class MainFrag1RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private static final int TYPE_HEAD = 0;
    private static final int TYPE_PICT = 1;
    private static final int TYPE_NAVI = 2;
    private static final int TYPE_LIST = 3;

    private static final int LIST_EXTRA_ITEM = 3; //头部条目数量，加上采摘信息列表的数量才是整个recycleView的条目数量

    private Context mContext = null;
    private List<MainHeadData> mHeadDataList = new ArrayList<>(); // 从这里取到头部图片Url和文字
    private List<PickParkBean> mListDataList = new ArrayList<>(); // 采摘园列表数据

    public MainFrag1RecyclerViewAdapter(Context context) {
        super();
        mContext = context;
    }

    public void addHeadSrc(List<MainHeadData> headDataList) {
        mHeadDataList = headDataList;
    }

    public void addListSrc(List<PickParkBean> ListDataList) {
        mListDataList = ListDataList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_HEAD;
            case 1:
                return TYPE_PICT;
            case 2:
                return TYPE_NAVI;
            default:
                return TYPE_LIST;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEAD:
                View viewHead = LayoutInflater.from(mContext).inflate(R.layout.main_frag_1_head, parent, false);
                ViewHolderHead holderHead = new ViewHolderHead(viewHead);
                holderHead.item1.setOnClickListener(this);
                holderHead.item2.setOnClickListener(this);
                holderHead.item3.setOnClickListener(this);
                holderHead.item4.setOnClickListener(this);
                holderHead.item5.setOnClickListener(this);
                holderHead.item6.setOnClickListener(this);
                holderHead.item7.setOnClickListener(this);
                holderHead.item8.setOnClickListener(this);
                return holderHead;
            case TYPE_PICT:
                View viewPict = LayoutInflater.from(mContext).inflate(R.layout.main_frag_1_pict, parent, false);
                return new ViewHolderPict(viewPict);
            case TYPE_NAVI:
                View viewNavi = LayoutInflater.from(mContext).inflate(R.layout.main_frag_1_navi, parent, false);
                ViewHolderNavi holderNavi = new ViewHolderNavi(viewNavi);
                holderNavi.leftNavi.setOnClickListener(this);
                holderNavi.rightNavi.setOnClickListener(this);
                return holderNavi;
            case TYPE_LIST:
                View viewList = LayoutInflater.from(mContext).inflate(R.layout.pick_list_item, parent, false);
                final ViewHolderList holderList = new ViewHolderList(viewList);
                holderList.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = holderList.getAdapterPosition();
                        Log.d("-----posi---", position + "");
                        Intent intent = new Intent(mContext, PickDetailActivity.class);
                        intent.putExtra("oid", mListDataList.get(position - 3).getObjectId());
                        mContext.startActivity(intent);
                    }
                });
                return holderList;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof ViewHolderList) {
            // 最常用放前面
            // 添加数据用
            PickParkBean pickPark = mListDataList.get(position - LIST_EXTRA_ITEM);
            ((ViewHolderList) holder).title.setText(pickPark.getTitle());
            ((ViewHolderList) holder).ratingBar.setRating(pickPark.getRating());
            ((ViewHolderList) holder).ratingText.setText(String.format("%.1f", pickPark.getRating()));
            ((ViewHolderList) holder).price.setText(String.format("%s", pickPark.getPrice()));
            ((ViewHolderList) holder).distance.setText(pickPark.getLocation());

        } else if (holder instanceof ViewHolderHead) {
            for (int i = 0; i < ((ViewHolderHead) holder).linearLayoutList.size(); i++) {

//                使用本地资源
//                ((ImageView) ((((ViewHolderHead) holder).linearLayoutList).get(i).getChildAt(0))).setImageResource(fruitIcon[i]);
//                ((TextView) ((((ViewHolderHead) holder).linearLayoutList).get(i).getChildAt(1))).setText(fruitName[i]);
//                Log.d("onBind", Const.SERVER_URL + mHeadDataList.get(i).getIcon());

//                xUtils加载图片
//                x.image().bind(((ImageView) ((((ViewHolderHead) holder).linearLayoutList).get(i).getChildAt(0))), Const.SERVER_URL + mHeadDataList.get(i).getIcon(), imageOptions);

//                Picasso加载图片
                Picasso.with(mContext).load(mHeadDataList.get(i).getIcon().getFileUrl()).into((ImageView) ((ViewHolderHead) holder).linearLayoutList.get(i).getChildAt(0));
                ((TextView) ((ViewHolderHead) holder).linearLayoutList.get(i).getChildAt(1)).setText(mHeadDataList.get(i).getName());
            }
        } else if (holder instanceof ViewHolderPict) {
            (((ViewHolderPict) holder).pagerView).setAdapter(new MainFrag1RollPagerViewAdapter());
        } else if (holder instanceof ViewHolderNavi) {
            ((ViewHolderNavi) holder).leftTitle.setText("个性推荐");
            ((ViewHolderNavi) holder).leftContent.setText("智能发现采摘园");

            ((ViewHolderNavi) holder).rightTitle.setText("信息分享");
            ((ViewHolderNavi) holder).rightContent.setText("分享采摘园信息");
        }
    }

    @Override
    public int getItemCount() {
        //首部信息加载不出来，下面内容也不显示
        //要不然会很奇怪？
        if (mHeadDataList.size() == 0) {
            return 0;
        } else {
            return (mListDataList.size() + LIST_EXTRA_ITEM);
        }
    }

    // 首部、轮播图、导航、列表分别使用不同ViewHolder
    private class ViewHolderHead extends RecyclerView.ViewHolder {
        List<LinearLayout> linearLayoutList = null;
        LinearLayout item1;
        LinearLayout item2;
        LinearLayout item3;
        LinearLayout item4;
        LinearLayout item5;
        LinearLayout item6;
        LinearLayout item7;
        LinearLayout item8;

        ViewHolderHead(View itemView) {
            super(itemView);

            // 头部8个元素由于使用include公用布局，无法设置id，且8个元素总是同时更新
            // 所以才用上层布局遍历得到子布局
            linearLayoutList = new ArrayList<>();

            item1 = (LinearLayout) itemView.findViewById(R.id.main_frag_1_head_item_1);
            item2 = (LinearLayout) itemView.findViewById(R.id.main_frag_1_head_item_2);
            item3 = (LinearLayout) itemView.findViewById(R.id.main_frag_1_head_item_3);
            item4 = (LinearLayout) itemView.findViewById(R.id.main_frag_1_head_item_4);
            item5 = (LinearLayout) itemView.findViewById(R.id.main_frag_1_head_item_5);
            item6 = (LinearLayout) itemView.findViewById(R.id.main_frag_1_head_item_6);
            item7 = (LinearLayout) itemView.findViewById(R.id.main_frag_1_head_item_7);
            item8 = (LinearLayout) itemView.findViewById(R.id.main_frag_1_head_item_8);

            linearLayoutList.add(item1);
            linearLayoutList.add(item2);
            linearLayoutList.add(item3);
            linearLayoutList.add(item4);
            linearLayoutList.add(item5);
            linearLayoutList.add(item6);
            linearLayoutList.add(item7);
            linearLayoutList.add(item8);
        }
    }

    private class ViewHolderPict extends RecyclerView.ViewHolder {
        RollPagerView pagerView;

        ViewHolderPict(View itemView) {
            super(itemView);
            pagerView = (RollPagerView) itemView.findViewById(R.id.main_frag_1_pict_viewpager);
        }
    }

    private class ViewHolderNavi extends RecyclerView.ViewHolder {
        RelativeLayout leftNavi;
        TextView leftTitle;
        TextView leftContent;
        ImageView leftImg;

        RelativeLayout rightNavi;
        TextView rightTitle;
        TextView rightContent;
        ImageView rightImg;

        ViewHolderNavi(View itemView) {
            super(itemView);
            leftNavi = (RelativeLayout) itemView.findViewById(R.id.navi_left);
            leftTitle = (TextView) itemView.findViewById(R.id.main_frag_1_navi_left_text_title);
            leftContent = (TextView) itemView.findViewById(R.id.main_frag_1_navi_left_text_content);
            leftImg = (ImageView) itemView.findViewById(R.id.main_frag_1_navi_left_img);

            rightNavi = (RelativeLayout) itemView.findViewById(R.id.navi_right);
            rightTitle = (TextView) itemView.findViewById(R.id.main_frag_1_navi_right_text_title);
            rightContent = (TextView) itemView.findViewById(R.id.main_frag_1_navi_right_text_content);
            rightImg = (ImageView) itemView.findViewById(R.id.main_frag_1_navi_right_img);
        }
    }

    private class ViewHolderList extends RecyclerView.ViewHolder {
        View view;
        ImageView imageView;
        TextView title;
        RatingBar ratingBar;
        TextView ratingText;
        TextView price;
        TextView distance;

        ViewHolderList(View itemView) {
            super(itemView);
            this.view = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.pick_list_item_img);
            title = (TextView) itemView.findViewById(R.id.pick_list_item_title);
            ratingBar = (RatingBar) itemView.findViewById(R.id.pick_list_item_ratingbar);
            ratingText = (TextView) itemView.findViewById(R.id.pick_list_item_rating_text);
            price = (TextView) itemView.findViewById(R.id.pick_list_item_price);
            distance = (TextView) itemView.findViewById(R.id.pick_list_item_location);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(mContext, PickListActivity.class);

        switch (view.getId()) {
            case R.id.main_frag_1_head_item_1:
                intent.putExtra("title", mHeadDataList.get(0).getName());
                Log.d("---D---", mHeadDataList.get(0).getName());
                mContext.startActivity(intent);
                break;
            case R.id.main_frag_1_head_item_2:
                intent.putExtra("title", mHeadDataList.get(1).getName());
                mContext.startActivity(intent);
                break;
            case R.id.main_frag_1_head_item_3:
                intent.putExtra("title", mHeadDataList.get(2).getName());
                mContext.startActivity(intent);
                break;
            case R.id.main_frag_1_head_item_4:
                intent.putExtra("title", mHeadDataList.get(3).getName());
                mContext.startActivity(intent);
                break;
            case R.id.main_frag_1_head_item_5:
                intent.putExtra("title", mHeadDataList.get(4).getName());
                mContext.startActivity(intent);
                break;
            case R.id.main_frag_1_head_item_6:
                intent.putExtra("title", mHeadDataList.get(5).getName());
                mContext.startActivity(intent);
                break;
            case R.id.main_frag_1_head_item_7:
                intent.putExtra("title", mHeadDataList.get(6).getName());
                mContext.startActivity(intent);
                break;
            case R.id.main_frag_1_head_item_8:
                Toast.makeText(mContext, "更多种类敬请期待", Toast.LENGTH_SHORT).show();
//                intent.putExtra("title", mHeadDataList.get(7).getName());
//                mContext.startActivity(intent);
                break;
            case R.id.navi_left:
//                Toast.makeText(mContext, "left", Toast.LENGTH_SHORT).show();
                Intent recomIntent = new Intent(mContext, RecomListActivity.class);
                mContext.startActivity(recomIntent);
                break;
            case R.id.navi_right:
                SharedPreferences sp = mContext.getSharedPreferences("login_info", MODE_PRIVATE);
                boolean isLogin = sp.getBoolean("is_login", false);
                if (isLogin) {
                    Intent uploadIntent = new Intent(mContext, UploadPickPark.class);
                    mContext.startActivity(uploadIntent);
                } else {
                    Toast.makeText(mContext, "登陆后才可以分享信息", Toast.LENGTH_SHORT).show();
                    Intent loginIntent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(loginIntent);
                }
                break;
        }
    }
}
