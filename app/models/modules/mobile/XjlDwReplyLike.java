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
@Table(name = "xjl_dw_reply_like")
public class XjlDwReplyLike extends GenericModel {

	
	@Id
	@Column(name="like_id")
	public Long likeId;
	@Column(name="reply_id")
	public Long replyId;
	@Column(name = "wx_open_id")
	public String wxOpenId;
	@Column(name = "status")
	public String status;
	@Column(name = "create_time")
	public Date createTime;
	
	
	public static XjlDwReplyLike doQueryReplyLike(String wxOpenId,String replyId){
		String sql="select * from xjl_dw_reply_like  where status='0AA' and wx_open_id='"+wxOpenId+"' and reply_id='"+replyId+"'";
		Map<String,String> condition = new HashMap<>();
    	SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<XjlDwReplyLike> data = ModelUtils.queryData(1,10, ret,XjlDwReplyLike.class);
		if(data.isEmpty()){
			return null;
		}else{
			return data.get(0);
		}
	}
	
	
	public static int doCountReplyByReplyId(String replyId){
		String sql = "select count(1) from xjl_dw_reply_like where  status='0AA' and reply_id='"+replyId+"'";
		Map<String,String> condition = new HashMap<>();
    	SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<XjlDwReplyLike> data = ModelUtils.queryData(1,10, ret);
		if(data.isEmpty()){
			return 0;
		}else{
			return Integer.parseInt(String.valueOf(data.get(0)));
		}
	}
	
}
