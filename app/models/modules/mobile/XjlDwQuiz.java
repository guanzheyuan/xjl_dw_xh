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
@Table(name = "xjl_dw_quiz")
public class XjlDwQuiz extends GenericModel  {
	private static org.slf4j.Logger log = LoggerFactory.getLogger(XjlDwQuiz.class);
	

	@Id
	@Column(name="quiz_id")
	public Long quizId;
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
	public List<XjlDwReply> listMap = null;
	
	public static Map doQueryQuiz(Map<String, String> condition,int pageIndex, int pageSize){
		String sql="select * from xjl_dw_quiz where status='0AA'";
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<XjlDwQuiz> data = ModelUtils.queryData(pageIndex, pageSize, ret, XjlDwQuiz.class);
		return ModelUtils.createResultMap(ret, data);
	}
	
    public static XjlDwQuiz doQueryByPrimaryKey(String quizId){
    	String sql="select * from xjl_dw_quiz where status='0AA' and quiz_id='"+quizId+"'";
    	Map<String, String> condition = new HashMap<>();
    	SQLResult ret = ModelUtils.createSQLResult(condition, sql);
    	List<XjlDwQuiz> data = ModelUtils.queryData(1, 10, ret, XjlDwQuiz.class);
    	if(!data.isEmpty()){
    		data.get(0).listMap = (List<XjlDwReply>) XjlDwReply.doQueryByPrimaryKey(quizId).get("data");
    		return data.get(0);
    	}else{
    		return null;
    	}
     
    }
}
