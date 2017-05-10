package utot.utot.poem;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
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
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import java.util.Calendar;

import io.realm.Realm;
import utot.utot.R;
import utot.utot.alarm.TabbedAlarm;
import utot.utot.customobjects.OnSwipeListener;
import utot.utot.customobjects.Poem;
import utot.utot.customviews.TextViewPlus;
import utot.utot.helpers.BitmapMaker;
import utot.utot.helpers.CreateObjects;
import utot.utot.helpers.FinalVariables;
import utot.utot.settings.ClickBrodcast;

/**
 * Created by elysi on 12/16/2016.
 */
public class ShowPoems extends AppCompatActivity {

    private TextViewPlus poem;
    private PercentRelativeLayout displayImg;

    private CallbackManager callbackManager;
    private GestureDetector mGestureDetector;
    private Realm realm;

    private RelativeLayout.LayoutParams rParams;

    private Poem randomPoem;

    private ActionMethods actionMethods;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window wind = getWindow();
        wind.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        wind.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        wind.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        System.out.println("CHECK: I HAVE BEEN ACTIVATED!!!");
        if(!FacebookSdk.isInitialized()) FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_show_poems);

        callbackManager = CallbackManager.Factory.create();
        Realm.init(getApplicationContext());

        realm = Realm.getDefaultInstance();

        randomPoem = CreateObjects.getRandomPoem();
        if(randomPoem == null){
            Toast.makeText(getApplicationContext(), "No poems are available", Toast.LENGTH_LONG);
            startActivity(new Intent(this, TabbedAlarm.class));
            finish();
        }

        displayImg = (PercentRelativeLayout) findViewById(R.id.poemAndImage);
        rParams = (RelativeLayout.LayoutParams) displayImg.getLayoutParams();

        poem = (TextViewPlus) findViewById(R.id.poem);
        ImageView bg = (ImageView) findViewById(R.id.backgroundPic);

        TextViewPlus saveText = (TextViewPlus)findViewById(R.id.saveButton);
        TextViewPlus discardText = (TextViewPlus)findViewById(R.id.discardButton);
        TextViewPlus shareText = (TextViewPlus)findViewById(R.id.shareButton);

        CreateObjects.setPoemDisplay(this, poem, bg, randomPoem);
        final Intent goToMain = new Intent(ShowPoems.this, TabbedAlarm.class);

        actionMethods = new ActionMethods(Realm.getDefaultInstance(), this, goToMain, randomPoem);
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

                    return true;
                } else if (d == Direction.left) {

                    actionMethods.savePoem();

                    return true;

                } else if (d == Direction.right) {

                   actionMethods.sharePoem(callbackManager, displayImg);

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
            }
        });

        findViewById(R.id.discard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionMethods.discardPoem();
            }
        });

        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               actionMethods.sharePoem(callbackManager, displayImg);

            }
        });
    }



    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
