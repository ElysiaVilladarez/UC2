package utot.utot.settings;

/**
 * Created by elysi on 12/28/2016.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;

import io.realm.Realm;
import io.realm.RealmResults;
import utot.utot.R;
import utot.utot.customobjects.Poem;
import utot.utot.customviews.ButtonPlus;
import utot.utot.customviews.TextViewPlus;
import utot.utot.helpers.BitmapMaker;
import utot.utot.helpers.CreateObjects;
import utot.utot.helpers.FinalVariables;
import utot.utot.poem.ClickPoem;


public class BrodcastAdapter extends RecyclerView.Adapter<BrodcastAdapter.ViewHolder> {

private RealmResults<Poem> poems;
private static Activity act;
private CallbackManager callbackManager;
private int status;

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextViewPlus date;

    public ViewHolder(View view) {
        super(view);

        date = (TextViewPlus) view.findViewById(R.id.date);
        view.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent clicked = new Intent(act, ClickBrodcast.class);
        clicked.putExtra("POEM_POS", getAdapterPosition());
        clicked.putExtra("STATUS", status);
        act.startActivity(clicked);
        act.finish();
    }
}


    public BrodcastAdapter(RealmResults<Poem> poems, Activity act, CallbackManager callbackManager, int status) {
        this.poems = poems;
        this.act = act;
        this.callbackManager = callbackManager;
        this.status = status;
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

        holder.date.setText(FinalVariables.brodcastDateFormat.format(poem.getDateAdded()));

    }

    @Override
    public int getItemCount() {
        return poems.size();
    }
}