package whw.zjms.com.imageaddview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.quick.PermissionUtils;
import com.yanzhenjie.permission.quick.listener.PermissionListener;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.listener.OnCheckedListener;
import com.zhihu.matisse.listener.OnSelectedListener;

import java.util.ArrayList;
import java.util.List;

import whw.zjms.com.imageaddview.view.AddPhotoView;
import whw.zjms.com.imageaddview.view.core.AddPhotoManage;
import whw.zjms.com.imageaddview.view.core.GifSizeFilter;
import whw.zjms.com.imageaddview.view.core.Glide4Engine;
import whw.zjms.com.imageaddview.view.listener.PhotoEditActionListener;
import whw.zjms.com.imageaddview.view.pre.PreViewActivity;

public class MainActivity extends AppCompatActivity implements PhotoEditActionListener, PermissionListener {
    AddPhotoManage mAddPhotoManage;
    private final int REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AddPhotoView mAddView = findViewById(R.id.iv_add);

        mAddPhotoManage = mAddView.getAddPhotoManage();
        mAddPhotoManage.setPhotoEditActionListener(this);
    }

    @Override
    public void deleteImage(int position) {

    }

    @Override
    public void previewImage(int position, List<String> image) {
        Intent intent = new Intent(this, PreViewActivity.class);
        intent.putStringArrayListExtra(PreViewActivity.IMAGE, (ArrayList<String>) image);
        intent.putExtra(PreViewActivity.POSITION, position);
        startActivity(intent);
    }

    @Override
    public void addImage(AddPhotoManage manage, int position) {

            PermissionUtils.with(this).permission(Permission.CAMERA, Permission
                    .READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE).listener(this).start();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data!=null) {
                List<String> list = Matisse.obtainPathResult(data);
                mAddPhotoManage.addData(list);
            }

        }

    }
    @SuppressLint("CheckResult")
    @Override
    public void loadImage(String image, int position, ImageView iv) {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.sample_footer_loading).error(R.drawable.sample_footer_loading);
        Glide.with(this).load(image).apply(options).into(iv);
    }

    @Override
    public void onGranted(int requestCode) {
        Matisse.from(this)
                .choose(MimeType.ofAll(), false)
                .countable(true)
                .capture(true)
                .captureStrategy(
                        new CaptureStrategy(true, "whw.zjms.com.imageaddview.fileprovider"))
                .maxSelectable(9)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(
                        getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
//                                            .imageEngine(new GlideEngine())  // for glide-V3
                .imageEngine(new Glide4Engine())    // for glide-V4
                .setOnSelectedListener(new OnSelectedListener() {
                    @Override
                    public void onSelected(
                            @NonNull List<Uri> uriList, @NonNull List<String> pathList) {
                        // DO SOMETHING IMMEDIATELY HERE
                        Log.e("onSelected", "onSelected: pathList=" + pathList);

                    }
                })
                .originalEnable(true)
                .maxOriginalSize(10)
                .setOnCheckedListener(new OnCheckedListener() {
                    @Override
                    public void onCheck(boolean isChecked) {
                        // DO SOMETHING IMMEDIATELY HERE
                        Log.e("isChecked", "onCheck: isChecked=" + isChecked);
                    }
                })
                .forResult(REQUEST_CODE);
    }

    @Override
    public void onDenied(int requestCode) {

    }
}
