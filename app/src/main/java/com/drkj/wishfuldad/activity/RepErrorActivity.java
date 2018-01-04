package com.drkj.wishfuldad.activity;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ListView;

import com.drkj.wishfuldad.BaseActivity;
import com.drkj.wishfuldad.BaseApplication;
import com.drkj.wishfuldad.R;
import com.drkj.wishfuldad.adapter.ChatViewAdapter;
import com.drkj.wishfuldad.bean.ChatResultBean;
import com.drkj.wishfuldad.bean.LoginResultBean;
import com.drkj.wishfuldad.bean.MessageInfo;
import com.drkj.wishfuldad.db.DbController;
import com.drkj.wishfuldad.net.ServerNetClient;
import com.drkj.wishfuldad.util.Constants;
import com.drkj.wishfuldad.util.SpUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RepErrorActivity extends BaseActivity {

    @BindView(R.id.list_chat)
    ListView chatList;
    @BindView(R.id.et_message)
    EditText message;
    private ChatViewAdapter chatAdapter;
    private List<MessageInfo> messageInfos = new ArrayList<>();

    private Timer timer;
    private TimerTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rep_error);

        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                ServerNetClient.getInstance().getApi()
                        .getMsgList(SpUtil.getToken(RepErrorActivity.this, "token"), 1, SpUtil.getInt(RepErrorActivity.this, "mid"))
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ChatResultBean>() {
                            @Override
                            public void accept(ChatResultBean chatResultBean) throws Exception {
                                for (ChatResultBean.DataBean dataBean : chatResultBean.getData()) {
                                    MessageInfo messageInfo = new MessageInfo();
                                    messageInfo.setContent(dataBean.getContent());
                                    messageInfo.setType(Constants.CHAT_ITEM_TYPE_ACCPET);
                                    if (!TextUtils.isEmpty(dataBean.getContent())&&dataBean.getFromid()==1) {
                                        messageInfos.add(messageInfo);
                                        DbController.getInstance().insertChatData(messageInfo);
                                    }
                                    SpUtil.putInt(RepErrorActivity.this, "mid", dataBean.getId());
                                }
                                chatAdapter.notifyDataSetChanged();
                                chatList.setSelection(chatAdapter.getCount() - 1);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                throwable.printStackTrace();
                            }
                        });
            }
        };
        timer.schedule(task, 0, 5000);

    }

    @OnClick(R.id.back_imageview)
    void back() {
        finish();
    }

    @OnClick(R.id.btn_send_message)
    void sendMessage() {
        if (!TextUtils.isEmpty(message.getText().toString())) {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setType(Constants.CHAT_ITEM_TYPE_SEND);
            messageInfo.setContent(message.getText().toString());
            messageInfos.add(messageInfo);
            chatAdapter.refresh(messageInfos);
            chatAdapter.notifyDataSetChanged();
            DbController.getInstance().insertChatData(messageInfo);
            chatList.setSelection(chatAdapter.getCount() - 1);

            ServerNetClient.getInstance().getApi()
                    .sendMsg(SpUtil.getToken(RepErrorActivity.this, "token"), message.getText().toString())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ChatResultBean>() {
                        @Override
                        public void accept(ChatResultBean chatResultBean) throws Exception {
                            for (ChatResultBean.DataBean dataBean : chatResultBean.getData()) {
                                SpUtil.putInt(RepErrorActivity.this, "mid", dataBean.getId());
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            throwable.printStackTrace();
                        }
                    });

            message.setText("");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initWidget();
        LoadData();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null)
            timer.cancel();
    }

    private void initWidget() {
        chatAdapter = new ChatViewAdapter(this, messageInfos);
        chatList.setAdapter(chatAdapter);
    }


    /**
     * 构造聊天数据
     */
    private void LoadData() {
        List<MessageInfo> list = DbController.getInstance().queryChatData();
        messageInfos.addAll(list);
        chatList.setSelection(chatAdapter.getCount() - 1);
//        MessageInfo messageInfo = new MessageInfo();
//        messageInfo.setContent("你好，请问有什么可以帮你？");
//        messageInfo.setType(Constants.CHAT_ITEM_TYPE_ACCPET);
////        messageInfo.setHeader("http://tupian.enterdesk.com/2014/mxy/11/2/1/12.jpg");
//        messageInfos.add(messageInfo);
//
//        MessageInfo messageInfo1 = new MessageInfo();
//        messageInfo1.setFilepath("http://www.trueme.net/bb_midi/welcome.wav");
//        messageInfo1.setVoiceTime(3000);
//        messageInfo1.setContent("你好，请问有什么可以帮你？");
//        messageInfo1.setType(Constants.CHAT_ITEM_TYPE_SEND);
////        messageInfo1.setSendState(Constants.CHAT_ITEM_SEND_SUCCESS);
//        messageInfo1.setHeader("http://img.dongqiudi.com/uploads/avatar/2014/10/20/8MCTb0WBFG_thumb_1413805282863.jpg");
//        messageInfos.add(messageInfo1);
//
//        MessageInfo messageInfo2 = new MessageInfo();
//        messageInfo2.setImageUrl("http://img4.imgtn.bdimg.com/it/u=1800788429,176707229&fm=21&gp=0.jpg");
//        messageInfo2.setContent("你好，请问有什么可以帮你？");
//        messageInfo2.setType(Constants.CHAT_ITEM_TYPE_ACCPET);
//        messageInfo2.setHeader("http://tupian.enterdesk.com/2014/mxy/11/2/1/12.jpg");
//        messageInfos.add(messageInfo2);
//
//        MessageInfo messageInfo3 = new MessageInfo();
//        messageInfo3.setContent("你好，请问有什么可以帮你？你好，请问有什么可以帮你？你好，请问有什么可以帮你？你好，请问有什么可以帮你？你好，请问有什么可以帮你？你好，请问有什么可以帮你？你好，请问有什么可以帮你？你好，请问有什么可以帮你？你好，请问有什么可以帮你？你好，请问有什么可以帮你？你好，请问有什么可以帮你？");
//        messageInfo3.setType(Constants.CHAT_ITEM_TYPE_SEND);
//        messageInfos.add(messageInfo3);
//        messageInfos.add(messageInfo3);
//        messageInfos.add(messageInfo3);
//        messageInfos.add(messageInfo3);
//        messageInfos.add(messageInfo3);
//        messageInfos.add(messageInfo3);
//        messageInfos.add(messageInfo3);
//        messageInfos.add(messageInfo3);
//        messageInfos.add(messageInfo3);
//
//
//        chatAdapter.notifyDataSetChanged();
//        chatAdapter.addAll(messageInfos);
    }

}
