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
import utils.StringUtil;
import utils.jpa.SQLResult;

@Entity
@Table(name = "xjl_dw_checking")
public class XjlDwChecking extends GenericModel {
	private static org.slf4j.Logger log = LoggerFactory.getLogger(XjlDwChecking.class);
	
	@Id
	@Column(name = "CHECK_ID")
	public Long checkId;
	
	@Column(name = "WORK_DATE")
	public String workDate;
	
	@Column(name = "WORK_MONTH")
	public String workMonth;
	
	@Column(name = "AM")
	public String am;
	
	@Column(name = "PM")
	public String pm;
	
	@Column(name = "WX_OPEN_ID")
	public String wxOpenId;
	
	@Column(name = "STATUS")
	public String status;
	
	@Column(name = "CREATE_TIME")
	public Date createTime;
	
	/**
	 * 打卡分页
	 * @param condition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public static Map queryWxCheckingByPage(Map<String, String> condition,
			int pageIndex, int pageSize) {
			String sql = "select * ";
			sql += "from xjl_dw_checking a ";
			sql += "where 1=1 and status='0AA' ";
			if(StringUtil.isNotEmpty(condition.get("wxOpenId"))){
				String searchKeyWord = condition.get("wxOpenId");
				sql += "and a.wx_open_id='"+searchKeyWord+"' ";
		    }
			if(StringUtil.isNotEmpty(condition.get("workDay"))){
				String searchKeyWord = condition.get("workDay");
				sql += "and a.work_Date='"+searchKeyWord+"' ";
			}
			if(StringUtil.isNotEmpty(condition.get("workMonth"))){
				String searchKeyWord = condition.get("workMonth");
				sql += "and a.WORK_MONTH='"+searchKeyWord+"' ";
			}
			SQLResult ret = ModelUtils.createSQLResult(condition, sql);
			List<XjlDwChecking> data = ModelUtils.queryData(pageIndex, pageSize, ret, XjlDwChecking.class);
			return ModelUtils.createResultMap(ret, data);
	}
	
	/**
	 * 得到正常打卡数据
	 * @return
	 */
	public static Map queryWxCheckByWorking(){
		String sql="select * from xjl_dw_checking where status='0AA'  and  am is not  null and pm is not null";
		Map<String, String> condition = new HashMap<>();
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<XjlDwChecking> data = ModelUtils.queryData(1,1000000000, ret, XjlDwChecking.class);
		return ModelUtils.createResultMap(ret, data);
	}
	
	public static XjlDwChecking queryCheckingInfoByWxOpenId(String openid,String workDay){
		int pageIndex = 1;
        int pageSize = 100;
        Map condition = new HashMap<String, String>();
        if(StringUtil.isNotEmpty(openid)){
            condition.put("wxOpenId", openid);
        }
        if(StringUtil.isNotEmpty(workDay)){
        	condition.put("workDay", workDay);
        }
        Map returnMap = queryWxCheckingByPage(condition,pageIndex,pageSize);
        List<XjlDwChecking> retData = (List<XjlDwChecking>) returnMap.get("data");
        if(!retData.isEmpty()){
        	XjlDwChecking checking = retData.get(0);
        	return checking;
        }else{
        	return null;
        }
	}
	public static int modifyAmOrPm(String checkId,String pm){
		String sql="update xjl_dw_checking set  pm='"+pm+"' where CHECK_ID='"+checkId+"'";
		Map<String, String> condition = new HashMap<String, String>();
		return ModelUtils.executeDelete(condition, sql);
	}
	
	/**
	 * 通过月份和微信id获取考勤数据
	 * @param openid
	 * @param month
	 * @return
	 */
	public static Map  queryCheckingByMonth(String openid,String month){
		String sql = "select * from xjl_dw_checking where work_month= '"+month+"' and wx_open_id='"+openid+"' order by work_date desc";
		Map<String, String> condition = new HashMap<String, String>();
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<XjlDwChecking> data = ModelUtils.queryData(1, 1000000000, ret, XjlDwChecking.class);
		return ModelUtils.createResultMap(ret, data);
	}
	
}
