package utot.utot.settings;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;

import io.realm.Realm;
import utot.utot.R;
import utot.utot.asynctasks.BrodcastTask;
import utot.utot.asynctasks.SyncTask;
import utot.utot.asynctasks.SyncTask_2;
import utot.utot.customobjects.Alarm;
import utot.utot.customobjects.Poem;
import utot.utot.helpers.CheckInternet;
import utot.utot.helpers.Computations;
import utot.utot.helpers.FinalVariables;
import utot.utot.helpers.LoginCommon;
import utot.utot.login.InitialScreen;
import utot.utot.login.LoginActivity;

public class SettingsFragment extends Fragment {
private SharedPreferences prefs;
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
        Glide.with(getActivity()).load(R.mipmap.logo_white).into((ImageView)rootView.findViewById(R.id.logo));

        prefs = getActivity().getSharedPreferences(FinalVariables.PREFS_NAME, Context.MODE_PRIVATE);

        rootView.findViewById(R.id.bRODcastButton).setOnClickListener(LoginCommon.noFeatureAvailableClick(getActivity()));

        rootView.findViewById(R.id.syncButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    new SyncTask(getActivity().getBaseContext(), getActivity(),
                            prefs.getString(FinalVariables.EMAIL, "")).execute();

            }
        });

        rootView.findViewById(R.id.howToButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), HowToActivity.class));
            }
        });

        rootView.findViewById(R.id.logOutButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new android.app.AlertDialog.Builder(getActivity())
                        .setTitle("Syncing")
                        .setMessage("Do you want to sync your data first before logging out?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                    new SyncTask_2(getActivity().getBaseContext(), getActivity(),
                                            prefs.getString(FinalVariables.EMAIL, "")).execute();

                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                LoginManager.getInstance().logOut();

                                Computations.cancelAllAlarms(getActivity());

                                Realm realm = Realm.getDefaultInstance();
                                realm.beginTransaction();
                              realm.delete(Poem.class);
                               realm.delete(Alarm.class);
                                realm.commitTransaction();

                                realm.close();
                                prefs.edit().putBoolean(FinalVariables.LOGGED_IN, false).apply();
                                prefs.edit().putString(FinalVariables.EMAIL, "").apply();

                                getActivity().startActivity(new Intent(getActivity(), InitialScreen.class));
                                getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                                getActivity().finish();
                                dialog.dismiss();
                            }
                        })
                        .show();

            }
        });
        return rootView;
    }

}
