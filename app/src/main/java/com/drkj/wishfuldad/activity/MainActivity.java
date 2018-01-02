package com.drkj.wishfuldad.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
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
import com.drkj.wishfuldad.R;
import com.drkj.wishfuldad.util.SampleGattAttributes;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class MainActivity extends BaseActivity {

    private BaseAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;

    private List<String> list = new ArrayList<>();
    @BindView(R.id.list_view)
    ListView listView;
    @BindView(R.id.text1)
    TextView textView;
    private List<BleDevice> devices2 = new ArrayList<>();

    private String TAG = "GANLONG";
    private BleDevice device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "不支持ble", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "不支持蓝牙", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        BleManager.getInstance().init(getApplication());

        BleManager.getInstance()
                .enableLog(true)
                .setMaxConnectCount(7)
                .setOperateTimeout(5000);

        UMConfigure.setLogEnabled(true);
        UMConfigure.setEncryptEnabled(true);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLeDeviceListAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(mLeDeviceListAdapter);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (device != null) {
            BleManager.getInstance().disconnect(device);
        }
        BleManager.getInstance().destroy();
    }

    private void addText(String text) {
        textView.setText(textView.getText().toString() + "\n" + text);
    }

    @OnClick(R.id.button)
    void startScanBle() {
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
//                .setServiceUuids(serviceUuids)      // 只扫描指定的服务的设备，可选
//                .setDeviceName(true, names)   		// 只扫描指定广播名的设备，可选
//                .setDeviceMac(mac)                  // 只扫描指定mac的设备，可选
//                .setAutoConnect(isAutoConnect)      // 连接时的autoConnect参数，可选，默认false
                .setScanTimeOut(10000)              // 扫描超时时间，可选，默认10秒；小于等于0表示不限制扫描时间
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {

            }

            @Override
            public void onScanning(final BleDevice result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(result.getDevice().getName())) {

                            list.add(result.getDevice().getName());
                            mLeDeviceListAdapter.notifyDataSetChanged();
                            devices2.add(result);
                        }
                    }
                });

            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {

            }
        });
    }

    @OnClick(R.id.button3)
    void startNotify() {
        if (device != null) {
            BleManager.getInstance().notify(device, SampleGattAttributes.XXB,SampleGattAttributes.XXB_NOTIFY
                    , new BleNotifyCallback() {
                        @Override
                        public void onNotifySuccess() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    addText("打开通知成功");
                                }
                            });
                        }

                        @Override
                        public void onNotifyFailure(BleException exception) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    addText("打开通知失败");
                                }
                            });
                        }

                        @Override
                        public void onCharacteristicChanged(final byte[] data) {
                            Log.i(TAG, "onCharacteristicChanged: 收到通知");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

//                                        String s = new String(data) + data[data.length - 1];
                                        StringBuilder builder = new StringBuilder();
                                        for (int i = 0; i < data.length; i++) {
                                            builder.append(data[i] + ",");
                                        }
//                                        byte[] bytes = BleManager.getInstance().getBluetoothGatt(device).getService(UUID.fromString(SampleGattAttributes.XXB))
//                                                .getCharacteristic(UUID.fromString(SampleGattAttributes.XXB_NOTIFY))
//                                                .getValue();

                                        addText(builder.toString());

                                }
                            });
                        }
                    });
        }
    }

    @OnItemClick(R.id.list_view)
    void connect(int postion) {
        if (!BleManager.getInstance().isConnected(devices2.get(postion))) {
            BleManager.getInstance().cancelScan();
            BleManager.getInstance().connect(devices2.get(postion), new BleGattCallback() {
                @Override
                public void onStartConnect() {

                }

                @Override
                public void onConnectFail(BleException exception) {

                }

                @Override
                public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                    device = bleDevice;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
//                    startNotify();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    BluetoothGattService ser = gatt.getService(UUID.fromString(SampleGattAttributes.XXB));
                    BluetoothGattCharacteristic write = ser.getCharacteristic(UUID.fromString(SampleGattAttributes.XXB_WRITE));
                    write.setValue("XXB-CH".getBytes());
                    gatt.writeCharacteristic(write);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addText("连接成功");
                        }
                    });
                }

                @Override
                public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
                    Toast.makeText(getApplicationContext(), "连接断开", Toast.LENGTH_SHORT).show();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addText("连接断开");
                        }
                    });
                }
            });
        }

    }


    @OnClick({R.id.button2, R.id.button5, R.id.button6, R.id.button7,R.id.button8,R.id.button9,R.id.button10})
    void write(View view) {

        if (device != null) {
            byte[] bytes = null;
            String data = null;
            switch (view.getId()) {
                case R.id.button2:
                   data = "5858422d54512343";
                    break;
                case R.id.button5:
                    data= "5858422d54512301";
                    break;
                case R.id.button6:
                    data = "5858422d54512302";
                    break;
                case R.id.button7:
                    data = "5858422d5858234f4b";
                    break;
                case R.id.button8:
                    data = "5858422d54512300";
                    break;
                case R.id.button9:
                    data = "5858422d535323";
                    break;
                case R.id.button10:
                    data = "5858422d425423";
                    break;
            }
            BleManager.getInstance().write(device, SampleGattAttributes.XXB, SampleGattAttributes.XXB_WRITE,
                    HexUtil.hexStringToBytes(data), new BleWriteCallback() {
                        @Override
                        public void onWriteSuccess() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    addText("写入成功");
                                }
                            });
                        }

                        @Override
                        public void onWriteFailure(BleException exception) {

                        }
                    });
        }
    }

    @OnClick(R.id.button4)
    void getRssi() {
        if (device != null) {
            BleManager.getInstance().readRssi(device, new BleRssiCallback() {
                @Override
                public void onRssiFailure(BleException exception) {

                }

                @Override
                public void onRssiSuccess(final int rssi) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addText("信号强度" + rssi);
                        }
                    });
                }
            });
        }
    }
}
