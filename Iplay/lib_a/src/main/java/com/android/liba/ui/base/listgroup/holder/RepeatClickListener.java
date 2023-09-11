package com.android.liba.ui.base.listgroup.holder;

import android.text.style.ClickableSpan;
import android.view.View;

public abstract class RepeatClickListener extends ClickableSpan implements View.OnClickListener {

    private final long delayTime;

    private long clickTime = 0L;

    public RepeatClickListener() {
        this(800);
    }

    public RepeatClickListener(long delayTime) {
        this.delayTime = delayTime;
    }

    private boolean isRepeatClicked() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - clickTime < delayTime) {
            return false;
        }
        clickTime = currentTimeMillis;
        return true;
    }

    @Override
    public void onClick(View v) {
        if (isRepeatClicked()) {
            notRepeatClick(v);
        } else {
            canRepeatClick(v);
        }
    }

    public abstract void notRepeatClick(View v);

    public void canRepeatClick(View v) {
    }
}
