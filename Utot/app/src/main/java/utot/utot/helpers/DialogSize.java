package utot.utot.helpers;

import android.app.Activity;
import android.app.Dialog;
import android.util.DisplayMetrics;
import android.view.Window;

/**
 * Created by elysi on 12/13/2016.
 */
public class DialogSize {

    public static void setSize(Activity act, Dialog dialog){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;

        Window d = dialog.getWindow();
        d.setLayout(height / 2, width);

    }
}
