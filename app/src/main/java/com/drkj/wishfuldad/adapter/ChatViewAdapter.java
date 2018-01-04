package com.drkj.wishfuldad.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.drkj.wishfuldad.BaseApplication;
import com.drkj.wishfuldad.R;
import com.drkj.wishfuldad.bean.MessageInfo;
import com.drkj.wishfuldad.util.Constants;
import com.drkj.wishfuldad.view.RoundImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ganlong on 2017/12/18.
 */

public class ChatViewAdapter extends BaseAdapter {

    private List<MessageInfo> list = new ArrayList<>();
    private LayoutInflater inflater;
    public ChatViewAdapter(Context context){
        inflater = LayoutInflater.from(context);
    }
    public ChatViewAdapter(Context context,List<MessageInfo> list) {
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public void refresh(List<MessageInfo> datas) {
        if (datas == null) {
            datas = new ArrayList<>(0);
        }
        this.list = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        MessageInfo messageInfo = list.get(position);
        return messageInfo.getType();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatHolder chatHolder = null;
        if (convertView==null){
            chatHolder = new ChatHolder();
            if (list.get(position).getType()== Constants.CHAT_ITEM_TYPE_ACCPET){
                convertView = inflater.inflate(R.layout.chat_receive_item,null);
            }else if(list.get(position).getType()== Constants.CHAT_ITEM_TYPE_SEND){
                convertView = inflater.inflate(R.layout.chat_send_item,null);
            }
            chatHolder.timeTextView = convertView.findViewById(R.id.chat_item_date);
            chatHolder.contentTextView = convertView.findViewById(R.id.chat_item_content_text);
            chatHolder.userImageView = convertView.findViewById(R.id.chat_item_header);
            convertView.setTag(chatHolder);
        }else {
            chatHolder = (ChatHolder) convertView.getTag();
        }
        if(list.get(position).getType()== Constants.CHAT_ITEM_TYPE_SEND){
            String imageUrlPath = BaseApplication.getInstance().getBabyInfo().getHeadImage();
            if (!TextUtils.isEmpty(imageUrlPath)) {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(imageUrlPath);
                    Bitmap bitmap = BitmapFactory.decodeStream(fis);
                    chatHolder.userImageView.setImageBitmap(bitmap);
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
//        chatHolder.timeTextView.setText(list.get(position).getTime());
        chatHolder.contentTextView.setText(list.get(position).getContent());
        return convertView;
    }
    private class ChatHolder{

        private TextView timeTextView;

        private RoundImageView userImageView;

        private TextView contentTextView;
    }
}
