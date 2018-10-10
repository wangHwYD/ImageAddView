package whw.zjms.com.imageaddview.view.listener;

import android.widget.ImageView;

import java.util.List;

import whw.zjms.com.imageaddview.view.core.AddPhotoManage;


public interface PhotoEditActionListener {
    void deleteImage(int position);

    void previewImage(int position, List<String> image);

    void addImage(AddPhotoManage manage, int position);

    void loadImage(String image, int position, ImageView iv);

}
