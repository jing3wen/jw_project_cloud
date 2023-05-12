package com.jw_server.dl.service.core.utils.socketToPython;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;

/**
 * Description: 和python端交互的工具类
 * Author: jingwen
 * DATE: 2022/9/22 22:07
 */
@Component
public class DetectFileUtils {

    //python服务器主机
    private static String HOST;
    //python服务器端口号
    private static int PORT;

    @Value("${python.socket.host}")
    private void setHost(String host){
        HOST = host;
    }
    @Value("${python.socket.port}")
    private void setPort(String port){
        PORT = Integer.parseInt(port);
    }

    public static String getType(Object obj){
        return obj.getClass().toString();
    }


    public JSONObject remoteCall(JSONObject re){

//        ApiResponse apiResponse=new ApiResponse();
//        apiResponse.setCode(200);
//        apiResponse.setMessage("call_SocketService");
//        Object object=new Object();


        String str = re.toJSONString();
        // 访问服务进程的套接字
        Socket socket = null;
        //List<Question> questions = new ArrayList<>();
        System.out.println("调用远程接口:host=>"+HOST+",port=>"+PORT);
        try {
            // 初始化套接字，设置访问服务的主机和进程端口号，HOST是访问python进程的主机名称，可以是IP地址或者域名，PORT是python进程绑定的端口号
            socket = new Socket(HOST,PORT);
            // 获取输出流对象
            OutputStream os = socket.getOutputStream();
            PrintStream out = new PrintStream(os);
            // 发送内容
            out.print(str);
            // 告诉服务进程，内容发送完毕，可以开始处理
            out.print("over");
            // 获取服务进程的输入流
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"));
            String tmp = null;
            StringBuilder sb = new StringBuilder();
            // 读取内容
            while((tmp=br.readLine())!=null)
                sb.append(tmp).append('\n');
            // 解析结果
            JSONObject res = JSONObject.parseObject(sb.toString());

            return res;
        } catch (IOException e) {
            e.printStackTrace();
            //TODO 后期开发写一些异常常量字
            if(e.getMessage().equals("Connection refused: connect")){
                throw new RuntimeException("Connection refused: connect");
            }
        } finally {
            try {if(socket!=null) socket.close();} catch (IOException e) {}
            System.out.println("远程接口调用结束.");
        }
        return null;
    }


    public JSONObject  remoteCall_update_face_dataset(){
        //编写发送数据
        JSONObject send=new JSONObject();
        send.put("code",200);
        send.put("message","call_SocketService");
        JSONObject data=new JSONObject();
        data.put("service_name","update_face_dataset()");
        send.put("data",data);
        //发送请求
        return remoteCall(send);

    }


    public JSONObject remoteCall_predictImageOrVideo(int code,String message,String serviceName,String filePath,int saveResult){
        /**
         * 发送数据格式
         *{
         *   "code":200,
         *   "message": "call_SocketService",
         *   "data":{
         *       "service_name":"predictImage()",
         *        "service_params":{
         *               "image_path":"你的application.yml中的本地上传的路径/deep_learning/face_detect/face_detect_file/to_detect_file/jingwen_undetected.jpg",
         *               "save_iamge":1   # True
         *       }
         *   }
         *}
         **/
        /**
         * 此处调用socket对文件进行检测
         * res格式: {
         * 				"code":200
         *              "message":"识别成功"
         *              "data":{
         *              		"recognition_face_list": ["yuwenxing"],
         *              		"save_file_path": "你的application.yml中的本地上传的路径/deep_learning/face_detect/face_detect_file/detect_file_result/jingwen_detected.jpg"
         *              		//视频时  'recognition_face_list': ["yuwenxing", "luohong"],
         *              	    //		 "save_file_path": "你的application.yml中的本地上传的路径/deep_learning/face_detect/face_detect_file/detect_file_result/jingwen_detected_voice.mp4"
         *
         *              	}
         *
         *
         *          }
         * **/
        JSONObject send = new JSONObject();
        //封装发送数据
        send.put("code",code);
        send.put("message",message);
        //封装data
        JSONObject data=new JSONObject();
        data.put("service_name",serviceName);
        //封装service_params
        JSONObject service_params=new JSONObject();
        if(serviceName.equals("predictVideo()")){
            service_params.put("video_path",filePath);
            service_params.put("save_video",saveResult);
        }
        else if(serviceName.equals("predictImage()")){
            service_params.put("image_path",filePath);
            service_params.put("save_image",saveResult);
        }
        data.put("service_params",service_params);
        send.put("data",data);

        //System.out.println("send："+send);
        return remoteCall(send);
    }


//    public static void main(String[] args){

//        JSONObject re = new JSONObject();
//        //封装发送数据
//        re.put("code",200);
//        re.put("message","call_SocketService");
//        //封装data
//        JSONObject service_params=new JSONObject();
//        service_params.put("video_path","img/test_vedio2.mp4");
//        service_params.put("save_video",1);
//        JSONObject data=new JSONObject();
//        data.put("service_name","predictVideo()");
//        data.put("service_params",service_params);
//        re.put("data",data);
//
//        //通过scoket远程调用
//        JSONObject res=new SocketUtils().remoteCall(re);
//
//        System.out.println(res.get("code"));
//        System.out.println(res.get("message"));
//        System.out.println(res.get("data"));
//        JSONObject res=new DetectFileUtils().remoteCall_predictImageOrVideo(200,"call_SocketService",
//                "predictVideo()","D:/SpringBoot_v2-master_upload/my_upload/file_detection/huge_undetected.mp4",1).getJSONObject("data");
//        System.out.println(res);
//
//    }
}
