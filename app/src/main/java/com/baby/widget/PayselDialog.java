/**
 * probject:lvxin
 * @version 5.0
 * 
 * @author 3979434@qq.com
 */
package com.baby.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.baby.R;

public class PayselDialog extends Dialog implements View.OnClickListener {

	OnOperationListener operationListener;
	private View mDialogView;

	public PayselDialog(Context context) {
		super(context, R.style.custom_dialog);
		setContentView(R.layout.dialog_paysel);
		mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
		findViewById(R.id.wxpay).setOnClickListener(this);
		findViewById(R.id.pwxpay).setOnClickListener(this);

		findViewById(R.id.alipay).setOnClickListener(this);
		findViewById(R.id.palipay).setOnClickListener(this);

		findViewById(R.id.baidupay).setOnClickListener(this);
		findViewById(R.id.pbaidupay).setOnClickListener(this);

		findViewById(R.id.tongyipay).setOnClickListener(this);
		findViewById(R.id.ptongyipay).setOnClickListener(this);
		findViewById(R.id.cardpay).setOnClickListener(this);
		findViewById(R.id.cancel).setOnClickListener(this);

		setCancelable(true);
		setCanceledOnTouchOutside(false);
	}

	public void setOperationListener(OnOperationListener operationListener) {
		this.operationListener = operationListener;
	}

	public interface OnOperationListener {
		void onSelected(int id);
		void onCancel();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.cancel) {
			operationListener.onCancel();
		} else {
			operationListener.onSelected(v.getId());
		}
	}

	public void show() {
		super.show();
		mDialogView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dialog_in));
	}
}