package models.modules.mobile;

import java.text.SimpleDateFormat;
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

import play.Logger;
import play.db.jpa.GenericModel;
import utils.StringUtil;
import utils.jpa.SQLResult;

@Entity
@Table(name = "xjl_dw_userinfo")
public class WxUserInfo  extends GenericModel{
	private static org.slf4j.Logger log = LoggerFactory.getLogger(WxUserInfo.class);
	
	@Id
	@Column(name = "USERINFO_ID")
	public Long userInfoId;
	
	@Column(name = "COMPANY_NAME")
	public String companyName;
	
	@Column(name = "COMPANY_PROVINCE")
	public String conpanyProvince;
	
	@Column(name = "COMPANY_CITY")
	public String conpanyCity;
	
	@Column(name = "COMPANY_ADDRESS")
	public String conpanyAddress;
	
	@Column(name = "USERINFO_NAME")
	public String userinfoName;
	
	@Column(name = "iphone")
	public String iphone;
	
	@Column(name = "area")
	public String area;
	
	@Column(name = "USERINFO_TYPE")
	public String userinfoType;
	
	@Column(name = "WX_OPEN_ID")
	public String wxOpenId;
	
	@Column(name = "STATUS")
	public String status;
	
	@Column(name = "CREATE_TIME")
	public Date createTime;
	
	@Column(name = "ISADMIN")
	public String isadmin;
	
	@Column(name = "audit_time")
	public String auditTime;
	
	@Transient
	public String address;
	
	@Transient
	public String userinfoTypeName;
	
	@Transient
	public String statusName;
	
	@Transient
	public String report;
	
	@Transient
	public String reportId;
	
	
	
	
	/**
	 * 根据微信Id得到用户基本信息
	 * @param openid
	 * @return
	 */
	public static WxUserInfo getFindByOpenId(String openid){
		int pageIndex = 1;
        int pageSize = 100;
        Map condition = new HashMap<String, String>();
        if(StringUtil.isNotEmpty(openid)){
            condition.put("wxOpenId", openid);
        }
        Map returnMap = queryWxUserListByPage(condition,pageIndex,pageSize);
        List<WxUserInfo> retData = (List<WxUserInfo>) returnMap.get("data");
        if(retData.isEmpty()){
        	return null;
        }else{
        	WxUserInfo wxUserInfo = retData.get(0);
        	return wxUserInfo;
        }
	}
	
	public static WxUserInfo getFindByUserInfoId(String userInfoId){
		int pageIndex = 1;
        int pageSize = 100;
        Map condition = new HashMap<String, String>();
        condition.put("userInfoId", userInfoId);
        Map returnMap = queryWxUserListByPage(condition,pageIndex,pageSize);
        List<WxUserInfo> retData = (List<WxUserInfo>) returnMap.get("data");
        if(retData.isEmpty()){
        	return null;
        }else{
        	WxUserInfo wxUserInfo = retData.get(0);
        	return wxUserInfo;
        }
	}
	/**
	 * 查询用户基本信息
	 * @param condition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public static Map queryWxUserListByPage(Map<String, String> condition,
		int pageIndex, int pageSize) {
		String sql = "select * ";
		sql += "from xjl_dw_userinfo a ";
		sql += "where 1=1 and status='0AA' ";
		if(StringUtil.isNotEmpty(condition.get("wxOpenId"))){
			String searchKeyWord = condition.get("wxOpenId");
			sql += "and a.wx_open_id='"+searchKeyWord+"' ";
	    }
		if(StringUtil.isNotEmpty(condition.get("userinfoType"))){
			String searchKeyWord = condition.get("userinfoType");
			sql += "and a.USERINFO_TYPE !='' and isadmin is null ";
	    }
		if(StringUtil.isNotEmpty(condition.get("userInfoId"))){
			String searchKeyWord = condition.get("userInfoId");
			sql += "and a.USERINFO_ID ='"+searchKeyWord+"'";
	    }
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		List<WxUserInfo> data = ModelUtils.queryData(pageIndex, pageSize, ret, WxUserInfo.class);
		for (WxUserInfo wxUserInfo : data) {
			if(StringUtil.isNotEmpty(wxUserInfo.userinfoType)){
				wxUserInfo.userinfoTypeName = "0".equals(wxUserInfo.userinfoType)?"销售":"商务";
			}
			XjlDwReport report = XjlDwReport.queryReportByMonth(condition.get("year"), condition.get("month"), wxUserInfo.wxOpenId);
				wxUserInfo.report = null ==report?"false":sf.format(report.createTime);
				wxUserInfo.reportId = null == report?"false":String.valueOf(report.reportId);
		}
		return ModelUtils.createResultMap(ret, data);
	}
	
	/**
	 * 查询申请未审核的注册信息
	 * @param condition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public static Map queryUserInfoByList(Map<String, String> condition,int pageIndex, int pageSize){
		String sql="select * from xjl_dw_userinfo where status='0AB' order by CREATE_TIME desc";
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<WxUserInfo> data = ModelUtils.queryData(1, 500, ret, WxUserInfo.class);
		for (WxUserInfo wxUserInfo : data) {
			if(StringUtil.isNotEmpty(wxUserInfo.userinfoType)){
				wxUserInfo.userinfoTypeName = "0".equals(wxUserInfo.userinfoType)?"销售":"商务";
			}
			if(StringUtil.isNotEmpty(wxUserInfo.conpanyProvince)){
				XjlDwProvinces pro = XjlDwProvinces.queryProvinceByProvinceId(wxUserInfo.conpanyProvince);
				if(null != pro){
					wxUserInfo.address = pro.provinceName;
				}
				if(StringUtil.isNotEmpty(wxUserInfo.conpanyCity)){
					XjlDwCities city = XjlDwCities.queryCitiesByProvinceId(wxUserInfo.conpanyCity, wxUserInfo.conpanyProvince);
					if(null != city){
						wxUserInfo.address += city.city;
					}
				}
				wxUserInfo.address += wxUserInfo.conpanyAddress;
			}
			
		}
		return ModelUtils.createResultMap(ret, data);
	}
	
	/**
	 * 审核记录
	 * @param condition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public static Map queryUserInfoLog(Map<String, String> condition,int pageIndex, int pageSize){
		String sql="select * from xjl_dw_userinfo where status in ('0AA','0XX') and isadmin is null order by CREATE_TIME desc";
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<WxUserInfo> data = ModelUtils.queryData(1, 500, ret, WxUserInfo.class);
		for (WxUserInfo wxUserInfo : data) {
			if(StringUtil.isNotEmpty(wxUserInfo.userinfoType)){
				wxUserInfo.userinfoTypeName = "0".equals(wxUserInfo.userinfoType)?"销售":"商务";
			}
			wxUserInfo.statusName = "0AA".equals(wxUserInfo.status)?"通过":"0XX".equals(wxUserInfo.status)?"不通过":"";
			if(StringUtil.isNotEmpty(wxUserInfo.conpanyProvince)){
				XjlDwProvinces pro = XjlDwProvinces.queryProvinceByProvinceId(wxUserInfo.conpanyProvince);
				if(null != pro){
					wxUserInfo.address = pro.provinceName;
				}
				if(StringUtil.isNotEmpty(wxUserInfo.conpanyCity)){
					Logger.info("进入城市查询");
					XjlDwCities city = XjlDwCities.queryCitiesByProvinceId(wxUserInfo.conpanyCity, wxUserInfo.conpanyProvince);
					Logger.info("城市结果"+city);
					if(null != city){
						wxUserInfo.address += city.city;
					}
				}
				wxUserInfo.address += wxUserInfo.conpanyAddress;
			}
		}
		return ModelUtils.createResultMap(ret, data);
	}
	/**
	 * 通过微信id验证是否注册
	 * @param wxOpenId
	 * @return
	 */
	public static boolean queryUserInfoByWxOpenId(String wxOpenId){
		String sql = "select * from xjl_dw_userinfo where status='0AA' and  wx_open_id='"+wxOpenId+"'";
		Map<String, String> condition = new HashMap<>();
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<WxUserInfo> data = ModelUtils.queryData(1, 500, ret, WxUserInfo.class);
		if(data.isEmpty()){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 根据管理员标记和微信ID判断登录人是否是管理员
	 * @param wxOpenId
	 * @return
	 */
	public static boolean queryAdminInfoByFlag(String wxOpenId){
		String sql="select * from xjl_dw_userinfo where status='0AA'   and  wx_open_id='"+wxOpenId+"' and isadmin='0AA' ";
		Map<String, String> condition = new HashMap<>();
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<WxUserInfo> data = ModelUtils.queryData(1, 500, ret, WxUserInfo.class);
		if(data.isEmpty()){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isRegist(String wxOpenid){
		String sql="select * from xjl_dw_userinfo where status='0AB' and  wx_open_id='"+wxOpenid+"' ";
		Map<String, String> condition = new HashMap<>();
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<WxUserInfo> data = ModelUtils.queryData(1, 500, ret, WxUserInfo.class);
		if(data.isEmpty()){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean queryShangwuOrAdmin(String wxOpenId){
		String sql="select * from xjl_dw_userinfo where status='0AA' and  wx_open_id='"+wxOpenId+"' and (USERINFO_TYPE='1' or isadmin='0AA' )  ";
		Map<String, String> condition = new HashMap<>();
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<WxUserInfo> data = ModelUtils.queryData(1, 500, ret, WxUserInfo.class);
		if(data.isEmpty()){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 修改状态
	 * @param id
	 * @param status
	 * @return
	 */
	public static int modifyUserInfoIsPass(String id,String status,String time){
		String sql="update xjl_dw_userinfo set status='"+status+"',audit_time='"+time+"' where USERINFO_ID='"+id+"'";
		Map<String, String> condition = new HashMap<String, String>();
		return ModelUtils.executeDelete(condition, sql);
	}
	
}
