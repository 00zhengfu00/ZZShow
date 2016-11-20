package com.ys.yoosir.zzshow.mvp.ui.activities.base;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.view.WindowManager;

import com.ys.yoosir.zzshow.R;
import com.ys.yoosir.zzshow.mvp.presenter.interfaces.BasePresenter;
import com.ys.yoosir.zzshow.utils.SharedPreferencesUtil;

import butterknife.ButterKnife;

/**
 * 绑定 presenter
 * Created by Yoosir on 2016/10/19 0019.
 */
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity{

    protected  T mPresenter;

    private WindowManager mWindowManager = null;
    private View mNightView = null;
    private boolean mIsAddedView;


    public abstract int getLayoutId();

    public abstract void initVariables();

    public abstract void initViews();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutId = getLayoutId();

        setNightOrDayMode();
        setContentView(layoutId);
        initVariables();
        ButterKnife.bind(this);
        initViews();
        if(mPresenter != null){
            mPresenter.onCreate();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null){
            mPresenter.onDestroy();
        }
        removeNightModeMask();
    }

    public boolean ismIsAddedView() {
        return mIsAddedView;
    }

    public void setmIsAddedView(boolean mIsAddedView) {
        this.mIsAddedView = mIsAddedView;
    }

    public void changeToDay() {
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        mNightView.setBackgroundResource(android.R.color.transparent);
    }

    public void changeToNight() {
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        initNightView();
        mNightView.setBackgroundResource(R.color.night_mask);
    }

    /**
     *  设置主题样式，必须在setContentView 之前调用
     */
    private void setNightOrDayMode(){
        if(SharedPreferencesUtil.isNightMode()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            initNightView();
            mNightView.setBackgroundResource(R.color.night_mask);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    /**
     * 初始化 夜间模式 模板
     */
    private void initNightView(){
        //
        if(mIsAddedView){
            return;
        }
        WindowManager.LayoutParams nightViewParam = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT
        );
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mNightView = new View(this);
        mWindowManager.addView(mNightView,nightViewParam);
        mIsAddedView = true;
    }

    private void removeNightModeMask() {
        if (mIsAddedView) {
            // 移除夜间模式蒙板
            mWindowManager.removeViewImmediate(mNightView);
            mWindowManager = null;
            mNightView = null;
        }
    }
}
