package whw.zjms.com.imageaddview.view.core;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import whw.zjms.com.imageaddview.view.listener.PhotoEditActionListener;
import whw.zjms.com.imageaddview.view.listener.PhotoListener;
import whw.zjms.com.imageaddview.view.listener.PhotoResourceListener;
import whw.zjms.com.imageaddview.view.adapter.AddPhotoAdapter;
import whw.zjms.com.imageaddview.view.data.PhotoInfo;


public class AddPhotoManage implements PhotoListener, BaseQuickAdapter.OnItemChildClickListener,
        BaseQuickAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private AddPhotoAdapter mAdapter;
    private PhotoInfo mInfo;
    private List<String> mPhotoList = new ArrayList<>();
    private PhotoEditActionListener mPhotoEditActionListener;
    private PhotoResourceListener mPhotoResourceListener;
    private AddPhotoTask mPhotoAddTask;
    //默认边距 水平边距和垂直边距
    public static final float DEFALUT_MARGIN = 10;
    //默认最大张数
    public static final int DEFALUT_MAX_COUNT = 6;
    //默认可编辑
    public static final boolean DEFAULT_EDIT = true;
    //默认一行4张
    public static final int DEFAULT_ITEM_COUNT = 4;
    public static final String TAG = "AddPhotoManage";
    private Context mContext;


    public AddPhotoManage(RecyclerView rv, PhotoInfo info, Context context) {
        this.mRecyclerView = rv;
        this.mInfo = info;
        this.mContext = context;
    }

    public void init() {
        if (mAdapter == null) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, mInfo.itemCount,
                    GridLayoutManager.VERTICAL, false));
            if (mInfo.isEdit) {
                //可编辑显示按钮图标
                mPhotoList.add(TAG);
            }
            mAdapter = new AddPhotoAdapter(mPhotoList, mInfo);
            mAdapter.setListener(this);
            mPhotoAddTask = new AddPhotoTask(mAdapter);
            mAdapter.setOnItemChildClickListener(this);
            mAdapter.setOnItemClickListener(this);
            mPhotoAddTask = new AddPhotoTask(mAdapter);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    /**
     * 设置新的图标
     *
     * @param size     图片数量
     * @param listener
     */
    public void setData(int size, PhotoResourceListener listener) {
        this.mPhotoResourceListener = listener;
        mPhotoAddTask.setNewData(size);
    }

    /**
     * 添加新的图片
     *
     * @param data
     */
    public void addData(List<String> data) {
        int size = mAdapter.getData().size();
        List<String> list = mAdapter.getData();
        //首先将最后的添加按钮移除
        list.remove(size - 1);
        //添加新的数据
        list.addAll(data);
        //如果当前的张数没有小于最大张数就添加增加按钮
        if (list.size() < mInfo.maxCount) {
            list.add(AddPhotoManage.TAG);
        }
        mAdapter.notifyDataSetChanged();


    }

    /**
     * 获取已添加的图片
     *
     * @return
     */
    public List<String> getPhotoList() {
        return mAdapter.getData();
    }

    @Override
    public void photoResource(String text, int position, ImageView iv) {
        if (mPhotoEditActionListener != null) {
            mPhotoEditActionListener.loadImage(text, position, iv);
        } else if (mPhotoResourceListener != null) {
            String image = mPhotoResourceListener.loadImage(text, position, iv);
            if (mPhotoList == null) {
                mPhotoList = new ArrayList<>();
            }
            mPhotoList.add(position, image);
        }

    }

    /**
     * 获取最大可添加张数
     *
     * @return
     */
    public int getMaxCount() {
        int size = mAdapter.getData().size();
        return mInfo.maxCount - size + 1;
    }

    //delete
    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        //点击了删除 先移除点击的位置
        mAdapter.remove(position);
        int size = mAdapter.getData().size();
        if (size < mInfo.maxCount) {
            //最后一个不是添加按钮就显示添加按钮
            if (!mAdapter.getData().get(size - 1).equals(AddPhotoManage.TAG)) {
                mAdapter.addData(AddPhotoManage.TAG);
            }
        }
        if (mPhotoEditActionListener != null) {
            mPhotoEditActionListener.deleteImage(position);
        }
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        String image = mAdapter.getData().get(position);
        if (TextUtils.equals(image, AddPhotoManage.TAG)) {
            //添加图片
            if (mPhotoEditActionListener != null) {
                mPhotoEditActionListener.addImage(this, position);
            }
        } else {
            if (mPhotoEditActionListener != null) {
                List<String> list = new ArrayList<>(mAdapter.getData());
                if (list.size() < mInfo.maxCount) {
                    list.remove(list.size() - 1);
                }
                mPhotoEditActionListener.previewImage(position, list);
            } else if (mPhotoResourceListener != null) {

                mPhotoResourceListener.previewImage(position, mPhotoList);
            }
        }
    }

    public void setPhotoEditActionListener(PhotoEditActionListener listener) {
        this.mPhotoEditActionListener = listener;
    }

    public void remove() {
        mRecyclerView = null;
        mAdapter = null;
        mContext = null;
        mPhotoResourceListener = null;
        mPhotoResourceListener = null;
        if (mPhotoAddTask != null) {
            mPhotoAddTask.remove();
            mPhotoAddTask = null;
        }
    }

}
