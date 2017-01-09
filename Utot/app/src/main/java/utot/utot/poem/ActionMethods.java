package utot.utot.poem;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.support.percent.PercentRelativeLayout;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;

import java.util.Calendar;

import io.realm.Realm;
import utot.utot.R;
import utot.utot.customobjects.Poem;
import utot.utot.customviews.TextViewPlus;
import utot.utot.helpers.BitmapMaker;
import utot.utot.helpers.FinalVariables;
import utot.utot.settings.ClickBrodcast;

/**
 * Created by tonyv on 1/8/2017.
 */

public class ActionMethods {
    private Realm realm;
    private Intent intentAct;
    private Poem poem;
    private Activity act;
    private Animator.AnimatorListener animatorLis, animatorLis2;

    public ActionMethods(Realm realm, Activity act, Intent intentAct, Poem poem){
        this.realm = realm;
        this.intentAct = intentAct;
        this.poem = poem;
        this.act = act;
    }
    public void savePoem(){
        realm.beginTransaction();
        poem.setStatus(FinalVariables.POEM_SAVE);
        poem.setDateAdded(Calendar.getInstance().getTime());
        realm.commitTransaction();

        intentAct.putExtra(FinalVariables.ACTION_DONE, FinalVariables.POEM_SAVE);
        intentAct.putExtra("TABBED", 0);
        act.startActivity(intentAct);
        act.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        act.finish();

    }
    public void discardPoem(){
        realm.beginTransaction();
        poem.setStatus(FinalVariables.POEM_DISCARD);
        realm.commitTransaction();

        intentAct.putExtra(FinalVariables.ACTION_DONE, FinalVariables.POEM_DISCARD);
        act.startActivity(intentAct);
        act.overridePendingTransition(R.anim.pull_in_up, R.anim.slide_down);
        act.finish();
    }
    public void sharePoem(CallbackManager callbackManager, PercentRelativeLayout displayImg){
        realm.beginTransaction();
        poem.setStatus(FinalVariables.POEM_SHARE);
        realm.commitTransaction();

        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        BitmapMaker.fn_share(poem.getPrimaryKey(), callbackManager, act, (LoginButton) act.findViewById(R.id.login_fb),
                displayImg, root);
    }

    public void animateText(final TextViewPlus text){
//        animatorLis2 = new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                text.animate()
//                        .alpha(0.0f).setDuration(1500)
//                        .setListener(animatorLis).start();
//            }
//        };
//        animatorLis = new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                text.animate().alpha(1.0f).setDuration(1500).setListener(animatorLis2).start();
//            }
//        };
//
//        text.animate()
//                .alpha(0.0f).setDuration(1500)
//                .setListener(animatorLis).start();
        Animation fadeInAnimation = AnimationUtils.loadAnimation(act, R.anim.fade_in_out);
        text.setAnimation(fadeInAnimation);
    }

}
