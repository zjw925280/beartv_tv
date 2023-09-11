package com.android.liba.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.liba.R;
import com.android.liba.context.AppContext;
import com.android.liba.ui.base.listgroup.holder.RepeatClickListener;
import com.android.liba.util.AppUtil;

public class AgreementPrivacyDialog extends BaseDialog {

    private AgreementPrivacyListener listener;
    private Context mContext;
    private int titleColor;
    private int contextColor;
    private int spanColor;
    private int sureBg;

    private int llBg;

    private TextView tvTitle;
    private TextView tvContent;
    private TextView tvTip;

    protected AgreementPrivacyDialog(Context context, int titleColor, int contextColor, int spanColor, int sureBg, int llBg, AgreementPrivacyListener listener) {
        super(context, false);
        this.mContext = context;
        this.titleColor = titleColor;
        this.contextColor = contextColor;
        this.spanColor = spanColor;
        this.sureBg = sureBg;
        this.llBg = llBg;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @Override
    public View initContentView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.dialog_agreement_privacy, null);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setTextColor(titleColor);
        tvContent = view.findViewById(R.id.tvContent);
        tvTip = view.findViewById(R.id.tv_tip);
        CheckBox cbBox = view.findViewById(R.id.cb_box);
        tvContent.setTextColor(contextColor);
        RecyclerView rvPermission = view.findViewById(R.id.rvPermission);
        LinearLayout llBgshow = view.findViewById(R.id.llBgshow);
        llBgshow.setBackgroundResource(llBg);
        TextView tvSure = view.findViewById(R.id.tvSure);
        tvSure.setBackgroundResource(sureBg);
        handleText();
        if (listener != null) {
            listener.onContent(rvPermission);
        }
        view.findViewById(R.id.tvCancel).setOnClickListener(v -> {
            if (listener != null) {
                listener.onCancel();
            }
        });

        view.findViewById(R.id.tvSure).setOnClickListener(v -> {
            if (listener != null) {
                if (cbBox.isChecked()){
                    listener.onSure();
                    this.dismiss();
                }else {
                    AppContext.showToast("请先勾选隐私政策和用户协议");
                }
            }
        });
        return view;
    }

    private void handleText() {
        String welcome = mContext.getString(R.string.dialog_agreement_welcome) + AppUtil.getAppName(mContext);
        String one = mContext.getString(R.string.dialog_agreement_content_one);
        String two = mContext.getString(R.string.dialog_agreement_content_two);
        String three = mContext.getString(R.string.dialog_agreement_content_three);
        String four = mContext.getString(R.string.dialog_agreement_content_four);
        String five = mContext.getString(R.string.dialog_agreement_content_five);
        int welcomSize = welcome.length();
        int oneSize = welcomSize + one.length();
        int twoSize = oneSize + two.length();
        int threeSize = twoSize + three.length();
        int fourSize = threeSize + four.length();

        String selOne = "已阅读并同意";
        String selTwo = mContext.getString(R.string.dialog_agreement_content_two);
        String selThree = "和";
        String selFour = mContext.getString(R.string.dialog_agreement_content_four);
        int selOneSize = selOne.length();
        int selTwoSize = selOneSize + selTwo.length();
        int selThreeSize = selTwoSize + selThree.length();
        int selFourSize = selThreeSize + selFour.length();
        String selTip = new StringBuffer(selOne).append(selTwo).append(selThree).append(selFour).toString();
        SpannableStringBuilder tipBuilder = new SpannableStringBuilder(selTip);
        tipBuilder.setSpan(new RepeatClickListener() {
            @Override
            public void notRepeatClick(View v) {
                if (listener != null) {
                    listener.onAgreement();
                }
            }
        }, selOneSize, selTwoSize, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        tipBuilder.setSpan(new RepeatClickListener() {
            @Override
            public void notRepeatClick(View v) {
                if (listener != null) {
                    listener.onPrivacy();
                }
            }
        }, selThreeSize, selFourSize, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        tipBuilder.setSpan(new ForegroundColorSpan(spanColor), selOneSize, selTwoSize, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        tipBuilder.setSpan(new ForegroundColorSpan(spanColor), selThreeSize, selFourSize, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        tvTip.setMovementMethod(LinkMovementMethod.getInstance());
        tvTip.setText(tipBuilder);

        String agreement = new StringBuffer(welcome).append(one).append(two).append(three).append(four).append(five).toString();
        SpannableStringBuilder builder = new SpannableStringBuilder(agreement);
        builder.setSpan(new RepeatClickListener() {
            @Override
            public void notRepeatClick(View v) {
                if (listener != null) {
                    listener.onAgreement();
                }
            }
        }, oneSize, twoSize, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        builder.setSpan(new RepeatClickListener() {
            @Override
            public void notRepeatClick(View v) {
                if (listener != null) {
                    listener.onPrivacy();
                }
            }
        }, threeSize, fourSize, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        builder.setSpan(new ForegroundColorSpan(spanColor), oneSize, twoSize, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(spanColor), threeSize, fourSize, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        tvContent.setMovementMethod(LinkMovementMethod.getInstance());
        tvContent.setText(builder);
    }

    public static AgreementPrivacyDialog show(Context context, int titleColor, int contextColor, int spanColor, int sureBg, int llBg, AgreementPrivacyListener listener) {
        AgreementPrivacyDialog dialog = new AgreementPrivacyDialog(context, titleColor, contextColor, spanColor, sureBg, llBg, listener);
        dialog.show();
        return dialog;
    }
}
