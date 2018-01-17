package com.yuan.test.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.widget.ImageView;

import android.widget.ImageView.ScaleType;

public class MainActivity extends Activity {
	private ViewPager viewPager;
	private List<ImageView> imageViews;

	private int[] imageResId;
	private List<View> dots;

	private int currentItem = 5000;
	private MyAdapter pageAdapter;
	boolean nowAction = false;// 当前用户正在滑动视图

	// An ExecutorService that can schedule commands to run after a given delay,
	// or to execute periodically.
	private ScheduledExecutorService scheduledExecutorService;


	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			viewPager.setCurrentItem(currentItem);
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);


		//为pager准备资源 (留给adapter处理)
		imageResId = new int[] { R.drawable.a, R.drawable.b, R.drawable.c,
				R.drawable.d, R.drawable.e };    //page显示的图片

		imageViews = new ArrayList<ImageView>();  //page显示的imageView
		for (int i = 0; i < imageResId.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setImageResource(imageResId[i]);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageViews.add(imageView);
		}

		dots = new ArrayList<View>();  //所有指示点
		dots.add(findViewById(R.id.v_dot0));
		dots.add(findViewById(R.id.v_dot1));
		dots.add(findViewById(R.id.v_dot2));
		dots.add(findViewById(R.id.v_dot3));
		dots.add(findViewById(R.id.v_dot4));


		//为pager设置adapter和listener
		viewPager = (ViewPager) findViewById(R.id.vp);
		pageAdapter = new MyAdapter();
		viewPager.setCurrentItem(Integer.MAX_VALUE / 4);//当前显示的page
		viewPager.setAdapter(pageAdapter);

		viewPager.setOnPageChangeListener(new MyPageChangeListener());

	}

	@Override
	protected void onStart() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 3,
				TimeUnit.SECONDS);
		super.onStart();
	}
	@Override
	protected void onStop() {
		scheduledExecutorService.shutdown();
		super.onStop();
	}

	private class ScrollTask implements Runnable {  //定时轮询

		public void run() {
			synchronized (viewPager) {
				if (!nowAction) {
					System.out.println("currentItem: " + currentItem);
					currentItem = currentItem+1;
					handler.obtainMessage().sendToTarget();
				}
			}
		}
	}

	private class MyPageChangeListener implements OnPageChangeListener {

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {
			currentItem = position;
			changeDotsBg(currentItem % imageViews.size());
		}

		// 其中arg0这个参数
		// 有三种状态（0，1，2）。
		// arg0 == 1的时辰默示正在滑动，
		// arg0 == 2的时辰默示滑动完毕了，
		// arg0 == 0的时辰默示什么都没做。
		public void onPageScrollStateChanged(int arg0) {
			if (arg0 == 0) {
				nowAction = false;
			}
			if (arg0 == 1) {
				nowAction = true;
			}
			if (arg0 == 2) {
			}
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		private void changeDotsBg(int currentitem){
			for(int i = 0; i < dots.size();i++){
				dots.get(i).setBackgroundResource(R.drawable.dot_normal);
			}
			dots.get(currentitem).setBackgroundResource(R.drawable.dot_focused);
		}
	}

	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return Integer.MAX_VALUE / 2;
		}

		@Override
		public Object instantiateItem(View arg0, int position) {
			View view = null;
			if (position % imageViews.size() < 0) {
				view = imageViews.get(imageViews.size() + position);
			} else {
				view = imageViews.get(position % imageViews.size());
			}
			ViewParent vp = view.getParent();
			if (vp != null) {
				ViewGroup parent = (ViewGroup) vp;
				parent.removeView(view);
			}
			((ViewPager) arg0).addView(view);

			return view;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}
}