package utot.utot.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;

import io.realm.Realm;
import utot.utot.R;
import utot.utot.alarm.TabbedAlarm;
import utot.utot.customobjects.CustomFrameLayout;

public class LoginSplashScreen extends Activity {


    private static class StartMainActivityRunnable implements Runnable {
        // 2. Make sure we keep the source Activity as a WeakReference (more on that later)
        private WeakReference mActivity;
        /** Duration of wait **/
        private final int LOGO_DISPLAY_LENGTH = 2000;
        private View view1, view2;
        private Animation slide_up;

        private StartMainActivityRunnable(Activity activity, View view1, View view2, Animation slide_up) {
            mActivity = new WeakReference(activity);
            this.view1 = view1;
            this.view2 = view2;
            this.slide_up = slide_up;
        }

        @Override
        public void run() {
            // 3. Check that the reference is valid and execute the code
            if (mActivity.get() != null) {
                view2.setVisibility(View.VISIBLE);
                view1.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Activity activity = (Activity) mActivity.get();
                        Intent mainIntent = new Intent(activity, TabbedAlarm.class);
                        activity.startActivity(mainIntent);
                        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        activity.finish();

                    }
                }, LOGO_DISPLAY_LENGTH);


            }
        }
    }

    private final int QUOTE_DISPLAY_LENGTH = 4000;


    // 4. Declare the Handler as a member variable
    private Handler mHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_splash_sceen);
        Realm.init(getApplicationContext());
        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        View view2 = findViewById(R.id.utotLogo);

      //  Picasso.with(this).load(R.drawable.aa_1).into((CustomFrameLayout) findViewById(R.id.mainwindow));
        Picasso.with(this).load(R.drawable.aa_4).into((ImageView)view2);

        // 5. Pass a new instance of StartMainActivityRunnable with reference to 'this'.
        mHandler.postDelayed(new StartMainActivityRunnable(this, findViewById(R.id.quote), view2, slide_up), QUOTE_DISPLAY_LENGTH);
    }

    // 6. Override onDestroy()
    @Override
    public void onDestroy() {
        super.onDestroy();
        // 7. Remove any delayed Runnable(s) and prevent them from executing.
        mHandler.removeCallbacksAndMessages(null);

        // 8. Eagerly clear mHandler allocated memory
        mHandler = null;
    }

}
