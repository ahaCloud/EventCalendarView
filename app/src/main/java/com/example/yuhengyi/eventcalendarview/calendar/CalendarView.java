package com.example.yuhengyi.eventcalendarview.calendar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yuhengyi.eventcalendarview.DensityUtil;
import com.example.yuhengyi.eventcalendarview.R;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by yuhengyi on 2017/4/5.
 * 日历view
 */

public class CalendarView extends LinearLayout {
     /*  View */
    /**
     * 日历container
     */
    private LinearLayout mContainer;
    /**
     * 点击后出现的事件view
     */
//    private LinearLayout mEventView;

    /*  data  */
    private OnItemClickListener mOnItemClickListener;
    private Calendar mCalendar;
    private int mCurrentYear;
    private int mCurrentMonth;
    /**
     * 当月第一天是周几？
     */
    private int mFirstDayOfMonth;
    /**
     * 当月共有几天
     */
    private int mDaysOfCurrentMonth;
    private int mCellWidth;
    private int mCellHeight;
    private int mScreenWidth;
    private float mItemTextSize;
    /**
     * 上次点击的天的index
     */
    private int mLastClickIndex = -1;
    /**
     * 闰年、平年的每个月天数
     */
    private final int DAYS_OF_MONTH[][] = new int[][]{{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}, {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}};
    /**
     * 装每天的view 集合
     */
    private ArrayList<CalendarTextView> mDayViews = new ArrayList<>();

    /**
     * 添加event 列表
     */
    private LinearLayoutManager mRecyclerManager;
    private ItemAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ArrayList<String> mDataList = new ArrayList<>();

    private int mEventViewHeight;
    /**
     * 上一次eventView的index
     */
    private int mLastLine = -1;
    //    private LinkedList<RecyclerView> mEventViews = new LinkedList<>();
    private int mCommentPadding;
    private boolean mAnimatorExecute;

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    private void init(AttributeSet attrs) {

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;

        mRecyclerManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView = new RecyclerView(getContext());
        mRecyclerView.setLayoutManager(mRecyclerManager);
        mAdapter = new ItemAdapter(mDataList);
        mRecyclerView.setAdapter(mAdapter);
        mCommentPadding = DensityUtil.dp2px(getContext(), 10);
        mRecyclerView.setPadding(mCommentPadding, mCommentPadding, mCommentPadding, mCommentPadding);
        mRecyclerView.setBackgroundColor(getResources().getColor(R.color.gray_f5));

//        mEventView = new LinearLayout(getContext());
//        mEventView.setBackgroundColor(getResources().getColor(R.color.gray_f5));
        mCalendar = Calendar.getInstance();

        mDataList.add("金刚狼");
        mDataList.add("hello幽灵狙击手：就是我啊就是我");
        mDataList.add("hello幽灵狙击手：就是我啊就是我asdasdffasf");
        mDataList.add("hello幽灵狙击手：就是我啊就是我");
        mDataList.add("hello幽灵狙击手：就是我啊就是我");


        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarView);
        mCellWidth = DensityUtil.dp2px(getContext(), 42);

        mCellHeight = a.getDimensionPixelSize(R.styleable.CalendarView_itemHeight, DensityUtil.dp2px(getContext(), 56));

        mItemTextSize = a.getDimensionPixelSize(R.styleable.CalendarView_textSize, DensityUtil.dp2px(getContext(), 18));
        a.recycle();


        LayoutInflater.from(getContext()).inflate(R.layout.view_calendar, this);
        mContainer = (LinearLayout) findViewById(R.id.view_calendar_content);

    }


    /**
     * 创建view 并添加
     */
    private void initView() {
        mCurrentYear = mCalendar.get(Calendar.YEAR);
        mCurrentMonth = mCalendar.get(Calendar.MONTH);
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);
        mFirstDayOfMonth = mCalendar.get(Calendar.DAY_OF_WEEK);
        mDaysOfCurrentMonth = getDaysOfCurrentMonth();
        LinearLayout linearLayout = null;
        for (int i = 1 - (mFirstDayOfMonth - 1); i <= mDaysOfCurrentMonth; i++) {
            if (linearLayout == null) {
                linearLayout = initLineLayout();
            }
            if (linearLayout.getChildCount() == 7) {
                mContainer.addView(linearLayout);
                linearLayout = initLineLayout();
            }
            linearLayout.addView(getTextView(i));
            // 最后一行
            if (i == mDaysOfCurrentMonth)
                mContainer.addView(linearLayout);
        }
        //TODO  测试数据
        setEventData();
    }

    private LinearLayout initLineLayout() {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.topMargin = mCommentPadding;
        LinearLayout lineLayout = new LinearLayout(getContext());
        lineLayout.setLayoutParams(params);
        lineLayout.setPadding(mCommentPadding, 0, mCommentPadding, 0);
        lineLayout.setOrientation(LinearLayout.HORIZONTAL);
        return lineLayout;
    }

    private CalendarTextView getTextView(final int position) {
        final CalendarTextView itemView = new CalendarTextView(getContext());
        itemView.setTextColor(getResources().getColor(R.color.black));
        itemView.setTextSize(mItemTextSize);
        LayoutParams layoutParams = new LayoutParams(mCellWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.rightMargin = DensityUtil.dp2px(getContext(), 7.5f);
        itemView.setLayoutParams(layoutParams);
        if (position > 0) {
            itemView.setTag(position);
            itemView.setText(String.valueOf(position));
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null && !mAnimatorExecute) {
                        // TODO 如果该位置有数据才允许点击
                        itemView.setArrowVisible(View.VISIBLE);
                        hideOtherArrow(position);
                        addEventView((Integer) v.getTag());
                    }
                }
            });
            mDayViews.add(itemView);
        }
        return itemView;
    }

    /**
     * 当item 点击时添加的事件view
     *
     * @param position
     */
    private void addEventView(final int position) {
        // 到这个position 为止 一共有多少个view
        int viewsBeforePosition = (position + (mFirstDayOfMonth - 1));
        // 应该添加的位置
        final int index = viewsBeforePosition / 7 + (viewsBeforePosition % 7 == 0 ? 0 : 1);

        if (mLastLine == index) {

            if (position == mLastClickIndex) {
                if (mRecyclerView.getParent() != null) {
                    removeEventViewAnimation(mRecyclerView);
                    hideOtherArrow(-1);
                } else {

                    mContainer.addView(mRecyclerView, index);
                    addEventViewAnimation(mRecyclerView);
                }
            } else {
                //重新适配数据
                if (mRecyclerView.getParent() == null) {
                    mContainer.addView(mRecyclerView, index);
                    addEventViewAnimation(mRecyclerView);
                }

            }

        } else {
            if (mLastLine == -1) {
                // 第一次点击
                mContainer.addView(mRecyclerView, index);
                addEventViewAnimation(mRecyclerView);
            } else {
                if (mRecyclerView.getParent() != null) {
                    // 如果有打开的  先关闭 ,并且等关闭后再打开新的
                    removeEventViewAnimation(mRecyclerView);
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mContainer.addView(mRecyclerView, index);
                            addEventViewAnimation(mRecyclerView);
                        }
                    }, 530);
                } else {
                    // 没有打开的 则直接打开
                    mContainer.addView(mRecyclerView, index);
                    addEventViewAnimation(mRecyclerView);
                }
            }
        }
        mLastClickIndex = position;
        mLastLine = index;
    }

   /* private RecyclerView getCurrentVisibleRecyclerView(){
        for(RecyclerView recyclerView : mEventViews.values() ){
            if(recyclerView.getParent() !=null){
                return recyclerView;
            }
        }
        return null;
    }*/

    /**
     * 添加动画
     *
     * @param recyclerView
     */
    private void addEventViewAnimation(RecyclerView recyclerView) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.addUpdateListener(getUpdateAddListener(recyclerView));
        animator.addListener(getOpenEndListener());
        animator.setDuration(500);
        animator.start();
        mAnimatorExecute = true;
    }

    private ValueAnimator.AnimatorUpdateListener getUpdateAddListener(final RecyclerView recyclerView) {
        return new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float percent = (float) animation.getAnimatedValue();
                LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, ((int) (DensityUtil.dp2px(getContext(), 165) * percent)));
                recyclerView.setLayoutParams(params);
                recyclerView.setAlpha(percent);
            }
        };
    }

    private AnimatorListenerAdapter getOpenEndListener() {
        return new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mAnimatorExecute = false;
            }
        };
    }

    /**
     * 移除动画
     *
     * @param recyclerView
     */
    private void removeEventViewAnimation(RecyclerView recyclerView) {
        ValueAnimator animator = ValueAnimator.ofFloat(1, 0);
        animator.addUpdateListener(getUpdateRemoveListener(recyclerView));
        animator.addListener(getRemoveEndListener(recyclerView));
        animator.setDuration(500);
        animator.start();
        mAnimatorExecute = true;
    }

    private ValueAnimator.AnimatorUpdateListener getUpdateRemoveListener(final RecyclerView recyclerView) {
        return new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float percent = (float) animation.getAnimatedValue();
                LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, ((int) (DensityUtil.dp2px(getContext(), 165) * percent)));
                recyclerView.setLayoutParams(params);
                recyclerView.setAlpha(percent);
            }
        };
    }

    private AnimatorListenerAdapter getRemoveEndListener(final RecyclerView recyclerView) {
        return new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ((LinearLayout) (recyclerView.getParent())).removeView(recyclerView);
                mAnimatorExecute = false;
            }
        };
    }


   /* */

    /**
     * 每次点击不同行  都会有一个新的recyclerView
     *
     * @param position
     * @return
     *//*
    private RecyclerView getRecyclerView(int position, ArrayList<String> data) {


        mEventViews.add(recyclerView);
        if(mEventViews.size()>2)
            mEventViews.removeFirst();
        return recyclerView;
    }*/
    /*private int getLineNumber() {
        //第一天的位置 周日是1  周6 是 7
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);
        mFirstDayOfMonth = mCalendar.get(Calendar.DAY_OF_WEEK);
        mDaysOfCurrentMonth = getDaysOfCurrentMonth();
        // n 是除去第一行 剩余的天数
        int n = mDaysOfCurrentMonth - (8 - mFirstDayOfMonth);

        return 1 + n / 7 + (n % 7 == 0 ? 0 : 1);

    }*/
    private void hideOtherArrow(int position) {
        for (int i = 0; i < mDayViews.size(); i++) {
            if ((Integer) (mDayViews.get(i).getTag()) != position) {
                mDayViews.get(i).setArrowVisible(View.GONE);
            }
        }
    }

    /**
     * 计算这个月有多少天
     *
     * @return
     */
    private int getDaysOfCurrentMonth() {
        return DAYS_OF_MONTH[leapYear(mCurrentYear)][mCurrentMonth];
    }

    private static int leapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) ? 1 : 0;
    }

    /**
     * @param month 月份
     * @param year
     */
    public void setMonth(int month, int year) {
        mCalendar.set(Calendar.MONTH, month);

        if (year > 0) {
            mCalendar.set(Calendar.YEAR, year);
        }
        mContainer.removeAllViews();
        initView();

    }

    public View getViewForDay(int day) {
        return mDayViews.get(day);
    }

    public void setEventData() {
        //TODO 给每个View 加上图片   i = 日期-1 ，即1号的位置是0，31号的position是30
        //先填下假数据
        for (int i = 0; i < mDayViews.size(); i++) {
            mDayViews.get(i).setImageResource(R.mipmap.calendar_test_icon);
            mDayViews.get(i).setBackgound(getResources().getColor(R.color.black_60alpha));
            mDayViews.get(i).setTextColor(Color.WHITE);
        }
    }
    /**
     * 适配器
     */
    class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
        private ArrayList<String> list;

        public ItemAdapter(ArrayList<String> list) {
            this.list = list;
        }

        public void setList(ArrayList<String> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendar_list, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.movieIcon.setImageResource(R.mipmap.calendar_test);
            holder.movieName.setText(list.get(position));

            holder.movieIcon.setTag(position);
            holder.movieIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClickListener(v, (Integer) v.getTag());
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.item_calendar_movie_icon_iv)
            ImageView movieIcon;
            @BindView(R.id.item_calendar_movie_name_tv)
            TextView movieName;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
