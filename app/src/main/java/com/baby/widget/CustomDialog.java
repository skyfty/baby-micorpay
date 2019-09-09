/**
 * probject:lvxin
 * @version 5.0
 * 
 * @author 3979434@qq.com
 */
package com.baby.widget;

import com.baby.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class CustomDialog extends Dialog implements android.view.View.OnClickListener {

	OnOperationListener operationListener;
	private View mDialogView;

	public CustomDialog(Context context) {
		super(context, R.style.custom_dialog);
		this.init(context);
	}

	private void init(Context context) {
		setContentView(R.layout.dialog_custom);
		mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
		findViewById(R.id.dialogLeftBtn).setOnClickListener(this);
		findViewById(R.id.dialogRightBtn).setOnClickListener(this);
		setCancelable(true);
		setCanceledOnTouchOutside(false);
	}

	public CustomDialog(Context context,OnOperationListener operationListener) {
		super(context, R.style.custom_dialog);
		this.init(context);
		this.operationListener = operationListener;
	}

	public void setOperationListener(OnOperationListener operationListener) {
		this.operationListener = operationListener;
	}

	public void setTitle(CharSequence title) {

		((TextView) findViewById(R.id.dialogTitle)).setText(title);
	}

	public void setTag(Object tag) {

		findViewById(R.id.dialogTitle).setTag(tag);
	}

	public Object getTag() {

		return findViewById(R.id.dialogTitle).getTag();
	}

	public void setMessage(CharSequence message) {
		((TextView) findViewById(R.id.dialogText)).setText(message);
	}

	public void setButtonsText(CharSequence left, CharSequence right) {

		((TextView) findViewById(R.id.dialogLeftBtn)).setText(left);
		((TextView) findViewById(R.id.dialogRightBtn)).setText(right);
	}

	public interface OnOperationListener {
		void onLeftClick();
		void onRightClick();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialogLeftBtn:
			operationListener.onLeftClick();
			break;

		case R.id.dialogRightBtn:
			operationListener.onRightClick();
			break;
		}
	}

	public void show() {
		super.show();
		mDialogView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dialog_in));
	}
}