package com.dariushm2.PersianCaldroidSample.CaldroidFragment;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.dariushm2.PersianCaldroidSample.MainActivity;
import com.dariushm2.PersianCaldroidSample.R;
import com.dariushm2.PersianCaldroid.caldroidfragment.PersianCaldroidFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import calendar.PersianDate;

/**
 * Created by DARIUSH on 2016-12-10.
 */


public class CaldroidListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;


    private ArrayList<Payment> payments;
    CaldroidFragment caldroidFragment;
    HashMap<PersianDate, Integer> backgroundForDatesMap = new HashMap<>();
    private Context mContext;
    private Locale locale = new Locale("fa");
    PersianCaldroidFragment persianCaldroidFragment;
    private PersianDate clickedDate;

    public CaldroidListAdapter(Context context, CaldroidFragment caldroidFragment,
                               ArrayList<Payment> payments, HashMap<PersianDate, Integer> backgroundForDatesMap) {
        this.payments = payments;
        this.mContext = context;
        this.caldroidFragment = caldroidFragment;
        this.backgroundForDatesMap = backgroundForDatesMap;
    }

    private Context getContext() {
        return mContext;
    }

    public void refreshCaldroid() {
        persianCaldroidFragment.refresh();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == TYPE_ITEM) {
            View view = inflater.inflate(R.layout.listview_row_calendar, parent, false);
            return new ViewItem(view);
        } else { //if (viewType == TYPE_HEADER) {
            View view = inflater.inflate(R.layout.fragment_caldroid, parent, false);
            return new ViewHeader(view);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Typeface customFont = ResourcesCompat.getFont(getContext(), R.font.font_family_yekan);
        if (holder instanceof ViewHeader) {

            if (persianCaldroidFragment == null) {

                persianCaldroidFragment = new PersianCaldroidFragment();
                persianCaldroidFragment.setTypeface(customFont);
                persianCaldroidFragment.setOnDateClickListener(new PersianCaldroidFragment.OnDateClickListener() {
                    @Override
                    public void onDateClick(PersianDate persianDate) {
                        payments.clear();
                        clickedDate = persianDate;
                        if (backgroundForDatesMap.containsKey(persianDate))
                            caldroidFragment.updateListView(persianDate);
                        //notifyDataSetChanged();
                    }
                });
                persianCaldroidFragment.setOnChangeMonthListener(new PersianCaldroidFragment.OnChangeMonthListener() {
                    @Override
                    public void onChangeMonth() {
                        payments.clear();
                        //notifyDataSetChanged();

                    }
                });
                persianCaldroidFragment.setBackgroundResourceForDates(backgroundForDatesMap);
                ((MainActivity) mContext).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(
                                R.id.persianCaldroid,
                                persianCaldroidFragment,
                                PersianCaldroidFragment.class.getName()
                        ).commit();
            }

        } else if (holder instanceof ViewItem) {
            ViewItem viewItem = (ViewItem) holder;

            viewItem.txtLoanType.setTypeface(customFont);
            viewItem.txtLoanName.setTypeface(customFont);
            viewItem.txtAmount.setTypeface(customFont);
            viewItem.txtPaymentNumber.setTypeface(customFont);

            final int pos = position - 1;

            viewItem.txtLoanType.setText(Character.toString(payments.get(pos).getType()));


            viewItem.txtPaymentNumber.setText(String.format(locale, "%d", payments.get(pos).getPaymentNumber()));
            viewItem.txtAmount.setText(String.format(locale, "%,d", payments.get(pos).getAmount()));

            viewItem.txtLoanName.setText(payments.get(pos).getReceiver());

            PersianDate today = new PersianDate();
            if (clickedDate.equals(today)) {
                viewItem.txtPaymentNumber.setBackgroundResource(R.drawable.duetoday);
                viewItem.llStatus.setBackgroundResource(R.color.dueToday);
            } else {
                if (clickedDate.after(today)) {
                    viewItem.txtPaymentNumber.setBackgroundResource(R.drawable.unpaid);
                    viewItem.llStatus.setBackgroundResource(R.color.unpaid);
                } else if (clickedDate.before(today)) {
                    viewItem.txtPaymentNumber.setBackgroundResource(R.drawable.suspend);
                    viewItem.llStatus.setBackgroundResource(R.color.suspend);
                }
            }
        }
    }


    @Override
    public int getItemCount() {
        return payments.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }


    private static class ViewItem extends RecyclerView.ViewHolder {
        TextView txtLoanType;
        TextView txtLoanName;
        TextView txtAmount;
        LinearLayout llStatus;
        TextView txtPaymentNumber;

        ViewItem(View itemView) {
            super(itemView);
            txtLoanType = itemView.findViewById(R.id.txtLoanType);
            txtLoanName = itemView.findViewById(R.id.txtLoanName);
            txtPaymentNumber = itemView.findViewById(R.id.txtPaymentNumber);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            llStatus = itemView.findViewById(R.id.llStatus);
        }
    }

    private static class ViewHeader extends RecyclerView.ViewHolder {

        ViewHeader(View itemView) {
            super(itemView);

        }
    }


}
