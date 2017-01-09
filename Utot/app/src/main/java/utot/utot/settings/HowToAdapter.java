package utot.utot.settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import utot.utot.R;

/**
 * Created by tonyv on 1/10/2017.
 */

public class HowToAdapter extends FragmentPagerAdapter {

    private static ArrayList<Integer> imgId;

    public HowToAdapter(FragmentManager fragmentManager, ArrayList<Integer> imgId) {
        super(fragmentManager);
        this.imgId = imgId;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return imgId.size();
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        return new HowToFragment().newInstance(position);
    }

    public static class HowToFragment extends Fragment {

        public static HowToFragment newInstance(int pos) {
            HowToFragment fragment = new HowToFragment();
            Bundle args = new Bundle();
            args.putInt("POS", pos);
//        args.putString(ARG_PARAM2, param2);
            fragment.setArguments(args);
            return fragment;
        }

        public HowToFragment() {
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
            View rootView = inflater.inflate(R.layout.fragment_how_to, container, false);
            int pos = getArguments().getInt("POS");

            Glide.with(getActivity()).load(imgId).into((ImageView)rootView.findViewById(R.id.instruction));
            
            return rootView;
        }

    }
}
