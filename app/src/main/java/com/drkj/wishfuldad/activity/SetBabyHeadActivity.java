package com.drkj.wishfuldad.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.drkj.wishfuldad.BaseActivity;
import com.drkj.wishfuldad.BaseApplication;
import com.drkj.wishfuldad.R;
import com.drkj.wishfuldad.db.DbController;
import com.drkj.wishfuldad.util.FileUtil;
import com.drkj.wishfuldad.util.SpUtil;
import com.makeramen.roundedimageview.RoundedImageView;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SetBabyHeadActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.text_baby_name)
    TextView mBabyName;
    @BindView(R.id.image_baby_head)
    RoundedImageView mBabyHead;

    private Dialog mCameraDialog;
    private Uri imageUri;
    private String imageUrlPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_baby_head);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBabyName.setText(BaseApplication.getInstance().getBabyInfo().getName());
    }

    @OnClick(R.id.image_back)
    void back() {
        finish();
    }

    @OnClick(R.id.image_baby_head)
    void addHead() {
        MobclickAgent.onEvent(this, "babyAvatar");
        if (checkMission())
            showCameraControlDialog();
    }
    @OnClick(R.id.btn_complete)
    void complete(){

        MobclickAgent.onEvent(this, "completeSetBabyHead");
        if (TextUtils.isEmpty(imageUrlPath)){
            showToast("请设置宝宝头像");
            return;
        }
        SpUtil.putString(this,"token",BaseApplication.getInstance().getToken());
        BaseApplication.getInstance().getBabyInfo().setHeadImage(imageUrlPath);
        userUpdate();
        childUpdate();
        userIcon();
        DbController.getInstance().updateBabyInfoData(BaseApplication.getInstance().getBabyInfo());
        DbController.getInstance().updateYourInfoData(BaseApplication.getInstance().getYourInfo());
        finishAllActivity();
        startActivity(new Intent(this,CardActivity.class));
    }

    public boolean checkMission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        requestPermissions(new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, 555);
        return false;
    }


    private void showCameraControlDialog() {
        mCameraDialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.dialog_camera_control, null);
        root.findViewById(R.id.btn_take_photo).setOnClickListener(this);
        root.findViewById(R.id.btn_pick_photo).setOnClickListener(this);
        root.findViewById(R.id.btn_cancel).setOnClickListener(this);
        mCameraDialog.setContentView(root);
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        dialogWindow.setAttributes(lp);
        mCameraDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_take_photo:
                takePhoto();
                break;
            case R.id.btn_pick_photo:
                pickPhoto();
                break;
            case R.id.btn_cancel:
                cancel();
                break;
            default:
                break;
        }
    }

    void takePhoto() {
        File file = new File(Environment.getExternalStorageDirectory(), "tempImage.jpg");
        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            imageUri = Uri.fromFile(file);
        } else {
            imageUri = FileProvider.getUriForFile(this, this.getPackageName() + ".fileProvider", file);
        }
        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(takeIntent, 200);
    }

    void pickPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 300);
    }

    void cancel() {
        if (mCameraDialog != null) {
            mCameraDialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 200:
                    try {
                        startPhotoZoom(imageUri);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    break;
                case 300:
                    String filePath = FileUtil.parsePicturePath(this, data.getData());
                    File file = new File(filePath);
                    Uri uri;
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        uri = Uri.fromFile(file);
                    } else {
                        uri = FileUtil.getImageContentUri(this, file);
                    }
                    startPhotoZoom(uri);
                    break;
                case 400:
                    // 显示结果
                    Bundle extras = data.getExtras();
                    Bitmap photo;

                    if (extras != null) {
                        photo = extras.getParcelable("data");
                    } else {
                        String path = FileUtil.parsePicturePath(this, data.getData());
                        photo = BitmapFactory.decodeFile(path);
                    }
                    imageUrlPath = FileUtil.saveFile(this, "temphead.jpg", photo);
                    if (mCameraDialog != null) {
                        mCameraDialog.dismiss();
                    }
                    mBabyHead.setScaleType(ImageView.ScaleType.CENTER);
                    mBabyHead.setImageBitmap(photo);
//                    listener.settingComplete();
                default:
                    break;
            }

        }
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 400);
    }


}
