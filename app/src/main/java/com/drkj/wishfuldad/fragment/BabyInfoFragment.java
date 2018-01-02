package com.drkj.wishfuldad.fragment;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.drkj.wishfuldad.BaseActivity;
import com.drkj.wishfuldad.BaseApplication;
import com.drkj.wishfuldad.R;
import com.drkj.wishfuldad.activity.SetBabyDataActivity;
import com.drkj.wishfuldad.listener.SettingBabyInfoListener;
import com.drkj.wishfuldad.util.FileUtil;
import com.drkj.wishfuldad.view.RoundImageView;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class BabyInfoFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.baby_head_image_view1)
    RoundImageView headImageView;

    @BindView(R.id.text_birth_day)
    TextView birthdayTextView;

    private BaseActivity activity;

    @BindView(R.id.image_set_baby_info)
    ImageView setBabyInfoImageView;

    @BindView(R.id.edit_baby_info_name)
    EditText babyName;
    @BindView(R.id.text_blood)
    TextView bloodText;
    @BindView(R.id.text_sex)
    TextView sexText;
    @BindView(R.id.text_height)
    TextView height;
    @BindView(R.id.text_weight)
    TextView weight;

    //获取日期格式器对象
    java.text.SimpleDateFormat fmtDate = new java.text.SimpleDateFormat("yyyy-MM-dd");
    Dialog mCameraDialog;
    private Uri imageUri;
    private String imageUrlPath;
    SettingBabyInfoListener listener;

    public static final String[] blood = new String[]{"A", "B", "AB", "O"};
    public static final String[] sex = new String[]{"男", "女"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_babt_info, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.image_set_baby_info)
    void setBabyInfo() {
        MobclickAgent.onEvent(getContext(), "babyinfoset");

        startActivity(new Intent(activity, SetBabyDataActivity.class));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (BaseActivity) getActivity();
        if (BaseApplication.getInstance().getBabyInfo().getBirthDayYear() != 0) {
            //获取一个日历对象
            Calendar dateAndTime = Calendar.getInstance();
            dateAndTime.set(Calendar.YEAR, BaseApplication.getInstance().getBabyInfo().getBirthDayYear());
            dateAndTime.set(Calendar.MONTH, BaseApplication.getInstance().getBabyInfo().getBirthDayMonth());
            dateAndTime.set(Calendar.DAY_OF_MONTH, BaseApplication.getInstance().getBabyInfo().getBirthDayDay());
            birthdayTextView.setText(fmtDate.format(dateAndTime.getTime()));
        }
        babyName.setText(BaseApplication.getInstance().getBabyInfo().getName());
        String imageUrlPath = BaseApplication.getInstance().getBabyInfo().getHeadImage();
        if (!TextUtils.isEmpty(imageUrlPath)) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(imageUrlPath);
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                headImageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("BabyInfoFragment");

        int b = BaseApplication.getInstance().getBabyInfo().getBloodType();
        if (b != 4)
            bloodText.setText("血型:"+blood[b]);
        int s = BaseApplication.getInstance().getBabyInfo().getSex();
        if (s != 2)
            sexText.setText("性别:"+sex[s]);
        float h = BaseApplication.getInstance().getBabyInfo().getHeight();
        if (h>0){
            height.setText("身高:"+h+"cm");
        }
        float w = BaseApplication.getInstance().getBabyInfo().getWeight();
        if (w>0){
            weight.setText("体重:"+w+"Kg");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("BabyInfoFragment");
    }

    @OnClick(R.id.text_birth_day)
    void setBirthDay() {

        MobclickAgent.onEvent(getContext(), "babybirthday");
        //获取一个日历对象

        Calendar dateAndTime = Calendar.getInstance();
        DatePickerDialog dateDlg = new DatePickerDialog(activity,
                dateSetListener,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH)){
            @Override
            public void onDateChanged(@NonNull DatePicker view, int year, int month, int dayOfMonth) {
//                if (!isEighteen(year,month,dayOfMonth)){
                    super.onDateChanged(view, year, month, dayOfMonth);
//                }
            }
        };

        dateDlg.show();
    }
//    private boolean isEighteen(int year, int month, int dayOfMonth){
//        Calendar calendar = Calendar.getInstance();
//        int y = calendar.get(Calendar.YEAR);
//        int m = calendar.get(Calendar.MONTH);
//        int d = calendar.get(Calendar.DAY_OF_MONTH);
//        if (year)
//        return false;
//    }
    @OnClick(R.id.baby_head_image_view1)
    void addHead() {
        if (activity.checkMission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            showCameraControlDialog();
        }
    }


    private void showCameraControlDialog() {
        mCameraDialog = new Dialog(activity, R.style.ActionSheetDialogStyle);
        LinearLayout root = (LinearLayout) LayoutInflater.from(activity).inflate(
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

    //当点击DatePickerDialog控件的设置按钮时，调用该方法
    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {


        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            //修改日历控件的年，月，日
            //这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
            //获取一个日历对象
            Calendar dateAndTime = Calendar.getInstance();
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            BaseApplication.getInstance().getBabyInfo().setBirthDayYear(year);
            BaseApplication.getInstance().getBabyInfo().setBirthDayMonth(monthOfYear);
            BaseApplication.getInstance().getBabyInfo().setBirthDayDay(dayOfMonth);
            //将页面TextView的显示更新为最新时间
            birthdayTextView.setText(fmtDate.format(dateAndTime.getTime()));
        }
    };

    @OnTextChanged(R.id.edit_baby_info_name)
    void setName(CharSequence s, int start, int before, int count) {
        if (!TextUtils.isEmpty(s)) {
            if (listener != null) {
                listener.setName(s.toString());
            }
            BaseApplication.getInstance().getBabyInfo().setName(s.toString());
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_take_photo:
                File file = new File(Environment.getExternalStorageDirectory(), "tempImage.jpg");
                Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    imageUri = Uri.fromFile(file);
                } else {
                    imageUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileProvider", file);
                }
                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takeIntent, 200);
                break;
            case R.id.btn_pick_photo:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 300);
                break;
            case R.id.btn_cancel:
                if (mCameraDialog != null) {
                    mCameraDialog.dismiss();
                }
                break;
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    String filePath = FileUtil.parsePicturePath(activity, data.getData());
                    File file = new File(filePath);
                    Uri uri;
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        uri = Uri.fromFile(file);
                    } else {
                        uri = FileUtil.getImageContentUri(activity, file);
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
                        String path = FileUtil.parsePicturePath(activity, data.getData());
                        photo = BitmapFactory.decodeFile(path);
                    }
                    imageUrlPath = FileUtil.saveFile(activity, "temphead.jpg", photo);
                    BaseApplication.getInstance().getBabyInfo().setHeadImage(imageUrlPath);
                    if (mCameraDialog != null) {
                        mCameraDialog.dismiss();
                    }
                    headImageView.setImageBitmap(photo);
                    listener.settingComplete(imageUrlPath);
                    break;
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

    public void addSettingBabyInfoListener(SettingBabyInfoListener listener) {
        this.listener = listener;
    }

}
