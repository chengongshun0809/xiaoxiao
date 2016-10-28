package zz.itcast.jiujinhui.fragment;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import zz.itcast.jiujinhui.R;
import zz.itcast.jiujinhui.activity.KnowDetailActivity;
import zz.itcast.jiujinhui.activity.TradeServiceActivity;
import zz.itcast.jiujinhui.bean.Dealgoods;
import zz.itcast.jiujinhui.res.NetUtils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.StaticLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;

import com.lidroid.xutils.view.annotation.ViewInject;

@SuppressLint("ResourceAsColor")
public class TradeFragment extends BaseFragment {

	@ViewInject(R.id.tv_back)
	private ImageView tv_back;
	@ViewInject(R.id.tv__title)
	private TextView tv__title;

	@ViewInject(R.id.viewPager_menu)
	// 主页轮播图
	private zz.itcast.jiujinhui.view.AbSlidingPlayView viewPager;

	@ViewInject(R.id.ll_content)
	private LinearLayout ll_content;
	// 首页轮播的界面
	private List<ImageView> imageList;

	private final int[] imageIds = { R.drawable.a, R.drawable.b, R.drawable.c, };
	private TextView tv_rate;
	private TextView tv_name;
	private TextView tv_dealcode;
	private TextView tv_stock;
	private TextView tv_dealterm;
	private TextView btn_name;
	private TextView tv_lirengou;
	private TextView tv_tian;
	private SharedPreferences sp;

	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub
		ViewUtils.inject(this, view);
		tv_back.setVisibility(view.GONE);
		tv__title.setText("天天涨钱");
		// 设置播放方式为来回播放
		viewPager.setPlayType(1);
		// 设置播放间隔时间
		viewPager.setSleepTime(4000);
		initViewPager();
		sp = getActivity().getSharedPreferences("user", 0);

	}

	Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				UpdateUI();
				break;

			default:
				break;
			}

		};

	};
	private RelativeLayout btn_public;
	private TextView tv_jin;
	private TextView tv_deaTextView;
	private TextView tv_day;

	private void initViewPager() {
		// TODO Auto-generated method stub

		if (imageList != null) {
			imageList.clear();
			imageList = null;
		}
		imageList = new ArrayList<ImageView>();
		for (int i = 0; i < imageIds.length; i++) {

			ImageView image = new ImageView(getActivity());
			image.setBackgroundResource(imageIds[i]);

			imageList.add(image);
		}
		viewPager.addViews(imageList);
		// 开始轮播
		viewPager.startPlay();

	}

	@Override
	public void initData() {
		/*
		 * new Thread(new Runnable() {
		 * 
		 * private URL url;
		 * 
		 * @Override public void run() {
		 * 
		 * try { JSONObject jsonObject = new JSONObject();
		 * jsonObject.put("name", "NL");
		 * 
		 * String urlpath =
		 * "https://www.4001149114.com/NLJJ/dealwine/gettoken?appname=NL";
		 * 
		 * HttpsURLConnection conn = NetUtils.httpsconn(urlpath, jsonObject,
		 * "POST");
		 * 
		 * // 若传递成功，解析服务器返回的数据 int code = conn.getResponseCode(); if (code ==
		 * 200) { InputStream is = conn.getInputStream(); String json =
		 * NetUtils.readString(is); JSONObject jsonObject2 = new
		 * JSONObject(json); String s = jsonObject2.getString("message"); String
		 * t = jsonObject2.getString("token"); Log.e("ss", s);
		 * System.err.println(t); } else { Toast.makeText(getActivity(),
		 * "数据提交失败", 1).show(); }
		 * 
		 * } catch (Exception e) { // TODO: handle exception }
		 * 
		 * }
		 * 
		 * }).start();
		 */
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					String urlpath = "https://www.4001149114.com/NLJJ/ddapp/ttzqlist";
					HttpsURLConnection conn = NetUtils.httpsconnNoparm(urlpath,
							"GET");
					// 若连接服务器成功，返回数据
					int code = conn.getResponseCode();
					if (code == 200) {

						InputStream is = conn.getInputStream();
						String json = NetUtils.readString(is);
						// 解析json
						parsonJson(json);

					}

				} catch (Exception e) {
					// TODO: handle exception
				} // TODO Auto-generated method stub

			}

		}).start();

	}

	@SuppressLint("ResourceAsColor")
	private void parsonJson(String json) {
		// TODO Auto-generated method stub
		try {
			JSONObject jsonObject = new JSONObject(json);
			// System.err.println(jsonObject.toString());
			// 判断json是否传输成功
			String success = jsonObject.getString("message");
			Log.e("sss", "是否成功:" + success);
			// 置顶的酒金窖
			String maindealgood = jsonObject.getString("maindealgood");
			JSONObject jsonObject2 = new JSONObject(maindealgood);
			Log.e("v", jsonObject2.getString("dgid"));

			// 置顶酒金窖名字
			String name = jsonObject2.getString("name");
			Log.e("vv", jsonObject2.getString("name"));
			sp.edit().putString("name", name).commit();
			// 交易代码
			String dealcode = jsonObject2.getString("dealcode");
			sp.edit().putString("dealcode", dealcode).commit();
			// 限量发行
			String stock = jsonObject2.getString("stock");
			sp.edit().putString("stock", stock).commit();
			// 增益比例
			String rate = jsonObject2.getString("rate");
			sp.edit().putString("rate", rate).commit();
			// 我要认购的状态3
			String state = jsonObject2.getString("state");
			sp.edit().putString("state", state).commit();
			// 离认购期
			String dealterm = jsonObject2.getString("dealterm");
			sp.edit().putString("dealterm", dealterm).commit();
			// 其他酒金窖数组
			JSONArray dealgoodslist = jsonObject.getJSONArray("dealgoods");
			int length = dealgoodslist.length();
			sp.edit().putInt("length", length).commit();
             
			//解析dealgoods
			for (int i = 0; i < length; i++) {
				//龙潭的json
				JSONObject jsonObject3=dealgoodslist.getJSONObject(i);
				
				
			}
			
			
			
			
			
			
			Message message = new Message();
			message.what = 1;
			mHandler.sendMessage(message);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void UpdateUI() {
		// TODO Auto-generated method stub
		// 要获取要将动态加载内容置入的容器 LinearLayout
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View view = inflater.inflate(R.layout.trade_item_jiujiao, null);
		ll_content.addView(view);
		btn_public = (RelativeLayout) view.findViewById(R.id.btn_public);

		tv_rate = (TextView) view.findViewById(R.id.rate);
		tv_name = (TextView) view.findViewById(R.id.name);
		tv_dealcode = (TextView) view.findViewById(R.id.dealcode);
		tv_stock = (TextView) view.findViewById(R.id.stock);
		tv_lirengou = (TextView) view.findViewById(R.id.li);
		tv_dealterm = (TextView) view.findViewById(R.id.realprice);
		tv_tian = (TextView) view.findViewById(R.id.term_day);
		btn_name = (TextView) view.findViewById(R.id.btn_name);
		tv_rate.setText(sp.getString("rate", null));
		tv_name.setText(sp.getString("name", null));
		tv_dealcode.setText(sp.getString("dealcode", null));
		tv_stock.setText(sp.getString("stock", null));
		if ("3".equals(sp.getString("state", null))) {
			btn_name.setText("我要认购");
			btn_name.setTextSize(18);
			btn_name.setTextColor(R.color.white_btn_ren);
			tv_lirengou.setText("离认购还剩:");
			tv_lirengou.setTextSize(15);
			// tv_lirengou.setTextColor(R.color.red);
			tv_dealterm.setText(sp.getString("dealterm", null));
			tv_dealterm.setTextSize(15);
			// tv_dealterm.setTextColor(R.color.red);
			tv_tian.setText("天");
			tv_tian.setTextSize(15);
			btn_public.setVisibility(View.VISIBLE);
			// tv_tian.setTextColor(R.color.red);
		}
		int length = sp.getInt("length", 0);
		// 遍历JSONArray
		for (int i = 0; i < length; i++) {

			View view1 = inflater.inflate(R.layout.trade_item_jiujiao, null);
			ll_content.addView(view1);
			tv_jin = (TextView) view1.findViewById(R.id.li);
			tv_deaTextView = (TextView) view1.findViewById(R.id.realprice);
			tv_day = (TextView) view1.findViewById(R.id.term_day);
			tv_jin.setText("进入交易大厅>>");
			tv_deaTextView.setVisibility(View.GONE);
			tv_day.setVisibility(View.GONE);
			RelativeLayout btn_jinru = (RelativeLayout) view1
					.findViewById(R.id.btn_public);
			btn_jinru.setVisibility(View.GONE);
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getLayoutResID() {
		// TODO Auto-generated method stub
		return R.layout.frag_trade;

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {

		default:
			break;
		}

	}

}
