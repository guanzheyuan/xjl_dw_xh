package models.modules.mobile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.GenericModel;
import utils.jpa.SQLResult;

@Entity
@Table(name = "xjl_dw_review")
public class XjlDwReview extends GenericModel   {

	
	@Id
	@Column(name="review_id")
	public Long reviewId;
	@Column(name="forum_id")
	public Long forumId;
	@Column(name = "content")
	public String content;
	@Column(name = "wx_open_id")
	public String wxOpenId;
	@Column(name = "status")
	public String status;
	@Column(name = "create_time")
	public Date createTime;
	
	
	public static int doQueryCountByForumId(String forumId){
		String sql="select count(1) from xjl_dw_review where status='0AA' and forum_id='"+forumId+"'";
		Map<String,String> condition = new HashMap<>();
    	SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<XjlDwReview> data = ModelUtils.queryData(1,10, ret);
		if(data.isEmpty()){
			return 0;
		}else{
			return Integer.parseInt(String.valueOf(data.get(0)));
		}
	}
	
	public static Map doQueryList(String forumId){
		String sql="select * from xjl_dw_review where status='0AA' and forum_id='"+forumId+"'";
		Map<String,String> condition = new HashMap<>();
    	SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<XjlDwReview> data = ModelUtils.queryData(1,10, ret,XjlDwReview.class);
		return ModelUtils.createResultMap(ret, data);
	}
	
	
	
}
