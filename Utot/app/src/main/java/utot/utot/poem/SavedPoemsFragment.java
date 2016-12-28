package utot.utot.poem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.CallbackManager;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import utot.utot.R;
import utot.utot.alarm.TabbedAlarm;
import utot.utot.customobjects.Poem;
import utot.utot.customobjects.PoemPicture;
import utot.utot.helpers.FinalVariables;

public class SavedPoemsFragment extends Fragment {

    public static SavedPoemsFragment newInstance() {
        SavedPoemsFragment fragment = new SavedPoemsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SavedPoemsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_poem, container, false);

        
        RecyclerView poemList = (RecyclerView)rootView.findViewById(R.id.poemList);
        TextView noPoemsText = (TextView) rootView.findViewById(R.id.noPoemsText);

        Realm realm = Realm.getDefaultInstance();
        RealmResults<Poem> poems = realm.where(Poem.class).equalTo("status", FinalVariables.POEM_SAVE).findAllSorted("dateAdded", Sort.ASCENDING);


        if(poems.size() <= 0){
            poemList.setVisibility(View.GONE);
            noPoemsText.setVisibility(View.VISIBLE);
        } else {

            poemList.setVisibility(View.VISIBLE);
            noPoemsText.setVisibility(View.GONE);

            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            poemList.setLayoutManager(llm);


            poemList.setHasFixedSize(true);
            poemList.setAdapter(new PoemAdapter(poems,getActivity(), TabbedAlarm.callbackManager, FinalVariables.POEM_SAVE));

        }

        return rootView;
    }



}