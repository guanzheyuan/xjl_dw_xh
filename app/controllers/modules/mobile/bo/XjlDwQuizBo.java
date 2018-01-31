package controllers.modules.mobile.bo;

import models.modules.mobile.XjlDwQuiz;
import utils.DateUtil;
import utils.SeqUtil;

public class XjlDwQuizBo {

	public static XjlDwQuiz save(XjlDwQuiz xjlDwQuiz){
		xjlDwQuiz.quizId = SeqUtil.maxValue("xjl_dw_quiz", "quiz_id");
		xjlDwQuiz.status ="0AA";
		xjlDwQuiz.createTime = DateUtil.getNowDate();
		xjlDwQuiz = xjlDwQuiz.save();
		return xjlDwQuiz;
	}
}
