package models.modules.mobile;

import java.util.Date;
import java.util.HashMap;
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
@Table(name = "xjl_dw_salary")
public class XjlDwSalary extends GenericModel {
	private static org.slf4j.Logger log = LoggerFactory.getLogger(XjlDwSalary.class);
	

	@Id
	@Column(name = "salary_id")
	public Long salaryId;
	
	@Column(name = "month")
	public String month;
	@Column(name = "basic_salary")
	public String basicSalary;
	@Column(name = "subsidy")
	public String subsidy;
	@Column(name = "bonus")
	public String bonus;
	@Column(name = "telcharge")
	public String telcharge;
	@Column(name = "float")
	public String _float;
	@Column(name = "insurance")
	public String insurance;
	@Column(name = "otherdeductions")
	public String otherdeductions;
	@Column(name = "incometax")
	public String incometax;
	@Column(name = "userinfoId")
	public Long userinfoId;
	@Column(name = "WX_OPEN_ID")
	public String wxOpenId;
	@Column(name = "STATUS")
	public String status;
	@Column(name = "CREATE_TIME")
	public Date createTime;
	@Column(name = "reportwithhold")
	public String reportwithhold;
	@Column(name = "reportwidthholdcontent")
	public String reportwidthholdContent;
	@Column(name = "otherwithholdcontent")
	public String otherwithholdcontent;
	@Column(name = "total")
	public String total;
	
	
	public static Map querySalaryByUserInfoId(String userInfoId){
		String sql = "select * from xjl_dw_salary where status='0AA' and userinfoId='"+userInfoId+"' order by month desc ";
		Map<String, String> condition = new HashMap<>();
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<XjlDwSalary> data = ModelUtils.queryData(1, 100, ret, XjlDwSalary.class);
		return  ModelUtils.createResultMap(ret, data);
	}
	public static XjlDwSalary querySalaryByUserInfoIdAndtime(String userInfoId,String month){
		String sql="select * from xjl_dw_salary where status='0AA' and userinfoId='"+userInfoId+"' and month='"+month+"'";
		Map<String, String> condition = new HashMap<>();
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<XjlDwSalary> data = ModelUtils.queryData(1, 100, ret, XjlDwSalary.class);
		if(data.isEmpty()){
			return null;
		}else{
			return data.get(0);
		}
	}
	
   public static int modifySalary(XjlDwSalary xjlDwSalary){
	   String sql="update xjl_dw_salary set basic_salary='"+xjlDwSalary.basicSalary+"',subsidy='"+xjlDwSalary.subsidy+"',"
	   		+ "bonus='"+xjlDwSalary.bonus+"',telcharge='"+xjlDwSalary.telcharge+"',float='"+xjlDwSalary._float+"',"
	   		+ "insurance='"+xjlDwSalary.insurance+"',otherdeductions='"+xjlDwSalary.otherdeductions+"',"
	   		+ "incometax='"+xjlDwSalary.incometax+"',reportwithhold='"+xjlDwSalary.reportwithhold+"',"
	   		+ "reportwidthholdcontent='"+xjlDwSalary.reportwidthholdContent+"',otherwithholdcontent='"+xjlDwSalary.otherwithholdcontent+"',"
	   		+ "total='"+xjlDwSalary.total+"' where salary_id='"+xjlDwSalary.salaryId+"'";
	   Map<String, String> condition = new HashMap<String, String>();
		return ModelUtils.executeDelete(condition, sql);
   }
}
