package utot.utot.poem;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import io.realm.Realm;
import io.realm.RealmResults;
import utot.utot.R;
import utot.utot.customobjects.Poem;
import utot.utot.customviews.TextViewPlus;
import utot.utot.customviews.ZoomLayout;
import utot.utot.helpers.CreateObjects;
import utot.utot.helpers.FinalVariables;

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
        int status = getIntent().getIntExtra("STATUS", 0);
        Poem setPoem;
        if(status==FinalVariables.POEM_SAVE){
            setPoem = Realm.getDefaultInstance().where(Poem.class).equalTo("status", FinalVariables.POEM_SAVE).findAllSorted("dateAdded").
                    get(getIntent().getIntExtra("POEM_POS", 0));
        } else{
            setPoem = Realm.getDefaultInstance().where(Poem.class).equalTo("status", FinalVariables.POEM_BRODCAST).
                    findAll().get(getIntent().getIntExtra("POEM_POS", 0));
        }
        TextViewPlus poem = (TextViewPlus) findViewById(R.id.poem);
        ImageView bg = (ImageView)findViewById(R.id.backgroundPic);

        CreateObjects.setPoemDisplay(this, poem, bg, setPoem);

//        ScaleGestureDetector SGD;
//        SGD = new ScaleGestureDetector(this,new ScaleListener());
    }

//    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
//        @Override
//        public boolean onScale(ScaleGestureDetector detector) {
//            float scale = detector.getScaleFactor();
//            return true;
//        }
//    }
}
