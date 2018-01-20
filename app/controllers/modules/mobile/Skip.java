package controllers.modules.mobile;
import controllers.comm.SessionInfo;
import controllers.modules.mobile.filter.MobileFilter;
import models.modules.mobile.WxUser;
import models.modules.mobile.WxUserInfo;
import play.Logger;
import play.i18n.Messages;
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
	
	/**
	 * 跳转销售风采
	 */
	public static void toMarket(){
		render("modules/xjldw/mobile/product/market.html");
	}
	
}
