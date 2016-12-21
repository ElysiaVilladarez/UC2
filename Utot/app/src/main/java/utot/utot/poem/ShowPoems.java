package utot.utot.poem;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.IntegerRes;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.scalified.viewmover.configuration.MovingParams;
import com.scalified.viewmover.movers.ViewMover;
import com.scalified.viewmover.movers.ViewMoverFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import io.realm.Realm;
import utot.utot.R;
import utot.utot.alarm.TabbedAlarm;
import utot.utot.customobjects.OnSwipeListener;
import utot.utot.customobjects.Poem;
import utot.utot.customviews.TextViewPlus;
import utot.utot.helpers.BitmapMaker;
import utot.utot.login.LoginActivity;
import utot.utot.login.LoginSplashScreen;

/**
 * Created by elysi on 12/16/2016.
 */
public class ShowPoems extends AppCompatActivity {

    public final static String FOLDER_NAME = "/UtotCatalog";
    public final static int POEM_SHARE = 2;
    public final static int POEM_DISCARD = 1;
    public final static int POEM_SAVE = 0;
    public final static int POEM_NOT_SHOWN = 4;

    private TextViewPlus poem;
    private PercentRelativeLayout displayImg;

    private CallbackManager callbackManager;
    private GestureDetector mGestureDetector;
    private Realm realm;

    private RelativeLayout.LayoutParams rParams;

    private SharePhotoContent content;
    private ShareDialog shareDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        Window wind = getWindow();
        wind.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        wind.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        wind.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.activity_show_poems);
        Realm.init(getApplicationContext());

        realm = Realm.getDefaultInstance();

        displayImg = (PercentRelativeLayout) findViewById(R.id.poemAndImage);
        rParams = (RelativeLayout.LayoutParams) displayImg.getLayoutParams();

        poem = (TextViewPlus) findViewById(R.id.poem);

        mGestureDetector = new GestureDetector(this, new OnSwipeListener(displayImg) {
            @Override
            public boolean onSwipe(Direction d) {
                super.onSwipe(d);

                if (d == Direction.down) {

                    ShowPoems.this.startActivity(new Intent(ShowPoems.this, TabbedAlarm.class));
                    ShowPoems.this.overridePendingTransition(R.anim.pull_in_up, R.anim.slide_down);
                    ShowPoems.this.finish();
                    return true;
                } else if (d == Direction.right) {
                    Poem poem;
                    realm.beginTransaction();
                    poem = realm.createObject(Poem.class);
                    poem.setPrimaryKey((int)System.currentTimeMillis());
                    poem.setPoem("Poem Sample");
                    poem.setStatus(POEM_SHARE);
                    realm.commitTransaction();


                    System.out.println("CHECK: SHOW DIALOG ");

                    Bitmap bitmap1 = BitmapMaker.loadBitmapFromView(displayImg, displayImg.getWidth(), displayImg.getHeight());
                    String filename = Integer.toString(poem.getPrimaryKey());
                    BitmapMaker.saveBitmap(bitmap1,filename);

                    callbackManager = CallbackManager.Factory.create();
                    BitmapMaker.fn_share(filename, callbackManager, ShowPoems.this, (LoginButton)findViewById(R.id.login_fb));

//                    ShowPoems.this.startActivity(new Intent(ShowPoems.this, TabbedAlarm.class));
//                    ShowPoems.this.overridePendingTransition(R.anim.left_to_right_slide, R.anim.right_to_left_slide);
//                    ShowPoems.this.finish();
                    return true;

                } else if (d == Direction.left) {
                    realm.beginTransaction();
                    Poem poem = realm.createObject(Poem.class);
                    poem.setPrimaryKey((int)System.currentTimeMillis());
                    poem.setPoem("Poem Sample");
                    poem.setStatus(POEM_SAVE);
                    realm.commitTransaction();
                    


                    ShowPoems.this.startActivity(new Intent(ShowPoems.this, TabbedAlarm.class));

                    ShowPoems.this.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                    ShowPoems.this.finish();
                    return true;
                }

                return true;
            }

        });

        ((RelativeLayout)findViewById(R.id.mainwindow)).setOnTouchListener(new View.OnTouchListener() {

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
                realm.beginTransaction();
                Poem poem = realm.createObject(Poem.class);
                poem.setPoem("Poem Sample");
                realm.commitTransaction();


                ShowPoems.this.startActivity(new Intent(ShowPoems.this, TabbedAlarm.class));
                ShowPoems.this.finish();

            }
        });

        findViewById(R.id.discard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPoems.this.startActivity(new Intent(ShowPoems.this, TabbedAlarm.class));
                ShowPoems.this.finish();
            }
        });

    }



    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
