package controllers.modules.mobile;
import controllers.comm.SessionInfo;
import controllers.modules.mobile.filter.MobileFilter;
import models.modules.mobile.WxUser;
import play.Logger;
import play.i18n.Messages;
public class Skip extends MobileFilter {
	
	/**
	 * 跳转到注册认证
	 */
	public static void toRegister() {
        render("modules/xjldw/mobile/user/register.html");
    }
	
	/**
	 * 跳转到员工注册
	 */
	public static void toStaffRegister(){
		render("modules/xjldw/mobile/user/staff_register.html");
	}
	
}
