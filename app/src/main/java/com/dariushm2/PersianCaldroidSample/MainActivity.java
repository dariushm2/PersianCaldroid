package com.dariushm2.PersianCaldroidSample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.dariushm2.PersianCaldroidSample.CaldroidFragment.CaldroidFragment;

import java.util.Calendar;
import java.util.Locale;

import calendar.CivilDate;
import calendar.DateConverter;
import calendar.PersianDate;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PersianDate persianDate = DateConverter.civilToPersian(new CivilDate(Calendar.getInstance(new Locale("fa"))));
        Log.e("Date", persianDate.toStringInPersian());

        CivilDate civilDate = DateConverter.persianToCivil(new PersianDate());

        Log.e("Gregorian Date", civilDate.toString());

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
