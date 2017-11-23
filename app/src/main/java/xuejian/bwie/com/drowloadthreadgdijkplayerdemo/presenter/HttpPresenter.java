package xuejian.bwie.com.drowloadthreadgdijkplayerdemo.presenter;

import android.content.Context;

import xuejian.bwie.com.drowloadthreadgdijkplayerdemo.bean.MovieBean;
import xuejian.bwie.com.drowloadthreadgdijkplayerdemo.model.HttpModel;
import xuejian.bwie.com.drowloadthreadgdijkplayerdemo.view.Iview;

/**
 * Created by xue on 2017-11-23.
 */

public class HttpPresenter implements HttpModel.OnGetDataFinish{

    private final Iview iview;
    private final HttpModel httpModel;

    public HttpPresenter(Iview iview) {
        this.iview = iview;
        this.httpModel = new HttpModel() ;
        httpModel.setOnFinish(this);
    }
    //调取model接口，进行网络请求
    public void setMovieData(String url, String catalogId, String pnum){
        httpModel.getHttpData(url,catalogId,pnum);
    }

    @Override
    public void OnFinishListener(MovieBean movieBean) {
        iview.getJsonData(movieBean);
    }

}
