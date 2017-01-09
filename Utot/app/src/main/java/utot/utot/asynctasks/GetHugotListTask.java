package utot.utot.asynctasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import utot.utot.customobjects.Poem;
import utot.utot.helpers.CheckInternet;
import utot.utot.helpers.CreateObjects;
import utot.utot.helpers.FinalVariables;
import utot.utot.helpers.LoginCommon;
import utot.utot.login.LoginSplashScreen;

/**
 * Created by tonyv on 1/5/2017.
 */

public class GetHugotListTask extends AsyncTask<Void, Void, String> {
    private String hugotlist = "http://utotcatalog.technotrekinc.com/z_hugot_list.php";
    private Activity act;

    public GetHugotListTask(Activity act) {
        this.act = act;
    }

    @Override
    protected String doInBackground(Void... params) {
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet();
        String success = "";
        try {

            get.setURI(new URI(hugotlist));
            HttpResponse response = client.execute(get);
            String json = EntityUtils.toString(response.getEntity());
            JSONObject req = new JSONObject(json);
            success = req.getString("result");

            if (success.equals("success")) {
                JSONArray hugot_list = req.getJSONArray("hugot_list");
                for (int i = 0; i < hugot_list.length(); i++) {
                    JSONObject poem = hugot_list.getJSONObject(i);
                    int id = Integer.parseInt(poem.getString("id"));
                    if((int)Realm.getDefaultInstance().where(Poem.class).equalTo("primaryKey", id).count() <=0) {
                        CreateObjects.createPoem(id, poem.getString("short"),
                                poem.getString("photo"), FinalVariables.POEM_NOT_SHOWN);
                    }
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return success;
}

    @Override
    protected void onPostExecute(String result) {

        if (!result.equals("success")) {

            Toast.makeText(act, "EEROR: " + result, Toast.LENGTH_SHORT).show();

        }

    }
}