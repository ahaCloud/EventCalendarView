package com.example.yuhengyi.eventcalendarview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.yuhengyi.eventcalendarview.calendar.CalendarView;
import com.example.yuhengyi.eventcalendarview.calendar.OnItemClickListener;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.calendar)
    CalendarView mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mCalendar = (CalendarView) findViewById(R.id.calendar);

        Calendar calendar = Calendar.getInstance();
        mCalendar.setMonth(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
        mCalendar.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {

            }
        });

    }
}
