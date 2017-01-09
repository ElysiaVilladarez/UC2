package utot.utot.asynctasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.login.LoginManager;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import utot.utot.customobjects.Alarm;
import utot.utot.customobjects.Poem;
import utot.utot.helpers.CheckInternet;
import utot.utot.helpers.Computations;
import utot.utot.helpers.FinalVariables;
import utot.utot.helpers.LoginCommon;
import utot.utot.login.InitialScreen;
import utot.utot.login.LoginActivity;

/**
 * Created by elysi on 12/28/2016.
 */

public class SyncTask_2 extends AsyncTask<Void, Void, String> {
    private String username;
    private Context c;
    private String syncLink = "http://utotcatalog.technotrekinc.com/z_sync.php";
    //sample http://utotcatalog.technotrekinc.com/z_sync.php?alarms={1,1970-01-01%2019:35:00%20+0800,1,T%20,0,%20}&hugots={24,2016-12-27%2019:34:03%20+0800,bg17}&email=elysiajelenavilladarez@yahoo.com
    private static ProgressDialog progressDialog;
    private Activity act;

    public SyncTask_2(Context c, Activity act, String username) {
        this.username = username;
        this.c = c;
        this.act = act;
        this.progressDialog = new ProgressDialog(c);
    }

    @Override
    protected void onPreExecute() {
        progressDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setTitle("Syncing . . .");
        progressDialog.setMessage("Please make sure you have a stable internet connection");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(Void... params) {
        if(CheckInternet.hasActiveInternetConnection(act)) {
            Realm realm = Realm.getDefaultInstance();
            RealmResults<Alarm> alarms = realm.where(Alarm.class).findAll();
            RealmResults<Poem> poems = realm.where(Poem.class).equalTo("status", FinalVariables.POEM_SAVE).findAllSorted("dateAdded", Sort.ASCENDING);


            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost();
            HttpResponse response;
            String json;
            JSONObject req = null;
            String success = "";

            char q = '"';
            try {
                String alarmJSONArray = "";
//            int i =0;
                for (Alarm a : alarms) {
                    String syncAlarm = "{";
                    int isSet = 0;
                    if (a.isOn()) isSet = 1;
                    syncAlarm += Integer.toString(a.getPrimaryKey()) + ",";
                    syncAlarm += FinalVariables.serverDateFormat.format(a.getAlarmTime()) + ",";
                    syncAlarm += Integer.toString(isSet) + ",";
                    syncAlarm += Computations.translationToReadableTextServer(a.getAlarmFrequency()) + ",";
                    syncAlarm += Integer.toString(a.getAlarmAudioPos()) + ",";
                    syncAlarm += "(null)";
                    syncAlarm+="}";
//                syncAlarm+=Integer.toString(a.getPrimaryKey())+",";
//                syncAlarm+=FinalVariables.serverDateFormat.format(a.getAlarmTime())+",";
//                syncAlarm+=Integer.toString(isSet)+",";
//                syncAlarm+=Computations.translationToReadableTextServer(a.getAlarmFrequency())+",";
//                syncAlarm+=Integer.toString(a.getAlarmAudioPos())+",";
//                syncAlarm+="  ";
//                if(i!=alarms.size()-1){
//                    syncAlarm+=",";
//                }
                    alarmJSONArray+=syncAlarm;
//                i++;
                }
//                alarmJSONArray+=q;

//            i=0;
                String poemJSONArray = "";
                for (Poem p : poems) {
                    String syncPoem = "{";
                    syncPoem += Integer.toString(p.getPrimaryKey()) + ",";
                    syncPoem += FinalVariables.serverDateFormat.format(p.getDateAdded()) + ",";
                    syncPoem += p.getPic().getResourceName();
                    syncPoem += "}";
//                if(i!=poems.size()-1){
//                    syncPoem+=",";
//                }
                    poemJSONArray+=syncPoem;
//                i++;
                }
//                poemJSONArray+=q;

                List<NameValuePair> urlParameters = new ArrayList<>();
                urlParameters.add(new BasicNameValuePair("alarms", alarmJSONArray));
                urlParameters.add(new BasicNameValuePair("hugots", poemJSONArray));
                urlParameters.add(new BasicNameValuePair("email", username));
                System.out.println("CHECK: POEMS=" + poemJSONArray);
                System.out.println("CHECK: Alarms=" + alarmJSONArray);

                post.setURI(new URI(syncLink));
                post.setEntity(new UrlEncodedFormEntity(urlParameters, HTTP.UTF_8));
                response = client.execute(post);
                json = EntityUtils.toString(response.getEntity());
                req = new JSONObject(json);
                success = req.getString("result").trim();

            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (success.contains("success")) {
                System.out.println("CHECK: SUCCESS");
//            try {
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
            } else {
            }

            return success;
        }else return "";
    }

    @Override
    protected void onPostExecute(String result) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if(result.contains("success")){
            Toast.makeText(act, "Successfully synced data!", Toast.LENGTH_SHORT).show();
            LoginManager.getInstance().logOut();

            Computations.cancelAllAlarms(act);

            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.delete(Poem.class);
            realm.delete(Alarm.class);
            realm.commitTransaction();
            SharedPreferences prefs = act.getSharedPreferences(FinalVariables.PREFS_NAME, Context.MODE_PRIVATE);
            prefs.edit().putBoolean(FinalVariables.LOGGED_IN, false).apply();
            prefs.edit().putString(FinalVariables.EMAIL, "").apply();

            act.startActivity(new Intent(act, InitialScreen.class));
            act.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            act.finish();

        } else if (result.trim().isEmpty()){
            LoginCommon.noInternetDialog(act);
        } else{

            Toast.makeText(act,result, Toast.LENGTH_SHORT).show();

        }

    }
}
