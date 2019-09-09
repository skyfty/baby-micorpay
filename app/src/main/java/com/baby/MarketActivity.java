package com.baby;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.baby.entity.Login;
import com.baby.entity.ModCat;
import com.baby.util.BabyUtil;

public class MarketActivity extends WebCordovaActivity {

	protected String mAction;
	protected String market_id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Login login = BabyUtil.getLoginResult(this);
		if (login == null) {
			Toast.makeText(this, R.string.tip_not_login, Toast.LENGTH_LONG).show();
			MarketActivity.this.finish();
		}
		String uid = BabyUtil.getLoginId(this);
		Bundle extras = getIntent().getExtras();
		mAction = extras.getString("action", "index");
		switch(mAction) {
			case "index": {
				mUrl = "file:///android_asset/www/market_index.html?" + "login_role_id="+uid;
				break;
			}
			case "search_options": {
				mUrl = "file:///android_asset/www/market_search_options.html?" + "login_role_id="+uid;
				break;
			}
			case "add": {
				mUrl = "file:///android_asset/www/market_edit.html?login_role_id="+uid;
				break;
			}
            case "edit": {
                market_id = extras.getString("id");
                mUrl = "file:///android_asset/www/market_edit.html?id=" + market_id + "&login_role_id="+uid;
                break;
            }
			case "view": {
                market_id = extras.getString("id");
				mUrl = "file:///android_asset/www/market_view.html?id="+market_id + "&login_role_id="+uid;
				break;
			}
			case "pay": {
				market_id = extras.getString("id");
				mUrl = "file:///android_asset/www/market_pay.html?id=" + market_id + "&login_role_id="+uid;
				break;
			}
			case "product_add": {
				market_id = extras.getString("id");
				mUrl = "file:///android_asset/www/market_product_edit.html?id=" + market_id + "&login_role_id="+uid;
				break;
			}
			case "product_edit": {
				market_id = extras.getString("id");
				String market_product_id = extras.getString("market_product_id");
				mUrl = "file:///android_asset/www/market_product_edit.html?id=" + market_id +"&market_product_id="+ market_product_id + "&login_role_id="+uid;
				break;
			}
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	public Object onMessage(String id, Object data) {
		if (id.equals("onPageFinished")) {
			switch(mAction) {
				case "index": {
					calljs("deviceready");
					break;
				}
                case "view": {
                    Bundle extras = getIntent().getExtras();
                    calljs("init_market", extras.containsKey("market")?extras.getString("market"):"");
                    break;
                }
				case "add": {
					ModCat category = (ModCat)getIntent().getExtras().getBundle("category").getSerializable("mc");
					calljs("init_category",category.category_id,category.name);
					break;
				}

				default: {
					break;
				}
			}
		}
		return super.onMessage(id, data);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK ) {
			switch(mAction) {
				case "product_add":case "edit":case "product_edit":case "add": {
					calljs("on_back");
					break;
				}

				default: {
					finish();
					break;
				}
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
