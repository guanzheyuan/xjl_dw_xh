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

import play.db.jpa.GenericModel;
import utils.jpa.SQLResult;

@Entity
@Table(name = "xjl_dw_forum")
public class XjlDwForum extends GenericModel   {

	@Id
	@Column(name="forum_id")
	public Long forumId;
	@Column(name = "title")
	public String title;
	@Column(name = "content")
	public String content;
	@Column(name = "WX_OPEN_ID")
	public String wxOpenId;
	@Column(name = "STATUS")
	public String status;
	@Column(name = "CREATE_TIME")
	public Date createTime;
	
	@Transient
	public List<XjlDwReview> reviewList = null;
	
	public static Map doQuery(Map<String, String> condition,int pageIndex, int pageSize){
		String sql = "select * from xjl_dw_forum where status='0AA' order by CREATE_TIME desc";
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<XjlDwForum> data = ModelUtils.queryData(pageIndex, pageSize, ret, XjlDwForum.class);
		return ModelUtils.createResultMap(ret, data);
	}
	
	public static XjlDwForum doQueryByPrimaryKey(String forumId){
		String sql="select * from xjl_dw_forum where status='0AA' and forum_id='"+forumId+"'";
		Map<String, String> condition = new HashMap<>();
    	SQLResult ret = ModelUtils.createSQLResult(condition, sql);
    	List<XjlDwForum> data = ModelUtils.queryData(1, 10, ret, XjlDwForum.class);
    	if(!data.isEmpty()){
    		return data.get(0);
    	}else{
    		return null;
    	}
	}
}
