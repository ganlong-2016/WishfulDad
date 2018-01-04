package com.drkj.wishfuldad.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.drkj.wishfuldad.R;
import com.drkj.wishfuldad.bean.DataBean;
import com.drkj.wishfuldad.db.DbController;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PeeTotalFragment extends Fragment {
    @BindView(R.id.layout_pee_total)
    LinearLayout dataLayout;
    @BindView(R.id.layout_nodata)
    RelativeLayout nodataLayout;

    @BindView(R.id.image_today_pee1)
    ImageView todayPee1;
    @BindView(R.id.image_today_pee2)
    ImageView todayPee2;
    @BindView(R.id.image_today_pee3)
    ImageView todayPee3;
    @BindView(R.id.image_today_pee4)
    ImageView todayPee4;
    @BindView(R.id.image_today_pee5)
    ImageView todayPee5;
    @BindView(R.id.image_today_pee6)
    ImageView todayPee6;
    @BindView(R.id.layout_today_pee_more)
    LinearLayout todayPeeMore;
    @BindView(R.id.image_this_week_pee1)
    ImageView thisWeekPee1;
    @BindView(R.id.image_this_week_pee2)
    ImageView thisWeekPee2;
    @BindView(R.id.image_this_week_pee3)
    ImageView thisWeekPee3;
    @BindView(R.id.image_this_week_pee4)
    ImageView thisWeekPee4;
    @BindView(R.id.image_this_week_pee5)
    ImageView thisWeekPee5;
    @BindView(R.id.image_this_week_pee6)
    ImageView thisWeekPee6;
    @BindView(R.id.layout_this_week_pee)
    LinearLayout thisWeekPeeMore;
    @BindView(R.id.text_today_pee)
    TextView todayPeeText;
    @BindView(R.id.text_total_pee)
    TextView thisweekPeeText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pee_total, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("PeeTotalFragment");

        List<DataBean> dataBeans = DbController.getInstance().queryData();
        List<String> days = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd", Locale.CHINA);
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        String today = simpleDateFormat.format(calendar.getTime());

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        days.add(simpleDateFormat.format(calendar.getTime()));
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        days.add(simpleDateFormat.format(calendar.getTime()));
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        days.add(simpleDateFormat.format(calendar.getTime()));
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        days.add(simpleDateFormat.format(calendar.getTime()));
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        days.add(simpleDateFormat.format(calendar.getTime()));
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        days.add(simpleDateFormat.format(calendar.getTime()));
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        days.add(simpleDateFormat.format(calendar.getTime()));
        int todayPee = 0;
        int thisWeekPee = 0;
        for (DataBean dataBean:dataBeans){
            if (dataBean.getType()==DataBean.TYPE_DIAPERS){
                if (TextUtils.equals(today,dataBean.getDate())){
                    todayPee++;
                }
                for (String day:days){
                    if (TextUtils.equals(day,dataBean.getDate())){
                        thisWeekPee++;
                    }
                }
            }
        }
        if (thisWeekPee>0){
            dataLayout.setVisibility(View.VISIBLE);
            nodataLayout.setVisibility(View.GONE);
            todayPeeText.setText(todayPee+"");
            thisweekPeeText.setText(thisWeekPee+"");
            showData(todayPee,thisWeekPee);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageStart("PeeTotalFragment");
    }

    private void showData(int todayPee, int thisWeekPee) {
        switch (todayPee){
            default:
                todayPeeMore.setVisibility(View.VISIBLE);
            case 6:
                todayPee6.setVisibility(View.VISIBLE);
            case 5:
                todayPee5.setVisibility(View.VISIBLE);
            case 4:
                todayPee4.setVisibility(View.VISIBLE);
            case 3:
                todayPee3.setVisibility(View.VISIBLE);
            case 2:
                todayPee2.setVisibility(View.VISIBLE);
            case 1:
                todayPee1.setVisibility(View.VISIBLE);
            case 0:
                break;
        }
        switch (thisWeekPee){
            default:
                thisWeekPeeMore.setVisibility(View.VISIBLE);
            case 6:
                thisWeekPee6.setVisibility(View.VISIBLE);
            case 5:
                thisWeekPee5.setVisibility(View.VISIBLE);
            case 4:
                thisWeekPee4.setVisibility(View.VISIBLE);
            case 3:
                thisWeekPee3.setVisibility(View.VISIBLE);
            case 2:
                thisWeekPee2.setVisibility(View.VISIBLE);
            case 1:
                thisWeekPee1.setVisibility(View.VISIBLE);
            case 0:
                break;
        }
    }
}
