package controllers.modules.mobile.bo;

import models.modules.mobile.XjlDwReply;
import models.modules.mobile.XjlDwReport;
import utils.DateUtil;
import utils.SeqUtil;

public class XjlDwReplyBo {

	public static XjlDwReply save(XjlDwReply xjlDwReply){
		xjlDwReply.replyId =SeqUtil.maxValue("xjl_dw_reply", "reply_id");
		xjlDwReply.status ="0AA";
		xjlDwReply.createTime = DateUtil.getNowDate();
		xjlDwReply = xjlDwReply.save();
		return xjlDwReply;
	}
}
