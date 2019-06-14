package com.dariushm2.PersianCaldroid.caldroidfragment;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dariushm2.PersianCaldroid.Constants;
import com.dariushm2.PersianCaldroid.R;
import com.dariushm2.PersianCaldroid.entity.DayEntity;
import com.dariushm2.PersianCaldroid.util.Utils;

import java.util.HashMap;
import java.util.List;

import calendar.PersianDate;

public class MonthFragmentAdapter extends RecyclerView.Adapter<MonthFragmentAdapter.ViewHolder> {
    private Context context;
    private MonthFragment monthFragment;
    private final int TYPE_HEADER = 0;
    private final int TYPE_DAY = 1;
    private List<DayEntity> days;
    private int selectedDay = -1;

    private Utils utils;

    private final int firstDayDayOfWeek;
    private final int totalDays;
    private Typeface typeface;
    private HashMap<PersianDate, Integer> backgroundForDatesMap = new HashMap<>();

    public MonthFragmentAdapter(Context context, MonthFragment monthFragment, List<DayEntity> days, Typeface typeface, HashMap<PersianDate, Integer> backgroundForDatesMap) {
        firstDayDayOfWeek = days.get(0).getDayOfWeek();
        totalDays = days.size();
        this.monthFragment = monthFragment;
        this.context = context;
        this.days = days;
        utils = Utils.getInstance(context);
        this.typeface = typeface;
        this.backgroundForDatesMap = backgroundForDatesMap;

    }


    public void clearSelectedDay() {
        selectedDay = -1;
        notifyDataSetChanged();
    }

    public void selectDay(int dayOfMonth) {
        selectedDay = dayOfMonth + 6 + firstDayDayOfWeek;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView num;
        View today;
        View selectDay;
        View event;

        ViewHolder(View itemView) {
            super(itemView);

            num = itemView.findViewById(R.id.num);
            today = itemView.findViewById(R.id.today);
            selectDay = itemView.findViewById(R.id.select_day);
            event = itemView.findViewById(R.id.event);

            num.setTypeface(typeface);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            position += 6 - (position % 7) * 2;
            if (totalDays < position - 6 - firstDayDayOfWeek) {
                return;
            }

            if (position - 7 - firstDayDayOfWeek >= 0) {
                monthFragment.onClickItem(days
                        .get(position - 7 - firstDayDayOfWeek)
                        .getPersianDate());

                selectedDay = position;
                notifyDataSetChanged();
            }
        }

        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition();
            position += 6 - (position % 7) * 2;
            if (totalDays < position - 6 - firstDayDayOfWeek) {
                return false;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                try {
                    monthFragment.onLongClickItem(days
                            .get(position - 7 - firstDayDayOfWeek)
                            .getPersianDate());
                } catch (Exception e) {
                    // Ignore it for now
                    // I guess it will occur on CyanogenMod phones
                    // where Google extra things is not installed
                }
            }
            return false;
        }
    }

    @Override
    public MonthFragmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_day, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MonthFragmentAdapter.ViewHolder holder, int position) {
        position += 6 - (position % 7) * 2;
        if (totalDays < position - 6 - firstDayDayOfWeek) {
            return;
        }
        if (!isPositionHeader(position)) {
            if (position - 7 - firstDayDayOfWeek >= 0) {
                holder.num.setText(days.get(position - 7 - days.get(0).getDayOfWeek()).getNum());
                holder.num.setVisibility(View.VISIBLE);

                holder.num.setTextSize(20);

                if (days.get(position - 7 - firstDayDayOfWeek).isHoliday()) {
                    holder.num.setTextColor(ContextCompat.getColor(context, R.color.light_text_holiday));
                } else {
                    //holder.num.setTextColor(ContextCompat.getColor(context, R.color.dark_text_day));
                }

//                if (days.get(position - 7 - firstDayDayOfWeek).isEvent()) {
//                    holder.event.setVisibility(View.VISIBLE);
//                    Log.e("PersianDate", days.get(position - 7 - firstDayDayOfWeek).getPersianDate().toString());
//                    Log.e("Map", backgroundForDatesMap.toString());
//                    if (backgroundForDatesMap.containsKey(days.get(position - 7 - firstDayDayOfWeek).getPersianDate()))
//                        Log.e("Map", backgroundForDatesMap.get(days.get(position - 7 - firstDayDayOfWeek).getPersianDate()).toString());
//                } else {
//                    holder.event.setVisibility(View.GONE);
//                }

                if (days.get(position - 7 - firstDayDayOfWeek).isToday()) {
                    //holder.today.setVisibility(View.VISIBLE);
                    holder.event.setVisibility(View.VISIBLE);
                } else {
                    //holder.today.setVisibility(View.GONE);
                    holder.event.setVisibility(View.GONE);
                }

                PersianDate persianDate = days.get(position - 7 - firstDayDayOfWeek).getPersianDate();
                if (backgroundForDatesMap.containsKey(persianDate)) {
                    holder.today.setVisibility(View.VISIBLE);
                    Drawable background = getDrawable(holder.today.getBackground(), backgroundForDatesMap.get(persianDate));
                    holder.today.setBackgroundDrawable(background);
                }

                if (position == selectedDay) {
                    holder.selectDay.setVisibility(View.VISIBLE);

                    if (days.get(position - 7 - firstDayDayOfWeek).isHoliday()) {
                        holder.num.setTextColor(ContextCompat.getColor(context, R.color.light_text_holiday));
                    } else {
                        //holder.num.setTextColor(ContextCompat.getColor(context, colorPrimary.resourceId));
                    }
                } else {
                    holder.selectDay.setVisibility(View.GONE);
                }

            } else {
                holder.today.setVisibility(View.GONE);
                holder.selectDay.setVisibility(View.GONE);
                holder.num.setVisibility(View.GONE);
                holder.event.setVisibility(View.GONE);
            }
            //utils.setFontAndShape(holder.num);
        } else {
            holder.num.setText(Constants.FIRST_CHAR_OF_DAYS_OF_WEEK_NAME[position]);
            holder.num.setTextColor(ContextCompat.getColor(context, R.color.light_text_day_name));
            holder.num.setTextSize(20);
            holder.today.setVisibility(View.GONE);
            holder.selectDay.setVisibility(View.GONE);
            holder.event.setVisibility(View.GONE);
            holder.num.setVisibility(View.VISIBLE);
            //utils.setFont(holder.num);
        }
    }

    private Drawable getDrawable(Drawable background, int color) {
        if (background instanceof ShapeDrawable) {
            background.mutate();
            ((ShapeDrawable)background).getPaint().setColor(ContextCompat.getColor(context, color));
        } else if (background instanceof GradientDrawable) {
            background.mutate();
            ((GradientDrawable)background).setStroke(3, ContextCompat.getColor(context, color));
        }
        return background;
    }
    @Override
    public int getItemCount() {
        return 7 * 7; // days of week * month view rows
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        } else {
            return TYPE_DAY;
        }
    }

    private boolean isPositionHeader(int position) {
        return position < 7;
    }
}