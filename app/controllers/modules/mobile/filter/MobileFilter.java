package controllers.modules.mobile.filter;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;
import play.Logger;
import play.cache.Cache;
import play.i18n.Messages;
import play.mvc.Before;
import utils.CommonValidateUtil;
import utils.DateUtil;
import utils.HttpClientUtil;
import utils.StringUtil;
import controllers.LoginService;
import controllers.comm.BaseController;
import controllers.comm.SessionInfo;
import controllers.comm.Sign;
import controllers.modules.mobile.bo.WxUserBo;
import models.modules.mobile.WxServer;
import models.modules.mobile.WxUser;
 /**
 * @author    姓名   E-mail: 邮箱  Tel: 电话
 * @version   创建时间：2017-3-24 下午1:47:15
 * @describe  类说明
 */
public class MobileFilter extends BaseController{
	private static org.slf4j.Logger log = LoggerFactory.getLogger(MobileFilter.class);
	private static int REQ_TIME = 0;
	@Before(unless = { "Application.index","LoginService.index", "LoginService.mIndex","LoginService.login",
			"LoginService.logout","LoginService.mlogout" })
	static void checkLogin() {
		log.debug("checkLogin开始检查登录状态");
		String userAgent = request.headers.get("user-agent").value().toLowerCase();
		String deviceFlag = params.get("flag");
		boolean isNeedInterface =false;
		SessionInfo sessionInfo=getSessionInfo();
		WxUser 	wxUser = null;
		if(null != sessionInfo){
			log.debug("当前session不为空");
			wxUser = sessionInfo.getWxUser();
			log.debug("当前用户:" + wxUser);
			if(null !=wxUser){
				isNeedInterface = false;
				 wxUser = WxUser.getFindByOpenId(wxUser.wxOpenId);
				 if(null != wxUser){
					 sessionInfo.setWxUser(wxUser);
			 		 Cache.add(MobileFilter.getSessionKey(), sessionInfo);
			 		 log.debug("读取信息end");
				 }else{
					 sessionInfo=new SessionInfo();
					 isNeedInterface=true;
				 }
			}
		}else{
			log.debug("当前session为空");
			if(!isMobile(userAgent)){//pc
				log.debug("当前浏览器不是手机，这里认为是pc");
				if("testPC".equals(deviceFlag)){
					log.debug("是pc上的测试标示testPC");
					wxUser = WxUser.getFindByOpenId(null);
					if(null != wxUser){
						log.debug("使用模拟用户加入到session中，昵称是:" + wxUser.nickName);
						sessionInfo=new SessionInfo();
		        		sessionInfo.setWxUser(wxUser);
		        		setSessionInfo(sessionInfo);
						isNeedInterface = false;
					}else{
						log.error("数据库中一个微信用户都没有,模拟用户失败");
						Logger.error("++++++++++++++++模拟oxh64jkHZeWtbUYc2AMqDc0HiJZg登录失败");
					}
				}else{
					//跳转到登录口 后续增加
					LoginService.index();
				}
			}else{
				sessionInfo=new SessionInfo();
				isNeedInterface=true;
			}
		}
		log.debug("isNeedInterface状态:"+isNeedInterface);
		if(isNeedInterface){
			String Key = session.getId()+"_"+request.domain;
			WxServer wxService = null;
			if(Cache.get(Key)==null){
				String doMain = request.domain;
				if("localhost".equals(doMain)){
					doMain="dw201709.com/xh";
				}
				Map<String, String> condition = new HashMap<>();
				condition.put("doMain", doMain);
				Map mapRet = WxServer.queryWxServerListByPage(condition,1, 100);
				if(null != mapRet){
					List<WxServer> list = (List<WxServer>) mapRet.get("data");
					if(!list.isEmpty()){
						wxService = list.get(0);
						if(null != wxService){
							Cache.set(Key,wxService);
						}else{
							//跳转到查找服务页面 后续增加
						}
					}
				}
			}else{
				wxService  = (WxServer) Cache.get(Key);
			}
			String appId = wxService.appId;
			String secret = wxService.appSecret;
			Logger.info("得到开发者appId:"+appId);
			Logger.info("得到开发者secret:"+secret);
			if(!StringUtil.isNotEmpty(appId)||!StringUtil.isNotEmpty(secret)){
	    		nok(Messages.get("paramsLose"));
	    	}
			String scope = "snsapi_base"; 
			String openId = params.get("openId");
			String code = params.get("code");
			String url ="http://" + request.domain + request.url;
			Logger.info("----------------url:"+url);
			if (code == null) {
				Logger.info("===========redirectURL=========== " + code);
				String redirectURL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
						+ appId
						+ "&redirect_uri="
						+ url
						+ "&response_type=code&scope="+scope+"&state=1"
						+ "#wechat_redirect";
				redirect(redirectURL);
			}else{
				Logger.info("===========code=========== " + code);
				//通过code 调用网页授权认证得到openid
				url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
			            + appId + "&secret=" + secret + "&code=" + code
			            + "&grant_type=authorization_code";
				JSONObject json = HttpClientUtil.invoke(url, "POST", null);
				Logger.info("返回openidjson数据 = " + json);
				String subscribe = "0"; //是否关注了，0：未关注，1：已关注
				if (json!=null&&json.containsKey("openid")) {
					 openId = json.getString("openid");
				     subscribe = json.containsKey("subscribe")?json.getString("subscribe"):subscribe;
				     params.remove("code");// 一次性，用完作废
				 	 wxUser = WxUser.findById(openId);
					 //最多三天取一次
					 if(wxUser!=null&&(DateUtil.getNowDate().compareTo(DateUtil.getDateTimeNowFun(wxUser.upOpenidTime,"d",1))<=0)){
						wxUser = WxUser.getFindByOpenId(openId);
						sessionInfo.setWxUser(wxUser);
		        		setSessionInfo(sessionInfo);
					 }else{
						 if(wxUser==null){
							wxUser = new WxUser();
							wxUser.wxOpenId = openId;
						  }
						 String accessToken = null;
						 if("snsapi_userinfo".equals(scope)){
							 Logger.info("通过****网页授权****获取用户基本信息");
							//通过code换取的是一个特殊的网页授权access_token,与基础支持中的access_token（该access_token用于调用其他接口）不同
						        accessToken = json.getString("access_token");
					        	url = "https://api.weixin.qq.com/sns/userinfo?access_token="+accessToken+"&openid="+openId+"&lang=zh_CN ";
					        	json = HttpClientUtil.invoke(url, "POST", null);
					        	Logger.info("snsapi_userinfo json = " + json);
					        	if(json!=null&&json.containsKey("openid")&&json.containsKey("nickname")){
					        		wxUser.nickName = json.getString("nickname");
					        		wxUser.sex = json.getString("sex");
					        		wxUser.sex = "1".equals(wxUser.sex)?"男":"女";
					        		wxUser.language = json.getString("language");
					        		wxUser.city = json.getString("city");
					        		wxUser.province = json.getString("province"); 
					        		wxUser.country = json.getString("country");
					        		wxUser.headImgUrl = getWXSmallHeadImage(json.getString("headimgurl"));
					        		wxUser.isConcerned = "Y";
					    			//wxUser.wxRole="0";
					    			wxUser.openIdChanncel="web_grant";
					    			wxUser.upOpenidTime= DateUtil.getNowDate();
					        		wxUser = WxUserBo.save(wxUser);
					        		wxUser = WxUser.getFindByOpenId(openId);
					        		sessionInfo.setWxUser(wxUser);
					        		setSessionInfo(sessionInfo);
					        	}
						 }else if("snsapi_base".equals(scope)){
							 Logger.info("通过****获取用户基本信息接口****获取用户信息");
						     accessToken = Sign.getAccessToken(appId,secret,false);
						     url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="+accessToken+"&openid="+openId+"&lang=zh_CN";
						     json = HttpClientUtil.invoke(url, "POST", null);
						     if(json!=null){
						    	 Logger.info("start snsapi_base json >>>>>>>>>>>>>>> " + json);
						    	//判断返回json是否异常
					    		if(json.containsKey("errcode")){
					    			accessToken = Sign.getAccessToken(appId,secret,true);
					    			json = HttpClientUtil.invoke(url, "POST", null);
					    			REQ_TIME++;
					    			Logger.info("异常报错,进入重新请求计次" +REQ_TIME);
					    		}
					    		Logger.info(" success snsapi_base json >>>>>>>>>>>>>>>>>>>" + json);
						    	Logger.info(" wxUser.wxOpenId==== = " + wxUser.wxOpenId);
						    	if(json!=null&&json.containsKey("openid")){
						    		if(json.containsKey("nickname")){
						    			wxUser.nickName = json.getString("nickname");
						    			wxUser.sex = json.getString("sex");
						    			wxUser.sex = "1".equals(wxUser.sex)?"男":"女";
						        		wxUser.language = json.getString("language");
						    			wxUser.city = json.getString("city");
						    			wxUser.province = json.getString("province"); 
						    			wxUser.country = json.getString("country");
						    			
						    			wxUser.headImgUrl = getWXSmallHeadImage(json.getString("headimgurl"));
						    			wxUser.isConcerned = "Y";
						    			//wxUser.wxRole="0";
						    			wxUser.openIdChanncel="web_grant";
						    			wxUser.upOpenidTime=DateUtil.getNowDate();
						    			wxUser = WxUserBo.save(wxUser);
						        		wxUser = WxUser.getFindByOpenId(openId);
						        		sessionInfo.setWxUser(wxUser);
						        		setSessionInfo(sessionInfo);
						    		}
						    	}
						     }
						 }
					 }
				}else{
					Logger.info("...获取openid失败，请重试重试");
				}
			}
		}
	}
	
	/**
	 * 得到微信头像，使用小图
	 * @param headImageUrl
	 * @return
	 */
	private static String getWXSmallHeadImage(String headImageUrl){
		if (headImageUrl.endsWith("/0")){
			return headImageUrl.substring(0, headImageUrl.length()-2)+"/132";
		} else {
			return headImageUrl;
		}
	}
	private static boolean isMobile(String userAgent) {
		if (userAgent != null) {
			if (userAgent.indexOf("micromessenger") >= 0) {
				return true;
			} else if (userAgent.indexOf("pad") >= 0) {
				return true;
			} else if (userAgent.indexOf("android") >= 0) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 得到微信用户，同时从缓存中获取微信登录用户信息
	 * @return
	 */
	public static SessionInfo getSessionInfo() {
		String userKey = getSessionKey();
		log.debug("userKey:"+userKey);
		SessionInfo sessionInfo = null;
		if (Cache.get(userKey)!=null) {
			sessionInfo = (SessionInfo) Cache.get(userKey);
		}
		return sessionInfo;
	}
	public static WxUser getWXUser() {
		SessionInfo sessionInfo = getSessionInfo();
		if (sessionInfo!=null) {
			WxUser wxUser=sessionInfo.getWxUser();
			log.debug("wxUser信息 openId:"+wxUser.wxOpenId + " nickName:"+wxUser.nickName);
			return wxUser;
		}
		else{
			log.debug("session为空");
        	nok(Messages.get("appletSessionBeOverdue"));
		}
		return null;
	}
	private static void setSessionInfo(SessionInfo sessionInfo){
		Cache.set(getSessionKey(), sessionInfo);
	}
	public static String getSessionKey(){
		return session.getId()+"_userkey";
	}
	public static void main(String[] args) {
		System.out.println(MobileFilter.getWXSmallHeadImage("http://wx.qlogo.cn/mmopen/0wRpPfN90ibAwzs8Tsvm1T9dia4kdMEWIHqCsYR3IomWSSVtCPvXHk0gSMsLibypxRmuXEA1HROlVWZUa3vE031bU1dBs26cyKT/0"));
	}
	
}

 