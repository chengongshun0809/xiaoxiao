package zz.itcast.jiujinhui.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.PublicKey;

import zz.itcast.jiujinhui.R;
import zz.itcast.jiujinhui.activity.DrinkRecordActivity;
import zz.itcast.jiujinhui.activity.LoginActivity;
import zz.itcast.jiujinhui.activity.MyTiXianActivity;
import zz.itcast.jiujinhui.activity.PerInfoActivity;
import zz.itcast.jiujinhui.activity.ReChargeActivity;
import zz.itcast.jiujinhui.activity.TiXianRecordActivity;
import zz.itcast.jiujinhui.activity.TradeRecordActivity;
import zz.itcast.jiujinhui.activity.ZongZiChanActivity;
import zz.itcast.jiujinhui.bean.UserBean;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.UrlConnectionDownloader;

public class personFragment extends BaseFragment {
	@ViewInject(R.id.tv_back)
	private ImageView tv_back;
	@ViewInject(R.id.tv__title)
	private TextView tv__title;
	@ViewInject(R.id.zongzichan)
	private LinearLayout zongzichan;
	@ViewInject(R.id.drink_record)
	private LinearLayout drink_record;
	@ViewInject(R.id.trade_record)
	private LinearLayout trade_record;
	@ViewInject(R.id.tixianRecord)
	private LinearLayout tixianRecord;
	@ViewInject(R.id.tixian)
	private Button tixian;
	@ViewInject(R.id.recharge)
	private Button recharge;
	@ViewInject(R.id.personInfo)
	private LinearLayout personInfo;
	@ViewInject(R.id.NickName)
	private TextView NickName;

	// 圆形图片
	@ViewInject(R.id.circleImabeView)
	private zz.itcast.jiujinhui.view.CircleImageView circleImabeView;

	private SharedPreferences sp;
	
	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub
		ViewUtils.inject(this, view);
		tv_back.setVisibility(view.GONE);
		tv__title.setText("个人中心");
		//微信头像
		sp = getActivity().getSharedPreferences("user", 0);
        String headimgurl = sp.getString("headimg", null);
		Picasso.with(getActivity())
				.load(headimgurl)
				.into(circleImabeView);
        //微信昵称
               	String nickNameString=sp.getString("nickname", null);	
		           NickName.setText(nickNameString);
	}
	

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getLayoutResID() {
		// TODO Auto-generated method stub
		return R.layout.frag_person;
	}

	@Override
	public void initListener() {
		zongzichan.setOnClickListener(this);
		drink_record.setOnClickListener(this);
		trade_record.setOnClickListener(this);
		tixianRecord.setOnClickListener(this);
		tixian.setOnClickListener(this);
		recharge.setOnClickListener(this);
		personInfo.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.zongzichan:// 总资产
			Intent intent0 = new Intent(getActivity(), ZongZiChanActivity.class);
			startActivity(intent0);
			break;
		case R.id.drink_record:// 酒币记录
			Intent intent1 = new Intent(getActivity(),
					DrinkRecordActivity.class);
			startActivity(intent1);
			break;
		case R.id.trade_record:// 交易记录
			Intent intent2 = new Intent(getActivity(),
					TradeRecordActivity.class);
			startActivity(intent2);

			break;
		case R.id.tixianRecord:// 提现记录
			Intent intent3 = new Intent(getActivity(),
					TiXianRecordActivity.class);
			startActivity(intent3);
			break;
		case R.id.tixian:// 点击提现
			Intent intent4 = new Intent(getActivity(), MyTiXianActivity.class);
			startActivity(intent4);
			break;

		case R.id.recharge:// 点击充值
			Intent intent5 = new Intent(getActivity(), ReChargeActivity.class);
			startActivity(intent5);
			break;
		case R.id.personInfo:// 进入个人信息页面

			/*
			 * Dialog dialog = new AlertDialog.Builder(getActivity())
			 * .setTitle("确认退出?") .setNegativeButton("确定", new
			 * DialogInterface.OnClickListener() {
			 * 
			 * @Override public void onClick(DialogInterface dialog, int which)
			 * { // TODO Auto-generated method stub
			 * sp.edit().putBoolean("isLogined", false) .commit();
			 * 
			 * Intent intent6 = new Intent(getActivity(), LoginActivity.class);
			 * intent6.putExtra("person", "person");
			 * getActivity().startActivityForResult( intent6, 1);
			 * 
			 * } }) .setPositiveButton("取消", new
			 * DialogInterface.OnClickListener() {
			 * 
			 * @Override public void onClick(DialogInterface dialog, int which)
			 * { // TODO Auto-generated method stub dialog.dismiss(); }
			 * }).create(); dialog.setCanceledOnTouchOutside(true);
			 * dialog.show();
			 */

			Intent intent = new Intent(getActivity(), PerInfoActivity.class);
			intent.putExtra("shun", "shun");
			startActivityForResult(intent, 0);

			break;
		default:
			break;
		}

	}

}
