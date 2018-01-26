package controllers.modules.mobile;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.fabric.xmlrpc.base.Array;

import controllers.modules.mobile.bo.WxUserInfoBo;
import controllers.modules.mobile.bo.XjlDwCheckingBo;
import controllers.modules.mobile.bo.XjlDwReportBo;
import controllers.modules.mobile.bo.XjlDwSalesBo;
import controllers.modules.mobile.filter.MobileFilter;
import models.modules.mobile.WxUser;
import models.modules.mobile.WxUserInfo;
import models.modules.mobile.XjlDwChecking;
import models.modules.mobile.XjlDwCities;
import models.modules.mobile.XjlDwProvinces;
import models.modules.mobile.XjlDwReport;
import models.modules.mobile.XjlDwSales;
import play.Logger;
import utils.DateUtil;
import utils.StringUtil;

public class Execute  extends MobileFilter {
	
	
	private final static String GOTO="09:00:00";
	private final static String GOBACK="17:00:00";

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
	 * 注册认证保存
	 */
	public static void saveSign(){
		//得到参数
		WxUser wxuser = getWXUser();
		String companyName = params.get("companyName");
		String companyProvince = params.get("companyProvinces");
		String companyCity = params.get("companyCity");
		String companyAddress = params.get("companyAddress");
		String name = params.get("name");
		String iphone = params.get("iphone");
		String userType = params.get("userType");
		String arer  = params.get("arer");
		//组装参数
		WxUserInfo userinfo = new WxUserInfo();
		userinfo.companyName = StringUtil.isNotEmpty(companyName)?companyName:"";
		userinfo.conpanyProvince = StringUtil.isNotEmpty(companyProvince)?companyProvince:"";
		userinfo.conpanyCity = StringUtil.isNotEmpty(companyCity)?companyCity:"";
		userinfo.conpanyAddress = StringUtil.isNotEmpty(companyAddress)?companyAddress:"";
		userinfo.userinfoName = StringUtil.isNotEmpty(name)?name:"";
		userinfo.iphone = StringUtil.isNotEmpty(iphone)?iphone:"";
		userinfo.userinfoType = StringUtil.isNotEmpty(userType)?userType:"";
		userinfo.area = StringUtil.isNotEmpty(arer)?arer:"";
		userinfo.wxOpenId = wxuser.wxOpenId;
		WxUserInfoBo.save(userinfo);
	}
	
	/**
	 * 通过微信ID 验证是否注册
	 */
	public static void doVaildUserInfo(){
		WxUser wxuser = getWXUser();
		boolean flag = WxUserInfo.queryUserInfoByWxOpenId(wxuser.wxOpenId);
		ok(flag);
	}
	
	/**
	 * 查询待审核的注册信息
	 */
	public static void doQueryUserInfoForStay(){
		int pageIndex = StringUtil.getInteger(params.get("PAGE_INDEX"), 1);
		int pageSize = StringUtil.getInteger(params.get("PAGE_SIZE"), 100);
		Map condition = params.allSimple();
		Map ret = WxUserInfo.queryUserInfoByList(condition, pageIndex, pageSize);
		ok(ret);
	}
	
	/**
	 * 查询审核记录
	 */
	public static void doQueryUserInfoStayLog(){
		int pageIndex = StringUtil.getInteger(params.get("PAGE_INDEX"), 1);
		int pageSize = StringUtil.getInteger(params.get("PAGE_SIZE"), 100);
		Map condition = params.allSimple();
		Map ret = WxUserInfo.queryUserInfoLog(condition, pageIndex, pageSize);
		ok(ret);
	}
	/**
	 * 是否通过审核
	 */
	public static void doModifyUserInfoIsPass(){
		String status = params.get("status");
		String id = params.get("id");
		int ret = WxUserInfo.modifyUserInfoIsPass(id, status);
		ok(ret);
	}
	
	/**
	 * 查询打开记录是否存在
	 */
	public static void doQueryCheckingLog(){
		WxUser wxuser = getWXUser();
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		XjlDwChecking checking = XjlDwChecking.queryCheckingInfoByWxOpenId(wxuser.wxOpenId,sdf.format(d));
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH)+1;
		Map ret = XjlDwChecking.queryCheckingByMonth(wxuser.wxOpenId,String.valueOf(month));
		int workDay = 0;
		Logger.info("得到工作日集合");
		if(null != ret){
			List<XjlDwChecking> data  = (List<XjlDwChecking>) ret.get("data");
			for (XjlDwChecking xjlDwChecking : data) {
				Logger.info("记录编号"+xjlDwChecking.checkId);
				workDay ++;
			}
		}
		String checkFlag = null == checking?"X":StringUtil.isNotEmpty(checking.pm)?"A":"B";
		Map<String,Object> _tempMap = new HashMap<>();
		_tempMap.put("checkFlag", checkFlag);
		_tempMap.put("workDay", workDay);
		ok(_tempMap);
	}
	/**
	 * 新增考勤记录(上班)
	 */
	public static void doChecking(){
		WxUser wxuser = getWXUser();
	    Date d = new Date();
	    String checkFlag = params.get("checkFlag");
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		XjlDwChecking check = new XjlDwChecking();
		check.workDate = sdf.format(d);
		Calendar cal = Calendar.getInstance();
		check.workMonth = String.valueOf(cal.get(Calendar.MONTH)+1);
		DateFormat df3 = DateFormat.getTimeInstance();
		String am = "";
		if(d.getHours() < 10){
			am ="0"+d.getHours()+":"; 
		}else{
			am =d.getHours()+":"; 
		}
		if(d.getMinutes() <10){
			am+="0"+d.getMinutes()+":";
		}else{
			am+=d.getMinutes()+":";
		}
		if(d.getSeconds() <10){
			am+="0"+d.getSeconds();
		}else{
			am+=d.getSeconds();
		}
		if("X".equals(checkFlag)){
			check.am = am;
		}
		check.wxOpenId = wxuser.wxOpenId;
		check = XjlDwCheckingBo.save(check);
		ok(check);
	}
	/**
	 * 下班打卡记录
	 */
	public static void doCheckingOver(){
	    Date d = new Date();
	    WxUser wxuser = getWXUser();
	    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
	    DateFormat df3 = DateFormat.getTimeInstance();
	    String pm = "";
		if(d.getHours() < 10){
			pm ="0"+d.getHours()+":"; 
		}else{
			pm =d.getHours()+":"; 
		}
		if(d.getMinutes() <10){
			pm+="0"+d.getMinutes()+":";
		}else{
			pm+=d.getMinutes()+":";
		}
		if(d.getSeconds() <10){
			pm+="0"+d.getSeconds();
		}else{
			pm+=d.getSeconds();
		}
	    XjlDwChecking.modifyAmOrPm(wxuser.wxOpenId, pm,sf.format(d));
	    ok();
	}
	
	/**
	 * 查询所有月份数据
	 */
	public static void doQueryCheckingByGroupDate(){
		WxUser wxuser = getWXUser();
		Map ret = XjlDwChecking.queryCheckingGroupDate(wxuser.wxOpenId);
		List<Map<String,Object>> dataList = new ArrayList<>();
		if(null != ret){
			Map<String,Object> _temp = null;
			List<XjlDwChecking> data  = (List<XjlDwChecking>) ret.get("data");
			Logger.info("循环得到数据"+data.size());
			if(!data.isEmpty()){
				String flag = "";
				String _time ="";
				String _month = "";
				List<XjlDwChecking> _data = null;
				for (int i = 0; i < data.size(); i++) {
					_temp = new HashMap();
					 _time = String.valueOf(data.get(i)).substring(0, String.valueOf(data.get(i)).lastIndexOf("-"));
					 _month = _time.substring(_time.indexOf("-")+1, _time.length());
					 if(!_time.equals(flag)){
						flag = _time;
						_temp.put("workDate",_time);
						_temp.put("month", _month);
						_data = (List<XjlDwChecking>) XjlDwChecking.queryCheckingByMonth(wxuser.wxOpenId,String.valueOf(Integer.parseInt(_month))).get("data");
						Logger.info("统计出勤："+_data.size());
						_temp.put("turn",null==_data?0:_data.size());
						dataList.add(_temp);
					}
					
				}
			}
		}
		ok(dataList);
	}
	
	/**
	 * 月份查询考勤
	 * @throws ParseException 
	 */
	public static void doQueryCheckingByMonth() throws ParseException{
		WxUser wxuser = getWXUser();
		String month = params.get("month");
		Map ret = XjlDwChecking.queryCheckingByMonth(wxuser.wxOpenId, month);
		List<Map<String,Object>> listMap = new ArrayList<>();
		if(null != ret){
			//当前时间
	        Date now = new Date();
	        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			List<XjlDwChecking> data = (List<XjlDwChecking>) ret.get("data");
			Map<String,Object>  _map = null;
			Calendar calendar = Calendar.getInstance();  
			for (XjlDwChecking xjlDwChecking : data) {
				_map = new HashMap<>();
				_map.put("workday",xjlDwChecking.workDate);
				_map.put("am",xjlDwChecking.am);
				_map.put("pm",xjlDwChecking.pm);
				_map.put("amval",xjlDwChecking.am.compareTo(GOTO)<1);
				if(StringUtil.isNotEmpty(xjlDwChecking.pm)){
					Logger.info("记录："+xjlDwChecking.pm+">>"+xjlDwChecking.pm.compareTo(GOBACK));
				}
				_map.put("pmval", StringUtil.isNotEmpty(xjlDwChecking.pm)?xjlDwChecking.pm.compareTo(GOBACK)>=1:false);
				_map.put("isNow",sf.format(now).equals(xjlDwChecking.workDate));
				listMap.add(_map);
			}
			//统计是否有漏打卡日期
			if(!listMap.isEmpty()){
				int count = 0;
					if(count == 0){
						Date theDate=calendar.getTime();
						GregorianCalendar gcLast=(GregorianCalendar)Calendar.getInstance();
						gcLast.setTime(theDate);
						//设置为第一天
						gcLast.set(Calendar.DAY_OF_MONTH, 1);
						String day_first=sf.format(gcLast.getTime());
						boolean flag = false;
						Logger.info("这个月第一天"+sf.format(gcLast.getTime()));
						calendar.setTime(gcLast.getTime());  
					    calendar.add(Calendar.DAY_OF_MONTH, -1);  
						for (int i = 1; i < 30+1; i++) {
							if(calendar.getTime().getTime() <now.getTime()){
								Logger.info("小于今天日期"+sf.format(calendar.getTime()));
								calendar.setTime(gcLast.getTime());  
							    calendar.add(Calendar.DAY_OF_MONTH, +i);  
							    flag = XjlDwChecking.isnotCheckValid(sf.format(calendar.getTime()),wxuser.wxOpenId);
							    if(!flag){
									_map = new HashMap<>();
									_map.put("workday",sf.format(calendar.getTime()));
									_map.put("am","");
									_map.put("pm","");
									_map.put("amval",false);
									_map.put("pmval", false);
									_map.put("isNow",false);
									listMap.add(_map);
								}
							}
							
						   Collections.sort(listMap,new Comparator<Map<String,Object>>(){

							@Override
							public int compare(Map<String, Object> o1, Map<String, Object> o2) {
								 return o2.get("workday").toString().compareTo(o1.get("workday").toString());
							}
							   
						   });
						}
						
						 
					}
					count++;
			}
		}
		ok(listMap);
	}
	
	/**
	 * 统计考勤
	 * @throws ParseException 
	 */
	public static void doQueryCheckingAStatistics() throws ParseException{
		String day = params.get("month");
		WxUser wxuser = getWXUser();
		String[] monthArr =  day.split("-");
		String year = monthArr[0];
		String month = monthArr[1];
		int pageIndex = StringUtil.getInteger(params.get("PAGE_INDEX"), 1);
		int pageSize = StringUtil.getInteger(params.get("PAGE_SIZE"), 100);
		Map<String, String> condition = params.allSimple();
		condition.put("workMonth",String.valueOf(Integer.parseInt(month)));
		condition.put("wxOpenId",wxuser.wxOpenId);
		Map ret = XjlDwChecking.queryWxCheckingByPage(condition, pageIndex, pageSize);
		int quanqin = 0;
		int chidao = 0;
		int zaotui = 0;
		int queqing = 0;
		if(null != ret){
			List<XjlDwChecking> data = (List<XjlDwChecking>) ret.get("data");
			for (XjlDwChecking xjlDwChecking : data) {
				//全勤
				if(xjlDwChecking.am.compareTo(GOTO)<1 && xjlDwChecking.pm.compareTo(GOBACK)>1){
					quanqin ++;
					Logger.info("全勤"+quanqin);
				}
				Logger.info("迟到:"+xjlDwChecking.am+">>"+xjlDwChecking.am.compareTo(GOTO));
				//迟到
				if(xjlDwChecking.am.compareTo(GOTO)>=1){
					chidao++;
					Logger.info("迟到"+chidao);
				}
				//早退
				if(StringUtil.isNotEmpty(xjlDwChecking.pm)&&xjlDwChecking.pm.compareTo(GOBACK)>=1){
					zaotui++;
					Logger.info("早退"+zaotui);
				}
			}
			//缺勤
			Logger.info("日期："+Integer.parseInt(year)+"月："+Integer.parseInt(month));
			List<Date> dateDate = DateUtil.getDates(Integer.parseInt(year), Integer.parseInt(month));
			queqing = dateDate.size() - quanqin;
		}
		Map<String,Object> _temp = new HashMap<>();
		_temp.put("quanqin", quanqin);
		_temp.put("chidao", chidao);
		_temp.put("zaotui", zaotui);
		_temp.put("queqing", queqing);
		ok(_temp);
	}
	
	/**
	 * 述职新增
	 */
	public static void doSaveReport(){
		WxUser wxuser = getWXUser();
		String workProgress = params.get("workProgress");
		String workPlan = params.get("workPlan");
		String isevection = params.get("isevection");
		//当前时间
        Date now = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月");
		XjlDwReport xjlDwReport = new XjlDwReport();
		xjlDwReport.workProgress = workProgress;
		Calendar cal = Calendar.getInstance();
		xjlDwReport.month = String.valueOf(cal.get(Calendar.MONTH)+1);
		xjlDwReport.year = String.valueOf(cal.get(Calendar.YEAR));
		xjlDwReport.workPlan = workPlan;
		xjlDwReport.title=sf.format(now)+"述职报告";
		xjlDwReport.isevection = isevection;
		xjlDwReport.wxOpenId = wxuser.wxOpenId;
		xjlDwReport = XjlDwReportBo.save(xjlDwReport);
		ok(xjlDwReport);
	}
	
	/**
	 * 述职修改
	 */
	public static void modifyReport(){
		String workProgress = params.get("workProgress");
		String workPlan = params.get("workPlan");
		String isevection = params.get("isevection");
		String reportId = params.get("reportId");
		Logger.info("");
		XjlDwReport.modifyReportById(reportId, workProgress, workPlan, isevection);
		ok();
	}
	
	
	/**
	 * 根据时间统计员工的述职报告数量
	 */
	public static void doQueryCountReport(){
		String _year = params.get("year");
		String _month = params.get("month");
		int ret = XjlDwReport.queryCountReportForAdmin(_year, String.valueOf(Integer.parseInt(_month)));
		ok(ret);
	}
	
	/**
	 * 根据reportid 修改内容
	 */
	public static void doQueryReportById(){
		String id = params.get("reportId");
		XjlDwReport report = XjlDwReport.queryReportByid(id);
		ok(report);
	}
	/**
	 * 述职列表
	 */
	public static void doQueryReportList(){
		int pageIndex = StringUtil.getInteger(params.get("PAGE_INDEX"), 1);
		int pageSize = StringUtil.getInteger(params.get("PAGE_SIZE"), 100);
		Map condition = params.allSimple();
		WxUser wxuser = getWXUser();
		condition.put("wxOpenId",wxuser.wxOpenId);
		Map ret = XjlDwReport.queryReportByPage(condition, pageIndex, pageSize);
		List<Map<String,Object>> listMap = new ArrayList<>();
		Map<String,Object> _map = null;
		List<XjlDwReport> newDate = new ArrayList<>();
		String _year = "";
		if(null != ret){
			//得到当前月份
			Calendar cal = Calendar.getInstance();
			int month = cal.get(Calendar.MONTH)+1;
			String year = String.valueOf(cal.get(Calendar.YEAR));
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			List<XjlDwReport> data = (List<XjlDwReport>) ret.get("data");
			int allmonth = 12;
			for (XjlDwReport xjlDwReport : data) {
				int flag = 0;
				 //本年
				if(xjlDwReport.year.equals(year)){
					if(xjlDwReport.year.compareTo(_year) != 0){
						if(flag == 0){
							_year = xjlDwReport.year;
							newDate.add(xjlDwReport);
						}
					}
				}else{
					//非本年
					if(xjlDwReport.year.compareTo(_year) != 0){
						if(flag == 0){
							_year = xjlDwReport.year;
							newDate.add(xjlDwReport);
						}
					}
				}
			}
			if(!newDate.isEmpty()){
				for (XjlDwReport xjlDwReport : newDate) {
					for (int i = 1; i < allmonth+1; i++) {
						_map = new HashMap<>();
						//本年
						if(xjlDwReport.year.equals(year)){
							if(i<=Integer.parseInt(xjlDwReport.month)){
								_map.put("title",year+"年"+i+"月");
								_map.put("isSave", XjlDwReport.queryReportByisSave(year,String.valueOf(i), wxuser.wxOpenId));
								_map.put("isThis",i==month);
								XjlDwReport report = XjlDwReport.queryReportByMonth(year, String.valueOf(i), wxuser.wxOpenId);
								Logger.info("述职"+report);
								if(StringUtil.isNotEmpty(report)){
									_map.put("reportId",report.reportId);
									_map.put("createtime",sf.format(report.createTime));
								}
								listMap.add(_map);
							}
						}else{
							if(i>=Integer.parseInt(xjlDwReport.month)){
								Logger.info("遍历循环数据："+xjlDwReport.year+"年"+i+"月");
								_map.put("title",xjlDwReport.year+"年"+i+"月");
								_map.put("isSave", XjlDwReport.queryReportByisSave(xjlDwReport.year,String.valueOf(i), wxuser.wxOpenId));
								_map.put("isThis",false);
								XjlDwReport report = XjlDwReport.queryReportByMonth(xjlDwReport.year, String.valueOf(i), wxuser.wxOpenId);
								Logger.info("述职"+report);
								if(StringUtil.isNotEmpty(report)){
									_map.put("reportId",report.reportId);
									_map.put("createtime",sf.format(report.createTime));
								}
								listMap.add(_map);
							}
						}
					}
				}
			}
		}
		ok(listMap);
	}
	
	/**
	 * 判断当年当月是否录入过述职
	 */
	public static void isSaveReport(){
		WxUser wxuser = getWXUser();
		Calendar cal = Calendar.getInstance();
		String month = String.valueOf(cal.get(Calendar.MONTH)+1);
		String year = String.valueOf(cal.get(Calendar.YEAR));
		boolean flag = XjlDwReport.queryReportByisSave(year, month, wxuser.wxOpenId);
		ok(flag);
	}
	
	/**
	 * 查询有效员工信息
	 */
	public static void doQueryWxUserInfo(){
		int pageIndex = StringUtil.getInteger(params.get("PAGE_INDEX"), 1);
		int pageSize = StringUtil.getInteger(params.get("PAGE_SIZE"), 100);
		Map condition = params.allSimple();
		//得到当前月份
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH)+1;
		String year = String.valueOf(cal.get(Calendar.YEAR));
		condition.put("userinfoType","true");
		String _year = params.get("year");
		String _month = params.get("month");
		Logger.info("得到年："+_year+""+_month);
		condition.put("year",StringUtil.isEmpty(_year)?year:_year);
		condition.put("month",StringUtil.isEmpty(_month)?String.valueOf(month):String.valueOf(Integer.parseInt(_month)));
		Map ret = WxUserInfo.queryWxUserListByPage(condition, pageIndex, pageSize);
		ok(ret);
	}
	
	/**
	 * 保存销售数据
	 * @throws ParseException 
	 */
	public static void doSaveWxUserInfo() throws ParseException{
		String userInfo = params.get("userinfoId");
		String month = params.get("month");
		String market = params.get("market");
		String returned = params.get("returned");
		String receivable = params.get("receivable");
		String client = params.get("client");
		String travel = params.get("travel");
		XjlDwSales sales = new XjlDwSales();
		WxUser wxuser = getWXUser();
		sales.userinfoId = userInfo;
		sales.workDate =  month;
		sales.market = market;
		sales.returned = returned;
		sales.receivable = receivable;
		sales.client = client;
		sales.travel = travel;
		sales.wxOpenId = wxuser.wxOpenId;
		XjlDwSalesBo.save(sales);
	}

	/**
	 * 销售管理页面
	 */
	public static void doQuerySalesByPage(){
		String width = params.get("width");
		int pageIndex = StringUtil.getInteger(params.get("PAGE_INDEX"), 1);
		int pageSize = StringUtil.getInteger(params.get("PAGE_SIZE"), 100);
		Map condition = params.allSimple();
		//得到所有的销售管理数据
		Map ret = XjlDwSales.querySalesByPage(condition, pageIndex, pageSize);
		List<Map<String,Object>> _data = new ArrayList<>();
	    if(null!=ret){
	    	List<XjlDwSales> data = (List<XjlDwSales>) ret.get("data");
	    	int _width = Integer.parseInt(width);
	    	int count = 0;
	    	WxUserInfo userinfo = null;
	    	String userInfoId = "";
	    	Map<String,Object> _temp = null;
	    	Map<String,Object> _temp1 = null;
	    	List<Map<String,Object>> listMap = null;
	    	List<XjlDwSales> salesList = null;
	    	//遍历销售管理数据组装
	    	for (XjlDwSales xjlDwSales : data) {
	    		if(!xjlDwSales.userinfoId.equals(userInfoId)){
	    			userInfoId = xjlDwSales.userinfoId;
	    			_temp = new HashMap<>();
	    			userinfo = WxUserInfo.getFindByUserInfoId(String.valueOf(xjlDwSales.userinfoId));
	    			_temp.put("username", userinfo.userinfoName);
	    			Logger.info("username:"+userinfo.userinfoName);
	    			 salesList = (List<XjlDwSales>) XjlDwSales.querySalesByWxOpenId(userInfoId).get("data");
	    			 Logger.info("销售管理数量:"+salesList.size());
	    			 listMap = new ArrayList<>();
	    			 for (XjlDwSales xjlDwSales1 : salesList) {
	    				 _temp1 = new HashMap<>();
	    				 Logger.info("销售日期："+xjlDwSales1.workDate);
	    				 _temp1.put("workDate", xjlDwSales1.workDate);
	    				 _temp1.put("client", xjlDwSales1.client);
	    				 _temp1.put("market", xjlDwSales1.market);
	    				 _temp1.put("receivable",xjlDwSales1.receivable);
	    				 _temp1.put("returned",xjlDwSales1.returned);
	    				 _temp1.put("travel",xjlDwSales1.travel);
	    				  if(count<4){
		 						_temp1.put("width", _width);
		 					}else{
		 						_temp1.put("width",_width+2);
		 				 }
	    				 listMap.add(_temp1);
	    			 }
	    			 _temp.put("saleList", listMap);
	    			 _data.add(_temp);
	    		}
			}
	    }
		ok(_data);
	}
	/**
	 * 根据微信编号获取自己的销售管理数据（销售）
	 */
	public static void doQuerySalesPage(){
		String width = params.get("width");
		WxUser wxuser = getWXUser();
		WxUserInfo userinfo = WxUserInfo.getFindByOpenId(wxuser.wxOpenId);
		Map ret  = XjlDwSales.querySalesByWxOpenId(String.valueOf(userinfo.userInfoId));
		if(null != ret){
			int count = 0;
			int _width = Integer.parseInt(width);
			List<XjlDwSales> data = (List<XjlDwSales>) ret.get("data");
			for (XjlDwSales xjlDwSales : data) {
				if(count == 0){
					xjlDwSales.userName = userinfo.userinfoName;
				}
				if(count<4){
					xjlDwSales.width = _width;
				}else{
					xjlDwSales.width = _width+2;
				}
				count++;
			}
		}
		ok(ret);
	}
}
