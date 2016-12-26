package utot.utot.settings;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;

import io.realm.Realm;
import utot.utot.R;
import utot.utot.customobjects.Alarm;
import utot.utot.customobjects.Poem;
import utot.utot.helpers.FinalVariables;
import utot.utot.login.LoginActivity;

public class SettingsFragment extends Fragment {

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SettingsFragment() {
        // Required empty public constructor
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container,false);
        Glide.with(getActivity()).load(R.mipmap.utotlogo1).into((ImageView)rootView.findViewById(R.id.logo));

        rootView.findViewById(R.id.bRODcastButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        rootView.findViewById(R.id.syncButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        rootView.findViewById(R.id.howToButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        rootView.findViewById(R.id.logOutButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();

                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.delete(Poem.class);
                realm.delete(Alarm.class);
                realm.commitTransaction();

                SharedPreferences prefs = getActivity().getSharedPreferences(FinalVariables.PREFS_NAME, Context.MODE_PRIVATE);
                prefs.edit().putBoolean(FinalVariables.LOGGED_IN, false).apply();

                getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                getActivity().finish();
            }
        });
        return rootView;
    }

}
