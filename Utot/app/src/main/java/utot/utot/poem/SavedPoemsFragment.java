package utot.utot.poem;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.realm.Realm;
import io.realm.RealmResults;
import utot.utot.R;
import utot.utot.customobjects.Poem;

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
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        poemList.setLayoutManager(llm);

        Realm realm = Realm.getDefaultInstance();
        RealmResults<Poem> poems = realm.where(Poem.class).findAll();

        poemList.setHasFixedSize(true);
        poemList.setAdapter(new PoemAdapter(poems, getActivity()));
        return rootView;
    }

}