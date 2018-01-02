package com.drkj.wishfuldad.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleRssiCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.clj.fastble.utils.HexUtil;
import com.drkj.wishfuldad.BaseActivity;
import com.drkj.wishfuldad.BaseApplication;
import com.drkj.wishfuldad.R;
import com.drkj.wishfuldad.bean.DataBean;
import com.drkj.wishfuldad.db.DbController;
import com.drkj.wishfuldad.util.SampleGattAttributes;
import com.drkj.wishfuldad.util.SpUtil;
import com.umeng.analytics.MobclickAgent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class CardActivity extends BaseActivity {

    @BindView(R.id.image_data)
    ImageView mImageData;
    @BindView(R.id.image_self)
    ImageView mImageSelf;
    @BindView(R.id.image_bluetooth)
    ImageView mBluetooth;
    @BindView(R.id.image_card_head)
    ImageView mImageHead;
    @BindView(R.id.text_card_baby_name)
    TextView mBabyName;

    @BindView(R.id.text_temperature)
    TextView temperatureText;
    @BindView(R.id.progress_temperature)
    ProgressBar temperatureProgress;
    @BindView(R.id.text_humidity)
    TextView humidityText;
    @BindView(R.id.progress_humidity)
    ProgressBar humidityProgress;
    @BindView(R.id.text_distance)
    TextView distanceText;
    @BindView(R.id.text_pee_times)
    TextView peeTimes;
    @BindView(R.id.text_tibei_times)
    TextView tibeiTimes;
    @BindView(R.id.image_power)
    ImageView powerImage;
    private Timer timer;
    private TimerTask task;
    private boolean distance = false;
    private boolean tibei = false;

    private BleDevice mDevice;
    private SoundPool Cry0;
    private SoundPool Cry1;
    private SoundPool Cry2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        if (!SpUtil.hasShowedGuidePage(this, "functionGuide")) {
            apply();
            SpUtil.setHasShowedGuidePage(this, "functionGuide", true);
        }
        Cry0 = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
        Cry0.load(this, R.raw.cry0, 1);
        Cry1 = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
        Cry1.load(this, R.raw.cry1, 1);
        Cry2 = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
        Cry2.load(this, R.raw.cry2, 1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBabyName.setText(BaseApplication.getInstance().getBabyInfo().getName());
        String imageUrlPath = BaseApplication.getInstance().getBabyInfo().getHeadImage();
        if (!TextUtils.isEmpty(imageUrlPath)) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(imageUrlPath);
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                mImageHead.setImageBitmap(bitmap);
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

        if (mDevice != null && BleManager.getInstance().isConnected(mDevice)) {
            int a = BaseApplication.getInstance().getSettingInfo().getWeatherType();
            switch (a) {
                case 0:
                    write(mDevice, SampleGattAttributes.XXB_TQ0);
                    break;
                case 1:
                    write(mDevice, SampleGattAttributes.XXB_TQ2);
                    break;
                case 2:
                    write(mDevice, SampleGattAttributes.XXB_TQ1);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null)
            timer.cancel();
    }

    private void apply() {
        final FrameLayout rootLayout = (FrameLayout) findViewById(android.R.id.content);
        final View layoutView = View.inflate(this, R.layout.function_guide_page1, null);
        final View layoutView2 = View.inflate(this, R.layout.function_guide_page2, null);
        final View layoutView3 = View.inflate(this, R.layout.function_guide_page3, null);
        final View layoutView4 = View.inflate(this, R.layout.function_guide_page4, null);
        final View layoutView5 = View.inflate(this, R.layout.function_guide_page5, null);
        rootLayout.addView(layoutView);
        layoutView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        layoutView.findViewById(R.id.image_guide_page1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootLayout.removeView(layoutView);
                rootLayout.addView(layoutView2);
            }
        });
        layoutView2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        layoutView2.findViewById(R.id.image_guide_page1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootLayout.removeView(layoutView2);
                rootLayout.addView(layoutView3);
            }
        });
        layoutView3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        layoutView3.findViewById(R.id.image_guide_page1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootLayout.removeView(layoutView3);
                rootLayout.addView(layoutView4);
            }
        });
        layoutView4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        layoutView4.findViewById(R.id.image_guide_page1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootLayout.removeView(layoutView4);
                rootLayout.addView(layoutView5);
            }
        });
        layoutView5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        layoutView5.findViewById(R.id.image_guide_page1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootLayout.removeView(layoutView5);
            }
        });
    }

    @OnClick({R.id.layout_data, R.id.layout_home, R.id.layout_self})
    void choosePage(View view) {
        switch (view.getId()) {
            case R.id.layout_data:
                MobclickAgent.onEvent(this, "dataButton");
                mImageData.setBackground(getResources().getDrawable(R.drawable.ic_data_pressed));
                startActivity(new Intent(this, DataActivity.class));
                break;
            case R.id.layout_home:
                break;
            case R.id.layout_self:
                MobclickAgent.onEvent(this, "infoButton");
                mImageSelf.setBackground(getResources().getDrawable(R.drawable.ic_self_pressed));
                startActivity(new Intent(this, BabyInfoActivity.class));
                break;
            default:
                break;
        }
    }

    @OnCheckedChanged({R.id.switch_tibei, R.id.switch_liju})
    void checkedChange(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switch_tibei:
                MobclickAgent.onEvent(this, "tickSwitch");
                tibei = isChecked;
                if (isChecked)
                    showDialog();
                break;
            case R.id.switch_liju:
                MobclickAgent.onEvent(this, "distanceSwitch");
                distance = isChecked;
                break;
            default:
                break;
        }
    }

    ProgressDialog progressDialog;

    @OnClick(R.id.image_bluetooth)
    void openBluetooth() {
        MobclickAgent.onEvent(this, "bluetooth");
        if (!checkMission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            return;
        }
        if (mDevice != null && BleManager.getInstance().isConnected(mDevice)) {
            showDisConnectDialog(mDevice);
            return;
        }
        BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter != null) {
            if (!mBluetoothAdapter.isEnabled()) {
                showDialog2();
            } else {
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("蓝牙设备搜索中...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                BleManager.getInstance().init(getApplication());
                BleManager.getInstance()
                        .enableLog(true)
                        .setMaxConnectCount(7)
                        .setOperateTimeout(5000);
                BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                        .setScanTimeOut(10000)              // 扫描超时时间，可选，默认10秒；小于等于0表示不限制扫描时间
                        .build();
                BleManager.getInstance().initScanRule(scanRuleConfig);
                BleManager.getInstance().scan(new BleScanCallback() {
                    @Override
                    public void onScanStarted(boolean success) {

                    }

                    @Override
                    public void onScanning(BleDevice result) {

                    }

                    @Override
                    public void onScanFinished(List<BleDevice> scanResultList) {
                        progressDialog.dismiss();
                        List<BleDevice> devices = new ArrayList<>();
                        for (BleDevice device : scanResultList) {
                            if (!TextUtils.isEmpty(device.getName()) && device.getName().startsWith("XXB")) {
                                devices.add(device);
                            }
                        }
                        if (devices.size() > 0)
                            showDeviceDialog(devices);
                    }
                });
            }
        }

    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this, R.style.MyDialog);
        dialog.setContentView(R.layout.dialog_hint);
        TextView textOK = dialog.findViewById(R.id.text_cancel);
        textOK.setTextColor(getResources().getColor(R.color.blue));
        textOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        TextView message = dialog.findViewById(R.id.text_message);
        message.setText("请在宝宝起床后，注意关闭踢被开关\n这能使踢被预测数据更准确");
        dialog.show();
    }

    private void showDialog2() {
        final Dialog dialog = new Dialog(this, R.style.MyDialog);
        dialog.setContentView(R.layout.dialog_hint2);
        TextView textOK = dialog.findViewById(R.id.text_ok);
        textOK.setTextColor(getResources().getColor(R.color.blue));
        textOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                Intent enableBtIntent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                enableBtIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(enableBtIntent);
            }
        });
        TextView textCancel = dialog.findViewById(R.id.text_cancel);
        textCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private Dialog deviceDialog;

    private void showDeviceDialog(final List<BleDevice> devices) {
        deviceDialog = new Dialog(this);
        deviceDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        deviceDialog.setContentView(R.layout.dialog_show_device);
        deviceDialog.setCancelable(true);
        ListView view = deviceDialog.findViewById(R.id.list_device);
        view.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return devices.size();
            }

            @Override
            public Object getItem(int position) {
                return devices.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(CardActivity.this).inflate(R.layout.item_devices, null);
                }
                TextView textView = convertView.findViewById(R.id.text_device_name);
                textView.setText(devices.get(position).getName());
                return convertView;
            }
        });
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MobclickAgent.onEvent(CardActivity.this, "bluetoothChoose");
                progressDialog = new ProgressDialog(CardActivity.this);
                progressDialog.setMessage("设备连接中...");
                progressDialog.show();
                connect(devices.get(position));


            }
        });
        deviceDialog.show();
    }

    private int num = 0;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            BleDevice device = (BleDevice) msg.obj;
            write(device, SampleGattAttributes.XXB_SS);
            write(device, SampleGattAttributes.XXB_BT);
            if (distance) {
                BleManager.getInstance().readRssi(device, new BleRssiCallback() {
                    @Override
                    public void onRssiFailure(BleException exception) {

                    }

                    @Override
                    public void onRssiSuccess(int rssi) {

                        int iRssi = Math.abs(rssi);
                        double power = (iRssi - 59) / (10 * 2.0);
                        double d = Math.pow(10, power);
                        BigDecimal b = new BigDecimal(d);
                        double f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                        distanceText.setText(f1 + "");
                        if (iRssi >= 85) {
                            num++;
                        } else {
                            num = 0;
                        }
                        if (num >= 3) {
                            num = 0;
                            notice();
                            noticeDistance();
                        }
                    }
                });
            }

        }
    };

    private void connect(final BleDevice device) {
        if (!BleManager.getInstance().isConnected(device)) {
            BleManager.getInstance().connect(device, new BleGattCallback() {
                @Override
                public void onStartConnect() {

                }

                @Override
                public void onConnectFail(BleException exception) {
                    progressDialog.dismiss();
                    showToast("连接失败");
                }

                @Override
                public void onConnectSuccess(final BleDevice bleDevice, BluetoothGatt gatt, int status) {
                    mDevice = bleDevice;
                    progressDialog.dismiss();
                    deviceDialog.dismiss();
                    mBluetooth.setImageResource(R.drawable.ic_bluetooth_connect);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    startNotify(bleDevice);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    write(bleDevice, SampleGattAttributes.XXB_CH);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    write(bleDevice, SampleGattAttributes.XXB_XX_OK);
                    int a = BaseApplication.getInstance().getSettingInfo().getWeatherType();
                    switch (a) {
                        case 0:
                            write(mDevice, SampleGattAttributes.XXB_TQ0);
                            break;
                        case 1:
                            write(mDevice, SampleGattAttributes.XXB_TQ2);
                            break;
                        case 2:
                            write(mDevice, SampleGattAttributes.XXB_TQ1);
                            break;
                        default:
                            break;
                    }
                    timer = new Timer();
                    task = new TimerTask() {
                        @Override
                        public void run() {
                            Message message = Message.obtain();
                            message.obj = bleDevice;
                            message.what = 12345;
                            handler.sendMessage(message);
                        }
                    };
                    timer.schedule(task, 0, 5000);
                }

                @Override
                public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
                    mBluetooth.setImageResource(R.drawable.ic_bluetooth_unconnect);
                    timer.cancel();
                    clearData();
                }
            });
        }

    }

    private void clearData() {
        temperatureText.setText("0");
        temperatureProgress.setProgress(0);
        humidityText.setText("0");
        humidityProgress.setProgress(0);
        peeTimes.setText("0");
        tibeiTimes.setText("0");
        distanceText.setText("0");
    }

    void startNotify(BleDevice device) {
        if (device != null) {
            BleManager.getInstance().notify(device, SampleGattAttributes.XXB, SampleGattAttributes.XXB_NOTIFY
                    , new BleNotifyCallback() {
                        @Override
                        public void onNotifySuccess() {

                        }

                        @Override
                        public void onNotifyFailure(BleException exception) {

                        }

                        @Override
                        public void onCharacteristicChanged(byte[] data) {
                            handleData(data);
                        }
                    });
        }
    }

    private void write(BleDevice device, String data) {
        BleManager.getInstance().write(device, SampleGattAttributes.XXB, SampleGattAttributes.XXB_WRITE,
                HexUtil.hexStringToBytes(data), new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess() {

                    }

                    @Override
                    public void onWriteFailure(BleException exception) {

                    }
                });
    }

    private void handleData(byte[] data) {
        if (data.length == 8) {
            if (data[4] == 84 && data[5] == 66) {
                if (tibei) {
                    tibeiTimes.setText(data[7] + "");
                    notice();
                    noticeTibei();
                }
                long stamp = System.currentTimeMillis();
                DbController.getInstance().insertData(DataBean.TYPE_TIBEI, stamp);
            }
            if (data[4] == 78 && data[5] == 78) {
                peeTimes.setText(data[7] + "");
                long stamp = System.currentTimeMillis();
                DbController.getInstance().insertData(DataBean.TYPE_PEE, stamp);
                notice();
                noticePee();
            }
            if (data[4] == 66 && data[5] == 84) {
                if (data[7] > 0 && data[7] <= 25) {
                    powerImage.setImageResource(R.drawable.ic_power_1);
                } else if (data[7] <= 50) {
                    powerImage.setImageResource(R.drawable.ic_power_2);
                } else if (data[7] <= 75) {
                    powerImage.setImageResource(R.drawable.ic_power_3);
                } else if (data[7] <= 100) {
                    powerImage.setImageResource(R.drawable.ic_power_4);
                }
            }
        }
        if (data.length == 11) {
            if (data[5] == 84 && data[6] == 66) {
                long stamp = System.currentTimeMillis() - (data[9] * 256 + data[10]) * 1000;
                DbController.getInstance().insertData(DataBean.TYPE_PEE, stamp);
            }
            if (data[5] == 78 && data[6] == 78) {
                long stamp = System.currentTimeMillis() - (data[9] * 256 + data[10]) * 1000;
                DbController.getInstance().insertData(DataBean.TYPE_PEE, stamp);
            }
            if (data[4] == 83 && data[5] == 83) {
                int a = data[7];
                int b = data[8];
                int c = data[9];
                int d = data[10];
                double ab = a + b / 100.0;
                double cd = c + d / 100.0;
                temperatureText.setText(ab + "");
                temperatureProgress.setProgress(a);
                humidityText.setText(cd + "");
                humidityProgress.setProgress(c);
            }
        }
    }

    private void notice() {
        switch (BaseApplication.getInstance().getSettingInfo().getMusicType()) {
            case 1:
                Cry0.play(1, 1, 1, 0, 0, 1);
                break;
            case 2:

                Cry1.play(1, 1, 1, 0, 0, 1);
                break;
            case 3:

                Cry2.play(1, 1, 1, 0, 0, 1);
                break;
            default:
                break;
        }
        if (BaseApplication.getInstance().getSettingInfo().isVibrate() == 0) {
            Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
            vib.vibrate(3000);
        }

    }

    private void noticeTibei() {
        if (!isForeground(this, "CardActivity")) {
            notification(BaseApplication.getInstance().getBabyInfo().getName() + "踢被子了!请注意为宝宝盖好被子!");
        }
        final Dialog dialog = new Dialog(this, R.style.MyDialog);
        dialog.setContentView(R.layout.dialog_hint);
        TextView textMeassge = dialog.findViewById(R.id.text_message);
        textMeassge.setText(BaseApplication.getInstance().getBabyInfo().getName() + "踢被子了!请注意为宝宝盖好被子!");
        TextView textCancel = dialog.findViewById(R.id.text_cancel);
        textCancel.setText("好的");
        textCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        dialog.show();


    }

    private void noticePee() {
        if (!isForeground(this, "CardActivity")) {
            notification(BaseApplication.getInstance().getBabyInfo().getName() + "尿尿了!请注意更换纸尿裤!");
        }
        final Dialog dialog = new Dialog(this, R.style.MyDialog);
        dialog.setContentView(R.layout.dialog_hint2);
        TextView textOK = dialog.findViewById(R.id.text_ok);
        TextView textMeassge = dialog.findViewById(R.id.text_message);
        textMeassge.setText(BaseApplication.getInstance().getBabyInfo().getName() + "尿尿了!请注意更换纸尿裤");
        textOK.setTextColor(getResources().getColor(R.color.blue));
        textOK.setText("已更换");
        textOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                long stamp = System.currentTimeMillis();
                DbController.getInstance().insertData(DataBean.TYPE_DIAPERS, stamp);
                write(mDevice, SampleGattAttributes.XXB_XX_OK);

            }
        });
        TextView textCancel = dialog.findViewById(R.id.text_cancel);
        textCancel.setText("暂不更换");
        textCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        dialog.show();


    }

    private void noticeDistance() {
        if (!isForeground(this, "CardActivity")) {
            notification(BaseApplication.getInstance().getBabyInfo().getName() + "离开了安全距离,请注意宝宝动向!");
        }
        final Dialog dialog = new Dialog(this, R.style.MyDialog);
        dialog.setContentView(R.layout.dialog_hint);
        TextView textMeassge = dialog.findViewById(R.id.text_message);
        textMeassge.setText(BaseApplication.getInstance().getBabyInfo().getName() + "离开了安全距离,请注意宝宝动向!");
        TextView textCancel = dialog.findViewById(R.id.text_cancel);
        textCancel.setText("好的");
        textCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        dialog.show();


    }

    private void showDisConnectDialog(final BleDevice device) {
        final Dialog dialog = new Dialog(this, R.style.MyDialog);
        dialog.setContentView(R.layout.dialog_hint2);
        TextView textOK = dialog.findViewById(R.id.text_ok);
        TextView textMeassge = dialog.findViewById(R.id.text_message);
        textMeassge.setText("确定断开连接?");
        textOK.setTextColor(getResources().getColor(R.color.blue));
        textOK.setText("确定");
        textOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                BleManager.getInstance().disconnect(device);
            }
        });
        TextView textCancel = dialog.findViewById(R.id.text_cancel);
        textCancel.setText("取消");
        textCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    private void notification(String message) {
        //获取NotificationManager实例
        NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //获取PendingIntent
        Intent mainIntent = new Intent(this, CardActivity.class);
        PendingIntent mainPendingIntent = PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //创建 Notification.Builder 对象
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "abc")
                .setSmallIcon(R.drawable.ic_logo)
                //点击通知后自动清除
                .setAutoCancel(true)
                .setContentText(message)
                .setContentIntent(mainPendingIntent);
        //发送通知
        notifyManager.notify(3, builder.build());

    }
}
