package xuejian.bwie.com.drowloadthreadgdijkplayerdemo.model;

import android.util.Log;

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
import xuejian.bwie.com.drowloadthreadgdijkplayerdemo.utils.ApiService;

/**
 * Created by xue on 2017-11-23.
 */

public class HttpModel implements Imodel{

    public static final String TAG="HttpModel";

    private OnGetDataFinish onGetDataFinish;
    //接口
    public interface OnGetDataFinish {
        void OnFinishListener(MovieBean movieBean);
    }

    public void setOnFinish(OnGetDataFinish onDataFinish){
        this.onGetDataFinish=onDataFinish;
    }


    @Override
    public void getHttpData(String url, String catalogId, String pnum) {

        OkHttpClient httpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggingInterceptor())//添加拦截器
                .connectTimeout(30, TimeUnit.SECONDS)//连接超时
                .readTimeout(30, TimeUnit.SECONDS)//读取超时
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .client(httpClient)
                .build();
        //动态代理得到网络接口
        ApiService apiService = retrofit.create(ApiService.class);

        Observable<MovieBean> movieData = apiService.getMovieData(catalogId, pnum);
            movieData.subscribeOn(Schedulers.io())//
                .observeOn(AndroidSchedulers.mainThread())//切换线程
                .subscribe(new Observer<MovieBean>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: ");
                    }

                    @Override
                    public void onNext(MovieBean movieBean) {
                        //请求到的值
                        Log.d(TAG, "onNext: "+movieBean.getMsg()+"=========="+movieBean.getCode());
                        Log.d(TAG, "onNext: "+movieBean.getRet().getList().get(0).getTitle());
                        onGetDataFinish.OnFinishListener(movieBean);
                    }
                });
    }
}
