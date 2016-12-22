package utot.utot.helpers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;

import utot.utot.R;
import utot.utot.alarm.TabbedAlarm;

/**
 * Created by elysi on 12/20/2016.
 */

public class BitmapMaker {
    public static ShareDialog shareDialog;
    public static SharePhotoContent content;


    public static void saveBitmap(Bitmap bitmap, String filename) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root, FinalVariables.FOLDER_NAME);
        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        File file = new File (myDir, filename + ".jpg");
        if (!file.exists ()) {
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }

    public static void fn_share(String path, CallbackManager callbackManager, Activity activity, LoginButton loginButton) {
        final Activity act = activity;
        FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Intent goToMain = new Intent(act, TabbedAlarm.class);
                goToMain.putExtra(FinalVariables.ACTION_DONE, FinalVariables.POEM_SHARE);
                act.startActivity(goToMain);
                act.overridePendingTransition(R.anim.left_to_right_slide, R.anim.right_to_left_slide);
                act.finish();
            }

            @Override
            public void onCancel() {
                Toast.makeText(act, "Sharing attempt canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(act, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println("CHECK: "+ error.getMessage());

            }
        };

        File file = new File(Environment.getExternalStorageDirectory().toString()+ FinalVariables.FOLDER_NAME, path+".jpg");

        Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(bmp)
                .build();
        content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();


        shareDialog = new ShareDialog(act);
        shareDialog.registerCallback(callbackManager, shareCallback);
        if(AccessToken.getCurrentAccessToken() == null){
            System.out.println("CHECK: LOG IN NEEDED");

            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    //              "User ID: "
//                        + loginResult.getAccessToken().getUserId()
//                        + "\n" +
//                        "Auth Token: "
//                        + loginResult.getAccessToken().getToken()
                    if(!AccessToken.getCurrentAccessToken().getPermissions().contains("publish_actions")){
                        LoginManager.getInstance().logInWithPublishPermissions(act,
                                Arrays.asList("publish_actions"));
                    }

                    checkFBApp(act);

                }

                @Override
                public void onCancel() {
                    Toast.makeText(act, "Login attempt cancelled", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(FacebookException e) {
                    Toast.makeText(act, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();

                }
            });

            loginButton.performClick();

        } else{
            checkFBApp(act);
        }



    }


    public static void checkFBApp(Activity act){
        if(ShareDialog.canShow(SharePhotoContent.class)) {
            System.out.println("CHECK: SHAREDIALOG");
            shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
        } else{
            new android.app.AlertDialog.Builder(act)
                    .setTitle("Facebook Application Required")
                    .setMessage("To properly share this post, you must have the Facebook Application installed in your device.")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        }
    }


}
