package zz.itcast.jiujinhui.wxapi;

import org.json.JSONException;
import org.json.JSONObject;

import zz.itcast.jiujinhui.activity.MainActivity;
import zz.itcast.jiujinhui.res.Constants;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendAuth;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

	
	private IWXAPI mApi;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mApi = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);
		mApi.handleIntent(getIntent(), this);
		sp = getSharedPreferences("user", MODE_PRIVATE);
		mApi.registerApp(Constants.APP_ID);
	}

	@Override
	public void onResp(BaseResp resp) {
		// TODO Auto-generated method stub
		// 重写方法拿到code
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK: // 发送成功
			
			
			String code1 = ((SendAuth.Resp) resp).token; // 即为所需的code
			Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
			// 拿到code,加上appid和secret拼接网址,请求数据得到包含token和openid来继续请求拿到用户数据
			String urlstr = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
					+ Constants.APP_ID
					+ "&secret="
					+ Constants.APP_SECRET
					+ "&code=" + code1 + "&grant_type=authorization_code";
			HttpUtils httpUtils = new HttpUtils();
			httpUtils.send(HttpRequest.HttpMethod.GET, urlstr,
					new RequestCallBack<String>() {

						private JSONObject jsonObject;

						@Override
						public void onSuccess(ResponseInfo<String> responseInfo) {
							// TODO Auto-generated method stub
							// TODO 拿到token了
							try {
								jsonObject = new JSONObject(responseInfo.result
										.toString());
								final String accessToken = jsonObject
										.getString("access_token");
								final String openid = jsonObject
										.getString("openid");
								System.err.println(accessToken+"   " +openid);
								// 获取access_token，openid后，就可以用来获取更多用户信息，比如微信昵称，头像，性别等。接口为：
								/*
								 * 这个是验证access_token 是否是有效的
								 * https://api.weixin.qq
								 * .com/sns/auth?access_token
								 * =ACCESS_TOKEN&openid=OPENID 正确的Json返回结果： {
								 * "errcode":0,"errmsg":"ok" }
								 */
								String url="https://api.weixin.qq.com/sns/auth?access_token=" + accessToken + "&openid=" + openid;
								HttpUtils httpUtils=new HttpUtils();
								httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {

									private JSONObject object;

									@Override
									public void onSuccess(
											ResponseInfo<String> responseInfo) {
										try {
											object = new JSONObject(responseInfo.result.toString());
											int errcode=object.getInt("errcode");
											if (errcode==0) {//说明access_token是有效的
												String urlString="https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openid + "&lang=zh_CN";
												HttpUtils httpUtils=new HttpUtils();
												httpUtils.send(HttpRequest.HttpMethod.GET, urlString, new RequestCallBack<String>() {

													@Override
													public void onSuccess(
															ResponseInfo<String> responseInfo) {
														// TODO Auto-generated method stub
														try {
															JSONObject json=new JSONObject(responseInfo.result.toString());
															//拿到昵称和头像
															  String nickname = json.getString("nickname");//昵称
	                                                            String headimgurl = json.getString("headimgurl");//头像
														
															Log.e("ms", nickname+"  "+headimgurl);
															Toast.makeText(WXEntryActivity.this, "登录成功",
																	Toast.LENGTH_SHORT).show();
															Intent intent = new Intent(WXEntryActivity.this,
																	MainActivity.class);
															startActivity(intent);
															sp.edit().putBoolean("isLogined", true).commit();
															//用户微信头像
															sp.edit().putString("headimg", headimgurl).commit();
														     //用户微信昵称
															sp.edit().putString("nickname", nickname).commit();
															
														} catch (JSONException e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}
														
														
													}

													@Override
													public void onFailure(
															HttpException error,
															String msg) {
														// TODO Auto-generated method stub
														
													}
												});
												
											}
											
											
											
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									
										
										
									}

									@Override
									public void onFailure(HttpException error,
											String msg) {
										// TODO Auto-generated method stub
										
									}
								});
								

							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

						@Override
						public void onFailure(HttpException error, String msg) {
							// TODO Auto-generated method stub

						}

					});

			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL: // 发送取消
			Toast.makeText(this, "取消登录", Toast.LENGTH_SHORT).show();
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED: // 发送被拒绝
			Toast.makeText(this, "登录失败", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
		finish();
	}

	@Override
	public void onReq(BaseReq resp) {
		// TODO Auto-generated method stub

	}

}