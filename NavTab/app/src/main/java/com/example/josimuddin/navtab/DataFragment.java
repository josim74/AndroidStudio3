package com.example.josimuddin.navtab;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by JosimUddin on 25/10/2017.
 */

public class DataFragment extends Fragment {
    View view;
    ViewPager viewPage;
    TabLayout tabLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.sample, container, false);
        viewPage = view.findViewById(R.id.view_pager);
        viewPage.setAdapter(new SliderAdapter(getChildFragmentManager()));
        tabLayout = view.findViewById(R.id.sliding_tabs);
        tabLayout.post(new Runnable(){
            @Override
            public void run(){
                tabLayout.setupWithViewPager(viewPage);
            }
        });
        return view;
    }
    private class SliderAdapter extends FragmentPagerAdapter {


        public SliderAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return 0;
        }
    }
}
