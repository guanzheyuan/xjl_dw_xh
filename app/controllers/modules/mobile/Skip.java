package controllers.modules.mobile;
import java.util.HashMap;
import java.util.Map;

import controllers.comm.SessionInfo;
import controllers.modules.mobile.filter.MobileFilter;
import models.modules.mobile.WxUser;
import models.modules.mobile.WxUserInfo;
import models.modules.mobile.XjlDwChecking;
import models.modules.mobile.XjlDwSalary;
import play.Logger;
import play.i18n.Messages;
import utils.StringUtil;
import utils.WxPushMsg;
public class Skip extends MobileFilter {
	
	/**
	 * 跳转到注册认证
	 */
	public static void toRegisterOrAudit() {
		WxUser wxuser = getWXUser();
		boolean flag = WxUserInfo.isRegist(wxuser.wxOpenId);
		if(flag){
			flag = WxUserInfo.queryAdminInfoByFlag(wxuser.wxOpenId);
			if(!flag){
				render("modules/xjldw/mobile/user/audit.html");
			}else{
				render("modules/xjldw/mobile/user/register.html");
			}
		}else{
			pushMsgForRegist(wxuser.wxOpenId);
		}
		  
    }
	
	public static void pushMsgForRegist(String wxOpenId){
		 Map<String, Object> mapData = new HashMap<String, Object>();
		 Map<String, Object> mapDataSon = new HashMap<String, Object>();
		 mapDataSon.put("value","操作提醒");
	     mapDataSon.put("color", "#68A8C3");
	     mapData.put("first", mapDataSon);
	     mapDataSon = new HashMap<String, Object>();
		 mapDataSon.put("value","注册认证操作");
		 mapData.put("keyword1", mapDataSon);
		 mapDataSon = new HashMap<String, Object>();
		 mapDataSon.put("value","您已经申请提交,请勿再次注册！");
		 mapData.put("keyword2", mapDataSon);
		 mapDataSon = new HashMap<String, Object>();
		 mapDataSon.put("value","申请已经提交，请等待审核！");
		 mapDataSon.put("color","#808080");
		 mapData.put("remark", mapDataSon);
		 WxPushMsg.wxMsgPusheTmplate("bMXSVB4eAjPlJwjRBheRQ_opJTwgxNFIuGWVGf1FDlc","", mapData,wxOpenId);
	}
	
	
	/**
	 * 跳转到员工注册
	 */
	public static void toStaffRegister(){
		render("modules/xjldw/mobile/user/staff_register.html");
	}
	
	/**
	 * 跳转到产品展示页面
	 */
	public static void toProduct(){
		render("modules/xjldw/mobile/product/product.html");
	}
	
	public static void toProductInfo(){
		render("modules/xjldw/mobile/product/product_info.html");
	}
	
	/**
	 * 跳转销售风采
	 */
	public static void toMarket(){
		render("modules/xjldw/mobile/product/market.html");
	}
	
	/**
	 * 跳转到一键导航
	 */
	public static void toNavigation(){
		WxUser wxuser = getWXUser();
		//判断是否注册
	   boolean flag = WxUserInfo.queryUserInfoByWxOpenId(wxuser.wxOpenId);
		Logger.info("该用户是否注册："+flag);
		if(flag){
			 flag = WxUserInfo.isRegist(wxuser.wxOpenId);
			  Logger.info("该用户是否提交注册："+flag);
			  if(flag){
				  render("modules/xjldw/mobile/user/register.html");
			  }else{
				  renderArgs.put("wxOpenId",wxuser.wxOpenId);
				  render("modules/xjldw/mobile/error/errorhtml.html");
			  }
		}else{
			renderArgs.put("wxUser",wxuser);
			render("modules/xjldw/mobile/map/navigation.html");
		}
	}
	
	/**
	 * 跳转到企业管理
	 */
	public static void toBusiness(){
		WxUser wxuser = getWXUser();
		//判断是否注册
		boolean flag = WxUserInfo.queryUserInfoByWxOpenId(wxuser.wxOpenId);
		Logger.info("该用户是否注册："+flag);
		if(flag){
			  flag = WxUserInfo.isRegist(wxuser.wxOpenId);
			  Logger.info("该用户是否提交注册："+flag);
			  if(flag){
				  render("modules/xjldw/mobile/user/register.html");
			  }else{
				  renderArgs.put("wxOpenId",wxuser.wxOpenId);
				  render("modules/xjldw/mobile/error/errorhtml.html");
			  }
		}else{
			WxUserInfo userInfo = WxUserInfo.getFindByOpenId(wxuser.wxOpenId);
			renderArgs.put("wxUser",wxuser);
			renderArgs.put("isadmin",StringUtil.isNotEmpty(userInfo.isadmin));
			renderArgs.put("userInfo",userInfo);
			if(StringUtil.isNotEmpty(userInfo.userinfoType)){
				render("modules/xjldw/mobile/business/business_manage.html");
			}
		}
	}
	
	/**
	 * 跳转到产品咨询
	 */
	public static void toProductConsult(){
		//只有员工才能跳转
		WxUser wxuser = getWXUser();
		//判断是否注册
	    boolean flag = WxUserInfo.queryUserInfoByWxOpenId(wxuser.wxOpenId);
		Logger.info("该用户是否注册："+flag);
		if(flag){
			 flag = WxUserInfo.isRegist(wxuser.wxOpenId);
			  Logger.info("该用户是否提交注册："+flag);
			  if(flag){
				  render("modules/xjldw/mobile/user/register.html");
			  }else{
				  renderArgs.put("wxOpenId",wxuser.wxOpenId);
				  render("modules/xjldw/mobile/error/errorhtml.html");
			  }
		}else{
		}
	}
	
	/**
	 * 跳转到考勤管理
	 */
	public static void toChecking(){
		String flag = params.get("flag");
		if(StringUtil.isNotEmpty(flag)){
			renderArgs.put("noShow", false);
		}else{
			WxUserInfo userInfo = WxUserInfo.getFindByUserInfoId(params.get("userinfoId"));
			renderArgs.put("wxOpenId",null!= userInfo?userInfo.wxOpenId:"");
			renderArgs.put("noShow", true);
		}
		render("modules/xjldw/mobile/business/checking.html");
	}
	
	/**
	 * 跳转到考勤log记录
	 */
	public static void toCheckingLog(){
		renderArgs.put("month",params.get("month"));
		render("modules/xjldw/mobile/business/check_log.html");
	}
	/**
	 * 跳转到述职报告新增页面
	 */
	public static void toReportAdd(){
		render("modules/xjldw/mobile/report/report_add.html");
	}
	
	/**
	 * 跳转到述职报告修改页面
	 */
	public static void toReportModify(){
		renderArgs.put("reportType","modify");
		renderArgs.put("reportId",params.get("reportId"));
		renderArgs.put("isThis",params.get("isThis"));
		render("modules/xjldw/mobile/report/report_add.html");
	}
	

	
	/**
	 * 跳转到述职报告列表
	 */
	public static void toReportList(){
		WxUser wxuser = getWXUser();
		boolean flag = WxUserInfo.queryAdminInfoByFlag(wxuser.wxOpenId);
		if(!flag){
			render("modules/xjldw/mobile/report/report_admin.html");
		}else{
			render("modules/xjldw/mobile/report/report_list.html");
		}
	}
	
	/**
	 * 跳转到销售管理页面
	 */
	public static void toSales(){
		WxUser wxuser = getWXUser();
		boolean flag =  WxUserInfo.queryShangwuOrAdmin(wxuser.wxOpenId);
		renderArgs.put("isadminorSw", flag);
		render("modules/xjldw/mobile/sales/sales_list.html");
	}
	
	/**
	 * 跳转到销售新增
	 */
	public static void toSalesAdd(){
		render("modules/xjldw/mobile/sales/sales_add.html");
	}
	
	/**
	 * 跳转到薪资管理
	 */
	public static void toSalaryList(){
		WxUser wxuser = getWXUser();
		boolean flag = WxUserInfo.queryAdminInfoByFlag(wxuser.wxOpenId);
//		if(!flag){
//			render("modules/xjldw/mobile/salary/salary_list.html");
//			renderArgs.put("noAdd", false);
//		}else{
			WxUserInfo userinfo = WxUserInfo.getFindByUserInfoId(String.valueOf(wxuser.wxUserInfo.userInfoId));
			renderArgs.put("userinfoName",userinfo.userinfoName);
			renderArgs.put("userinfoId",userinfo.userInfoId);
			renderArgs.put("isAdmin", StringUtil.isNotEmpty(userinfo.isadmin));
			render("modules/xjldw/mobile/salary/salary_info.html");
		//}
	}
	
	/**
	 * 跳转到薪资管理信息页面
	 */
	public static void toSalaryInfo(){
		Logger.info("路过信息资源信息页面："+params.get("userinfoId"));
		WxUserInfo userinfo = WxUserInfo.getFindByUserInfoId(String.valueOf(params.get("userinfoId")));
		renderArgs.put("userinfoName",userinfo.userinfoName);
		renderArgs.put("userinfoId",userinfo.userInfoId);
		renderArgs.put("isAdmin", StringUtil.isNotEmpty(userinfo.isadmin));
		render("modules/xjldw/mobile/salary/salary_info.html");
	}
	
	/**
	 * 跳转到薪资管理新增页面
	 */
	public static void toSalaryAdd(){
		WxUserInfo userinfo = WxUserInfo.getFindByUserInfoId(String.valueOf(params.get("userinfoId")));
		renderArgs.put("userinfoName",userinfo.userinfoName);
		renderArgs.put("userinfoId",userinfo.userInfoId);
		renderArgs.put("openid",userinfo.wxOpenId);
		render("modules/xjldw/mobile/salary/salary_add.html");
	}
	
	/**
	 * 跳转到薪资说明页面
	 */
	public static void toSalaryExplain(){
		XjlDwSalary xjlDwSalary = XjlDwSalary.querySararyByPrimaryId(params.get("id"));
		renderArgs.put("otherdeductions",null != xjlDwSalary?StringUtil.isNotEmpty(xjlDwSalary.otherwithholdcontent)?xjlDwSalary.otherwithholdcontent:"无":"");
		render("modules/xjldw/mobile/salary/salary_explain.html");
	}
	
	/**
	 * 跳转到薪资考勤查看
	 */
	public static void toSalaryChecking(){
		WxUserInfo userinfo = WxUserInfo.getFindByUserInfoId(String.valueOf(params.get("userinfoId")));
		renderArgs.put("wxOpenId",userinfo.wxOpenId);
		renderArgs.put("workDate",params.get("workDate"));
		render("modules/xjldw/mobile/salary/salary_checking.html");
	}
	
}
