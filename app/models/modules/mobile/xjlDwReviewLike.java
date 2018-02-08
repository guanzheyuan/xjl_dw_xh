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
@Table(name = "xjl_dw_review_like")
public class xjlDwReviewLike extends GenericModel  {
	
	
	
	
	@Id
	@Column(name="like_id")
	public Long likeId;
	@Column(name="review_id")
	public Long reviewId;
	@Column(name = "wx_open_id")
	public String wxOpenId;
	@Column(name = "status")
	public String status;
	@Column(name = "create_time")
	public Date createTime;
	
	
	public static xjlDwReviewLike doQueryReviewLike(String wxOpenId,String reviewId){
		String sql="select * from xjl_dw_review_like where status='0AA' and wx_open_id='"+wxOpenId+"' and review_id='"+reviewId+"'";
		Map<String,String> condition = new HashMap<>();
    	SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<xjlDwReviewLike> data = ModelUtils.queryData(1,10, ret,xjlDwReviewLike.class);
		if(data.isEmpty()){
			return null;
		}else{
			return data.get(0);
		}
	}
	
	public static int doCountReviewByReviewId(String reviewId){
		String sql = "select count(1) from xjl_dw_review_like where  status='0AA' and review_id='"+reviewId+"'";
		Map<String,String> condition = new HashMap<>();
    	SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<xjlDwReviewLike> data = ModelUtils.queryData(1,10, ret);
		if(data.isEmpty()){
			return 0;
		}else{
			return Integer.parseInt(String.valueOf(data.get(0)));
		}
	}

}
