package whw.zjms.com.imageaddview.view.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import whw.zjms.com.imageaddview.R;
import whw.zjms.com.imageaddview.view.core.AddPhotoManage;
import whw.zjms.com.imageaddview.view.listener.PhotoListener;
import whw.zjms.com.imageaddview.view.data.PhotoInfo;


public class AddPhotoAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private PhotoListener mListener;
    private PhotoInfo mInfo;

    public AddPhotoAdapter(@Nullable List<String> data, PhotoInfo info) {
        super(R.layout.item_add_photo, data);
        this.mInfo = info;

    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.addOnClickListener(R.id.iv_delete);
        boolean isEdit = mInfo.isEdit;
        int position = helper.getLayoutPosition();
        boolean isAddIcon = TextUtils.equals(item, AddPhotoManage.TAG);
        ImageView iv = helper.getView(R.id.iv);
        FrameLayout fl = helper.getView(R.id.fl_content);
        //将图片设置成正方形
        float margin = mInfo.margin * (mInfo.itemCount);
        ViewGroup.LayoutParams params = fl.getLayoutParams();
        float widgetWidth = (mInfo.width - margin) / (mInfo.itemCount);
        params.height = (int) widgetWidth;
        params.width = (int) widgetWidth;
        //设置垂直边距

        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) fl
                .getLayoutParams();
        marginLayoutParams.topMargin = (int) mInfo.topMargin;
        helper.setVisible(R.id.iv_delete, isEdit && !isAddIcon);
        if (isEdit) {
            if (!isAddIcon) {
                mListener.photoResource(item, position, iv);
            } else {
                iv.setImageResource(mInfo.addIcon);
                helper.setImageResource(R.id.iv_delete, mInfo.deleteIcon);
            }
        } else {
            mListener.photoResource("", position, iv);
        }

    }


    public void setListener(PhotoListener listener) {
        this.mListener = listener;
    }


}

