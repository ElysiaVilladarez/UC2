package utot.utot.poem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;

import io.realm.Realm;
import io.realm.RealmResults;
import utot.utot.R;
import utot.utot.customobjects.Poem;
import utot.utot.customviews.ButtonPlus;
import utot.utot.helpers.BitmapMaker;
import utot.utot.helpers.CreateObjects;
import utot.utot.helpers.FinalVariables;

public class PoemAdapter extends RecyclerView.Adapter<PoemAdapter.ViewHolder> {
 
    private RealmResults<Poem> poems;
	private static Activity act;
    private CallbackManager callbackManager;
 
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView poemView;
		public ImageView backgroundPic;
        public ImageButton deleteButton;
        public ButtonPlus shareButton;
        public LoginButton loginButton;
        public PercentRelativeLayout displayImg;
        public ViewHolder(View view) {
            super(view);

            poemView = (TextView) view.findViewById(R.id.poem);
			backgroundPic = (ImageView) view.findViewById(R.id.backgroundPic);
            deleteButton = (ImageButton) view.findViewById(R.id.deleteButton);
            shareButton = (ButtonPlus) view.findViewById(R.id.shareButton);
            displayImg = (PercentRelativeLayout) view.findViewById(R.id.displayImg);
            loginButton = (LoginButton) view.findViewById(R.id.login_fb);

            view.setOnClickListener(this);
            
        }


        @Override
        public void onClick(View v) {
            Intent clicked = new Intent(act, ClickPoem.class);
            clicked.putExtra("POEM_POS", getAdapterPosition());
            act.startActivity(clicked);
        }
    }
 
 
    public PoemAdapter(RealmResults<Poem> poems, Activity act, CallbackManager callbackManager) {
        this.poems = poems;
		this.act = act;
        this.callbackManager = callbackManager;
    }
 
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_poem, parent, false);
 
        return new ViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(ViewHolder holderT, int position) {
		final ViewHolder holder = holderT;
		final Poem poem = poems.get(position);
        final Realm realm = Realm.getDefaultInstance();
        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapMaker.fn_share(poem.getPrimaryKey(), callbackManager, act, holder.loginButton, holder.displayImg, Environment.getExternalStorageDirectory().getAbsolutePath());

            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View view) {
                                                       new AlertDialog.Builder(act)
                                                               .setTitle("Deleting Poem")
                                                               .setMessage("Do you really want to delete this poem from your saved poems?")
                                                               .setIcon(android.R.drawable.ic_dialog_alert)
                                                               .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                                                   public void onClick(DialogInterface dialog, int whichButton) {

                                                                       realm.beginTransaction();
                                                                       poem.setStatus(FinalVariables.POEM_NOT_SHOWN);
                                                                       realm.commitTransaction();

                                                                       PoemAdapter.this.notifyDataSetChanged();


                                                                   }
                                                               })
                                                               .setNegativeButton(android.R.string.no, null).show();
                                                   }
                                               });

        CreateObjects.setPoemDisplay(act, holder.poemView, holder.backgroundPic, poem);

    }
 
    @Override
    public int getItemCount() {
        return poems.size();
    }
}