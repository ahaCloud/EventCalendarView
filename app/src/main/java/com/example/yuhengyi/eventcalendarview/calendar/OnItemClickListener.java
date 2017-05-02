package com.example.yuhengyi.eventcalendarview.calendar;

import android.view.View;

/**
 * Created by yuhengyi on 2017/4/5.
 */

public interface OnItemClickListener {
    /**
     *
     * @param v
     * @param position  position 从1 开始
     * {@link com.example.yuhengyi.eventcalendarview.calendar.CalendarView.ItemAdapter#onBindViewHolder(CalendarView.ItemAdapter.ViewHolder, int)} }
     */
    void onItemClickListener(View v, int position);
}
