package controllers;

import play.*;
import play.cache.Cache;
import play.mvc.*;
import java.util.*;
import controllers.modules.mobile.filter.MobileFilter;
import models.*;
public class Application extends Controller {

    public static void index() {
        render("modules/xjldw/mobile/my/student_add.html");
    }  
    public static void mIndex() {
    }
    
 // 根目录下的访问txt文件自动匹配到/public/txt/目录下对应的文件上
 	public static void txt() {
 		Logger.info("request.url1:"+request.url.split("xh")[0]);
 		Logger.info("request.url2:"+request.url.split("xh")[1]);
 		render("/public/txt" + request.url.split("xh")[1]);
 		//render("/public/txt" + request.url);
 	}

 	public static void timeout() {
 		render();
 	}
 	public static void login() {
 		
 	}
 	//微信绑定学生
 	public static void mlogin() {
 	}

}