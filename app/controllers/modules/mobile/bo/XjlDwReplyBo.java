package controllers.modules.mobile.bo;

import models.modules.mobile.XjlDwReport;
import utils.DateUtil;
import utils.SeqUtil;

public class XjlDwReplyBo {

	public static XjlDwReport save(XjlDwReport xjlDwReport){
		xjlDwReport.reportId =SeqUtil.maxValue("xjl_dw_reply", "reply_id");
		xjlDwReport.status ="0AA";
		xjlDwReport.createTime = DateUtil.getNowDate();
		xjlDwReport = xjlDwReport.save();
		return xjlDwReport;
	}
}
