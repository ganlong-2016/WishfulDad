package com.drkj.wishfuldad;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.drkj.wishfuldad.bean.LoginResultBean;
import com.drkj.wishfuldad.net.ServerNetClient;
import com.drkj.wishfuldad.util.SpUtil;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by ganlong on 2017/12/13.
 */

public class BaseActivity extends AppCompatActivity {
    private static Stack<Activity> activityStack;
    private static final String TAG = "test_ganlong";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);  //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION); //透明导航栏
        }
        if (activityStack == null) {
            activityStack = new Stack();
        }
        activityStack.add(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    protected void showToast(String toast) {
        Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
    }

    public boolean checkMission(String permisson) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(permisson) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        requestPermissions(new String[]{permisson}, 555);
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityStack.remove(this);
    }

    public Activity currentActivity() {
        Activity activity = (Activity) activityStack.lastElement();
        return activity;
    }

    public void finishActivity() {
        Activity activity = (Activity) activityStack.lastElement();
    }

    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    public void finishActivity(Class<?> cls) {
        Iterator var1 = activityStack.iterator();

        while (var1.hasNext()) {
            Activity activity = (Activity) var1.next();
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }

    public void finishAllActivity() {
        int size = activityStack.size();

        for (int i = 0; i < size; ++i) {
            if (null != activityStack.get(i)) {
                ((Activity) activityStack.get(i)).finish();
            }
        }
        activityStack.clear();
    }

    public boolean isApplicationBroughtToBackground() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }
        return false;
    }

    protected void userUpdate(){
        ServerNetClient.getInstance().getApi().userUpdate(
                SpUtil.getToken(this,"token"),
                BaseApplication.getInstance().getYourInfo().getName(),
                BaseApplication.getInstance().getYourInfo().getAge(),
                BaseApplication.getInstance().getYourInfo().getRole()
                ).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginResultBean>() {
                    @Override
                    public void accept(LoginResultBean loginResultBean) throws Exception {
                        Log.i(TAG, "accept: success");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i(TAG, "accept: "+throwable.getMessage()+"-----"+throwable.getCause());
                    }
                });
    }
    protected void childUpdate(){
        ServerNetClient.getInstance().getApi().childUpdate(
                SpUtil.getToken(this,"token"),
                BaseApplication.getInstance().getBabyInfo().getName(),
                BaseApplication.getInstance().getBabyInfo().getAge(),
                BaseApplication.getInstance().getBabyInfo().getSex(),
                BaseApplication.getInstance().getBabyInfo().getWeight(),
                (int)BaseApplication.getInstance().getBabyInfo().getHeight(),
                BaseApplication.getInstance().getBabyInfo().getBloodType()
        ).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginResultBean>() {
                    @Override
                    public void accept(LoginResultBean loginResultBean) throws Exception {
                        Log.i(TAG, "accept: success");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i(TAG, "accept: "+throwable.getMessage()+"-----"+throwable.getCause());
                    }
                });
    }
    protected void userIcon(){
        File file = new File(BaseApplication.getInstance().getBabyInfo().getHeadImage());
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)//表单类型
                .addFormDataPart("token",  SpUtil.getToken(this,"token"));
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart("photo", file.getName(), imageBody);
        List<MultipartBody.Part> parts = builder.build().parts();
        ServerNetClient.getInstance().getApi().userIcon(
               parts
        ).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginResultBean>() {
                    @Override
                    public void accept(LoginResultBean loginResultBean) throws Exception {
                        Log.i(TAG, "accept: success");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i(TAG, "accept: "+throwable.getMessage()+"-----"+throwable.getCause());
                    }
                });
    }

}
