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

import utot.utot.helpers.Computations;
import utot.utot.helpers.CreateObjects;
import utot.utot.helpers.FinalVariables;
import utot.utot.login.LoginSplashScreen;
import utot.utot.register.RegisterActivity;

/**
 * Created by elysi on 12/21/2016.
 */

public class LoginTask extends AsyncTask<Void, Void, Integer> {
    private String username, password;
    private Context c;
    private String loginLink = "http://utotcatalog.technotrekinc.com/z_login.php";
    //sample http://utotcatalog.technotrekinc.com/z_login.php?email=elysiajelenavilladarez@yahoo.com&password=luffy1
    private static ProgressDialog progressDialog;
    private Activity act;

    public LoginTask(Context c, Activity act, String username, String password) {
        this.username = username;
        this.password = password;
        this.c = c;
        this.act = act;
        this.progressDialog = new ProgressDialog(c);
    }

    @Override
    protected void onPreExecute() {
        progressDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setTitle("Signing in . . .");
        progressDialog.setMessage("Please make sure you have a stable internet connection");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    @Override
    protected Integer doInBackground(Void... params) {
        loginLink += "?email=" + username + "&password=" + password;
//        + "&deviceToken="+ Settings.Secure.getString(getApplicationContext().getContentResolver(),
//                Settings.Secure.ANDROID_ID);
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet();
        HttpResponse response;
        String json;
        JSONObject req = null;
        String success = "";

        try {
            request.setURI(new URI(loginLink));
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

        RingtoneManager ringtoneManager = new RingtoneManager(c);
        ringtoneManager.setType(RingtoneManager.TYPE_ALARM);
        Cursor alarmsCursor = ringtoneManager.getCursor();
        int successInt = 0;
        if (!success.trim().isEmpty()) {
            try {
                successInt = 1;
                JSONArray alarm_list = req.getJSONArray("alarm_list");
                String ringtoneText = "";
                for (int i = 0; i < alarm_list.length(); i++) {
                    JSONObject alarm = alarm_list.getJSONObject(i);
                    int ringtonePos = alarm.getInt("sound");
                    if (alarmsCursor.getCount() != 0 && alarmsCursor.moveToFirst()) {
                        ringtoneText = ringtoneManager.getRingtoneUri(ringtonePos).toString();
                    }
                    CreateObjects.createAlarm(c, alarm.getInt("localid"), Computations.transformToBooleanJSONArray(alarm.getString("repeat_string")),
                            FinalVariables.serverDateFormat.parse(alarm.getString("date")), ringtoneText,
                            ringtonePos, false, Computations.isRepeating, alarm.getInt("is_set")==1);
                }

                JSONArray hugot_list = req.getJSONArray("hugot_list");
                for (int i = 0; i < hugot_list.length(); i++) {
                    JSONObject poem = hugot_list.getJSONObject(i);
                    CreateObjects.createPoem(poem.getInt("id"), poem.getString("short"), poem.getString("photo"));
                }

                JSONArray localhugot_list = req.getJSONArray("localhugot_list");
                for (int i = 0; i < localhugot_list.length(); i++) {
                    JSONObject poem = localhugot_list.getJSONObject(i);
                    System.out.println("CHECK: " + poem.getString("photo"));
                    CreateObjects.getLocalPoems(poem.getInt("id"), poem.getString("photo"),
                            FinalVariables.serverDateFormat.parse(poem.getString("date")));
                }


                } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else{
            successInt = 0;
        }

        return successInt;
    }

    @Override
    protected void onPostExecute(Integer result) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if(result==1){
            Intent next = new Intent(act, LoginSplashScreen.class);
            act.startActivity(next);
            act.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            act.finish();
        } else{
            Toast.makeText(act,"User does not exist", Toast.LENGTH_SHORT).show();
        }

    }
}
