package utot.utot.asynctasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.compat.BuildConfig;

import io.realm.Realm;
import utot.utot.alarm.TabbedAlarm;
import utot.utot.customobjects.Picture;
import utot.utot.helpers.FinalVariables;
import utot.utot.login.LoginSplashScreen;

/**
 * Created by elysi on 12/26/2016.
 */

public class CheckingStart extends AsyncTask<Void, Void, Integer> {
    private Activity act;


    public CheckingStart(Activity act) {
        this.act = act;
    }

    public void onPreExecute() {


    }

    @Override
    protected Integer doInBackground(Void... params) {
        final int currentVersionCode = BuildConfig.VERSION_CODE;
        SharedPreferences prefs = act.getSharedPreferences(FinalVariables.PREFS_NAME, Context.MODE_PRIVATE);

        // Get current version code
        if (prefs == null || prefs.getInt(FinalVariables.PREF_VERSION_CODE_KEY, FinalVariables.DOESNT_EXIST) == FinalVariables.DOESNT_EXIST) {
            // TODO This is a new install (or the user cleared the shared preferences)
            prefs.edit().putInt(FinalVariables.PREF_VERSION_CODE_KEY, currentVersionCode).apply();
            String bg = "bg";
            Realm realm = Realm.getDefaultInstance();
            for(int i =0; i <FinalVariables.pictureCount; i++){
                realm.beginTransaction();
                Picture pic = realm.createObject(Picture.class);
                pic.setResourceName(bg+Integer.toString(i));
                pic.setIsUsed(false);
                realm.commitTransaction();
            }

            return 2;

        } else if (currentVersionCode == prefs.getInt(FinalVariables.PREF_VERSION_CODE_KEY, FinalVariables.DOESNT_EXIST)) {
            //Normal run
            return -1;

        } else if (currentVersionCode > prefs.getInt(FinalVariables.PREF_VERSION_CODE_KEY, FinalVariables.DOESNT_EXIST)) {
            prefs.edit().putInt(FinalVariables.PREF_VERSION_CODE_KEY, currentVersionCode).apply();
            return -1;

            // TODO This is an upgrade

        } else return -1;


    }

    @Override
    public void onPostExecute(Integer i) {

    }
}