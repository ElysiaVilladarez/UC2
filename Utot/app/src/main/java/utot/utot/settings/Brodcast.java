package utot.utot.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmResults;
import utot.utot.R;
import utot.utot.customobjects.Poem;
import utot.utot.helpers.FinalVariables;

public class Brodcast extends AppCompatActivity {
    private RecyclerView brodcastList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brodcast);

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

            BrodcastAdapter brodcastAdapter = new BrodcastAdapter(brodPoems, this);
            brodcastList.setHasFixedSize(true);
            brodcastList.setAdapter(brodcastAdapter);
            brodcastAdapter.notifyDataSetChanged();
        }
    }
}
