package models.modules.mobile;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.slf4j.LoggerFactory;

import play.db.jpa.GenericModel;
import utils.jpa.SQLResult;

@Entity
@Table(name = "xjl_dw_sales")
public class XjlDwSales extends GenericModel {
	private static org.slf4j.Logger log = LoggerFactory.getLogger(XjlDwSales.class);
	
	@Id
	@Column(name = "SALES_ID")
	public Long salesId;
	
	@Column(name = "WORK_DATE")
	public String workDate;
	
	@Column(name = "market")
	public String market;
	
	@Column(name = "returned")
	public String returned;
	
	@Column(name = "receivable")
	public String receivable;
	
	@Column(name = "client")
	public String client;
	
	@Column(name = "travel")
	public String travel;
	
	@Column(name = "WX_OPEN_ID")
	public String wxOpenId;
	
	@Column(name = "STATUS")
	public String status;
	
	@Column(name = "CREATE_TIME")
	public Date createTime;
	
	@Column(name = "userinfoId")
	public String userinfoId;
	
	public static Map querySalesByPage(Map<String, String> condition,int pageIndex, int pageSize){
		String sql = "select * from xjl_dw_sales where status='0AA'";
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<XjlDwSales> data = ModelUtils.queryData(pageIndex, pageSize, ret, XjlDwSales.class);
		return ModelUtils.createResultMap(ret, data);
	}
	
}
