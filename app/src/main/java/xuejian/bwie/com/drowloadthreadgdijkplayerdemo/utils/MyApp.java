package xuejian.bwie.com.drowloadthreadgdijkplayerdemo.utils;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import xuejian.bwie.com.drowloadthreadgdijkplayerdemo.gen.DaoMaster;
import xuejian.bwie.com.drowloadthreadgdijkplayerdemo.gen.DaoSession;
import xuejian.bwie.com.drowloadthreadgdijkplayerdemo.gen.UserDao;

/**
 * Created by xue on 2017-11-22.
 * 初始化数据库
 * 单例
 */

public class MyApp extends Application{

    public static UserDao userDao;

    @Override
    public void onCreate() {
        super.onCreate();
        //初实话数据库
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "lenvess.db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        DaoSession daoSession = daoMaster.newSession();
        userDao = daoSession.getUserDao();


        //初始化fresco
        Fresco.initialize(this);
    }
}
