package utot.utot;

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
import utot.utot.helpers.CreateObjects;
import utot.utot.helpers.FinalVariables;

/**
 * Created by elysi on 12/28/2016.
 */

public class TestHTTP {

    public static void main(String[] args) {

        String brodLink = "http://utotcatalog.technotrekinc.com/z_announcement_list.php?email=alvincrisuy@gmail.com";

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
            try {
                System.out.println("CHECK: BRODCAST SUCCESS");
                JSONArray brodcast_list = req.getJSONArray("announcement_list");

                for (int i = 0; i < brodcast_list.length(); i++) {
                    JSONObject brod = brodcast_list.getJSONObject(i);
                    System.out.println("CHECK: brodcastobject" + brod);
                    System.out.println("CHECK: id=" + brod.getString("id"));
                    System.out.println("CHECK: short=" + brod.getString("hugotshort"));
                    System.out.println("CHECK: date=" + FinalVariables.serverDateFormat.parse(brod.getString("date")));
                    CreateObjects.createBrodcast(Integer.parseInt(brod.getString("id")), brod.getString("hugotshort"),
                            FinalVariables.serverDateFormat.parse(brod.getString("date")));

                }
//                System.out.println("CHECK: brodcastcount" + realm.where(Poem.class).equalTo("status", FinalVariables.POEM_BRODCAST).count());
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

}
