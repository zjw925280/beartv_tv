package com.gys.play.util.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gys.play.BR;
import com.gys.play.R;
import com.gys.play.adapter.base.Adapter;
import com.gys.play.entity.TextItem;

import java.util.List;

public class BottomDialog extends BaseDialog {
    List<TextItem> list;

    public BottomDialog(@NonNull Context context, List<TextItem> list) {
        super(context);
        this.list = list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View layout = layoutInflater.inflate(R.layout.dialog_bottom, null);
        RecyclerView rv = layout.findViewById(R.id.rv);
        rv.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rv.setAdapter(new Adapter(BR.item, R.layout.item_text, list));
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
}
