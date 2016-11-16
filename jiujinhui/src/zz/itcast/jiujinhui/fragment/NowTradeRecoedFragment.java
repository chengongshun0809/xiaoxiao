package zz.itcast.jiujinhui.fragment;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

import org.json.JSONArray;
import org.json.JSONObject;

import zz.itcast.jiujinhui.R;
import zz.itcast.jiujinhui.res.NetUtils;
import android.R.string;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class NowTradeRecoedFragment extends BaseFragment {
	private SharedPreferences sp;
	boolean stopThread = false;
	@ViewInject(R.id.line_chart)
	private lecho.lib.hellocharts.view.LineChartView lineChart;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				refreshdata();
				break;
			case 1:
				UpdateUI();

				break;
			default:
				break;
			}

		};

	};
	private JSONArray jsonArraylist;
	private String dgid;
	private String unionid;

	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub
		sp = getActivity().getSharedPreferences("user", 0);

		ViewUtils.inject(this, view);
		initLineChart();
		lineChart.setOnValueTouchListener(new LineChartOnValueSelectListener() {

			@Override
			public void onValueDeselected() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onValueSelected(int arg0, int arg1, PointValue arg2) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), arg1+"", 0).show();  
				//line.setHasLabelsOnlyForSelected(true);
				Log.e("点击节点", arg2 + "");
			}
		});

		
	}

	private double nowprice;
	private String nowcreatetime;
	// private LineChartView lineChartView;
	private List<PointValue> mPointValues = new ArrayList<PointValue>();
	private List<AxisValue> axisValuesX = new ArrayList<AxisValue>();
	private List<AxisValue> axisValuesY = new ArrayList<AxisValue>();

	// 画图表
	protected void UpdateUI() {
		// TODO Auto-generated method stub

		getAxisLables();// 获取x轴的标注 // getAyisLables();// 获取y轴的刻度;

		initLineChart();
		
	}

	private void initLineChart() {
		line = new Line(mPointValues);

		List<Line> lines = new ArrayList<Line>();
		line.setShape(ValueShape.CIRCLE);// 折线图上每个数据点的形状 这里是圆形 （有三种
											// ：ValueShape.SQUARE
											// ValueShape.CIRCLE
											// ValueShape.DIAMOND）
		line.setCubic(false);// 曲线是否平滑，即是曲线还是折线
		line.setFilled(false);// 是否填充曲线的面积
		//line.setHasLabels(true);// 曲线的数据坐标是否加上备注
		//
		line.setHasPoints(true);

		line.setPointRadius(3);
		line.setHasLabels(false);
		line.setHasLabelsOnlyForSelected(true);
		// 点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
		line.setHasLines(true);// 是否用线显示。如果为false 则没有曲线只有点显示
		//line.setHasPoints(false);// 是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
		line.setColor(Color.RED);
		// 圆点半径
		//line.setPointRadius(2);
		line.setStrokeWidth(1);// 设置折线宽度
		
		lines.add(line);
		LineChartData data = new LineChartData();
		data.setLines(lines);
		//data.setValueLabelBackgroundEnabled(true);
		data.setValueLabelsTextColor(Color.BLACK);
          // data.setValueLabelTypeface(typeface);
		// 坐标轴
		Axis axisX = new Axis(); // X轴
		axisX.setHasTiltedLabels(true); // X坐标轴字体是斜的显示还是直的，true是斜的显示
		axisX.setTextColor(Color.GRAY); // 设置字体颜色
		axisX.setName("实时详情数据"); // 表格名称
		axisX.setTextSize(10);// 设置字体大小
		axisX.setMaxLabelChars(10); // 最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
		axisX.setValues(axisValuesX); // 填充X轴的坐标值
		data.setAxisXBottom(axisX); // x 轴在底部
		// data.setAxisXTop(axisX); //x 轴在顶部
		axisX.setHasLines(false); // x 轴分割

		// Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
		Axis axisY = new Axis().setHasLines(true); // Y轴
		axisY.setName("价格");// y轴标注

		axisY.setTextSize(10);// 设置字体大小
		data.setAxisYLeft(axisY); // Y轴设置在左边
		// data.setAxisYRight(axisY); //y轴设置在右边

		// 设置行为属性，支持缩放、滑动以及平移
		lineChart.setInteractive(false);
		// lineChart.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
		// lineChart.setMaxZoom((float) 2);//最大方法比例
	
		lineChart.setLineChartData(data);
		
		lineChart.setValueSelectionEnabled(true);
		lineChart.setVisibility(View.VISIBLE);
		// lineChart.startDataAnimation();
		/*
		 * Viewport v = new Viewport(lineChart.getMaximumViewport()); v.left =
		 * 0; v.right= 5; lineChart.setCurrentViewport(v);
		 */

	}

	// X轴刻度的显示
	private void getAxisLables() { // TODO Auto- generated method
		for (int i = 0; i < priceList.size(); i++) {
			mPointValues.add(new PointValue(i, firstpriceList.get(i)));
			axisValuesX.add(new AxisValue(i).setValue(i).setLabel(
					firsttimeList.get(i)));
			axisValuesY.add(new AxisValue(i).setValue(firstpriceList.get(i)));// 添加Y轴显示的刻度值

		}

	}

	public static List<String> timeList;
	public static List<Float> priceList;

	public static List<String> firsttimeList;
	public static List<Float> firstpriceList;

	// String[] times = { "09:00", "11:30", "15:00", "22:00" };
	private String stateString;
	private Line line;

	protected void refreshdata() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (!stopThread) {

					String url_serviceinfo = "https://www.4001149114.com/NLJJ/ddapp/hallorder?unionid="
							+ unionid + "&dgid=" + dgid;

					HttpsURLConnection connection = NetUtils.httpsconnNoparm(
							url_serviceinfo, "POST");
					int code;
					try {
						code = connection.getResponseCode();
						if (code == 200) {
							InputStream iStream = connection.getInputStream();
							String infojson = NetUtils.readString(iStream);
							JSONObject jsonObject = new JSONObject(infojson);
							// Log.e("ssssssssss", jsonObject.toString());
							parseJson(jsonObject);
							Thread.sleep(30000);

						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}).start();
	}

	protected void parseJson(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		// 交易曲线
		try {
			jsonArraylist = jsonObject.getJSONArray("todaydeal");
			timeList = new ArrayList<String>();
			priceList = new ArrayList<Float>();
			firsttimeList = new ArrayList<String>();
			firstpriceList = new ArrayList<Float>();
			JSONObject object2 = (JSONObject) jsonArraylist.get(0);
			// 初始指导价的值时间
			double firstprice = object2.getDouble("price");
			String firsttime = object2.getString("createtime");
			firstpriceList.add((float) (firstprice / 100));
			firsttimeList.add(firsttime.substring(10, 16));
			for (int i = 1; i < jsonArraylist.length(); i++) {
				JSONObject object = (JSONObject) jsonArraylist.get(i);

				stateString = object.getString("state");
				// timeList.add(nowcreatetime.substring(10,16));

				if ("0".equals(stateString)) {

					nowprice = object.getDouble("price");
					nowcreatetime = object.getString("createtime");
					timeList.add(nowcreatetime.substring(10, 16));
					priceList.add((float) (nowprice / 100));
				}

			}
			firstpriceList.addAll(priceList);
			firsttimeList.addAll(timeList);
			Log.e("wewweew", firstpriceList + "");
			Log.e("eeeeeeeeeeeeee", firsttimeList + "");
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		// 获取数据
		Message message = new Message();
		message.what = 0;
		handler.sendMessageDelayed(message, 500);
		dgid = getActivity().getIntent().getStringExtra("dealdgid");
		unionid = sp.getString("unionid", null);

	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getLayoutResID() {
		// TODO Auto-generated method stub
		return R.layout.newtrade_record;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		stopThread = true;
		super.onDestroy();
	}
}
