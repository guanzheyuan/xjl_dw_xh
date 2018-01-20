package models.modules.mobile;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.slf4j.LoggerFactory;

import play.Logger;
import play.db.jpa.GenericModel;
import utils.jpa.SQLResult;

@Entity
@Table(name = "xjl_dw_provinces")
public class XjlDwProvinces extends GenericModel{

	private static org.slf4j.Logger log = LoggerFactory.getLogger(XjlDwProvinces.class);
	
	@Id
	@Column(name = "PROVINCES_ID")
	public Long provincesId;
	
	@Column(name = "PROVINCEID")
	public String provinceId;
	
	@Column(name = "PROVINCE_NAME")
	public String provinceName;
	
	@Column(name = "STATUS")
	public String status;
	
	@Transient
	public List<XjlDwCities> cityList;
	
	public static Map queryProvinces(Map<String, String> condition,int pageIndex, int pageSize){
		String sql="select * from xjl_dw_provinces where status='0AA'";
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<XjlDwProvinces> data = ModelUtils.queryData(pageIndex, pageSize, ret, XjlDwProvinces.class);
		for (XjlDwProvinces xjlDwProvinces : data) {
			if(null != xjlDwProvinces.provinceId){
				condition.put("provinceid",xjlDwProvinces.provinceId);
				xjlDwProvinces.cityList = (List<XjlDwCities>) XjlDwCities.queryCitiesByProvinceId(condition,1,500).get("data");
			}
		}
		return ModelUtils.createResultMap(ret,data);
	}
	
	public static XjlDwProvinces queryProvinceByProvinceId(String provinceId){
		String sql="select * from xjl_dw_provinces where status='0AA' and PROVINCEID='"+provinceId+"'";
		Map<String, String> condition = new HashMap<>();
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<XjlDwProvinces> data = ModelUtils.queryData(1, 10, ret, XjlDwProvinces.class);
		if(!data.isEmpty()){
			return data.get(0);
		}else{
			return new XjlDwProvinces();
		}
	}
	
}
