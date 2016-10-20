package zz.itcast.jiujinhui.activity;

import net.tsz.afinal.FinalActivity;
import zz.itcast.jiujinhui.R;
import zz.itcast.jiujinhui.res.Constants;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;

public class PerInfoActivity extends BaseActivity {
	// 整个平台的Controller,负责管理整个SDK的配置、操作等处理
	private UMSocialService mController = UMServiceFactory
			.getUMSocialService(Constants.DESCRIPTOR);

	@ViewInject(R.id.tuichu)
	private RelativeLayout tuichu;
	private SharedPreferences sp;
	@ViewInject(R.id.tv_back)
	private ImageView tv_back;
	@ViewInject(R.id.tv__title)
	private TextView tv__title;

	@Override
	public int getLayoutResID() {
		// TODO Auto-generated method stub
		return R.layout.perinfo_activity;
	}

	private String isshun;

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		sp = getSharedPreferences("user", 0);
		isshun = getIntent().getStringExtra("shun");
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub
		tuichu.setOnClickListener(this);
		tv_back.setOnClickListener(this);

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		ViewUtils.inject(this);
		tv__title.setText("个人设置");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tuichu:
			Dialog dialog = new AlertDialog.Builder(this)
					.setTitle("确认退出?")
					.setNegativeButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									if ("shun".equals(isshun)) {
										setResult(200);
									}
									sp.edit().putBoolean("isLogined", false)
											.commit();

									finish();
									logout(SHARE_MEDIA.WEIXIN);// 注销微信授权

								}
							})
					.setPositiveButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							}).create();
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();

			break;
		case R.id.tv_back:
			finish();
			break;

		default:
			break;
		}

	}

	protected void logout(final SHARE_MEDIA platform) {
		// TODO Auto-generated method stub
		mController.deleteOauth(PerInfoActivity.this, platform,
				new SocializeClientListener() {

					@Override
					public void onStart() {

					}

					@Override
					public void onComplete(int status, SocializeEntity entity) {
						String showText = "解除" + platform.toString() + "平台授权成功";
						if (status != StatusCode.ST_CODE_SUCCESSED) {
							showText = "解除" + platform.toString() + "平台授权失败["
									+ status + "]";
						}
						Toast.makeText(PerInfoActivity.this, showText,
								Toast.LENGTH_SHORT).show();
					}
				});

	}

}
