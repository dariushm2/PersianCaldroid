package com.dariushm2.PersianCaldroidSample;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dariushm2.PersianCaldroidSample.CaldroidFragment.CaldroidFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainFragment mainFragment = new MainFragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.tabFragment, mainFragment, "MainFragment").commit();

    }

    public void setFragmentCalendarView() {
        CaldroidFragment caldroidFragment = new CaldroidFragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.tabFragment, caldroidFragment, "CaldroidFragment")
                .addToBackStack("CaldroidFragment").commit();
    }
}
