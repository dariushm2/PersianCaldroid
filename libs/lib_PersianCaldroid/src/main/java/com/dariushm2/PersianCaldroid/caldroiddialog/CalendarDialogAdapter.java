package com.dariushm2.PersianCaldroid.caldroiddialog;

import android.os.Bundle;

import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.dariushm2.PersianCaldroid.Constants;

public class CalendarDialogAdapter extends FragmentStatePagerAdapter {

    public CalendarDialogAdapter(FragmentManager fm) {
        super(fm);
    }

    private Fragment mCurrentFragment;


    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            mCurrentFragment = ((Fragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }

//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        Fragment fragment = (Fragment) super.instantiateItem(container, position);
//        mCurrentFragment = fragment;
//        return fragment;
//    }

    @Override
    public Fragment getItem(int position) {
        MonthDialog fragment = new MonthDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.OFFSET_ARGUMENT, position - Constants.MONTHS_LIMIT / 2);
        fragment.setArguments(bundle);
        return fragment;
    }



    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return Constants.MONTHS_LIMIT;
    }

}
