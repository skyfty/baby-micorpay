package com.baby;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.baby.widget.CustomDialog;
import com.unionpay.cloudpos.DeviceException;
import com.unionpay.cloudpos.POSTerminal;
import com.unionpay.cloudpos.printer.Format;
import com.unionpay.cloudpos.printer.PrinterDevice;

import org.json.JSONException;
import org.json.JSONObject;

public class PrinterActivity extends AppCompatActivity implements CustomDialog.OnOperationListener  {

	private PrinterDevice printerDevice;
	private String printLog;
	private TextView logView;
	JSONObject market = null;
	CustomDialog dialog;
	boolean running = false;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		try {
			market = new JSONObject(extras.getString("market", ""));
		} catch (JSONException e) {
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
			finish();
		}
		setContentView(R.layout.activity_printer);
		printerDevice = (PrinterDevice) POSTerminal.getInstance(getApplicationContext()).getDevice("cloudpos.device.printer");
		logView = (TextView) findViewById(R.id.txt);

		dialog  = new CustomDialog(this);
		dialog.setOperationListener(this);
		onRightClick();
	}

	@Override
	public void onLeftClick() {
		finish();
	}

	@Override
	public void onRightClick() {
		dialog.hide();
		new Thread(new Runnable() {@Override public void run() {runPrint();}}).start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			if (printerDevice != null) {
				printerDevice.close();
			}
		} catch (DeviceException e) {
		}
	}

	private Handler handler = new Handler();

	private Runnable myRunnable = new Runnable() {
		public void run() {
			logView.setText(printLog);
		}
	};

	private void finishPrint(int code) {
		switch(code) {
			case -3: {
				dialog.setButtonsText(this.getString(R.string.back), this.getString(R.string.retry));
				dialog.setTitle(this.getString(R.string.failed));
				dialog.setMessage(this.getString(R.string.tip_print_ourpage));
				break;
			}
			case -1: {
				dialog.setButtonsText(this.getString(R.string.complete), this.getString(R.string.print_next));
				dialog.setTitle(this.getString(R.string.complete));
				dialog.setMessage(this.getString(R.string.tip_print_next));
				break;
			}
			default: {
				dialog.setButtonsText(this.getString(R.string.back), this.getString(R.string.retry));
				dialog.setTitle(this.getString(R.string.failed));
				dialog.setMessage(this.getString(R.string.tip_print_failed));
				break;
			}
		}
		dialog.show();
	}

	public void runPrint() {
		try {
			running = true;
			printLog = "正在打开打印机设备，请稍后...\n";
			handler.post(myRunnable);

			printerDevice.open();

			printLog += "打印机设备打开成功...\n";
			handler.post(myRunnable);

			if (printerDevice.queryStatus() == PrinterDevice.STATUS_OUT_OF_PAPER) {
				printLog += "打印机缺纸...\n";
				handler.post(myRunnable);
				runOnUiThread(new Runnable() {@Override public void run() {
					PrinterActivity.this.finishPrint(-3);

				}});
			} else if (printerDevice.queryStatus() == PrinterDevice.STATUS_PAPER_EXIST) {
				printLog += "打印机状态正常，开始打印...\n";
				handler.post(myRunnable);

				startPrint();

				printLog += "打印机完毕\n";
				handler.post(myRunnable);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						PrinterActivity.this.finishPrint(-1);

					}
				});
			}
			printerDevice.close();

		} catch (DeviceException e) {
			printLog += "打印机设备打开失败...\n";
			handler.post(myRunnable);
			runOnUiThread(new Runnable() {@Override public void run() {
				PrinterActivity.this.finishPrint(-2);
			}});
		} finally {
			running = false;
		}
	}

	private void startPrint() throws DeviceException {
		JSONObject service_product = market.optJSONObject("service_product");

//		format.setParameter("align", "left");
//		format.setParameter("size", "extra-small");
//		printerDevice.printText(format, "extra-small(特小)，银联POS签购单\n");
//		format.setParameter("size", "small");
//		printerDevice.printText(format, "small(小)，银联POS签购单\n");
//		format.setParameter("size", "medium");
//		printerDevice.printText(format, "medium(中)，银联POS签购单\n");
//		format.setParameter("size", "large");
//		printerDevice.printText(format, "large(大)，银联POS签购单\n");
//		format.setParameter("size", "extra-large");
//		printerDevice.printText(format, "extra-large(特大)，银联POS签购单\n");
//
//		format = new Format();
//
//		printerDevice.printText(format, "<BARCODE_CODABAR 条码上方>\n");
//		format.setParameter("HRI-location", "up-down");
//		printerDevice.printBarcode(format, printerDevice.BARCODE_UPC_A, "123456789012");
//
		Format format = new Format();
		printerDevice.printText("\n");

		format.setParameter("align", "center");
		format.setParameter("bold", "true");
		format.setParameter("size", "medium");
		printerDevice.printText(format, "服务订单收据");
		printerDevice.printText("\n");
		printerDevice.printText("\n");
		format.clear();

		format.setParameter("align", "left");
		format.setParameter("size", "small");
		printerDevice.printText(format, "终端：" + "sldkf" + "\n");

		if (market.has("idcode")) {
			printerDevice.printText(format, "订单编号：" + market.optString("idcode") + "\n");
		}

		if (service_product.has("name")) {
			printerDevice.printText(format, "服务人员姓名：" + service_product.optString("name") + "\n");
		}

		if (service_product.has("idcode")) {
			printerDevice.printText(format, "服务人员编号：" + service_product.optString("idcode") + "\n");
		}

		if (market.has("customer_name")) {
			printerDevice.printText(format, "客户姓名：" + market.optString("customer_name") + "\n");
		}

		if (market.has("customer_telephone")) {
			printerDevice.printText(format, "手机号码：" + market.optString("customer_telephone") + "\n");
		}

		if (market.has("pay_channel_name")) {
			printerDevice.printText(format, "交款方式：" + market.optString("pay_channel_name") + "\n");
		}
		if (market.has("market_info")) {
			printerDevice.printText(format, "交款项目：" + market.optString("market_info") + "\n");
		}
		if (market.has("market_pay_amount")) {
			printerDevice.printText(format, "交款金额：" + market.optString("market_pay_amount") + "\n");
		}
		if (market.has("market_pay_time")) {
			printerDevice.printText(format, "交款时间：" + market.optString("market_pay_time") + "\n");
		}
		printerDevice.printText(format, "备注：");
		printerDevice.printText("\n");
		printerDevice.printText("\n");
		printerDevice.printText("\n");
		printerDevice.printText("\n");
		printerDevice.printText("\n");
		printerDevice.printText("\n");
//		printerDevice.printText(format, "商户编号：123456789000010\n");
//		printerDevice.printText(format, "终端编号：12345610\n");
//		printerDevice.printText(format, "操作员号：01\n");
//		printerDevice.printText(format, "发卡行号：01020000\n");
//		printerDevice.printText(format, "发卡行：中国工商银行\n");
//		printerDevice.printText(format, "卡号：621795****0017\n");
//		printerDevice.printText(format, "收单行号：00072900\n");
//		printerDevice.printText(format, "收单行：未知机构\n");
//		printerDevice.printText(format, "交易类型：消费（SALE）\n");
//		printerDevice.printText(format, "有效期：1507  批次号：000111\n");
//		printerDevice.printText(format, "凭证号：001127\n");
//		printerDevice.printText(format, "参考号：103425000437\n");
//		printerDevice.printText(format, "日期时间：11/23 10:34:25\n");
//		printerDevice.printText(format, "金额（AMOUNT）：0.01元\n");
//		printerDevice.printText(format, "备注：");
//		printerDevice.printText("\n");
//		printerDevice.printText(format, "AID：A00000033310101");
//		printerDevice.printText(format, "ARQC：TC");
//		printerDevice.printText(format, "持卡人签名：");
//		printerDevice.printText(format, "CARD HOLDER SIGNATURE");
//		printerDevice.printText("\n");
//		printerDevice.printText("\n");
//		printerDevice.printText(format, "本人确认以上交易，同意将其写入本卡账户");
//		printerDevice.printText(format,
//			"I ACKNOWLEDGE SATISFACTORY RECEIPT OF RELATIVE GOODS/SERVICES");
//		printerDevice.printlnText("商户名称：研究院-何舟");
//		printerDevice.printlnText("商户编号：123456789000010");
//		printerDevice.printlnText("终端编号：12345610");
//		printerDevice.printlnText( "操作员号：01");
//		printerDevice.printlnText( "发卡行号：01020000");
//		printerDevice.printlnText( "发卡行：中国工商银行");
//		printerDevice.printlnText( "卡号：621795****0017");
//		printerDevice.printlnText( "收单行号：00072900");
//		printerDevice.printlnText( "收单行：未知机构");
//		printerDevice.printlnText( "交易类型：消费（SALE）");
//		printerDevice.printlnText( "有效期：1507  批次号：000111");
//		printerDevice.printlnText("凭证号：001127");
//		printerDevice.printlnText( "参考号：103425000437");
//		printerDevice.printlnText( "日期时间：11/23 10:34:25");
//		printerDevice.printlnText( "金额（AMOUNT）：0.01元");
//		printerDevice.printlnText( "备注：");
//		printerDevice.printlnText( "AID：A00000033310101");
//		printerDevice.printlnText( "ARQC：TC");
//		printerDevice.printlnText( "持卡人签名：");
//		printerDevice.printlnText("CARD HOLDER SIGNATURE");
//		printerDevice.printlnText("\n");
//		printerDevice.printlnText("本人确认以上交易，同意将其写入本卡账户");
//		printerDevice.printlnText(
//			"I ACKNOWLEDGE SATISFACTORY RECEIPT OF RELATIVE GOODS/SERVICES");

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK ) {
			if (!running) {
				finish();
			}
		}
		return false;
	}
}
