package com.demo.shy.horizontalmore;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends Activity {

    private HorizontalScrollView horizontalScroll;
    private LinearLayout ll_horizontal_kill;
    private int llHeight;
    private LinearLayout linear;
    private boolean isFlag = true;
    private int width;
    private View child;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initData() {
        //给horizontalscroll添加item
        ll_horizontal_kill.removeAllViews();
        for (int i = 0; i < 5; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.scroll_item, null);
            ll_horizontal_kill.addView(view);
        }

        ll_horizontal_kill.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                llHeight = ll_horizontal_kill.getHeight();
                ll_horizontal_kill.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        //监听手势滑动
        horizontalScroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP: {
                        //水平滑动的距离。大于0表示从右往左滑动,小于0表示从左向右滑动
                        int scrollUP = v.getScrollX();
                        if (ll_horizontal_kill.getChildCount() > 5 && Math.abs(scrollUP) + horizontalScroll.getWidth() > horizontalScroll.getChildAt(0).getWidth() - width  && scrollUP > 0) {

                            Intent intent = new Intent(MainActivity.this, JumpActivity.class);
                            startActivity(intent);
                            int count = ll_horizontal_kill.getChildCount();
                            child = ll_horizontal_kill.getChildAt(count - 1);

                        }
                    }
                    break;
                    case MotionEvent.ACTION_MOVE: {
                        int scrollX = v.getScrollX();
                        if (Math.abs(scrollX) + horizontalScroll.getWidth() >= horizontalScroll.getChildAt(0).getWidth() && scrollX > 0) {
                            linear = new LinearLayout(MainActivity.this);
                            linear.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, llHeight));
                            linear.setGravity(Gravity.CENTER);
                            //添加箭头
                            linear.setOrientation(LinearLayout.HORIZONTAL);
                            ImageView imgArrow = new ImageView(MainActivity.this);
                            imgArrow.setImageResource(R.mipmap.arrow);
                            imgArrow.setPadding(10, 0, 10, 0);
                            linear.addView(imgArrow);
                            //添加文字
                            TextView textView = new TextView(MainActivity.this);
                            textView.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            textView.setEms(1);
                            textView.setText("释放查看更多");
                            textView.setTextColor(Color.GRAY);
                            linear.addView(textView);

                            //防止重复添加
                            if (isFlag) {
                                ll_horizontal_kill.addView(linear);
                                linear.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                                    @Override
                                    public void onGlobalLayout() {
                                        width = linear.getWidth();
                                        linear.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                    }
                                });
                                isFlag = false;
                            }

                        }
                    }
                    break;
                }
                return false;
            }
        });

    }

    private void initView() {
        horizontalScroll = (HorizontalScrollView) findViewById(R.id.scroll_kill);
        ll_horizontal_kill = (LinearLayout) findViewById(R.id.ll_horizontal_kill);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //界面重新可见时,移除加载更多的view
        if(null != child){
            ll_horizontal_kill.removeView(child);
            horizontalScroll.pageScroll(0);
            isFlag = true;

        }
    }
}
