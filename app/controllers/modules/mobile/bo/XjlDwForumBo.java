package controllers.modules.mobile.bo;

import models.modules.mobile.XjlDwForum;
import play.db.jpa.GenericModel;
import utils.DateUtil;
import utils.SeqUtil;

public class XjlDwForumBo {
	
	public static XjlDwForum save(XjlDwForum xjlDwForum){
		xjlDwForum.forumId = SeqUtil.maxValue("xjl_dw_forum", "forum_id");
		xjlDwForum.status="0AA";
		xjlDwForum.createTime =  DateUtil.getNowDate();
		xjlDwForum = xjlDwForum.save();
		return xjlDwForum;  
	}

}
