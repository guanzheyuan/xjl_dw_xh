package controllers.modules.mobile.bo;

import models.modules.mobile.XjlDwReview;
import utils.DateUtil;
import utils.SeqUtil;

public class XjlDwReviewBo {

	public static XjlDwReview save(XjlDwReview xjlDwReview){
		xjlDwReview.reviewId = SeqUtil.maxValue("xjl_dw_review", "review_id");
		xjlDwReview.status="0AA";
		xjlDwReview.createTime =  DateUtil.getNowDate();
		xjlDwReview = xjlDwReview.save();
		return xjlDwReview;
	}
}
