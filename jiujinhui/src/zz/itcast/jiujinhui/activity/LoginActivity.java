package zz.itcast.jiujinhui.activity;

import java.util.Map;

import zz.itcast.jiujinhui.R;
import zz.itcast.jiujinhui.bean.UserBean;
import zz.itcast.jiujinhui.fragment.personFragment;
import zz.itcast.jiujinhui.res.Constants;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.weixin.controller.UMWXHandler;

public class LoginActivity extends BaseActivity {

	// 整个平台的Controller,负责管理整个SDK的配置、操作等处理
	private UMSocialService mController = UMServiceFactory
			.getUMSocialService(Constants.DESCRIPTOR);

	@ViewInject(R.id.tv__title)
	private TextView tv__title;

	@ViewInject(R.id.btn_weixin)
	private Button btn_weixin;

	@ViewInject(R.id.tv_back)
	private ImageView tv_back;
	private boolean isLogined;// �Ƿ��Ѿ���¼

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
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(LoginActivity.this, appId,
				appSecret);
		wxHandler.setRefreshTokenAvailable(true);
		wxHandler.addToSocialSDK();

	}

	@Override
	public int getLayoutResID() {
		// TODO Auto-generated method stub
		return R.layout.activity_login;
	}

	private String ishappy;

	private SharedPreferences sp;

	@Override
	public void initData() {
		// TODO Auto-generated method stub

		ishappy = getIntent().getStringExtra("happy");

	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

		tv_back.setOnClickListener(this);
		btn_weixin.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		/*
		 * case R.id.btn_regiest:// ע�� Intent intent1 = new Intent(this,
		 * RegisterActivity.class); startActivity(intent1); break;
		 */
		/*
		 * case R.id.btn_login:// �
		 * 
		 * if ("happy".equals(ishappy)) { setResult(200); }
		 * 
		 * sp.edit().putBoolean("isLogined", true).commit(); finish(); break;
		 */
		/*
		 * case R.id.forget:// ������� Intent intent3 = new Intent(this,
		 * ForgetPwdActivity.class); startActivity(intent3); break;
		 */
		case R.id.tv_back:

			finish();

			break;
		// 微信登录
		case R.id.btn_weixin:
			if (isWXAppInstalledAndSupported() == false) {
				Toast.makeText(LoginActivity.this, "未安装微信客户端，请您先安装",
						Toast.LENGTH_SHORT).show();
			} else {
				login(SHARE_MEDIA.WEIXIN);
			}

			break;

		default:
			break;
		}

	}

	/**
	 * 授权。如果授权成功，则获取用户信息
	 * 
	 * @param platform
	 */
	private void login(SHARE_MEDIA platform) {
		mController.doOauthVerify(LoginActivity.this, platform,
				new UMAuthListener() {

					@Override
					public void onStart(SHARE_MEDIA platform) {
						Toast.makeText(LoginActivity.this, "授权开始",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onError(SocializeException e,
							SHARE_MEDIA platform) {
						Toast.makeText(LoginActivity.this, "授权失败",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onComplete(Bundle value, SHARE_MEDIA platform) {
						// 获取uid
						String uid = value.getString("uid");
						if (!TextUtils.isEmpty(uid)) {
							// uid不为空，获取用户信息
							getUserInfo(platform);

							Toast.makeText(LoginActivity.this, "登录成功",
									Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(LoginActivity.this,
									MainActivity.class);
							startActivity(intent);
							sp.edit().putBoolean("isLogined", true).commit();
							finish();

						} else {
							Toast.makeText(LoginActivity.this, "授权失败...",
									Toast.LENGTH_LONG).show();
						}

					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
						Toast.makeText(LoginActivity.this, "授权取消",
								Toast.LENGTH_SHORT).show();

					}
				});

	}

	/**
	 * 获取用户信息
	 * 
	 * @param platform
	 */
	private void getUserInfo(SHARE_MEDIA platform) {
		mController.getPlatformInfo(LoginActivity.this, platform,
				new UMDataListener() {

					@Override
					public void onStart() {

					}

					@Override
					public void onComplete(int status, Map<String, Object> info) {
						// String showText = "";
						// if (status == StatusCode.ST_CODE_SUCCESSED) {
						// showText = "用户名：" +
						// info.get("screen_name").toString();
						// Log.d("#########", "##########" + info.toString());
						// } else {
						// showText = "获取用户信息失败";
						// }

						if (info != null) {
							Toast.makeText(LoginActivity.this, info.toString(),
									Toast.LENGTH_SHORT).show();
							//用户微信头像
							sp.edit().putString("headimg", (String) info.get("headimgurl")).commit();
						     //用户微信昵称
							sp.edit().putString("nickname", (String) info.get("nickname")).commit();
						
						}
					}
				});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
