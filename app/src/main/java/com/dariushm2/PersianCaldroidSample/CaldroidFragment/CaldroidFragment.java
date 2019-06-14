package com.dariushm2.PersianCaldroidSample.CaldroidFragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dariushm2.PersianCaldroidSample.R;

import java.util.ArrayList;
import java.util.HashMap;

import calendar.PersianDate;

/**
 * Created by dariushm2 on 12/15/17.
 */

public class CaldroidFragment extends Fragment {

    ArrayList<Payment> payments = new ArrayList<>();
    HashMap<PersianDate, Integer> datesColors;
    RecyclerView recyclerView;
    CaldroidListAdapter caldroidListAdapter;

    @Nullable
    @Override
    public View onCreateView(@Nullable  LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calendar_payment, container, false);


        setDatesColors();

        caldroidListAdapter =
                new CaldroidListAdapter(getContext(), this, payments, datesColors);


        recyclerView = view.findViewById(R.id.rvCalendarList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(caldroidListAdapter);

        return view;
    }


    public void updateListView(PersianDate persianDate) {
        setPaymentsList(persianDate);
    }


    private void setPaymentsList(PersianDate persianDate) {
        payments.clear();
        payments.add(new Payment('و', "بانک مهر اقتصاد", 3, 125000));
        payments.add(new Payment('و', "بانک ملی", 3, 200000));
        payments.add(new Payment('ک', "کرایه مسکن", 3, 350000));
        payments.add(new Payment('س', "سرویس مدریه", 3, 50000));
        payments.add(new Payment('ش', "شهریه دانشگاه", 3, 720000));
        caldroidListAdapter.notifyDataSetChanged();
    }

    private void setDatesColors() {
        datesColors = new HashMap<>();
        PersianDate d1 = new PersianDate();
        d1.addDays(-5);
        datesColors.put(d1, R.color.suspend);
        PersianDate d2 = new PersianDate();
        d2.addDays(-2);
        datesColors.put(d2, R.color.suspend);
        PersianDate d3 = new PersianDate();
        //d3.addDays(3);
        datesColors.put(d3, R.color.dueToday);
        PersianDate d4 = new PersianDate();
        d4.addDays(3);
        datesColors.put(d4, R.color.unpaid);
        PersianDate d5 = new PersianDate();
        d5.addDays(6);
        datesColors.put(d5, R.color.unpaid);

    }


}
