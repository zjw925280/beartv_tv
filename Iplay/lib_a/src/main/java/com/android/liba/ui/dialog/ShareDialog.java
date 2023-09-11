package com.android.liba.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.liba.R;
import com.android.liba.ui.base.CommonSetData;
import com.android.liba.ui.base.listgroup.SingleTypeAdapter;
import com.android.liba.ui.base.listgroup.holder.ViewHolder;

import java.util.ArrayList;

public class ShareDialog extends BaseBottomDialog {

    private ShareDialogButtonListener listener;
    private Context mContext;
    private ArrayList<CommonSetData> commonSetDataArrayList;

    protected ShareDialog(Context context, ArrayList<CommonSetData> commonSetDataArrayList, ShareDialogButtonListener listener) {
        super(context);
        this.mContext = context;
        this.commonSetDataArrayList = commonSetDataArrayList;
        this.listener = listener;
    }

    @Override
    public View getContentView() {
        View view = getLayoutInflater().inflate(R.layout.dialog_share, null);
        RecyclerView rvShare = view.findViewById(R.id.rv_share);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext,2);
        rvShare.setLayoutManager(gridLayoutManager);
        SingleTypeAdapter<CommonSetData> singleTypeAdapter = new SingleTypeAdapter<CommonSetData>(mContext, R.layout.item_share,
                commonSetDataArrayList) {
            @Override
            protected void convert(ViewHolder holder, CommonSetData commonSetData, int position) {
                String content = commonSetData.getContent();
                holder.setText(R.id.tv_share, content);
                ImageView imgShareIcon = holder.getView(R.id.img_share_icon);
                imgShareIcon.setImageResource(commonSetData.getIcon());
                holder.itemView.setOnClickListener(v -> {
                    switch (content) {
                        case "微信":
                            if (listener != null) {
                                listener.onShareWx();
                                dismiss();
                            }
                            break;
                        case "朋友圈":
                            if (listener != null) {
                                listener.onSharePyq();
                                dismiss();
                            }
                            break;
                        case "qq好友":
                            if (listener != null) {
                                listener.onShareQq();
                                dismiss();
                            }
                            break;
                        case "qq空间":
                            if (listener != null) {
                                listener.onShareQqZone();
                                dismiss();
                            }
                            break;
                    }
                });
            }
        };
        rvShare.setAdapter(singleTypeAdapter);
        return view;
    }

    public static ShareDialog show(Context context, ArrayList<CommonSetData> commonSetDataArrayList, ShareDialogButtonListener listener) {
        ShareDialog dialog = new ShareDialog(context, commonSetDataArrayList, listener);
        dialog.show();
        return dialog;
    }
}
