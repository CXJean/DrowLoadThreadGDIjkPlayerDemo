package xuejian.bwie.com.drowloadthreadgdijkplayerdemo;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import xuejian.bwie.com.drowloadthreadgdijkplayerdemo.utils.DownloadUtil;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ProgressBar mProgressBar;
    private Button start;
    private Button pause;


    private TextView total;
    private int max;
    private DownloadUtil mDownloadUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }
    //初始化控件
    private void initView() {
        total= (TextView) findViewById(R.id.textView);
        start= (Button) findViewById(R.id.start);
        pause= (Button) findViewById(R.id.delete);
        mProgressBar= (ProgressBar) findViewById(R.id.progressBar);

    }
    //获取数据
    private void initData() {
        //url
        String urlString = "http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4";
        //存储路径
        String localPath = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/localVideo";
        //实例化greendao
        mDownloadUtil = new DownloadUtil(2, localPath, "adc.mp4", urlString,
                this);

        mDownloadUtil.setOnDownloadListener(new DownloadUtil.OnDownloadListener() {
            @Override
            public void downloadStart(int fileSize) {
                Log.w(TAG, "fileSize::" + fileSize);
                max = fileSize;
                mProgressBar.setMax(fileSize);
            }

            @Override
            public void downloadProgress(int downloadedSize) {
                Log.w(TAG, "Compelete::" + downloadedSize);
                mProgressBar.setProgress(downloadedSize);
                total.setText((int) downloadedSize * 100 / max + "%");
            }

            @Override
            public void downloadEnd() {
                Log.w(TAG, "ENd");
            }
        });
        //开始下载
        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mDownloadUtil.start();
            }
        });
        //暂停
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mDownloadUtil.pause();
            }
        });

    }

}
