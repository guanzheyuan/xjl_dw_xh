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
@Table(name = "xjl_dw_reply")
public class XjlDwReply extends GenericModel  {
	private static org.slf4j.Logger log = LoggerFactory.getLogger(XjlDwReply.class);
	

	@Id
	@Column(name="reply_id")
	public Long replyId;
	@Column(name="quiz_id")
	public Long quizId;
	@Column(name = "content")
	public String content;
	@Column(name = "WX_OPEN_ID")
	public String wxOpenId;
	@Column(name = "STATUS")
	public String status;
	@Column(name = "CREATE_TIME")
	public Date createTime;
	
	public static int doQueryCountByQuizid(String quizId){
		String sql = "select * from xjl_dw_reply where status='0AA' and quiz_id='"+quizId+"'";
		Map<String,String> condition = new HashMap<>();
    	SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<XjlDwReply> data = ModelUtils.queryData(1,10, ret);
		if(data.isEmpty()){
			return 0;
		}else{
			return Integer.parseInt(String.valueOf(data.get(0)));
		}
	}
	
	public static Map doQueryByPrimaryKey(String quizId){
		String sql = "select * from xjl_dw_reply where status='0AA' and quiz_id='"+quizId+"' order by create_time desc ";
		Map<String,String> condition = new HashMap<>();
    	SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<XjlDwReply> data = ModelUtils.queryData(1,999999, ret);
		return ModelUtils.createResultMap(ret, data);
	}
}
