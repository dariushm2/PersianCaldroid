package com.dariushm2.PersianCaldroid.caldroiddialog;

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

import java.util.List;

import calendar.PersianDate;

public class MonthDialog extends Fragment {
    private Utils utils;
    private PersianCaldroidDialog persianCaldroidDialog;
    private PersianDate persianDate;
    private int offset;
    private MonthDialogAdapter adapter;
    private PersianDate selectedDate;

    private Typeface typeface;
    private Pair<PersianDate, Integer> backgroundResourceForDate;


    public void refresh() {
        adapter.clearSelectedDay();
        //adapter.notifyDataSetChanged();
    }

    public PersianDate getSelectedDate(){
        return selectedDate;
    }

    public void clearSelectedDate(){
        selectedDate = null;
        persianCaldroidDialog.clearSelectedDate();

    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        utils = Utils.getInstance(getContext());

        persianCaldroidDialog = (PersianCaldroidDialog) getActivity()
                .getSupportFragmentManager()
                .findFragmentByTag(PersianCaldroidDialog.class.getName());

        View view;

        view = inflater.inflate(R.layout.dialog_month, container, false);
        this.typeface = persianCaldroidDialog.getTypeface();
        this.selectedDate = persianCaldroidDialog.getSelectedDate();
        this.backgroundResourceForDate = persianCaldroidDialog.getBackgroundResourceForDate();

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

        RecyclerView recyclerView = view.findViewById(R.id.RecyclerView);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MonthDialogAdapter(getContext(), this, days, typeface, backgroundResourceForDate);

        recyclerView.setAdapter(adapter);



        LocalBroadcastManager.getInstance(getContext()).registerReceiver(setCurrentMonthReceiver,
                new IntentFilter(Constants.BROADCAST_INTENT_TO_MONTH_DIALOG));

        return view;
    }

    private BroadcastReceiver setCurrentMonthReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int value = intent.getExtras().getInt(Constants.BROADCAST_FIELD_TO_MONTH_DIALOG);
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
        clearSelectedDate();
        persianCaldroidDialog.selectDay(day);
    }

    public void onLongClickItem(PersianDate day) {
        //persianCaldroidDialog.addEventOnCalendar(day);
    }


    private void updateTitle() {
//        utils.setActivityTitleAndSubtitle(
//                getActivity(),
//                utils.getMonthName(persianDate),
//                utils.formatNumber(persianDate.getYear()));

        persianCaldroidDialog.setMonthYearTitle(utils.getWeekDayName(persianDate), persianDate.getDayOfMonth(),
                utils.getMonthName(persianDate), persianDate.getYear());
    }

}
