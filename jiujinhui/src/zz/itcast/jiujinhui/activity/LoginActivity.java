package zz.itcast.jiujinhui.activity;

import zz.itcast.jiujinhui.R;
import zz.itcast.jiujinhui.res.Constants;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendAuth;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


public class LoginActivity extends BaseActivity {

	
	@ViewInject(R.id.tv__title)
	private TextView tv__title;

	@ViewInject(R.id.btn_weixin)
	private RelativeLayout btn_weixin;

	@ViewInject(R.id.tv_back)
	private ImageView tv_back;
	private boolean isLogined;// �Ƿ��Ѿ���¼
    //手机号登录
	@ViewInject(R.id.phone_login)
	private TextView phone_login;
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		ViewUtils.inject(this);
		sp = getSharedPreferences("user", MODE_PRIVATE);
		tv__title.setText("登录");
		// 添加微信、微信朋友圈平台
		addWXPlatform();

	}

	// 判断用户是否安装微信客户端
	private boolean isWXAppInstalledAndSupported() {
		IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
		msgApi.registerApp("wxdb59e14854a747c8");

		boolean sIsWXAppInstalledAndSupported = msgApi.isWXAppInstalled()
				&& msgApi.isWXAppSupportAPI();

		return sIsWXAppInstalledAndSupported;
	}

	// 添加微信、微信朋友圈平台
	private void addWXPlatform() {
		// TODO Auto-generated method stub
		// 注意：在微信授权的时候，必须传递appSecret
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appId = "wxdb59e14854a747c8";
		String appSecret = "110c41c7c74c9074d8e41a0399466e10";
		

	}

	@Override
	public int getLayoutResID() {
		// TODO Auto-generated method stub
		return R.layout.activity_login;
	}

	private String ishappy;

	private SharedPreferences sp;

	private IWXAPI api;

	@Override
	public void initData() {
		// TODO Auto-generated method stub

		ishappy = getIntent().getStringExtra("happy");
   
		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);
		api.registerApp(Constants.APP_ID);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

		tv_back.setOnClickListener(this);
		btn_weixin.setOnClickListener(this);
		phone_login.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

	
		case R.id.tv_back:

			finish();

			break;
		// 微信登录
		case R.id.btn_weixin:
			if (isWXAppInstalledAndSupported() == false) {
				Toast.makeText(LoginActivity.this, "未安装微信客户端，请您先安装",
						Toast.LENGTH_SHORT).show();
			} else {
				//微信登录
				SendAuth.Req req=new SendAuth.Req();
				req.scope="snsapi_userinfo";
				req.state="none";
				api.sendReq(req);
				
			}

			break;
		case R.id.phone_login:
              Intent intent=new Intent(LoginActivity.this,PhoneNumberLoginActivity.class);
              startActivity(intent);
			

			break;

		default:
			break;
		}

	}



	

	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
