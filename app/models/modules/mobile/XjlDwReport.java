package models.modules.mobile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.slf4j.LoggerFactory;

import play.db.jpa.GenericModel;
import utils.jpa.SQLResult;

@Entity
@Table(name = "xjl_dw_report")
public class XjlDwReport extends GenericModel {
	private static org.slf4j.Logger log = LoggerFactory.getLogger(XjlDwReport.class);
	
	@Id
	@Column(name = "REPORT_ID")
	public Long reportId;
	
	@Column(name = "TITLE")
	public String title;
	
	@Column(name = "WORK_PROGRESS")
	public String workProgress;
	
	@Column(name = "WORK_PLAN")
	public String workPlan;
	
	@Column(name = "ISEVECTION")
	public String isevection;
	
	@Column(name = "WX_OPEN_ID")
	public String wxOpenId;
	
	@Column(name = "STATUS")
	public String status;
	
	@Column(name = "CREATE_TIME")
	public Date createTime;
	
	@Transient
	public String time;
	
	@Column(name = "month")
	public String month;
	
	@Column(name = "year")
	public String year;
	
	
	/**
	 * 列表分页
	 * @param condition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public static Map queryReportByPage(Map<String, String> condition,int pageIndex, int pageSize){
		String sql="select * from xjl_dw_report where status='0AA' ";
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<XjlDwReport> data = ModelUtils.queryData(pageIndex, pageSize, ret, XjlDwReport.class);
		return ModelUtils.createResultMap(ret, data);
	}
	
	
	
	/**
	 * 根据编号获取述职报告
	 * @param id
	 * @return
	 */
    public static XjlDwReport queryReportByid(String id){
    	String sql="select * from xjl_dw_report where REPORT_ID='"+id+"'";
    	Map<String,String> condition = new HashMap<>();
    	SQLResult ret = ModelUtils.createSQLResult(condition, sql);
    	List<XjlDwReport> data = ModelUtils.queryData(1, 10, ret, XjlDwReport.class);
    	if(data.isEmpty()){
    		return new XjlDwReport();
    	}else{
    		return data.get(0);
    	}
    }
    
    /**
     * 判断年月 是否录入过述职
     * @param year
     * @param month
     * @return
     */
    public static boolean queryReportByisSave(String year,String month,String wxopenId){
    	String sql="select * from xjl_dw_report where year='"+year+"' and month='"+month+"' and WX_OPEN_ID='"+wxopenId+"'";
    	Map<String,String> condition = new HashMap<>();
    	SQLResult ret = ModelUtils.createSQLResult(condition, sql);
    	List<XjlDwReport> data = ModelUtils.queryData(1, 10, ret, XjlDwReport.class);
    	if(data.isEmpty()){
    		return false;
    	}else{
    		return true;
    	}
    }
    
    public static XjlDwReport queryReportByMonth(String year,String month,String wxopenId){
    	String sql="select * from xjl_dw_report where year='"+year+"' and month='"+month+"' and WX_OPEN_ID='"+wxopenId+"'";
    	Map<String,String> condition = new HashMap<>();
    	SQLResult ret = ModelUtils.createSQLResult(condition, sql);
    	List<XjlDwReport> data = ModelUtils.queryData(1, 10, ret, XjlDwReport.class);
    	if(data.isEmpty()){
    		return null;
    	}else{
    		return data.get(0);
    	}
    }
    
    /**
     * 修改
     * @param reportId
     * @param work_progress
     * @param work_plan
     * @param isevection
     * @return
     */
    public static int modifyReportById(String reportId,String work_progress,String work_plan,String isevection){
    	String sql="update xjl_dw_report set work_progress='"+work_progress+"',work_plan='"+work_plan+"',isevection='"+isevection+"' where REPORT_ID='"+reportId+"'";
    	Map<String, String> condition = new HashMap<String, String>();
		return ModelUtils.executeDelete(condition, sql);
    }
}
