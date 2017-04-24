package utot.utot.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.CallbackManager;

import io.realm.Realm;
import io.realm.RealmResults;
import utot.utot.R;
import utot.utot.alarm.TabbedAlarm;
import utot.utot.asynctasks.BrodcastTask;
import utot.utot.customobjects.Poem;
import utot.utot.helpers.ActionBarMaker;
import utot.utot.helpers.FinalVariables;
import utot.utot.poem.PoemAdapter;

public class Brodcast extends AppCompatActivity {
    private RecyclerView brodcastList;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brodcast);

        ImageButton exit = (ImageButton)findViewById(R.id.exit);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMain = new Intent(Brodcast.this, TabbedAlarm.class);
                goToMain.putExtra("TABBED", 2);
                Brodcast.this.startActivity(goToMain);
                Brodcast.this.finish();
            }
        });

        brodcastList = (RecyclerView)findViewById(R.id.brodcastList);
        TextView noBrodcastText = (TextView) findViewById(R.id.noBrodcastText);

        Realm realm = Realm.getDefaultInstance();
        RealmResults<Poem> brodPoems = realm.where(Poem.class).equalTo("status", FinalVariables.POEM_BRODCAST).findAll();
        if(brodPoems.size()<=0){
            brodcastList.setVisibility(View.GONE);
            noBrodcastText.setVisibility(View.VISIBLE);
        } else{
            brodcastList.setVisibility(View.VISIBLE);
            noBrodcastText.setVisibility(View.GONE);

            LinearLayoutManager llm = new LinearLayoutManager(this);
            brodcastList.setLayoutManager(llm);

            callbackManager = CallbackManager.Factory.create();
            BrodcastAdapter brodcastAdapter = new BrodcastAdapter(brodPoems, this, callbackManager, FinalVariables.POEM_BRODCAST);
            brodcastList.setHasFixedSize(true);
            brodcastList.setAdapter(brodcastAdapter);
            brodcastAdapter.notifyDataSetChanged();
        }
        realm.close();
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onBackPressed(){
        Intent goToMain = new Intent(this, TabbedAlarm.class);
        goToMain.putExtra("TABBED", 2);
        Brodcast.this.startActivity(goToMain);
        Brodcast.this.finish();
    }
//    @Override
//    public void onDestroy(){
//        super.onDestroy();
//        SharedPreferences prefs = getSharedPreferences(FinalVariables.PREFS_NAME, Context.MODE_PRIVATE);
//        new BrodcastTask(this.getBaseContext(), this, prefs.getString(FinalVariables.EMAIL, ""), true).execute();
//    }
}
