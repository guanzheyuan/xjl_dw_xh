package controllers.modules.mobile;
import controllers.comm.SessionInfo;
import controllers.modules.mobile.filter.MobileFilter;
import models.modules.mobile.WxUser;
import models.modules.mobile.WxUserInfo;
import models.modules.mobile.XjlDwChecking;
import play.Logger;
import play.i18n.Messages;
import utils.StringUtil;
public class Skip extends MobileFilter {
	
	/**
	 * 跳转到注册认证
	 */
	public static void toRegisterOrAudit() {
		WxUser wxuser = getWXUser();
		boolean flag = WxUserInfo.queryAdminInfoByFlag(wxuser.wxOpenId);
		if(!flag){
			render("modules/xjldw/mobile/user/audit.html");
		}else{
			render("modules/xjldw/mobile/user/register.html");
		}
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
		renderArgs.put("wxUser",wxuser);
		render("modules/xjldw/mobile/map/navigation.html");
	}
	
	/**
	 * 跳转到企业管理
	 */
	public static void toBusiness(){
		WxUser wxuser = getWXUser();
		WxUserInfo userInfo = WxUserInfo.getFindByOpenId(wxuser.wxOpenId);
		renderArgs.put("wxUser",wxuser);
		renderArgs.put("userInfo",userInfo);
		render("modules/xjldw/mobile/business/business_manage.html");
	}
	
	/**
	 * 跳转到考勤管理
	 */
	public static void toChecking(){
		render("modules/xjldw/mobile/business/checking.html");
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
		render("modules/xjldw/mobile/sales/sales_list.html");
	}
	
	/**
	 * 跳转到销售新增
	 */
	public static void toSalesAdd(){
		render("modules/xjldw/mobile/sales/sales_add.html");
	}
	
}
