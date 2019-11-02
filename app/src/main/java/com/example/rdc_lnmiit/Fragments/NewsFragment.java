package com.example.rdc_lnmiit.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rdc_lnmiit.Fragments.NewsCategoryFragments.BusinessNewsFragment;
import com.example.rdc_lnmiit.Fragments.NewsCategoryFragments.EntertainmentNewsFragment;
import com.example.rdc_lnmiit.Fragments.NewsCategoryFragments.GeneralNewsFragment;
import com.example.rdc_lnmiit.Fragments.NewsCategoryFragments.HealthNewsFragment;
import com.example.rdc_lnmiit.Fragments.NewsCategoryFragments.ScienceNewsFragment;
import com.example.rdc_lnmiit.Fragments.NewsCategoryFragments.SportsNewsFragment;
import com.example.rdc_lnmiit.Fragments.NewsCategoryFragments.TechnologyNewsFragment;
import com.example.rdc_lnmiit.MainActivity;
import com.example.rdc_lnmiit.R;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import static android.content.Context.MODE_PRIVATE;

public class NewsFragment extends Fragment {

    ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.toolbar_title.setText("India News");

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        SmartTabLayout viewPagerTab = (SmartTabLayout) view.findViewById(R.id.viewpagertab);

        FragmentPagerItems pages = FragmentPagerItems.with(getContext())
                .add("General", GeneralNewsFragment.class)
                .add("Technology", TechnologyNewsFragment.class)
                .add("Entertainment", EntertainmentNewsFragment.class)
                .add("Health", HealthNewsFragment.class)
                .add("Science", ScienceNewsFragment.class)
                .add("Sports", SportsNewsFragment.class)
                .add("Business", BusinessNewsFragment.class)
                .create();
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                this.getChildFragmentManager(), pages);

        viewPager.setAdapter(adapter);
        viewPagerTab.setViewPager(viewPager);

        Boolean firstLoad = getActivity().getSharedPreferences("PREFERENCE1", MODE_PRIVATE)
                .getBoolean("firstLoad", true);

        if (firstLoad) {

            TapTargetView.showFor(getActivity(), TapTarget.forView(viewPagerTab, "Select Categories", "Click to view full coverarge and swipe down to refresh")
                            .cancelable(true)
                            .tintTarget(true),
                    new TapTargetView.Listener() {
                        @Override
                        public void onTargetClick(TapTargetView view) {
                            super.onTargetClick(view);
                        }
                    });

            getActivity().getSharedPreferences("PREFERENCE1", MODE_PRIVATE).edit().putBoolean("firstLoad", false).commit();

        }

        return view;
    }
}
