package cn.qbcbyb.library.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by 秋云 on 2014/7/24.
 */
public class FragmentViewPagerAdapter extends FragmentStatePagerAdapter {
    List<Class<? extends Fragment>> fragmentClassList;

    public FragmentViewPagerAdapter(FragmentManager fm, List<Class<? extends Fragment>> fragmentClassList) {
        super(fm);
        this.fragmentClassList = fragmentClassList;
    }

    @Override
    public Fragment getItem(int arg0) {
        try {
            return fragmentClassList.get(arg0).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getCount() {
        return fragmentClassList.size();
    }

}
