package utot.utot.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import utot.utot.R;
import utot.utot.customviews.TextViewPlus;

/**
 * Created by elysi on 12/28/2016.
 */

public class ActionBarMaker {
    private static TextViewPlus titleText;
    public static void makeActionBar(final AppCompatActivity act, String title, View.OnClickListener click){
        Toolbar toolbar = (Toolbar)act.findViewById(R.id.my_toolbar);
        act.setSupportActionBar(toolbar);

        titleText = (TextViewPlus)toolbar.findViewById(R.id.actTitle);
        titleText.setText(title);
        ImageButton exit = (ImageButton)toolbar.findViewById(R.id.exit);
        if(click!=null){
            exit.setVisibility(View.VISIBLE);
            exit.setOnClickListener(click);
        } else{
            exit.setVisibility(View.GONE);
        }

    }

    public static void setTitleText(String title){
        titleText.setText(title);
    }

}
