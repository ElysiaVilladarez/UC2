package utot.utot.poem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import io.realm.Realm;
import io.realm.RealmResults;
import utot.utot.R;
import utot.utot.customobjects.Poem;
import utot.utot.customviews.TextViewPlus;
import utot.utot.customviews.ZoomLayout;

public class ClickPoem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_poem);

        final ZoomLayout zoomlayout=(ZoomLayout)findViewById(R.id.zoomLayout);
        zoomlayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                zoomlayout.init(ClickPoem.this);
                return false;
            }
        });

        TextViewPlus poem = (TextViewPlus) findViewById(R.id.poem);

        Poem setPoem = Realm.getDefaultInstance().where(Poem.class).findAll().get(getIntent().getIntExtra("POEM_POS", 0));
        poem.setText(setPoem.getPoem());
//        ScaleGestureDetector SGD;
//        SGD = new ScaleGestureDetector(this,new ScaleListener());
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scale = detector.getScaleFactor();
            return true;
        }
    }
}
