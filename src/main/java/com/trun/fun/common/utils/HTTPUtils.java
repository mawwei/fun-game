/** 
 * @Title:TODO  
 * @DesripAddresstion:TODO
 * @Company:CSN
 * @ClassName:LogCommonUtil.java
 * @author:Administrator
 * @CreateDate:2015年6月17日   
 * @UpdateUser:Administrator  
 * @Version:0.1 
 *    
 */ 

package com.trun.fun.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;

/** 
 * @ClassName: LogCommonUtil 
 * @DescripAddresstion: TODO 
 * @author Administrator
 * @create_date 2015年6月17日
 * @version 1.0
 */
public class HTTPUtils {

    public static final String LOCALHOST = "127.0.0.1";

    private static Logger logger = LoggerFactory.getLogger(HTTPUtils.class);




    /**
     * @author zengjie
     * @description //TODOgetIpAddr
     * @date  2019/3/6
     * @param request
     * @version 1.0      
     * @return java.lang.String
     **/
	public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Real-IP");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("X-Forwarded-For");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress =request.getRemoteAddr();
            if(LOCALHOST.equals(ipAddress)){
                //根据网卡取本机配置的ipAddress   
                InetAddress inet=null;   
                 try {   
                     inet = InetAddress.getLocalHost();   
                 } catch (UnknownHostException e) {
                     logger.error(e.getMessage());
                 }
                 if (inet != null) {
                     ipAddress= inet.getHostAddress();
                 }
               }  
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割   
        if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15   
            if(ipAddress.indexOf(",")>0){   
                ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));   
            }   
        }
        return ipAddress;
    }


    /**
     *
     * @return
     */
    public static HttpServletRequest getRequest(){
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        return sra.getRequest();
    }


    /**
     *
     * @return
     */
    public static HttpServletResponse getResponse(){
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        return sra.getResponse();
    }
}
