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
import com.drkj.wishfuldad.bean.TibeiDataBean;
import com.drkj.wishfuldad.db.DbController;
import com.drkj.wishfuldad.view.DataLayout;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlanketTotalFragment extends Fragment {

    @BindView(R.id.datalayout)
    DataLayout mDataLayout;
    @BindView(R.id.text_baby_name)
    TextView babyName;

    @BindView(R.id.layout_nodata)
    RelativeLayout layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blanket_total, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("TibeiFragment");

        List<DataBean> dataBeans = DbController.getInstance().queryData();
        if (dataBeans.size() > 0) {

            long stamp = System.currentTimeMillis();
            Date date = new Date(stamp);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
            String day = simpleDateFormat.format(date);
            List<TibeiDataBean> datas = new ArrayList<>();
            for (int i = 0; i < 24; i++) {
                TibeiDataBean bean = new TibeiDataBean();
                bean.setTime(i);
                int number = 0;
                for (DataBean dataBean : dataBeans) {
                    if (dataBean.getType() == DataBean.TYPE_TIBEI && TextUtils.equals(day, dataBean.getDate())) {
                        if (dataBean.getHour() == i) {
                            number++;
                        }
                    }
                }
                bean.setNumber(number);
                if (number > 0)
                    datas.add(bean);
            }
            if (datas.size() > 0) {
                layout.setVisibility(View.GONE);
                mDataLayout.setVisibility(View.VISIBLE);
                mDataLayout.setData(datas);
            }
        }
        babyName.setText(BaseApplication.getInstance().getBabyInfo().getName() + "，今日踢被统计");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("TibeiFragment");
    }
}
