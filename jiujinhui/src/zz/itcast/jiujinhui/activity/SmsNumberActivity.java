package zz.itcast.jiujinhui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import zz.itcast.jiujinhui.R;

public class SmsNumberActivity extends BaseActivity {
      
	@ViewInject(R.id.SmsSubmit)
	private RelativeLayout SmsSubmit;
	
	@Override
	public int getLayoutResID() {
		// TODO Auto-generated method stub
		return R.layout.smsnumberverification_activity;
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub
		SmsSubmit.setOnClickListener(this);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
              ViewUtils.inject(this);
	}
        @Override
        public void onClick(View v) {
        	// TODO Auto-generated method stub
        	super.onClick(v);
        	switch (v.getId()) {
			case R.id.SmsSubmit:
			String extra = getIntent().getStringExtra("sms");
			if (extra.equals("tixian")) {
				Intent intent=new Intent(SmsNumberActivity.this,MyTiXianActivity.class);
				startActivity(intent);
			}else if (extra.equals("recharge")) {
				Intent intent1=new Intent(SmsNumberActivity.this,ReChargeActivity.class);
				startActivity(intent1);
			}
				finish();
				break;

			default:
				break;
			}
        	
        	
        }
}
