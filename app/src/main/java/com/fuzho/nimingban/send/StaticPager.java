package com.fuzho.nimingban.send;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fuzho.nimingban.send.fragment.Emotion;

/**
 * Created by fuzho on 2016/9/19.
 */
public class StaticPager extends FragmentStatePagerAdapter {

    public StaticPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1)
        return new Emotion();
        return new Fragment();
    }

    @Override
    public int getCount() {
        return 2;
    }
}
