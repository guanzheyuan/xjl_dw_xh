package controllers.modules.mobile.bo;

import models.modules.mobile.XjlDwReport;
import utils.DateUtil;
import utils.SeqUtil;

public class XjlDwReportBo {
	
	public static XjlDwReport save(XjlDwReport xjlDwReport){
		xjlDwReport.reportId = SeqUtil.maxValue("xjl_dw_report", "REPORT_ID");
		xjlDwReport.status="0AA";
		xjlDwReport.createTime =  DateUtil.getNowDate();
		xjlDwReport = xjlDwReport.save();
		return xjlDwReport;
	}

}
