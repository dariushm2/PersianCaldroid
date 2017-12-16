package com.dariushm2.PersianCaldroidSample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dariushm2.PersianCaldroid.caldroiddialog.PersianCaldroidDialog;
import com.dariushm2.PersianCaldroidSample.CaldroidFragment.CaldroidFragment;

import calendar.PersianDate;

/**
 * Created by dariushm2 on 12/15/17.
 */

public class MainFragment extends Fragment {


    private PersianDate pickedDate = null;
    private TextView txtPickedDate;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        RelativeLayout rlPickDate = view.findViewById(R.id.rlPickDate);
        txtPickedDate = view.findViewById(R.id.txtPickedDate);
        rlPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (pickedDate == null)
                    onDateClick();
                else
                    onDateClick(pickedDate);

            }
        });
        Button btnCalendarView = view.findViewById(R.id.btnCalendarView);
        btnCalendarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).setFragmentCalendarView();
            }
        });
        return view;
    }

    private void onDateClick(PersianDate persianDate) {
        PersianCaldroidDialog persianCaldroidDialog = new PersianCaldroidDialog()
                .setOnDateSetListener(new PersianCaldroidDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(PersianCaldroidDialog dialog, PersianDate date) {
                        pickedDate = date;
                        txtPickedDate.setText(date.toStringInPersian());
                        dialog.dismiss();
                    }
                });
        persianCaldroidDialog.setTypeface(ResourcesCompat.getFont(getContext(), R.font.font_family_yekan));
        persianCaldroidDialog.setSelectedDate(persianDate);
        persianCaldroidDialog.show(getActivity().getSupportFragmentManager(), PersianCaldroidDialog.class.getName());
    }

    private void onDateClick() {

        PersianCaldroidDialog persianCaldroidDialog = new PersianCaldroidDialog()
                .setOnDateSetListener(new PersianCaldroidDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(PersianCaldroidDialog dialog, PersianDate date) {
                        pickedDate = date;
                        txtPickedDate.setText(date.toStringInPersian());
                        dialog.dismiss();
                    }
                });
        persianCaldroidDialog.setTypeface(ResourcesCompat.getFont(getContext(), R.font.font_family_yekan));
        persianCaldroidDialog.show(getActivity().getSupportFragmentManager(), PersianCaldroidDialog.class.getName());

    }
}
