package com.trun.fun.modules.loophole.service.impl;
 
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.CopyOnWriteArraySet;
 

@Component
@Slf4j
@Service
@ServerEndpoint("/websocket/{fileName}")
public class WebSocketServer {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();
 
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
 
    //接收sid
    private String fileName = "";
    private  final  String fileDirectory=System.getProperty("user.dir")+"/src/main/resources/jar/";
    private  final  String htmlDirectory=System.getProperty("user.dir")+"/src/main/resources/htmlDirectory/";

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("fileName") String fileName) throws IOException {
        System.out.println("建立连接");
        this.session = session;
        webSocketSet.add(this);     //加入set中
        this.fileName=fileName;
    }





    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        //断开连接情况下，更新主板占用情况为释放
        log.info("释放的sid为："+fileName);
        //这里写你 释放的时候，要处理的业务
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
 
    }
 
    /**
     * 收到客户端消息后调用的方法
     * @ Param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        log.info("收到来自窗口" + fileName + "的信息:" + message);
        File subFile=new File(htmlDirectory);
        if(!subFile.exists()){
            subFile.mkdir();
        }
        //执行命令生成报告
        String batPath = "D:\\dependency-check-6.2.2-release\\dependency-check\\bin\\dependency-check.bat -project"+fileName +" -s "+fileDirectory+" -o "+htmlDirectory+" -cveValidForHours 0";
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", batPath);
        processBuilder.directory();
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                sendMessage(line);
            }
            int exitCode = process.waitFor();
            sendMessage(exitCode+"");
            if (exitCode == 0) {
                System.out.println("Bat script executed successfully.");

            } else {
                System.err.println("Error executing bat script with exit code: " + exitCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendMessage(e.getMessage());
        } finally {
            // 关闭资源
            reader.close();
        }
    }


    /**
     * @ Param session
     * @ Param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }
 
    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
 
    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message, @PathParam("sid") String sid) throws IOException {
        log.info("推送消息到窗口" + sid + "，推送内容:" + message);
 
        for (WebSocketServer item : webSocketSet) {
            try {
                //这里可以设定只推送给这个sid的，为null则全部推送
                if (sid == null) {
//                    item.sendMessage(message);
                } else if (item.fileName.equals(sid)) {
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }
 
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }
 
    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }
 
    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
 
    public static CopyOnWriteArraySet<WebSocketServer> getWebSocketSet() {
        return webSocketSet;
    }




}
 