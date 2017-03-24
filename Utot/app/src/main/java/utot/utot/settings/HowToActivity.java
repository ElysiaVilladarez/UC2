package utot.utot.settings;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import utot.utot.R;
import utot.utot.customobjects.Instructions;

public class HowToActivity extends AppCompatActivity {
    private ViewPager mPager;
    private CirclePageIndicator mIndicator;
    private HowToAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to);

        ArrayList<Instructions> inst = new ArrayList<>();
        Instructions a = new Instructions(R.mipmap.how_to_1, "Set your alarm");
        Instructions b = new Instructions(R.mipmap.how_to_2, "Find your alarm");
        Instructions c = new Instructions(R.mipmap.how_to_3, "You can snooze or dismiss when it alarms");
        Instructions d = new Instructions(R.mipmap.how_to_4, "Click DISMISS to get hugot lines!");
        Instructions e = new Instructions(R.mipmap.how_to_4_2, "Save, discard, or share hugots by swiping\nor clicking the arrows");
        Instructions f = new Instructions(R.mipmap.how_to_5, "Find your saved hugots here!\nYou can share it too!");
        Instructions g = new Instructions(R.mipmap.how_to_6, "Get more hugot lines by setting alarms");

        inst.add(a);
        inst.add(b);
        inst.add(c);
        inst.add(d);
        inst.add(e);
        inst.add(f);
        inst.add(g);

        mAdapter = new HowToAdapter(getSupportFragmentManager(), inst);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);

        findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
