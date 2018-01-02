package com.drkj.wishfuldad.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.drkj.wishfuldad.BaseActivity;
import com.drkj.wishfuldad.R;
import com.drkj.wishfuldad.view.PeeRecordLayout;
import com.drkj.wishfuldad.view.VerticalRuleView;

import butterknife.BindView;

public class TestViewActivity extends BaseActivity {
    @BindView(R.id.text_kedu)
    TextView textView;
    private PeeRecordLayout layout;
    @BindView(R.id.kedu)
    VerticalRuleView ruleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_view);
//        layout = findViewById(R.id.datalayout);
//        List<PeeDataBean> list = new ArrayList<>();
//
//        PeeDataBean bean = new PeeDataBean();
//        bean.setDay(2);
//        bean.setTime("13:05");
//        list.add(bean);
//        PeeDataBean bean2 = new PeeDataBean();
//        bean2.setDay(3);
//        bean2.setTime("08:20");
//        list.add(bean2);
//        PeeDataBean bean3 = new PeeDataBean();
//        bean3.setDay(2);
//        bean3.setTime("03:50");
//        list.add(bean3);
//        PeeDataBean bean4 = new PeeDataBean();
//        bean4.setDay(6);
//        bean4.setTime("22:50");
//        list.add(bean4);
//        PeeDataBean bean5 = new PeeDataBean();
//        bean5.setDay(1);
//        bean5.setTime("23:50");
//        list.add(bean5);
//        PeeDataBean bean6 = new PeeDataBean();
//        bean6.setDay(1);
//        bean6.setTime("02:30");
//        list.add(bean6);
//        layout.setData(list);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ruleView.setCallBack(new VerticalRuleView.CallBack() {
            @Override
            public void onScaleChanging(float scale) {
                int a = (int) scale;
                if (a % 10 == 0) {
                    int c = a/10;
                    textView.setText(c + "cm");
                } else {

                    float b = a / 10f;
                    textView.setText(b + "cm");
                }
            }
        });
    }
}
