package com.gys.play.util.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gys.play.BR;
import com.gys.play.R;
import com.gys.play.adapter.base.Adapter;
import com.gys.play.entity.TextTimeItem;
import com.gys.play.entity.TextTimeItemClickListener;
import com.gys.play.util.DateFormatUtils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BottomTimeDialog extends BaseDialog {
    int year;
    int month;
    Listener listener;

    /**
     * 传递类型为 yyyy-MM 时间 2022-01
     */
    public BottomTimeDialog(@NonNull Context context, String time, Listener listener) {
        super(context);
        String[] split = time.split("-");
        this.year = Integer.parseInt(split[0]);
        this.month = Integer.parseInt(split[1]);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View layout = layoutInflater.inflate(R.layout.dialog_bottom_select_time, null);
        TextView yearText = layout.findViewById(R.id.year);

        layout.findViewById(R.id.left).setOnClickListener(v -> {
            yearText.setText(--year + "年");
        });
        layout.findViewById(R.id.right).setOnClickListener(v -> {
            Integer integer = Integer.valueOf(DateFormatUtils.long2Str(System.currentTimeMillis()));
            if (year < integer) {
                yearText.setText(++year + "年");
            }
        });
        TextTimeItemClickListener l = item -> {
            dismiss();
            int position = item.getPosition() + 1;
            if (position < 10) {
                listener.time(year + "-0" + position);
            } else {
                listener.time(year + "-" + position);
            }
        };
        ArrayList<TextTimeItem> list = new ArrayList();
        for (int i = 0; i < 12; i++) {
            list.add(new TextTimeItem(l, i, (i + 1) == month));
        }
        RecyclerView rv = layout.findViewById(R.id.rv);
        rv.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rv.setAdapter(new Adapter(BR.item, R.layout.item_time_text, list));
        layout.findViewById(R.id.back).setOnClickListener(view -> dismiss());
        ViewGroup.LayoutParams clp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(layout, clp);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(visibility -> {
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    //布局位于状态栏下方
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                    //全屏
                    View.SYSTEM_UI_FLAG_FULLSCREEN |
                    //隐藏导航栏
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            uiOptions |= 0x00001000;
            getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置弹窗在底部弹出
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
    }

    @Override
    protected int[] onSetupDialogFrameSize(int screenWidth, int screenHeight) {
        int[] size = new int[2];
        size[0] = screenWidth;
        size[1] = screenHeight;
        return size;
    }

    public interface Listener {
        void time(String time);
    }
}
