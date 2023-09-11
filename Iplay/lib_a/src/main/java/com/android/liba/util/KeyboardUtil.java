package com.android.liba.util;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.android.liba.jk.OnListener;

public class KeyboardUtil {

    public static void setEditTextAction(final EditText editText, final OnListener<String> inputListener) {
        editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_GO) {
//                    hideSoftInput(editText.getContext(), editText);
                    if (inputListener != null) {
                        if (TextUtils.isEmpty(editText.getText()) || TextUtils.isEmpty(editText.getText().toString().trim())) {
                            inputListener.onListen("");
                        } else {
                            String textStr = editText.getText().toString().trim();
                            inputListener.onListen(textStr);
                        }
                    }
                }
                return false;
            }
        });
    }

    /**
     * 如果显示着就隐藏；如果没显示那就显示
     * 切换键盘状态
     */
    public static void toggleSoftInput(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 隐藏键盘
     */
    public static void hideSoftInput(Activity activity) {
        if (activity == null) return;
        View currentFocus = activity.getCurrentFocus();
        if (currentFocus == null) return;
        IBinder windowToken = currentFocus.getWindowToken();
        if (windowToken == null) return;
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 强制隐藏输入法
     *
     * @param view 接受输入的view
     */
    public static void focusHideSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 隐藏输入法
     */
    public static void hideSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 显示输入法
     */
    public static void showSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }
} 
