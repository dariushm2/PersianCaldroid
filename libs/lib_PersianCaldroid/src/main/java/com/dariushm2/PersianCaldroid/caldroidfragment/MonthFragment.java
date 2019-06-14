package com.dariushm2.PersianCaldroid.caldroidfragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dariushm2.PersianCaldroid.Constants;
import com.dariushm2.PersianCaldroid.R;
import com.dariushm2.PersianCaldroid.entity.DayEntity;
import com.dariushm2.PersianCaldroid.util.Utils;

import java.util.HashMap;
import java.util.List;

import calendar.PersianDate;

public class MonthFragment extends Fragment {
    private Utils utils;
    private PersianCaldroidFragment persianCaldroidFragment;

    private PersianDate persianDate;
    private int offset;
    private MonthFragmentAdapter adapter;

    private Typeface typeface;
    private HashMap<PersianDate, Integer> backgroundForDatesMap = new HashMap<>();
    private Pair<PersianDate, Integer> backgroundResourceForDate;


    public void refresh() {
        adapter.notifyDataSetChanged();
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        utils = Utils.getInstance(getContext());

        persianCaldroidFragment = (PersianCaldroidFragment) getActivity()
                .getSupportFragmentManager()
                .findFragmentByTag(PersianCaldroidFragment.class.getName());


        View view = inflater.inflate(R.layout.fragment_month, container, false);
        this.typeface = persianCaldroidFragment.getTypeface();
        this.backgroundForDatesMap = persianCaldroidFragment.getBackgroundForDatesMap();

        offset = getArguments().getInt(Constants.OFFSET_ARGUMENT);
        List<DayEntity> days = utils.getDays(offset);


        persianDate = utils.getToday();
        int month = persianDate.getMonth() - offset;
        month -= 1;
        int year = persianDate.getYear();

        year = year + (month / 12);
        month = month % 12;
        if (month < 0) {
            year -= 1;
            month += 12;
        }

        month += 1;
        persianDate.setMonth(month);
        persianDate.setYear(year);
        persianDate.setDayOfMonth(1);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MonthFragmentAdapter(getContext(), this, days, typeface, backgroundForDatesMap);


        recyclerView.setAdapter(adapter);


        if (offset == 0 && persianCaldroidFragment.getViewPagerPosition() == offset) {
            //persianCaldroidFragment.selectDay(utils.getToday());
            updateTitle();
        }

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(setCurrentMonthReceiver,
                new IntentFilter(Constants.BROADCAST_INTENT_TO_MONTH_FRAGMENT));

        return view;
    }

    private BroadcastReceiver setCurrentMonthReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int value = intent.getExtras().getInt(Constants.BROADCAST_FIELD_TO_MONTH_FRAGMENT);
            if (value == offset) {
                updateTitle();

                int day = intent.getExtras().getInt(Constants.BROADCAST_FIELD_SELECT_DAY);
                if (day != -1) {
                    adapter.selectDay(day);
                }

                //utils.checkYearAndWarnIfNeeded(persianDate.getYear());

            } else if (value == Constants.BROADCAST_TO_MONTH_FRAGMENT_RESET_DAY) {
                adapter.clearSelectedDay();
            }
        }
    };

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(setCurrentMonthReceiver);
        super.onDestroy();
    }

    public void onClickItem(PersianDate day) {
        persianCaldroidFragment.selectDay(day);
    }

    public void onLongClickItem(PersianDate day) {
        //persianCaldroidFragment.addEventOnCalendar(day);
    }


    private void updateTitle() {
//        utils.setActivityTitleAndSubtitle(
//                getActivity(),
//                utils.getMonthName(persianDate),
//                utils.formatNumber(persianDate.getYear()));

        persianCaldroidFragment.setMonthYearTitle(utils.getMonthName(persianDate), persianDate.getYear());

    }

}
