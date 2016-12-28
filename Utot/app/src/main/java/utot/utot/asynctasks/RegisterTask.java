package utot.utot.asynctasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.RingtoneManager;
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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import utot.utot.helpers.CheckInternet;
import utot.utot.helpers.Computations;
import utot.utot.helpers.CreateObjects;
import utot.utot.helpers.FinalVariables;
import utot.utot.helpers.LoginCommon;
import utot.utot.login.LoginSplashScreen;
import utot.utot.register.RegisterActivity;

/**
 * Created by elysi on 12/26/2016.
 */

public class RegisterTask extends AsyncTask<Void, Void, String> {
    private String username, password, fbId, fname, lname;
    private Context c;
    private String registerLink = "http://utotcatalog.technotrekinc.com/z_registration.php";
    private static ProgressDialog progressDialog;
    private Activity act;

    public RegisterTask(Context c, Activity act, String username, String password, String fbId, String fname, String lname) {
        this.username = username;
        this.password = password;
        this.fbId = fbId;
        this.fname = fname;
        this.lname = lname;
        this.c = c;
        this.act = act;
        this.progressDialog = new ProgressDialog(c);
    }

    @Override
    protected void onPreExecute() {
        progressDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setTitle("Creating account . . .");
        progressDialog.setMessage("Please make sure you have a stable internet connection");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(Void... params) {
        if(CheckInternet.hasActiveInternetConnection(act)) {
            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("fbId", fbId));
            urlParameters.add(new BasicNameValuePair("email", username));
            urlParameters.add(new BasicNameValuePair("password", password));
            urlParameters.add(new BasicNameValuePair("fname", fname));
            urlParameters.add(new BasicNameValuePair("lname", lname));
//        urlParameters.add(new BasicNameValuePair("deviceToken", "12345"));


            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost();
            HttpResponse response;
            String json;
            JSONObject req = null;
            String success = "";

            try {
                post.setURI(new URI(registerLink));
                post.setEntity(new UrlEncodedFormEntity(urlParameters));
                response = client.execute(post);
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

                System.out.println("CHECK: Successfully registered!");
//            try {
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
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
        if(result.equals("success")){

            Intent next = new Intent(act, LoginSplashScreen.class);
            next.putExtra(FinalVariables.EMAIL, username);
            act.startActivity(next);
            act.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            act.finish();
        } else if(result.trim().isEmpty()){
            LoginCommon.noInternetDialog(act);
        } else{
            LoginManager.getInstance().logOut();
            Toast.makeText(act,result, Toast.LENGTH_SHORT).show();

        }

    }
}