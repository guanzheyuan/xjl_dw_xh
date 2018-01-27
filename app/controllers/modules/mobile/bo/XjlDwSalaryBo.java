package controllers.modules.mobile.bo;

import models.modules.mobile.XjlDwSalary;
import utils.DateUtil;
import utils.SeqUtil;

public class XjlDwSalaryBo {
	
	public static XjlDwSalary save(XjlDwSalary xjlDwSalary){
		xjlDwSalary.salaryId = SeqUtil.maxValue("xjl_dw_salary", "salary_id");
		xjlDwSalary.status = "0AA";
		xjlDwSalary.createTime=DateUtil.getNowDate();
		xjlDwSalary = xjlDwSalary.save();
		return xjlDwSalary;
	}

}
