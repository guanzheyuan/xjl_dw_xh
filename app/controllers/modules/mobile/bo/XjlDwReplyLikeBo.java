package controllers.modules.mobile.bo;

import models.modules.mobile.XjlDwReplyLike;
import utils.DateUtil;
import utils.SeqUtil;

public class XjlDwReplyLikeBo {

	
	public static XjlDwReplyLike save(XjlDwReplyLike xjlDwReplyLike){
		xjlDwReplyLike.likeId = SeqUtil.maxValue("xjl_dw_reply_like", "like_id");
		xjlDwReplyLike.status="0AA";
		xjlDwReplyLike.createTime = DateUtil.getNowDate();
		xjlDwReplyLike = xjlDwReplyLike.save();
		return xjlDwReplyLike;
	}
}
