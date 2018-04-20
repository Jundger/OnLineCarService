package com.jundger.carservice.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 14246 on 2017/12/9.
 */

public class ClearEditText extends AppCompatEditText implements TextWatcher, View.OnFocusChangeListener {

    /**
     * 左右两侧图片资源
     */
    private Drawable left, right;
    /**
     * 是否获取焦点，默认没有焦点
     */
    private boolean hasFocus = false;
    /**
     * 手指抬起时的X坐标
     */
    private int xUp = 0;

    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWedgits();
    }

    /**
     * 初始化各组件
     */
    private void initWedgits() {
        try {
            // 获取drawableLeft图片，如果在布局文件中没有定义drawableLeft属性，则此值为空
            left = getCompoundDrawables()[0];
            // 获取drawableRight图片，如果在布局文件中没有定义drawableRight属性，则此值为空
            right = getCompoundDrawables()[2];
            initDatas();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化数据
     */
    private void initDatas() {
        try {
            // 第一次显示，隐藏删除图标
            setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
            addListeners();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加事件监听
     */
    private void addListeners() {
        try {
            setOnFocusChangeListener(this);
            addTextChangedListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    // 获取点击时手指抬起的X坐标
                    xUp = (int) event.getX();
                    // 当点击的坐标到当前输入框右侧的距离小于等于getCompoundPaddingRight()的距离时，则认为是点击了删除图标
                    // getCompoundPaddingRight()的说明：Returns the right padding of the view, plus space for the right Drawable if any.
                    if ((getWidth() - xUp) <= getCompoundPaddingRight()) {
                        if (!TextUtils.isEmpty(getText().toString())) {
                            setText("");
                        }
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (hasFocus) {
            if (TextUtils.isEmpty(charSequence)) {
                // 如果为空，则不显示删除图标
                setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
            } else {
                // 如果非空，则要显示删除图标
                if (null == right) {
                    right = getCompoundDrawables()[2]; // 获取控件右边缘图片资源
                }
                // 设置左右边图片都显示
                setCompoundDrawablesWithIntrinsicBounds(left, null, right, null);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onFocusChange(View view, boolean b) {
        try {
            this.hasFocus = b;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (hasFocus) {
            if (null == right) {
                right = getCompoundDrawables()[2]; // 获取控件右边缘图片资源
            }
            if (getText().length() > 0) {
                // 设置左边图片显示
                setCompoundDrawablesWithIntrinsicBounds(left, null, right, null);
            }
        } else if (!hasFocus) {
            setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
        }
    }

//    protected void setClearIconVisible(boolean visible) {
//        Drawable right = visible ? right : null;   //高
//        setCompoundDrawables(getCompoundDrawables()[0],
//                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
//    }
}
