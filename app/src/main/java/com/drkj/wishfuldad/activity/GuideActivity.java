package com.drkj.wishfuldad.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.drkj.wishfuldad.BaseActivity;
import com.drkj.wishfuldad.R;
import com.drkj.wishfuldad.fragment.GuidePageFragment;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends BaseActivity {

    private ViewPager guideViewPager;
    List<Fragment> fragments = new ArrayList<>();
    private ImageView dot1;
    private ImageView dot2;
    private ImageView dot3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        guideViewPager = (ViewPager) findViewById(R.id.viewpager_guide);
        int[] layoutIds = new int[]{
                R.layout.page_tab1_guide,
                R.layout.page_tab2_guide,
                R.layout.page_tab3_guide,
        };
        dot1 = findViewById(R.id.image_dot1);
        dot2 = findViewById(R.id.image_dot2);
        dot3 = findViewById(R.id.image_dot3);
        for (int i = 0; i < 3; i++) {
            GuidePageFragment pageFragment = new GuidePageFragment();
            Bundle args = new Bundle();
            args.putInt("index", i);
            args.putInt("count", 3);
            args.putInt("layoutId", layoutIds[i]);

            pageFragment.setArguments(args);
            fragments.add(pageFragment);
        }

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };
        guideViewPager.setAdapter(adapter);
        guideViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        dot1.setBackground(getResources().getDrawable(R.drawable.shape_circle_dot_selected));
                        dot2.setBackground(getResources().getDrawable(R.drawable.shape_circle_dot_unselected));
                        dot3.setBackground(getResources().getDrawable(R.drawable.shape_circle_dot_unselected));
                        break;
                    case 1:
                        dot1.setBackground(getResources().getDrawable(R.drawable.shape_circle_dot_unselected));
                        dot2.setBackground(getResources().getDrawable(R.drawable.shape_circle_dot_selected));
                        dot3.setBackground(getResources().getDrawable(R.drawable.shape_circle_dot_unselected));
                        break;
                    case 2:
                        dot1.setBackground(getResources().getDrawable(R.drawable.shape_circle_dot_unselected));
                        dot2.setBackground(getResources().getDrawable(R.drawable.shape_circle_dot_unselected));
                        dot3.setBackground(getResources().getDrawable(R.drawable.shape_circle_dot_selected));
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
