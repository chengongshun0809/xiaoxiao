package zz.itcast.jiujinhui.activity;

import java.util.ArrayList;

import zz.itcast.jiujinhui.R;
import zz.itcast.jiujinhui.fragment.BuyChartFragment;
import zz.itcast.jiujinhui.fragment.EveryDayTradeRecordFragment;
import zz.itcast.jiujinhui.fragment.NowTradeRecoedFragment;
import zz.itcast.jiujinhui.fragment.SaleChartFragment;
import zz.itcast.jiujinhui.res.Arith;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class TradeServiceActivity extends BaseActivity {
	// 买入
	@ViewInject(R.id.rb_buy_service)
	private RadioButton rb_buy_service;
	// 卖出
	@ViewInject(R.id.rb_sale_service)
	private RadioButton rb_sale_service;

	// 提货
	@ViewInject(R.id.rb_tihuo_service)
	private RadioButton rb_tihuo_service;
	// 转让
	@ViewInject(R.id.rb_zhuanrang_service)
	private RadioButton rb_zhuanrang_service;

	// 交易曲线
	@ViewInject(R.id.tabs)
	private PagerSlidingTabStrip tabs;
	@ViewInject(R.id.trade_pager)
	private ViewPager trade_pager;

	//
	// 交易曲线
	private ArrayList<Fragment> fragmentsList1;
	// 买入卖出曲线
	@ViewInject(R.id.tabs_buysale)
	private PagerSlidingTabStrip tabs_buysale;
	@ViewInject(R.id.buy_sale_pager)
	private ViewPager buy_sale_pager;

	private ArrayList<Fragment> fragmentsList2;
	@ViewInject(R.id.hscrollview)
	private HorizontalScrollView hscrollview;
	@ViewInject(R.id.ll_scroll)
	private LinearLayout ll_scroll;
	@ViewInject(R.id.tv_back)
	private ImageView tv_back;
	@ViewInject(R.id.tv__title)
	private TextView tv__title;

	// 定义一个Handler对象
	private final Handler handler = new Handler();

	@Override
	public void initListener() {
		// TODO Auto-generated method stub
		tv_back.setOnClickListener(this);

		rb_buy_service.setOnClickListener(this);
		rb_sale_service.setOnClickListener(this);
		rb_tihuo_service.setOnClickListener(this);
		rb_zhuanrang_service.setOnClickListener(this);

	}

	// 当前滚动距离
	int currentX = 0;

	// 实现滚动线程
	private Runnable hscrollRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			// 判断宽度
			int off = ll_scroll.getMeasuredWidth();

			hscrollview.scrollBy(off, 0);

			handler.postDelayed(this, 4000);

		}
	};

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		ViewUtils.inject(this);
		tv__title.setText("交易服务");
		handler.post(hscrollRunnable);

	}

	@Override
	public int getLayoutResID() {
		// TODO Auto-generated method stub
		return R.layout.frag_trade_service;
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		fragmentsList1 = new ArrayList<Fragment>();
		fragmentsList1.add(new NowTradeRecoedFragment());
		fragmentsList1.add(new EveryDayTradeRecordFragment());
		fragmentsList2 = new ArrayList<Fragment>();
		fragmentsList2.add(new SaleChartFragment());
		fragmentsList2.add(new BuyChartFragment());
		trade_pager.setAdapter(new MypagerAdapter(getSupportFragmentManager(),
				fragmentsList1));
		buy_sale_pager.setAdapter(new MyBuySalepagerAdapter(
				getSupportFragmentManager(), fragmentsList2));
		tabs.setViewPager(trade_pager);
		tabs_buysale.setViewPager(buy_sale_pager);
		tabs.setShouldExpand(true);
		tabs_buysale.setShouldExpand(true);
	}

	// 交易曲线图适配器
	public class MypagerAdapter extends FragmentPagerAdapter {
		private ArrayList<Fragment> fragmentsList1;

		public MypagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
			super(fm);
			this.fragmentsList1 = fragments;
		}

		public MypagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		private final String[] titles1 = { "实时", "每天" };

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return titles1[position];
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			return fragmentsList1.get(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return titles1.length;
		}

	}

	// 买入卖出图标适配器
	public class MyBuySalepagerAdapter extends FragmentPagerAdapter {
		private ArrayList<Fragment> fragmentsList2;

		public MyBuySalepagerAdapter(FragmentManager fm,
				ArrayList<Fragment> fragments) {
			super(fm);
			this.fragmentsList2 = fragments;
		}

		public MyBuySalepagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		private final String[] titles2 = { "卖出", "买入" };

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return titles2[position];
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			return fragmentsList2.get(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return titles2.length;
		}

	}

	private static TradeServiceActivity instance;
	private ImageView product_ordsubmit_count_sub;
	private ImageView product_ordsubmit_count_add;
	private TextView product_ordsubmit_count;
	private EditText product_ordsubmit_price;
	private TextView product_total_price;
	private Button dialog_ok;
	private Button diaog_cancel;
	private int count_buy;


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.tv_back:
			finish();
			break;

		case R.id.rb_buy_service:
			showBuyDialog();
			break;
		case R.id.rb_sale_service:
          showSaleDialog();
			break;

		case R.id.rb_tihuo_service:
             showTihuoDialog();
			break;

		case R.id.rb_zhuanrang_service:
            shouTransDialog();
			break;

		default:
			break;
		}
	}
	 int trans_count;
	//转让
	 private void shouTransDialog() {
		// TODO Auto-generated method stub
		 LayoutInflater inflater = LayoutInflater.from(this);
	 		View transView = (View) inflater.inflate(R.layout.trans_service, null);
		 transTextView = (TextView) transView.findViewById(R.id.product_ordsubmit_count);
		 transOk = (Button) transView.findViewById(R.id.dialog_ok);
		 transCancel = (Button) transView.findViewById(R.id.dialog_cancel);
		 transAdd = (ImageView) transView.findViewById(R.id.product_ordsubmit_count_add);
		transReduce = (ImageView) transView.findViewById(R.id.product_ordsubmit_count_sub);
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setView(transView);
			builder.setCancelable(false);
			dialog1 = builder.show();
		 transAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
             trans_count++;
             transTextView.setText(""+trans_count);
			}
		});
		 transReduce.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (trans_count>1) {
					trans_count--;
					   transTextView.setText(""+trans_count);
				}else {
					trans_count=1;
				}
				
			}
		});
		 transOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		 transCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog1.dismiss();
				trans_count=1;
			}
		});
		 
		 
	}

	//提货
	int tihuo_count;
     private void showTihuoDialog() {
    	 LayoutInflater inflater = LayoutInflater.from(this);
 		View tihuoView = (View) inflater.inflate(R.layout.tihuo_service, null);
		tihuoTextView = (TextView) tihuoView.findViewById(R.id.product_ordsubmit_count);
 		tihuoAdd = (ImageView) tihuoView.findViewById(R.id.product_ordsubmit_count_add);
 		tihuoreduce = (ImageView) tihuoView.findViewById(R.id.product_ordsubmit_count_sub);
    	tihuoOk = (Button) tihuoView.findViewById(R.id.dialog_ok);
    	
    	tihuocancel = (Button) tihuoView.findViewById(R.id.dialog_cancel);
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(tihuoView);
		builder.setCancelable(false);
		dialog0 = builder.show();
    	
 		tihuoAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tihuo_count++;
				tihuoTextView.setText(""+tihuo_count);
			}
		});
 		tihuoreduce.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (tihuo_count>1) {
					tihuo_count--;
					tihuoTextView.setText(""+tihuo_count);
				}else {
					tihuo_count=1;
				}
			}
		});
 		
 		tihuoOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
 		tihuocancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog0.dismiss();
				tihuo_count=1;
			}
		});
 		
	}

	int sale_count=1;
	//卖出按钮
   private void showSaleDialog() {
		// TODO Auto-generated method stub
	   LayoutInflater inflater = LayoutInflater.from(this);
		View saleView = (View) inflater.inflate(R.layout.sale_service, null);
		product_ordsubmit_count2 = (TextView) saleView.findViewById(R.id.product_ordsubmit_count);
	    //卖出价
		salePrice = (EditText) saleView.findViewById(R.id.product_ordsubmit_price);
	   //加号
		add = (ImageView) saleView.findViewById(R.id.product_ordsubmit_count_add);
	   //减号
		reduce = (ImageView) saleView.findViewById(R.id.product_ordsubmit_count_sub);
		ok = (Button) saleView.findViewById(R.id.dialog_ok);
		cancel = (Button) saleView.findViewById(R.id.dialog_cancel);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(saleView);
		builder.setCancelable(false);
		dialog = builder.show();
		add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sale_count++;
				product_ordsubmit_count2.setText(""+sale_count);
				
			}
		});
		reduce.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (sale_count>1) {
					sale_count--;
					product_ordsubmit_count2.setText(""+sale_count);
				}else {
					sale_count=1;
				}
				
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				sale_count=1;
			}
		});
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
	}

	//买入按钮
	private void showBuyDialog() {
		// TODO Auto-generated method stub

		LayoutInflater inflater = LayoutInflater.from(this);
		View view = (View) inflater.inflate(R.layout.buy_service, null);
		// 增加
		product_ordsubmit_count_sub = (ImageView) view
				.findViewById(R.id.product_ordsubmit_count_sub);
		// 减少
		product_ordsubmit_count_add = (ImageView) view
				.findViewById(R.id.product_ordsubmit_count_add);
		// 数量
		product_ordsubmit_count = (TextView) view
				.findViewById(R.id.product_ordsubmit_count);
          
		// 买入价格
		product_ordsubmit_price = (EditText) view
				.findViewById(R.id.product_ordsubmit_price);
		product_ordsubmit_price.addTextChangedListener(textWatcher);

		// 总价钱
		product_total_price = (TextView) view
				.findViewById(R.id.product_total_price);

		dialog_ok = (Button) view.findViewById(R.id.dialog_ok);
		diaog_cancel = (Button) view.findViewById(R.id.dialog_cancel);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(view);
		builder.setCancelable(false);
		dialog2 = builder.show();
		
	      
		
		
       //取消按钮
		diaog_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog2.dismiss();
				count_buy=1;
			}
		});
		//确定按钮
		dialog_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		
		product_ordsubmit_count_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				count_buy++;
				product_ordsubmit_count.addTextChangedListener(textWatcher);
				product_ordsubmit_count.setText(count_buy + "");

			}

		});
		product_ordsubmit_count_sub.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (count_buy > 1) {
					
					count_buy--;
					product_ordsubmit_count.addTextChangedListener(textWatcher);

					product_ordsubmit_count.setText(count_buy + "");
                 
				
				} else {
					count_buy = 1;
				}

			}
		});
	

	}
	//买入价监听
    private TextWatcher	textWatcher = new TextWatcher() {
		private CharSequence charSequence;
		private String price;

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

			price = product_ordsubmit_price.getText().toString().trim();
			if (!TextUtils.isEmpty(price)) {
				
				product_total_price.setText(""+Arith.mul(Double.parseDouble(price), count_buy));
			} else {
				
				product_total_price.setText("0.00");
			}

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			charSequence = s;
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}
	};
	
	private Dialog dialog2;
	private EditText salePrice;
	private ImageView add;
	private ImageView reduce;
	private Button ok;
	private Button cancel;
	private Dialog dialog;
	private TextView product_ordsubmit_count2;
	private TextView tihuoTextView;
	private ImageView tihuoAdd;
	private ImageView tihuoreduce;
	private Button tihuoOk;
	private Button tihuocancel;
	private Dialog dialog0;
	private TextView transTextView;
	private Button transOk;
	private Button transCancel;
	private Dialog dialog1;
	private ImageView transAdd;
	private ImageView transReduce;
	


}
