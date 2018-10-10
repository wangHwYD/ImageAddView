package whw.zjms.com.imageaddview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;

import whw.zjms.com.imageaddview.R;
import whw.zjms.com.imageaddview.view.core.AddPhotoManage;
import whw.zjms.com.imageaddview.view.data.PhotoInfo;


public class AddPhotoView extends RecyclerView {

    private AddPhotoManage mAddPhotoManage;
    private PhotoInfo mInfo;
    private Context mContext;
    private static final String TAG = "AddPhotoView";

    public AddPhotoView(@NonNull Context context) {
        super(context);
    }

    public AddPhotoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        int width = MeasureSpec.getSize(widthSpec);
        if (width > 0 && mInfo.width == 0) {
            mInfo.width = width;
            mAddPhotoManage.init();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

    public AddPhotoManage getAddPhotoManage() {

        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) getLayoutParams();
        lp.leftMargin = (int) (mInfo.margin);
        setLayoutParams(lp);
        mAddPhotoManage = new AddPhotoManage(this, mInfo, mContext);
        return mAddPhotoManage;

    }


    private void init(Context context, AttributeSet attrs) {
        this.mContext = context.getApplicationContext();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AddPhotoView);
        float margin = typedArray.getDimension(R.styleable.AddPhotoView_add_photo_horizontal_margin,
                dp2px(AddPhotoManage.DEFALUT_MARGIN));
        float topMagin = typedArray.getDimension(R.styleable.AddPhotoView_add_photo_vertical_margin,
                dp2px(AddPhotoManage.DEFALUT_MARGIN));
        int maxCount = typedArray.getInteger(R.styleable.AddPhotoView_add_photo_max_count,
                AddPhotoManage.DEFALUT_MAX_COUNT);
        int itemCount = typedArray.getInteger(R.styleable.AddPhotoView_add_photo_horizontal_count,
                AddPhotoManage.DEFAULT_ITEM_COUNT);
        boolean isEdit = typedArray.getBoolean(R.styleable.AddPhotoView_add_photo_edit_mode,
                AddPhotoManage
                        .DEFAULT_EDIT);
        int deleteIcon = typedArray.getResourceId(R.styleable.AddPhotoView_add_photo_delete_icon,
                R.mipmap.ic_photo_delete);
        int addIcon = typedArray.getResourceId(R.styleable.AddPhotoView_add_photo_add_icon, R
                .mipmap.image_add);
        mInfo = new PhotoInfo(isEdit, itemCount, deleteIcon, addIcon, maxCount, margin, topMagin);
        typedArray.recycle();

    }

    private int dp2px(float dpValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mContext = null;
        if (mAddPhotoManage != null) {
            mAddPhotoManage.remove();
        }
    }


}
