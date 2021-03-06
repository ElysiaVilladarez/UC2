package utot.utot.settings;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;

import java.util.Calendar;

import io.realm.Realm;
import utot.utot.R;
import utot.utot.alarm.TabbedAlarm;
import utot.utot.customobjects.BrodcastDelete;
import utot.utot.customobjects.OnSwipeListener;
import utot.utot.customobjects.Poem;
import utot.utot.customviews.TextViewPlus;
import utot.utot.helpers.BitmapMaker;
import utot.utot.helpers.CreateObjects;
import utot.utot.helpers.FinalVariables;
import utot.utot.poem.ActionMethods;
import utot.utot.poem.ShowPoems;

/**
 * Created by elysi on 12/28/2016.
 */

public class ClickBrodcast extends AppCompatActivity {

    private TextViewPlus poem;
    private PercentRelativeLayout displayImg;

    private CallbackManager callbackManager;
    private GestureDetector mGestureDetector;
    private Realm realm;

    private RelativeLayout.LayoutParams rParams;

    private Poem brodcastPoem;

    private Intent goToMain;

    private ActionMethods actionMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_show_poems);

        callbackManager = CallbackManager.Factory.create();
        Realm.init(getApplicationContext());

        realm = Realm.getDefaultInstance();

        brodcastPoem = realm.where(Poem.class).equalTo("status", FinalVariables.POEM_BRODCAST).
                findAll().get(getIntent().getIntExtra("POEM_POS", 0));

//        realm.beginTransaction();
//        brodcastPoem.setPic(CreateObjects.getRandomPicture(realm));
//        realm.commitTransaction();

        displayImg = (PercentRelativeLayout) findViewById(R.id.poemAndImage);
        rParams = (RelativeLayout.LayoutParams) displayImg.getLayoutParams();

        TextViewPlus saveText = (TextViewPlus)findViewById(R.id.saveButton);
        TextViewPlus discardText = (TextViewPlus)findViewById(R.id.discardButton);
        TextViewPlus shareText = (TextViewPlus)findViewById(R.id.shareButton);

        poem = (TextViewPlus) findViewById(R.id.poem);
        ImageView bg = (ImageView)findViewById(R.id.backgroundPic);

        CreateObjects.setPoemDisplay(this, poem, bg, brodcastPoem);
        goToMain = new Intent(this, Brodcast.class);

        actionMethods = new ActionMethods(realm, this, goToMain, brodcastPoem);

        actionMethods.animateText(saveText);
        actionMethods.animateText(discardText);
        actionMethods.animateText(shareText);

        mGestureDetector = new GestureDetector(this, new OnSwipeListener(displayImg) {
            @Override
            public boolean onSwipe(Direction d) {
                super.onSwipe(d);
                callbackManager = CallbackManager.Factory.create();
                if (d == Direction.down) {

                    actionMethods.discardPoem();

                    CreateObjects.createBrodDelete(brodcastPoem.getPrimaryKey());
                    return true;
                } else if (d == Direction.left) {
                    actionMethods.savePoem();

                    CreateObjects.createBrodDelete(brodcastPoem.getPrimaryKey());
                    return true;

                } else if (d == Direction.right) {

                    actionMethods.sharePoem(callbackManager, displayImg);

                    CreateObjects.createBrodDelete(brodcastPoem.getPrimaryKey());

                    return true;
                }

                return true;
            }

        });

        findViewById(R.id.mainwindow).setOnTouchListener(new View.OnTouchListener() {

                                                             @Override
                                                             public boolean onTouch(View v, MotionEvent event) {

//                                                                 final int min_distance = 100;
//                                                                 float deltaX = event.getX();
//                                                                 float deltaY = event.getY();
//                                                                 RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                                                                 switch (event.getAction())
//                                                                 {
//                                                                     case MotionEvent.ACTION_MOVE:
//                                                                         params.leftMargin = (int) event.getRawX() - (v.getWidth() / 2);
//                                                                         displayImg.setAlpha(0.5f);
//                                                                         displayImg.setLayoutParams(params);
//                                                                         if (Math.abs(deltaX) > Math.abs(deltaY)) {
//                                                                             if (Math.abs(deltaX) > min_distance) {
//                                                                                 // left or right
//                                                                                 if (deltaX < 0) {
//
//                                                                                 }
//                                                                                 if (deltaX > 0) {
//                                                                                     params.rightMargin = (int) event.getRawX() - (v.getWidth() / 2);
//                                                                                     displayImg.setAlpha(0.5f);
//                                                                                     displayImg.setLayoutParams(params);
//                                                                                 }
//                                                                             } else {
//                                                                                 //not long enough swipe...
//                                                                                 displayImg.setLayoutParams(rParams);
//                                                                             }
//                                                                         }
//                                                                         //VERTICAL SCROLL
//                                                                         else {
//                                                                             if (Math.abs(deltaY) > min_distance) {
//                                                                                 // top or down
//                                                                                 if (deltaY < 0) {
//
//                                                                                 }
//                                                                                 if (deltaY > 0) {
//                                                                                     params.topMargin = (int) (event.getRawY() - v.getHeight()/2);
//                                                                                 }
//                                                                             } else {
//                                                                                 //not long enough swipe...
//
//                                                                                 displayImg.setLayoutParams(rParams);
//                                                                             }
//                                                                         }
//
//                                                                         break;
//
//                                                                     case MotionEvent.ACTION_UP:
//
////                                                                         displayImg.setLayoutParams(rParams);
//                                                                         break;
//
//                                                                     case MotionEvent.ACTION_DOWN:
////                                                                         params.topMargin = (int) event.getRawY() - v.getHeight();
////                                                                         params.leftMargin = (int) event.getRawX() - (v.getWidth() / 2);
//                                                                         displayImg.setLayoutParams(params);
//
//                                                                         break;
//                                                                 }

                                                                 boolean eventConsumed = mGestureDetector.onTouchEvent(event);
                                                                 if (eventConsumed) {
                                                                     return true;
                                                                 } else
                                                                     return false;
                                                             }
                                                         }

        );

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionMethods.savePoem();

                CreateObjects.createBrodDelete(brodcastPoem.getPrimaryKey());
            }
        });

        findViewById(R.id.discard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionMethods.discardPoem();
                CreateObjects.createBrodDelete(brodcastPoem.getPrimaryKey());

            }
        });

        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionMethods.savePoem();

                CreateObjects.createBrodDelete(brodcastPoem.getPrimaryKey());


            }
        });
        realm.close();
    }



    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed(){
        ClickBrodcast.this.startActivity(goToMain);
        ClickBrodcast.this.overridePendingTransition(R.anim.pull_in_up, R.anim.slide_down);
        ClickBrodcast.this.finish();
    }

}