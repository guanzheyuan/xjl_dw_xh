package controllers.modules.mobile.bo;

import models.modules.mobile.XjlDwChecking;
import utils.DateUtil;
import utils.SeqUtil;

public class XjlDwCheckingBo {

	public static XjlDwChecking save(XjlDwChecking xjlDwChecking){
		xjlDwChecking.checkId =SeqUtil.maxValue("xjl_dw_checking", "CHECK_ID");
		xjlDwChecking.status="0AA";
		xjlDwChecking.createTime = DateUtil.getNowDate();
		xjlDwChecking = xjlDwChecking.save();
		return xjlDwChecking;
	}
}
