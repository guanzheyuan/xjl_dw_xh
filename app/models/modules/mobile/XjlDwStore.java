package models.modules.mobile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.db.jpa.GenericModel;
import utils.StringUtil;
import utils.jpa.SQLResult;

@Entity
@Table(name = "xjl_dw_store")
public class XjlDwStore extends GenericModel   {

	@Id
	@Column(name="store_id")
	public Long storeId;
	@Column(name = "title")
	public String title;
	@Column(name = "file_id")
	public String fileId;
	@Column(name = "WX_OPEN_ID")
	public String wxOpenId;
	@Column(name = "STATUS")
	public String status;
	@Column(name = "CREATE_TIME")
	public Date createTime;
	@Transient
	public String time ; 
	
	public static Map doQueryStoreList(Map<String, String> condition,int pageIndex, int pageSize){
		String sql = "select * from xjl_dw_store where status='0AA' ";
		if(StringUtil.isNotEmpty(condition.get("title"))){
			sql+=" and title like '%"+condition.get("title")+"%'";
		}
		sql+="order by CREATE_TIME desc";
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<XjlDwStore> data = ModelUtils.queryData(pageIndex, pageSize, ret, XjlDwStore.class);
		if(!data.isEmpty()){
			  SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			  for (XjlDwStore xjlDwStore : data) {
				  xjlDwStore.time = sf.format(xjlDwStore.createTime);
			}
		}
		return ModelUtils.createResultMap(ret, data);
	}
	
}
