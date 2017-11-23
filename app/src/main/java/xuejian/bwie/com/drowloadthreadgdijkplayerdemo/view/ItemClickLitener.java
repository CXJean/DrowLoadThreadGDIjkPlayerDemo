package xuejian.bwie.com.drowloadthreadgdijkplayerdemo.view;

import android.view.View;

/**
 * Created by xue on 2017-11-23.
 * item条目点击接口
 */

public interface ItemClickLitener {


    void onItemClick(View view, int position);

    void onItemLongClick(View view, int position);

    //    Item 内部View点击
    public void onItemSubViewClick(View view, int postion);

}
