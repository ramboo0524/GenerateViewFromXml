package com.genview.demo;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

//自定义Dialog  
public class AnimDialog extends Dialog {

	private Window window = null;
	private View view = null ;
	private TranslateAnimation translateAnimation;
	private Context context ;

	public AnimDialog(Context context) {
		super(context);
		this.context = context ;
	}

	@Override
	public void setContentView(View view) {
		super.setContentView(view);
		
		this.view = view ;
		
		AnimationSet animationSet = new AnimationSet(true);
		translateAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		// 设置X轴的平移方式和值，设置平移到的X轴的平移方式和值，Y轴的平移方式和值，设置平移到的Y轴的平移方式和值
		translateAnimation.setDuration(1000);
		animationSet.setFillAfter(true);
		animationSet.setFillBefore(false);
		// 得到对话框
		window = getWindow();
		// 设置对话框背景为透明
		window.setBackgroundDrawableResource(R.color.color_invisiable);
		window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,  
				 WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		WindowManager.LayoutParams wl = window.getAttributes();
		// 根据x，y坐标设置窗口需要显示的位置
		// wl.x = x; //x小于0左移，大于0右移
		// wl.y = y; //y小于0上移，大于0下移
		// wl.alpha = 0.6f; //设置透明度
		// 获取屏幕宽、高用
		DisplayMetrics d = context.getResources().getDisplayMetrics();
		Configuration config = context.getResources().getConfiguration();
		wl.width = (d.widthPixels);
//		wl.height = (d.heightPixels);
		// 设置重力
		wl.gravity = Gravity.BOTTOM;
		window.setAttributes(wl);
		
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		view.startAnimation(translateAnimation);
		super.show();
	}
}