package com.drkj.wishfuldad.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.drkj.wishfuldad.BaseActivity;
import com.drkj.wishfuldad.R;
import com.drkj.wishfuldad.fragment.BlanketTotalFragment;
import com.drkj.wishfuldad.fragment.PeeGuestFragment;
import com.drkj.wishfuldad.fragment.PeeRecordFragment;
import com.drkj.wishfuldad.fragment.PeeTotalFragment;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DataActivity extends BaseActivity implements View.OnClickListener{

    private ViewPager viewPager;
    private PagerAdapter adapter;
    private List<Fragment> fragments = new ArrayList<>();

    private ImageView backImageView;
    private TextView peeRecordTextView;
    private TextView peeGuestTextView;
    private TextView peeTotalTextView;
    private TextView blanketTotalTextView;

    @BindView(R.id.image_pee_record_select)
    ImageView peeRecordSelect;
    @BindView(R.id.image_pee_guest_select)
    ImageView peeGuestSelect;
    @BindView(R.id.image_pee_total_select)
    ImageView peeTotalSelect;
    @BindView(R.id.image_tibei_select)
    ImageView tibeiSelect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        initView();
    }

    private void initView() {
        backImageView = findViewById(R.id.imageView_back);
        backImageView.setOnClickListener(this);

        peeRecordTextView = findViewById(R.id.urine_record_text);
        peeRecordTextView.setOnClickListener(this);

        peeGuestTextView = findViewById(R.id.urine_forecast_text);
        peeGuestTextView.setOnClickListener(this);

        peeTotalTextView = findViewById(R.id.diapers_number_text);
        peeTotalTextView.setOnClickListener(this);

        blanketTotalTextView = findViewById(R.id.kickquilt_num_text);
        blanketTotalTextView.setOnClickListener(this);
        viewPager = findViewById(R.id.viewPage_data);
        fragments.add(new PeeRecordFragment());
        fragments.add(new PeeGuestFragment());
        fragments.add(new PeeTotalFragment());
        fragments.add(new BlanketTotalFragment());

        adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                peeRecordSelect.setVisibility(View.GONE);
                peeGuestSelect.setVisibility(View.GONE);
                peeTotalSelect.setVisibility(View.GONE);
                tibeiSelect.setVisibility(View.GONE);
                switch (position) {
                    case 0:
                        peeRecordTextView.setTextColor(getResources().getColor(R.color.toolbar_text_check));
                        peeRecordTextView.getPaint().setFakeBoldText(true);
                        peeGuestTextView.setTextColor(getResources().getColor(R.color.toolbar_text_uncheck));
                        peeGuestTextView.getPaint().setFakeBoldText(false);
                        peeTotalTextView.setTextColor(getResources().getColor(R.color.toolbar_text_uncheck));
                        peeTotalTextView.getPaint().setFakeBoldText(false);
                        blanketTotalTextView.setTextColor(getResources().getColor(R.color.toolbar_text_uncheck));
                        blanketTotalTextView.getPaint().setFakeBoldText(false);

                        peeRecordSelect.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        peeRecordTextView.setTextColor(getResources().getColor(R.color.toolbar_text_uncheck));
                        peeRecordTextView.getPaint().setFakeBoldText(false);
                        peeGuestTextView.setTextColor(getResources().getColor(R.color.toolbar_text_check));
                        peeGuestTextView.getPaint().setFakeBoldText(true);
                        peeTotalTextView.setTextColor(getResources().getColor(R.color.toolbar_text_uncheck));
                        peeTotalTextView.getPaint().setFakeBoldText(false);
                        blanketTotalTextView.setTextColor(getResources().getColor(R.color.toolbar_text_uncheck));
                        blanketTotalTextView.getPaint().setFakeBoldText(false);

                        peeGuestSelect.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        peeRecordTextView.setTextColor(getResources().getColor(R.color.toolbar_text_uncheck));
                        peeRecordTextView.getPaint().setFakeBoldText(false);
                        peeGuestTextView.setTextColor(getResources().getColor(R.color.toolbar_text_uncheck));
                        peeGuestTextView.getPaint().setFakeBoldText(false);
                        peeTotalTextView.setTextColor(getResources().getColor(R.color.toolbar_text_check));
                        peeTotalTextView.getPaint().setFakeBoldText(true);
                        blanketTotalTextView.setTextColor(getResources().getColor(R.color.toolbar_text_uncheck));
                        blanketTotalTextView.getPaint().setFakeBoldText(false);

                        peeTotalSelect.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        peeRecordTextView.setTextColor(getResources().getColor(R.color.toolbar_text_uncheck));
                        peeRecordTextView.getPaint().setFakeBoldText(false);
                        peeGuestTextView.setTextColor(getResources().getColor(R.color.toolbar_text_uncheck));
                        peeGuestTextView.getPaint().setFakeBoldText(false);
                        peeTotalTextView.setTextColor(getResources().getColor(R.color.toolbar_text_uncheck));
                        peeTotalTextView.getPaint().setFakeBoldText(false);
                        blanketTotalTextView.setTextColor(getResources().getColor(R.color.toolbar_text_check));
                        blanketTotalTextView.getPaint().setFakeBoldText(true);

                        tibeiSelect.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setOffscreenPageLimit(3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_back:
                finish();
                break;
            case R.id.urine_record_text:
                MobclickAgent.onEvent(this, "nnRecord");
                viewPager.setCurrentItem(0,true);
                break;
            case R.id.urine_forecast_text:
                MobclickAgent.onEvent(this, "nnDivine");
                viewPager.setCurrentItem(1,true);
                break;
            case R.id.diapers_number_text:
                MobclickAgent.onEvent(this, "nnCount");
                viewPager.setCurrentItem(2,true);
                break;
            case R.id.kickquilt_num_text:
                MobclickAgent.onEvent(this, "tbRecord");
                viewPager.setCurrentItem(3,true);
                break;
            default:
                break;
        }
    }
}
