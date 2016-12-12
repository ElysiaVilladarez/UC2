package utot.utot.poem;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import io.realm.RealmResults;
import utot.utot.R;
import utot.utot.customobjects.Poem;

public class PoemAdapter extends RecyclerView.Adapter<PoemAdapter.ViewHolder> {
 
    private RealmResults<Poem> poems;
	private static Activity act;
	private static int pos;
 
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView poemView;
		public ImageView backgroundPic;
        public ViewHolder(View view) {
            super(view);
            poemView = (TextView) view.findViewById(R.id.poem);
			backgroundPic = (ImageView) view.findViewById(R.id.backgroundPic);
            
        }
		
		
    }
 
 
    public PoemAdapter(RealmResults<Poem> poems, Activity act) {
        this.poems = poems;
		this.act = act;
    }
 
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_poem, parent, false);
 
        return new ViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(ViewHolder holderT, int position) {
        pos = position;
		final ViewHolder holder = holderT;
		Poem poem = poems.get(pos);
		
		holder.poemView.setText(poem.getPoem());
	//	Picasso.with(act).load().into(holder.backgroundPic);
		
    }
 
    @Override
    public int getItemCount() {
        return poems.size();
    }
}