package com.baby.widget;

import com.baby.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;


public class MessageDialog extends Dialog implements android.view.View.OnClickListener {

	private Context mContext;
	private EditText mContentEdit;
	private Button mOkBtn;

	public interface OnFinishClick {
		void onFinishListener(String content);
	}
	private OnFinishClick mOnFinishClick;

	public void setOnClearClickLister(OnFinishClick alertDo) {
		this.mOnFinishClick = alertDo;
	}
	public MessageDialog(Context context) {
		super(context);
	}
	
	public MessageDialog(Context context, OnFinishClick alertDo) {
		super(context);
		mContext = context;
		this.mOnFinishClick = alertDo;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_message);
		initComponent();
	}

	private void initComponent() {
		mOkBtn = (Button) findViewById(R.id.ok_btn);
		mOkBtn.setOnClickListener(this);
		mContentEdit = (EditText) findViewById(R.id.content);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ok_btn:
			if (mOnFinishClick != null) {
				mOnFinishClick.onFinishListener(mContentEdit.getText().toString());
			}
			hideKeyBoard();
			MessageDialog.this.dismiss();
			break;

		default:
			break;
		}
	}

	private void hideKeyBoard() {
		if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
			InputMethodManager manager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
			manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
}
