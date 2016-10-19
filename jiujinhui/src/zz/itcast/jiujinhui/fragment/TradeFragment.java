package zz.itcast.jiujinhui.fragment;

import java.util.ArrayList;
import java.util.List;

import zz.itcast.jiujinhui.R;
import zz.itcast.jiujinhui.activity.TradeServiceActivity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
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
	//主页轮播图
	private zz.itcast.jiujinhui.view.AbSlidingPlayView viewPager;

	// 首页轮播的界面
	private List<ImageView> imageList;

	private final int[] imageIds = { R.drawable.a, R.drawable.b, R.drawable.c,
			 };

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
		
		if (imageList!=null) {
			imageList.clear();
			imageList=null;
		}
		imageList = new ArrayList<ImageView>();
		for (int i = 0; i < imageIds.length; i++) {
			
			ImageView image = new ImageView(getActivity());
			image.setBackgroundResource(imageIds[i]);

			imageList.add(image);
		}
		viewPager.addViews(imageList);
		//开始轮播
		viewPager.startPlay();
		
		
	}

	@Override
	public void initData() {
		
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

		default:
			break;
		}

	}

}
