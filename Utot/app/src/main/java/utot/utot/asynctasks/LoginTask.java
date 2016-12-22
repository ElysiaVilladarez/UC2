package utot.utot.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by elysi on 12/21/2016.
 */

public class LoginTask extends AsyncTask<String, Void, Void> {
    String username, password;
    Context c;
    private final String loginLink="";
    private static ProgressDialog progressDialog;

    public LoginTask(Context c, String username, String password){
        this.username = username;
        this.password = password;
        this.c = c;
    }
    @Override
    protected void onPreExecute() {
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setMessage("Signing in . . .");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }
    @Override
    protected Void doInBackground(String... params) {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet();
        HttpResponse response;
        String json;
        JSONObject req;
        int success = 0;

        try {
            request.setURI(new URI(loginLink));
            response = client.execute(request);
            json = EntityUtils.toString(response.getEntity());
            req = new JSONObject(json);
            success = req.getInt("success");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (success == 1) {

        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
