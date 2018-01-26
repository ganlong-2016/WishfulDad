package com.drkj.wishfuldad.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.drkj.wishfuldad.BaseApplication;
import com.drkj.wishfuldad.R;
import com.drkj.wishfuldad.bean.DataBean;
import com.drkj.wishfuldad.bean.PeeDataBean;
import com.drkj.wishfuldad.db.DbController;
import com.drkj.wishfuldad.view.PeeRecordLayout;
import com.drkj.wishfuldad.view.PeeRecordView;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PeeRecordFragment extends Fragment {

    @BindView(R.id.datalayout)
    PeeRecordLayout mlayout;
    @BindView(R.id.text_baby_name)
    TextView babyName;

    @BindView(R.id.layout_nodata)
    RelativeLayout layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pee_record, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("PeeRecordFragment");

        babyName.setText(BaseApplication.getInstance().getBabyInfo().getName() + "，最近7天尿点记录");
//        List<DataBean> dataBeans = DbController.getInstance().queryData();
        List<DataBean> dataBeans = loadData();
        if (dataBeans.size() > 0) {

            List<String> days = new ArrayList<>();
            SimpleDateFormat format = new SimpleDateFormat("MM-dd");
            for (int i = 6; i >= 0; i--) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - i);
                Date day = calendar.getTime();
                String result = format.format(day);
                days.add(result);
            }

            List<DataBean> beans = new ArrayList<>();
            for (DataBean dataBean : dataBeans) {
                if (dataBean.getType() == DataBean.TYPE_PEE) {
                    for (String day : days) {
                        if (TextUtils.equals(day, dataBean.getDate())) {
                            beans.add(dataBean);
                        }
                    }
                }
            }
            if (beans.size() > 0) {
                layout.setVisibility(View.GONE);
                mlayout.setVisibility(View.VISIBLE);
                mlayout.setDays(days);
                mlayout.setData(beans);
            }
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageStart("PeeRecordFragment");
    }

    private List<DataBean> loadData(){
        List<DataBean> dataBeans = new ArrayList<>();

        DataBean dataBean = new DataBean();
        dataBean.setType(DataBean.TYPE_PEE);
        dataBean.setDate("01-22");
        dataBean.setTime("03:18");
        dataBeans.add(dataBean);

        DataBean dataBean1 = new DataBean();
        dataBean1.setType(DataBean.TYPE_PEE);
        dataBean1.setDate("01-26");
        dataBean1.setTime("23:18");
        dataBeans.add(dataBean1);

        DataBean dataBean2 = new DataBean();
        dataBean2.setType(DataBean.TYPE_PEE);
        dataBean2.setDate("01-24");
        dataBean2.setTime("13:18");
        dataBeans.add(dataBean2);

        DataBean dataBean3 = new DataBean();
        dataBean3.setType(DataBean.TYPE_PEE);
        dataBean3.setDate("01-23");
        dataBean3.setTime("18:18");
        dataBeans.add(dataBean3);
        return dataBeans;
    }
}
