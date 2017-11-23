package xuejian.bwie.com.drowloadthreadgdijkplayerdemo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xuejian.bwie.com.drowloadthreadgdijkplayerdemo.bean.MovieBean;
import xuejian.bwie.com.drowloadthreadgdijkplayerdemo.model.MyXRecyclerAdapter;
import xuejian.bwie.com.drowloadthreadgdijkplayerdemo.presenter.HttpPresenter;
import xuejian.bwie.com.drowloadthreadgdijkplayerdemo.utils.API;
import xuejian.bwie.com.drowloadthreadgdijkplayerdemo.utils.ApiService;
import xuejian.bwie.com.drowloadthreadgdijkplayerdemo.view.ItemClickLitener;
import xuejian.bwie.com.drowloadthreadgdijkplayerdemo.view.Iview;

public class MoviesActivity extends AppCompatActivity implements Iview{

   public static final String catalogId = "402834815584e463015584e539330016";
    private static final String TAG = "MoviesActivity";
    private XRecyclerView xRecyclerview;
    private HttpPresenter httpPresenter;
    private MyXRecyclerAdapter myXRecyclerAdapter;
    private ProgressDialog progressDlg;
    int pnum=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        initView();
    }

    private void initView() {
        xRecyclerview = (XRecyclerView) findViewById(R.id.xRecyclerview);

        //允许刷新，加载更多
        xRecyclerview.setPullRefreshEnabled(true);
        xRecyclerview.setLoadingMoreEnabled(true);

        //线性布局
        xRecyclerview.setLayoutManager(new LinearLayoutManager(this));
//        //添加分割线
//        xRecyclerview.addItemDecoration(new DividerItemDecoration(MainActivity.this,DividerItemDecoration.VERTICAL_LIST));
        //请求数据
        httpPresenter = new HttpPresenter(this);
        httpPresenter.setMovieData(API.PAGE_Url,catalogId,pnum+"");
    }

    @Override
    public void getJsonData(final MovieBean movieBean) {
        myXRecyclerAdapter = new MyXRecyclerAdapter(this, movieBean);
        xRecyclerview.setAdapter(myXRecyclerAdapter);

        xRecyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(MoviesActivity.this,"刷新...."+pnum, Toast.LENGTH_SHORT).show();
                pnum=0;
                httpPresenter.setMovieData(API.PAGE_Url,catalogId,pnum+"");
                myXRecyclerAdapter.notifyDataSetChanged();//刷新适配器
                xRecyclerview.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                Toast.makeText(MoviesActivity.this,"加载更多...."+pnum,Toast.LENGTH_SHORT).show();
                pnum++;
                Log.d(TAG, "onLoadMore: 加载更多页数"+pnum);
                httpPresenter.setMovieData(API.PAGE_Url,catalogId,pnum+"");

                myXRecyclerAdapter.notifyDataSetChanged();
                xRecyclerview.loadMoreComplete();
            }
        });
        //点击事件
        myXRecyclerAdapter.setItemClickListener(new ItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MoviesActivity.this,"点击了条目"+position,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(MoviesActivity.this,"长按了条目"+position,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemSubViewClick(View view, int postion) {
                Toast.makeText(MoviesActivity.this,"点击了条目"+postion,Toast.LENGTH_SHORT).show();
                progressDlg = new ProgressDialog(
                        MoviesActivity.this);
                progressDlg.setTitle("下载");
                progressDlg.setMessage("下载图片");
                progressDlg.setIcon(R.mipmap.ic_launcher);
                progressDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置水平进度条
                progressDlg.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
                progressDlg.setCancelable(true);// 设置是否可以通过点击Back键取消
                progressDlg.setButton(DialogInterface.BUTTON_POSITIVE, "确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == DialogInterface.BUTTON_POSITIVE){
                            System.out.println("Click POSITIVE");
                        }
                    }
                });
                progressDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == DialogInterface.BUTTON_NEGATIVE){
                            System.out.println("Click negative");
                        }
                    }
                });
                progressDlg.show();
                //线程更新进度条
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        int i = 0;
                        while (i < 100) {
                            try {
                                Thread.sleep(200);
                                // 更新进度条的进度,可以在子线程中更新进度条进度
                                progressDlg.incrementProgressBy(5);
                                progressDlg.incrementSecondaryProgressBy(10);//二级进度条更新方式
                                i+=5;
                                if (i==99){
                                    Toast.makeText(MoviesActivity.this,"下载完成..........",Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {

                            }
                        }
                        // 在进度条走完时删除Dialog
                        progressDlg.dismiss();
                    }
                }).start();
            }
        });
    }
}
