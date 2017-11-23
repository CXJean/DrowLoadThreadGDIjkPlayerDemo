package xuejian.bwie.com.drowloadthreadgdijkplayerdemo.utils;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;
import xuejian.bwie.com.drowloadthreadgdijkplayerdemo.bean.MovieBean;

/**
 * Created by xue on 2017-11-23.
 */

public interface ApiService {
    //网络请求
    @FormUrlEncoded
    @POST("columns/getVideoList.do")//分类
    Observable<MovieBean> getMovieData(@Field("catalogId") String catalogId , @Field("pnum") String pnum);

}
