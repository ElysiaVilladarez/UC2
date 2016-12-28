package utot.utot.login;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;

import io.realm.Realm;
import utot.utot.R;
import utot.utot.asynctasks.CheckingStart;
import utot.utot.customviews.ClearTextView;
import utot.utot.helpers.FinalVariables;
import utot.utot.register.RegisterActivity;

import static android.R.attr.permission;

public class InitialScreen extends AppCompatActivity {
    private boolean requestGranted;
    private boolean requestGranted2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_initial_screen);

        Realm.init(this.getApplicationContext());
        AccessToken facebookAccessToken = AccessToken.getCurrentAccessToken();
        boolean sessionExpired;
        if(facebookAccessToken != null){
            sessionExpired = facebookAccessToken.isExpired();
        }else{
            sessionExpired = true;
        }

        SharedPreferences prefs = getSharedPreferences(FinalVariables.PREFS_NAME, Context.MODE_PRIVATE);

        if(!sessionExpired || prefs.getBoolean(FinalVariables.LOGGED_IN, false)){
            InitialScreen.this.startActivity(new Intent(InitialScreen.this, LoginSplashScreen.class));
            InitialScreen.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }


        new CheckingStart(this).execute();


        Glide.with(this).load(R.mipmap.utotlogo1).into((ImageView) findViewById(R.id.utotLogo));

        ClearTextView register = (ClearTextView) findViewById(R.id.registerButton);
        ClearTextView login = (ClearTextView) findViewById(R.id.loginButton);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                checkDrawOverlayPermission();
            }
        } else{
            requestGranted = true;
        }
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Settings.canDrawOverlays(InitialScreen.this)) {
                        requestGranted = true;
                    } else {
                        requestGranted = false;
                        checkDrawOverlayPermission();
                    }

//                    if (permission != PackageManager.PERMISSION_GRANTED) {
//                        requestGranted2 = false;
//                        checkWriteExternalStoragePermission();
//                    } else requestGranted2 =true;
                }
                if (requestGranted) {
                    InitialScreen.this.startActivity(new Intent(InitialScreen.this, RegisterActivity.class));
                    InitialScreen.this.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                    finish();
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Settings.canDrawOverlays(InitialScreen.this)) {
                        requestGranted = true;
                    } else {
                        requestGranted = false;
                        checkDrawOverlayPermission();
                    }
//                    if (permission != PackageManager.PERMISSION_GRANTED) {
//                        requestGranted2 = false;
//                        checkWriteExternalStoragePermission();
//                    } else requestGranted2 =true;
                }
                if (requestGranted) {
                    InitialScreen.this.startActivity(new Intent(InitialScreen.this, LoginActivity.class));
                    finish();
                    InitialScreen.this.overridePendingTransition(R.anim.left_to_right_slide, R.anim.right_to_left_slide);
                }

            }
        });

    }

    public final static int REQUEST_CODE = 0;

    public void checkDrawOverlayPermission() {
        /** check if we already  have permission to draw over other apps */
        /** if not construct intent to request permission */
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                    /** request permission via start activity for result */
                    startActivityForResult(intent, REQUEST_CODE);

    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /** check if received result code
         is equal our requested code for draw permission  */
        if (requestCode == REQUEST_CODE) {
            /** if so check once again if we have permission */
            if (Settings.canDrawOverlays(this)) {
                requestGranted = true;
            }

        }
    }




//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_WRITE_STORAGE: {
//
//                if (grantResults.length == 0
//                        || grantResults[0] !=
//                        PackageManager.PERMISSION_GRANTED) {
//
//
//                    requestGranted2 = true;
//
//                } else {
//                    requestGranted2 = false;
//
//                }
//                return;
//            }
//        }
//    }
}
