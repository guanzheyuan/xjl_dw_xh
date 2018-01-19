package controllers.modules.mobile;
import java.util.Map;

import controllers.modules.mobile.filter.MobileFilter;
import models.modules.mobile.XjlDwCities;
import models.modules.mobile.XjlDwProvinces;
import utils.StringUtil;

public class Execute  extends MobileFilter {

	/**
	 * 省份
	 */
	public static void queryProvinces(){
		int pageIndex = StringUtil.getInteger(params.get("PAGE_INDEX"), 1);
		int pageSize = StringUtil.getInteger(params.get("PAGE_SIZE"), 100);
		Map condition = params.allSimple();
		Map ret = XjlDwProvinces.queryProvinces(condition, pageIndex, pageSize);
		ok(ret);
	}
	
	/**
	 * 关联城市
	 */
	public static void queryCity(){
		int pageIndex = StringUtil.getInteger(params.get("PAGE_INDEX"), 1);
		int pageSize = StringUtil.getInteger(params.get("PAGE_SIZE"), 100);
		Map condition = params.allSimple();
		condition.put("provinceid",params.get("provinceid"));
		Map ret = XjlDwCities.queryCitiesByProvinceId(condition, pageIndex, pageSize);
		ok(ret);
	}
	
}
