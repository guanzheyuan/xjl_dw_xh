package controllers.modules.mobile.bo;

import models.modules.mobile.xjlDwReviewLike;
import play.db.jpa.GenericModel;
import utils.DateUtil;
import utils.SeqUtil;

public class XjlDwReviewLikeBo {
	
	public static xjlDwReviewLike save(xjlDwReviewLike xjlDwReviewLike){
		xjlDwReviewLike.likeId = SeqUtil.maxValue("xjl_dw_review_like", "like_id");
		xjlDwReviewLike.status="0AA";
		xjlDwReviewLike.createTime =DateUtil.getNowDate();
		xjlDwReviewLike = xjlDwReviewLike.save();
		return xjlDwReviewLike;
	}

}
