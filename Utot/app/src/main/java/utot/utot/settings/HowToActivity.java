package utot.utot.settings;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.viewpagerindicator.CirclePageIndicator;

import utot.utot.R;

public class HowToActivity extends AppCompatActivity {
    private ViewPager mPager;
    private CirclePageIndicator mIndicator;
    private HowToAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to);

        mAdapter = new HowToAdapter(getSupportFragmentManager(), null);

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
    }
}
