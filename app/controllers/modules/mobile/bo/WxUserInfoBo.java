package controllers.modules.mobile.bo;
import javax.persistence.Entity;
import javax.persistence.Table;

import models.modules.mobile.WxUserInfo;
import utils.DateUtil;
import utils.SeqUtil;

public class WxUserInfoBo {
	
	public static WxUserInfo save(WxUserInfo wxUserInfo){
		wxUserInfo.userInfoId = SeqUtil.maxValue("xjl_dw_userinfo", "USERINFO_ID");
		wxUserInfo.status="0AA";
		wxUserInfo.createTime = DateUtil.getNowDate();
		wxUserInfo = wxUserInfo.save();
		return wxUserInfo;
	}

}
