package org.porks.arctouch.floripaomnibus.actdetails;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for the ViewPager
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> viewPageList = new ArrayList<>();
    private final List<String> viewPageTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    /**
     * Add a ViewPage and it's title
     *
     * @param fragment The fragment representing the ViewPage
     * @param title    The ViewPage's title
     */
    public void addFragment(Fragment fragment, String title) {
        this.viewPageList.add(fragment);
        this.viewPageTitleList.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return this.viewPageList.get(position);
    }

    @Override
    public int getCount() {
        return this.viewPageList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.viewPageTitleList.get(position);
    }
}
