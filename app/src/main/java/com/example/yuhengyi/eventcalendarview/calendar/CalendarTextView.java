package com.example.yuhengyi.eventcalendarview.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yuhengyi.eventcalendarview.DensityUtil;
import com.example.yuhengyi.eventcalendarview.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yuhengyi on 2017/4/12.
 */

public class CalendarTextView extends FrameLayout {
    @BindView(R.id.calendar_item_iv)
    ImageView mCalendarItemIv;
    @BindView(R.id.calendar_item_tv)
    TextView mCalendarItemTv;
    @BindView(R.id.calendar_bottom_arrow)
    ImageView mCalendarBottomArrow;
    private View mRootView;

    public CalendarTextView(Context context) {
        super(context);
        init();
    }

    public CalendarTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CalendarTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.layout_calendar_textview, this);
        ButterKnife.bind(this, mRootView);
    }

    public ImageView getItemIv() {
        return mCalendarItemIv;
    }

    public TextView getItemTv() {
        return mCalendarItemTv;
    }

    public void setTextColor(int color) {
        mCalendarItemTv.setTextColor(color);
    }

    public void setTextSize(float size) {
        mCalendarItemTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, DensityUtil.px2sp(getContext(), size));
    }

    public void setText(String text) {
        mCalendarItemTv.setText(text);
    }

    public void setBackgound(int resId) {
        mCalendarItemTv.setBackgroundColor(resId);
    }

    public void setImageResource(int resId) {
        mCalendarItemIv.setImageResource(resId);
    }

    public void setArrowVisible(int visible) {
        mCalendarBottomArrow.setVisibility(visible);
    }
}
