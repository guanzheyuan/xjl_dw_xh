package controllers.modules.mobile.bo;

import models.modules.mobile.XjlDwSales;
import utils.DateUtil;
import utils.SeqUtil;

public class XjlDwSalesBo {
	
	public static XjlDwSales save(XjlDwSales xjlDwSales){
		xjlDwSales.salesId = SeqUtil.maxValue("xjl_dw_sales", "SALES_ID");
		xjlDwSales.status="0AA";
		xjlDwSales.createTime =  DateUtil.getNowDate();
		xjlDwSales = xjlDwSales.save();
		return xjlDwSales;
	}

}
