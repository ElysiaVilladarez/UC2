package utot.utot.asynctasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.login.LoginManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;

import io.realm.Realm;
import io.realm.RealmResults;
import utot.utot.customobjects.BrodcastDelete;
import utot.utot.customobjects.Poem;
import utot.utot.helpers.CheckInternet;
import utot.utot.helpers.Computations;
import utot.utot.helpers.CreateObjects;
import utot.utot.helpers.FinalVariables;
import utot.utot.helpers.LoginCommon;
import utot.utot.login.LoginSplashScreen;
import utot.utot.settings.Brodcast;

/**
 * Created by elysi on 12/22/2016.
 */

public class BrodcastTask extends AsyncTask<Void, Void, String> {
    private String username;
    private Context c;
    private String brodLink = "http://utotcatalog.technotrekinc.com/z_announcement_list.php";
//    private String brodLink = "http://utotcatalog.technotrekinc.com/z_announcement_list.php?email=alvincrisuy@gmail.com";
    private static ProgressDialog progressDialog;
    private Activity act;
    private RealmResults<Poem> deletedAnnouncements;
    private boolean onDestroy;

    public BrodcastTask(Context c, Activity act, String username, boolean onDestroy) {
        this.username = username;
        this.c = c;
        this.act = act;
        this.progressDialog = new ProgressDialog(c);
        this.onDestroy = onDestroy;
    }

    @Override
    protected void onPreExecute() {
        progressDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setTitle("Syncing bRODcasts . . .");
        progressDialog.setMessage("Please make sure you have a stable internet connection");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(Void... params) {
        if(CheckInternet.hasActiveInternetConnection(act)) {
            String deletedAnnouncements = "";

            Realm realm  = Realm.getDefaultInstance();
            RealmResults<BrodcastDelete> brodcastPoemsDiscarded = realm.where(BrodcastDelete.class).findAll();


            for(BrodcastDelete p: brodcastPoemsDiscarded){
                deletedAnnouncements+="{" + Integer.toString(p.getId()) + "}";
            }

            System.out.println("CHECK: DELETEDAn=" + deletedAnnouncements);

            realm.beginTransaction();
            realm.delete(BrodcastDelete.class);
            realm.commitTransaction();

            brodLink += "?email=" + username;
            if(brodcastPoemsDiscarded.size()> 0)  brodLink += "&deletedAnnouncements=" + deletedAnnouncements;


            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            HttpResponse response;
            String json;
            JSONObject req = null;
            String success = "";

            try {
                request.setURI(new URI(brodLink));
                response = client.execute(request);
                json = EntityUtils.toString(response.getEntity());
                req = new JSONObject(json);
                success = req.getString("result");
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            if (success.equals("success")) {
                System.out.println("CHECK: SUCCESS");
                RealmResults<Poem> poems = realm.where(Poem.class).equalTo("status", FinalVariables.POEM_BRODCAST).findAll();
                realm.beginTransaction();
                poems.deleteAllFromRealm();
                realm.commitTransaction();
                try {
                    JSONArray brodcast_list = req.getJSONArray("announcement_list");

                    for (int i = 0; i < brodcast_list.length(); i++) {
                        JSONObject brod = brodcast_list.getJSONObject(i);
                        CreateObjects.createBrodcast(Integer.parseInt(brod.getString("id")), brod.getString("hugotshort"),
                                    FinalVariables.serverDateFormat2.parse(brod.getString("date")));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return success;
        } else{
            return "";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if(result.equals("success") || !onDestroy) {
            Intent next = new Intent(act, Brodcast.class);
            act.startActivity(next);
            act.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            act.finish();
        } else if(result.trim().isEmpty()){
            LoginCommon.noInternetDialog(act);
        } else{
            Toast.makeText(act,result, Toast.LENGTH_LONG).show();
        }


    }
}
