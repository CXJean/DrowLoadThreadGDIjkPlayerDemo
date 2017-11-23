package xuejian.bwie.com.drowloadthreadgdijkplayerdemo.model;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;

import xuejian.bwie.com.drowloadthreadgdijkplayerdemo.MoviesActivity;
import xuejian.bwie.com.drowloadthreadgdijkplayerdemo.R;
import xuejian.bwie.com.drowloadthreadgdijkplayerdemo.bean.MovieBean;
import xuejian.bwie.com.drowloadthreadgdijkplayerdemo.utils.GlideImageLoader;
import xuejian.bwie.com.drowloadthreadgdijkplayerdemo.view.ItemClickLitener;

/**
 * Created by xue on 2017-11-23.
 */

public class MyXRecyclerAdapter extends XRecyclerView.Adapter<XRecyclerView.ViewHolder>{

    private Context context;
    private MovieBean movieBean;
    private ArrayList mlist;

    public MyXRecyclerAdapter(Context context, MovieBean movieBean) {
        this.context = context;
        this.movieBean = movieBean;
    }

    private  enum  Item_Type{
        type1,
        type2
    }
    //实例化接口
    private ItemClickLitener mItemClickListener;

    public void setItemClickListener(ItemClickLitener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    @Override
    public XRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType==Item_Type.type1.ordinal()){
            View view = LayoutInflater.from(context).inflate(R.layout.head_banner, parent, false);
            ViewHolderBanner viewHolderBanner = new ViewHolderBanner(view);
            return viewHolderBanner;
        }else if (viewType==Item_Type.type2.ordinal()){
            View view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);
            ViewHolderList viewHolderList = new ViewHolderList(view);
            return viewHolderList;
        }
    return null;
    }

    @Override
    public void onBindViewHolder(XRecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolderBanner){
            mlist = new ArrayList();
            for (int i = 0;i<6;i++){
                mlist.add(movieBean.getRet().getList().get(i).getPic());
                Toast.makeText(context,movieBean.getRet().getList().get(i).getPic(),Toast.LENGTH_SHORT);
            }
            //设置banner动画效果
            ((ViewHolderBanner)holder).mybanner.setBannerAnimation(Transformer.RotateDown);
//            //设置banner样式
//            ((ViewHolderBanner)holder).mybanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
//            设置图片加载器
            ((ViewHolderBanner)holder).mybanner.setImageLoader(new GlideImageLoader());
            ((ViewHolderBanner)holder).mybanner.setImages(mlist);
            ((ViewHolderBanner)holder).mybanner.start();
            ((ViewHolderBanner) holder).mybanner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    Toast.makeText(context,"图片"+position,Toast.LENGTH_SHORT).show();
                }
            });
        }else if (holder instanceof ViewHolderList){
            ((ViewHolderList)holder).title.setText(movieBean.getRet().getList().get(position).getTitle());

            Uri uri= Uri.parse(movieBean.getRet().getList().get(position).getPic());
            Log.d("适配器", "onBindViewHolder:--------图片------- "+uri);
            ((ViewHolderList)holder).img.setImageURI(uri);

            ((ViewHolderList) holder).description.setText(movieBean.getRet().getList().get(position).getDescription());
            String m="暂无";
;            if (null!=movieBean.getRet().getList().get(position).getAirTime()){
                ((ViewHolderList) holder).airTime.setText(movieBean.getRet().getList().get(position).getAirTime());
             }
            ((ViewHolderList) holder).airTime.setText("上映日期:"+m);
            ((ViewHolderList) holder).duration.setText("时长:"+movieBean.getRet().getList().get(position).getDuration());
            //图片点击
            ((ViewHolderList) holder).img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null!=mItemClickListener){
                        mItemClickListener.onItemSubViewClick(v, position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0) {
            return Item_Type.type1.ordinal();
        }else if (position>0){
            return Item_Type.type2.ordinal();
        }
       return -1;
    }

    @Override
    public int getItemCount() {
        return movieBean.getRet().getList().size();
    }

    class ViewHolderBanner extends XRecyclerView.ViewHolder{

        public Banner mybanner;

        public ViewHolderBanner(View view) {
            super(view);
            mybanner = (Banner) view.findViewById(R.id.banner);
        }
    }
    class ViewHolderList extends XRecyclerView.ViewHolder {

        public SimpleDraweeView img;
        public TextView title;
        public TextView description;
        public TextView airTime;
        public TextView duration;

        public ViewHolderList(final View itemView) {
            super(itemView);
            img = (SimpleDraweeView) itemView.findViewById(R.id.item_img);
            title = (TextView) itemView.findViewById(R.id.item_title);
            description = (TextView) itemView.findViewById(R.id.item_description);
            airTime = (TextView) itemView.findViewById(R.id.item_airTime);
            duration = (TextView) itemView.findViewById(R.id.item_duration);
            //item点击
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null!=mItemClickListener){
                        mItemClickListener.onItemClick(itemView, getPosition());
                    }
                }
            });
            //长按回调
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (null != mItemClickListener) {
                        mItemClickListener.onItemLongClick(itemView, getPosition());
                    }
                    return true;
                }
            });
        }
    }
}
