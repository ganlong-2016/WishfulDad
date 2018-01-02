package com.drkj.wishfuldad.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.drkj.wishfuldad.BaseApplication;
import com.drkj.wishfuldad.R;
import com.drkj.wishfuldad.bean.DataBean;
import com.drkj.wishfuldad.db.DbController;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PeeGuestFragment extends Fragment {

    @BindView(R.id.text_pee_guest1)
    TextView peeGuest1;
    @BindView(R.id.text_pee_guest2)
    TextView peeGuest2;
    @BindView(R.id.text_pee_guest3)
    TextView peeGuest3;
    @BindView(R.id.text_baby_name)
    TextView babyName;
    @BindView(R.id.layout_pee_guest1)
    LinearLayout layout1;
    @BindView(R.id.layout_pee_guest2)
    LinearLayout layout2;
    @BindView(R.id.layout_pee_guest3)
    LinearLayout layout3;
    @BindView(R.id.layout_nodata)
    RelativeLayout nodaLayout;
    @BindView(R.id.layout_pee_guest)
    LinearLayout dataLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("PeeGuestFragment");

        babyName.setText(BaseApplication.getInstance().getBabyInfo().getName() + "，尿尿预测");
        long stamp = System.currentTimeMillis();
        Date date = new Date(stamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        List<Integer> times = new ArrayList<>();
        List<DataBean> beans = DbController.getInstance().queryData();
        for (int i = 0; i < 24; i++){
            int number =0;
            for (DataBean bean : beans) {
                if (bean.getType() == DataBean.TYPE_PEE && TextUtils.equals(bean.getDate(), simpleDateFormat.format(date))) {
                    if (bean.getHour()==i){
                        number++;
                    }
                }
            }
            if (number>3){
                times.add(i);
            }
        }
        if (times.size()==0){
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 1);
            Date day = calendar.getTime();
            String result = simpleDateFormat.format(day);
            for (int i = 0; i < 24; i++){
                int number =0;
                for (DataBean bean : beans) {
                    if (bean.getType() == DataBean.TYPE_PEE && TextUtils.equals(bean.getDate(), result)) {
                        if (bean.getHour()==i){
                            number++;
                        }
                    }
                }
                if (number>3){
                    times.add(i);
                }
            }
            if (times.size()>0){

            }
        }else{
            dataLayout.setVisibility(View.VISIBLE);
            nodaLayout.setVisibility(View.GONE);
            if (times.size()==1){
                layout1.setVisibility(View.VISIBLE);
                int a = times.get(0);
                peeGuest1.setText(a+":00~"+(a+1)+":00");
            }else if (times.size()==2){
                layout1.setVisibility(View.VISIBLE);
                int a = times.get(0);
                peeGuest1.setText(a+":00~"+(a+1)+":00");
                layout2.setVisibility(View.VISIBLE);
                int b = times.get(1);
                peeGuest2.setText(b+":00~"+(b+1)+":00");
            }else {
                layout1.setVisibility(View.VISIBLE);
                int a = times.get(0);
                peeGuest1.setText(a+":00~"+(a+1)+":00");
                layout2.setVisibility(View.VISIBLE);
                int b = times.get(1);
                peeGuest2.setText(b+":00~"+(b+1)+":00");
                layout3.setVisibility(View.VISIBLE);
                int c = times.get(2);
                peeGuest3.setText(c+":00~"+(c+1)+":00");
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("PeeGuestFragment");
    }
}
