package models.modules.mobile;

import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.slf4j.LoggerFactory;

import play.db.jpa.GenericModel;
import utils.StringUtil;
import utils.jpa.SQLResult;


@Entity
@Table(name = "xjl_dw_cities")
public class XjlDwCities extends GenericModel {
	private static org.slf4j.Logger log = LoggerFactory.getLogger(XjlDwCities.class);

	@Id
	@Column(name = "CITIES_ID")
	public Long citiesId;
	
	@Column(name = "CITYID")
	public String cityId;
	
	@Column(name = "CITY")
	public String city;
	
	@Column(name = "PROVINCEID")
	public String provinceid;
	
	@Column(name = "STATUS")
	public String status;
	
	public static Map queryCitiesByProvinceId(Map<String, String> condition,int pageIndex, int pageSize){
		String sql="select * from xjl_dw_cities where status='0AA' ";
		if(StringUtil.isNotEmpty(condition.get("provinceid"))){
			sql+=" and PROVINCEID='"+condition.get("provinceid")+"'";
		}
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<XjlDwCities> data = ModelUtils.queryData(pageIndex, pageSize, ret, XjlDwCities.class);
		return ModelUtils.createResultMap(ret,data);
	}
}
