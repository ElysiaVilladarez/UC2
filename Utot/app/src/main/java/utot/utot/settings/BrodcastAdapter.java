package utot.utot.settings;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.realm.Realm;
import io.realm.RealmResults;
import utot.utot.R;
import utot.utot.alarm.EditingAlarmActivity;
import utot.utot.customobjects.Poem;

/**
 * Created by elysi on 12/21/2016.
 */

public class BrodcastAdapter extends RecyclerView.Adapter<BrodcastAdapter.ViewHolder> {

private RealmResults<Poem> poems;
private static Activity act;
private Realm realm;


public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public ViewHolder(View view) {
        super(view);

    }

    public void onClick(View view) {
        Intent edits = new Intent(act, EditingAlarmActivity.class);
        edits.putExtra("POS", getAdapterPosition());
        act.startActivity(edits);
        act.finish();
    }
}


    public BrodcastAdapter(RealmResults<Poem> poems, Activity act) {
        this.poems = poems;
        this.act = act;
    }

    @Override
    public BrodcastAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_brodcast, parent, false);

        return new BrodcastAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BrodcastAdapter.ViewHolder holderT, int position) {
        final BrodcastAdapter.ViewHolder holder = holderT;
        final Poem poem = poems.get(position);
        realm = Realm.getDefaultInstance();

    }

    @Override
    public int getItemCount() {
        return poems.size();
    }
}