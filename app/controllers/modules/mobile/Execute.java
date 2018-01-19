package controllers.modules.mobile;
import java.util.Map;

import controllers.modules.mobile.bo.WxUserInfoBo;
import controllers.modules.mobile.filter.MobileFilter;
import models.modules.mobile.WxUser;
import models.modules.mobile.WxUserInfo;
import models.modules.mobile.XjlDwCities;
import models.modules.mobile.XjlDwProvinces;
import utils.StringUtil;

public class Execute  extends MobileFilter {

	/**
	 * 省份
	 */
	public static void queryProvinces(){
		int pageIndex = StringUtil.getInteger(params.get("PAGE_INDEX"), 1);
		int pageSize = StringUtil.getInteger(params.get("PAGE_SIZE"), 100);
		Map condition = params.allSimple();
		Map ret = XjlDwProvinces.queryProvinces(condition, pageIndex, pageSize);
		ok(ret);
	}
	
	/**
	 * 注册认证保存
	 */
	public static void saveSign(){
		//得到参数
		WxUser wxuser = getWXUser();
		String companyName = params.get("companyName");
		String companyProvince = params.get("companyProvinces");
		String companyCity = params.get("companyCity");
		String companyAddress = params.get("companyAddress");
		String name = params.get("name");
		String iphone = params.get("iphone");
		String userType = params.get("userType");
		String arer  = params.get("arer");
		//组装参数
		WxUserInfo userinfo = new WxUserInfo();
		userinfo.companyName = StringUtil.isNotEmpty(companyName)?companyName:"";
		userinfo.conpanyProvince = StringUtil.isNotEmpty(companyProvince)?companyProvince:"";
		userinfo.conpanyCity = StringUtil.isNotEmpty(companyCity)?companyCity:"";
		userinfo.conpanyAddress = StringUtil.isNotEmpty(companyAddress)?companyAddress:"";
		userinfo.userinfoName = StringUtil.isNotEmpty(name)?name:"";
		userinfo.iphone = StringUtil.isNotEmpty(iphone)?iphone:"";
		userinfo.userinfoType = StringUtil.isNotEmpty(userType)?userType:"";
		userinfo.area = StringUtil.isNotEmpty(arer)?arer:"";
		userinfo.wxOpenId = wxuser.wxOpenId;
		WxUserInfoBo.save(userinfo);
	}
	
	/**
	 * 通过微信ID 验证是否注册
	 */
	public static void doVaildUserInfo(){
		WxUser wxuser = getWXUser();
		boolean flag = WxUserInfo.queryUserInfoByWxOpenId(wxuser.wxOpenId);
		ok(flag);
	}
	
	/**
	 * 查询待审核的注册信息
	 */
	public static void doQueryUserInfoForStay(){
		int pageIndex = StringUtil.getInteger(params.get("PAGE_INDEX"), 1);
		int pageSize = StringUtil.getInteger(params.get("PAGE_SIZE"), 100);
		Map condition = params.allSimple();
		Map ret = WxUserInfo.queryUserInfoByList(condition, pageIndex, pageSize);
		ok(ret);
	}
	/**
	 * 是否通过审核
	 */
	public static void doModifyUserInfoIsPass(){
		String status = params.get("status");
		String id = params.get("id");
		int ret = WxUserInfo.modifyUserInfoIsPass(id, status);
		ok(ret);
	}
}
