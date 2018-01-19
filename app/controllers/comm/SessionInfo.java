package controllers.comm;
import java.io.Serializable;

import models.modules.mobile.WxUser;
import models.modules.mobile.WxUserInfo;
public class SessionInfo implements Serializable {
	
	private String deviceFlag;
	private WxUser wxUser;
	private WxUserInfo wxUserInfo;

	
	public WxUserInfo getWxUserInfo() {
		return wxUserInfo;
	}
	public void setWxUserInfo(WxUserInfo wxUserInfo) {
		this.wxUserInfo = wxUserInfo;
	}
	public WxUser getWxUser() {
		return wxUser;
	}
	public void setWxUser(WxUser wxUser) {
		this.wxUser = wxUser;
	}
	public String getDeviceFlag() {
		return deviceFlag;
	}

	public void setDeviceFlag(String deviceFlag) {
		this.deviceFlag = deviceFlag;
	}
}
