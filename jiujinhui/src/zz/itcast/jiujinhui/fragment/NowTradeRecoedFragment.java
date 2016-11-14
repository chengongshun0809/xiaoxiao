package zz.itcast.jiujinhui.fragment;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
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
import android.view.View;

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

		initLineChart();// 初始化

	}

	private double nowprice;
	private double nowcreatetime;
	private LineChartView lineChartView;
	private List<PointValue> mPointValues = new ArrayList<PointValue>();
	private List<AxisValue> axisValuesY = new ArrayList<AxisValue>();
	private List<AxisValue> axisValuesX = new ArrayList<AxisValue>();

	// 画图表
	protected void UpdateUI() {
		// TODO Auto-generated method stub

		getAxisLables();// 获取x轴的标注
		//getAyisLables();// 获取y轴的刻度;
		getAxisPoints();// 获取坐标点
       initLineChart();
	}

	private void initLineChart() {
		// TODO Auto-generated method stub
		Line line = new Line(mPointValues).setColor(Color.parseColor("#C0D79C")); // 折线的颜色
		List<Line> lines = new ArrayList<Line>(); 
		line.setShape(ValueShape.CIRCLE);// 折线图上每个数据点的形状 这里是圆形 （有三种
											// ：ValueShape.SQUARE
											// ValueShape.CIRCLE
											// ValueShape.SQUARE）
		line.setCubic(false);//曲线是否平滑
		line.setFilled(false);// 是否填充曲线的面积
		// line.setHasLabels(true);//曲线的数据坐标是否加上备注
		line.setHasLabelsOnlyForSelected(true);// 点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
		line.setHasLines(true);// 是否用直线显示。如果为false 则没有曲线只有点显示
		line.setHasPoints(true);// 是否显示圆点 如果为false 则没有原点只有点显示
		line.setStrokeWidth(1);// 设置折线宽度
		lines.add(line);
		LineChartData data = new LineChartData();
		data.setLines(lines);

		// 坐标轴 
		Axis axisX = new Axis(); // X轴

		axisX.setLineColor(Color.BLACK);
		axisX.setHasTiltedLabels(true);
		axisX.setTextColor(Color.BLACK); // 设置字体颜色
		axisX.setName("实时的交易曲线图"); // 表格名称
		axisX.setTextSize(10);// 设置字体大小
		//axisX.setMaxLabelChars(4); // 最多几个X轴坐标
		// axisX.setValues(mAxisValues); //填充X轴的坐标名称
		axisX.setHasTiltedLabels(false);// 不旋转45度
		data.setAxisXBottom(axisX); // x 轴在底部
		// data.setAxisXTop(axisX); //x 轴在顶部

		Axis axisY = new Axis(); // Y轴
		axisY.setLineColor(Color.BLACK);
		//axisY.setMaxLabelChars(4); // 默认是3，只能看最后三个数字
		//axisY.setName("指导价");// y轴标注
		axisY.setTextSize(10);// 设置字体大小

		data.setAxisYLeft(axisY); // Y轴设置在左边
		// data.setAxisYRight(axisY); //y轴设置在右边

		// 设置行为属性，支持缩放、滑动以及平移
	
		lineChart.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
		lineChart.setContainerScrollEnabled(true,
				ContainerScrollType.HORIZONTAL);
		lineChart.setLineChartData(data);
		lineChart.setValueSelectionEnabled(false);//设置节点点击后动画
		lineChart.setVisibility(View.VISIBLE);
		lineChart.setInteractive(false);//是否可以缩放
	}
        
	// 图中每个点的显示
	private void getAxisPoints() {
		
         
		for (int i = 0; i < jsonArraylist.length(); i++) {
			mPointValues.add(new PointValue(i,(float) nowprice));
		}
	}
	// X轴刻度的显示
	private void getAxisLables() {
		// TODO Auto-generated method stub
		for (int i = 0; i < 3; i++) {
			//axisValuesX.add(new AxisValue(i).setLabel(times[i]));
			axisValuesX.add(new AxisValue(i).setLabel(nowcreatetime+""));
		}

	}

	// Y轴刻度的显示
	private void getAyisLables() {
		// TODO Auto-generated method stub
		for (int i = 0; i < 3; i++) {
			//axisValuesY.add(new AxisValue(i).setLabel(nowprice + ""));
			axisValuesY.add(new AxisValue(i*10*(i+1)).setLabel(i+""));
		}
	}

	String[] times = { "09:00", "11:30", "15:00", "22:00" };

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
			for (int i = 0; i < jsonArraylist.length(); i++) {
				JSONObject object = (JSONObject) jsonArraylist.get(i);
				  nowprice = object.getDouble("price");
				  //createtime不能转换为double
				  
				nowcreatetime = object.getDouble("createtime");
				
				
				
			}
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
