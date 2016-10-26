package zz.itcast.jiujinhui.fragment;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.json.JSONArray;
import org.json.JSONObject;

import zz.itcast.jiujinhui.R;
import zz.itcast.jiujinhui.activity.KnowDetailActivity;
import zz.itcast.jiujinhui.activity.TradeServiceActivity;
import zz.itcast.jiujinhui.bean.test;
import zz.itcast.jiujinhui.res.MyX509TrustManager;
import zz.itcast.jiujinhui.res.NetUtils;
import android.R.interpolator;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.HurlStack.UrlRewriter;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

public class TradeFragment extends BaseFragment {

	@ViewInject(R.id.tv_back)
	private ImageView tv_back;
	@ViewInject(R.id.tv__title)
	private TextView tv__title;
	@ViewInject(R.id.rengou)
	private RelativeLayout rengou;
	@ViewInject(R.id.LongTan)
	private RelativeLayout LongTan;
	@ViewInject(R.id.viewPager_menu)
	// 主页轮播图
	private zz.itcast.jiujinhui.view.AbSlidingPlayView viewPager;
	// 了解详情
	@ViewInject(R.id.know_detail)
	private RelativeLayout know_detail;

	// 首页轮播的界面
	private List<ImageView> imageList;

	private final int[] imageIds = { R.drawable.a, R.drawable.b, R.drawable.c, };

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

	}

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
		new Thread(new Runnable() {

			private URL url;

			@Override
			public void run() {
				
				try {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("name", "NL");
					
					String urlpath = "https://www.4001149114.com/NLJJ/dealwine/gettoken?appname=NL";
					
					HttpsURLConnection conn = NetUtils.httpsconn(urlpath, jsonObject,"POST");
				           
					// 若传递成功，解析服务器返回的数据
					int code = conn.getResponseCode();
					if (code == 200) {
						InputStream is = conn.getInputStream();
						String json = NetUtils.readString(is);
						JSONObject jsonObject2 = new JSONObject(json);
						String s = jsonObject2.getString("message");
						String t = jsonObject2.getString("token");
						Log.e("ss", s);
						System.err.println(t);
					} else {
						Toast.makeText(getActivity(), "数据提交失败", 1)
								.show();
					}

				}  catch (Exception e) {
					// TODO: handle exception
				}

				
			}		
			
		}).start();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub
		rengou.setOnClickListener(this);
		LongTan.setOnClickListener(this);
		know_detail.setOnClickListener(this);
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
		case R.id.LongTan:
			Intent intent0 = new Intent(getActivity(),
					TradeServiceActivity.class);
			startActivity(intent0);

			break;
		case R.id.rengou:
			Intent intent1 = new Intent(getActivity(),
					TradeServiceActivity.class);
			startActivity(intent1);

			break;
		case R.id.know_detail:
			Intent intent = new Intent(getActivity(), KnowDetailActivity.class);
			startActivity(intent);

			break;

		default:
			break;
		}

	}

}
