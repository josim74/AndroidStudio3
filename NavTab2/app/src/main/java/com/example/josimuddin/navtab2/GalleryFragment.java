package com.example.josimuddin.navtab2;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment {

private AppBarLayout appBarLayout;
private TabLayout tabs;
private ViewPager viewPager;
    public GalleryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AppBarLayout appBarLayout;
        appBarLayout = getView().findViewById(R.id.appbar);
        int marginTop = appBarLayout.getHeight();

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_gallery, container, false);

        View contenedor = (View) container.getParent();
        appBarLayout = contenedor.findViewById(R.id.appbar);
        tabs = new TabLayout(getActivity());
        tabs.setTabTextColors(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"));
        tabs.setTabMode(0);
        appBarLayout.addView(tabs);

        viewPager = (ViewPager) view.findViewById(R.id.pager_gallery);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabs.setupWithViewPager(viewPager);
        
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        appBarLayout.removeView(tabs);
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter{
        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        String[] tituloTabs = {"ARTISTAS", "ALBUMS", "CANCIONES","IMPORTS", "SLIDESHOW", "TOOLS"};

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ArtistasFragment();
                case 1:
                    return new AlbumsFragment();
                case 2:
                    return new CancionsFragment();
                case 3:
                    return new ImportFragment();
                case 4:
                    return new SlideShowFragment();
                case 5:
                    return new ToolsFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tituloTabs[position];
        }
    }
}
