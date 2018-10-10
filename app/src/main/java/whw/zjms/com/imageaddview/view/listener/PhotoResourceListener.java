package whw.zjms.com.imageaddview.view.listener;

import android.widget.ImageView;

import java.util.List;


public interface PhotoResourceListener {
    String loadImage(String image, int position, ImageView iv);

    void previewImage(int position, List<String> image);
}
