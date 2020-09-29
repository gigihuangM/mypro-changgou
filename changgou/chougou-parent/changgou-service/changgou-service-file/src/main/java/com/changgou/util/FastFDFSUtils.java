package com.changgou.util;

import com.changgou.file.FastFDFSFile;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;

import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 *    文件上传
 *    文件删除
 *    文件下载
 *    文件信息获取
 *    Storage信息获取
 *    Tracker信息获取
 */
public class FastFDFSUtils {
    /**
     * 加载Tracker信息
     */
    static {
        try {
            //查找文件classpath下的文件路径
            String filename = new ClassPathResource("fdfs_client.conf").getPath();
            ClientGlobal.init(filename);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String[] upload(FastFDFSFile fastFDFSFile)throws Exception{
        //附加参数
        NameValuePair[] mate_list=new NameValuePair[1];
        mate_list[0]=new NameValuePair("author",fastFDFSFile.getAuthor());
        //创建一个Tracker访问的客户端对象TrackerClient

        TrackerServer trackerServer = getTrackerServer();
        //通过TrackerServer的连接信息可以获取Storage的连接信息，创建Storage对象存储Storage的连接信息
        StorageClient storageClient = getStorageClient(trackerServer);
        //通过StorageClient访问Storage，实现文件上传，并且获取文件上传后的信息
        /**
         * 1.上传文件的字节数组
         * 2.文件的扩展名
         * 3 附加参数
         *     返回的2个参数： upload[0] d代表的是storage的组名字
         *                     upload[1] 是文件的名字 M00/02/44/DDD.jpg
         *
         */

        String[] uploads = storageClient.upload_file(fastFDFSFile.getContent(), fastFDFSFile.getExt(), null);
        return uploads;
    }

    /**
     * 获取文件信息
     */
    public static FileInfo getFile(String groupName,String remoteFileName)throws  Exception{


        TrackerServer trackerServer = getTrackerServer();

        StorageClient storageClient=getStorageClient(trackerServer);

        return storageClient.get_file_info(groupName, remoteFileName);
    }

    /**
     * 文件的下载
     * @return
     * @throws Exception
     */

    public static InputStream downloadFile(String groupName, String remoteFileName) throws Exception{

        TrackerServer trackerServer = getTrackerServer();

        StorageClient storageClient=getStorageClient(trackerServer);

        byte[] buffer = storageClient.download_file(groupName, remoteFileName);

        return new ByteArrayInputStream(buffer);
    }

    public static void deleteFile(String groupName, String remoteFileName) throws Exception{
        TrackerServer trackerServer = getTrackerServer();

        StorageClient storageClient=getStorageClient(trackerServer);

        //删除文件
        storageClient.delete_file(groupName,remoteFileName);
    }

    /**
     * 获取Storage的信息
     */
    public static StorageServer getStorages() throws Exception{
        TrackerClient trackerClient=new TrackerClient();

        TrackerServer trackerServer = trackerClient.getConnection();

        return trackerClient.getStoreStorage(trackerServer);

    }

    /**
     * 获取storage的ip和端口
     */
    public static  ServerInfo[] getServerInfo(String groupName, String remoteFileName)throws Exception{
        TrackerClient trackerClient=new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        return trackerClient.getFetchStorages(trackerServer, groupName, remoteFileName);
    }

    public static String getTrackerInfo() throws Exception{

        TrackerServer trackerServer = getTrackerServer();
        int tracker_http_port = ClientGlobal.getG_tracker_http_port();
        String hostString = trackerServer.getInetSocketAddress().getHostString();
        String url="http://"+hostString+":"+tracker_http_port;
        return url;
    }

    /**
     * 获取TrackerServer
     * @return
     * @throws Exception
     */
    public static TrackerServer getTrackerServer()throws Exception{
        TrackerClient trackerClient=new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        return trackerServer;
    }

    public static StorageClient getStorageClient(TrackerServer trackerServer){
        StorageClient storageClient = new StorageClient(trackerServer, null);
        return storageClient;
    }

    public static void main(String[] args) throws Exception {
//        InputStream is = downloadFile("group1", "M00/00/00/wKhZb17tgqOAOW9rAACwiKEEMl8338.png");
//        FileOutputStream os=new FileOutputStream("D:/1.jpg");
//
//        byte[] buffer=new byte[1024];
//        while (is.read(buffer)!=-1){
//            os.write(buffer);
//        }
//        os.flush();
//        os.close();
//        is.close();

        //文件删除
//        deleteFile("group1", "M00/00/00/wKhZb17tl2iASLVcAAD5nXCJI34372.jpg");
//        StorageServer storages = getStorages();
//        System.out.println(storages.getStorePathIndex());
//        System.out.println(storages.getInetSocketAddress().getHostString());
//        ServerInfo[] groups = getServerInfo("group1", "M00/00/00/wKhZb17tgHCANboJAACwiKEEMl8067.png");
//        for (ServerInfo group : groups) {
//            System.out.println(group.getIpAddr());
//            System.out.println(group.getPort());
//        }
//        String trackerInfo = getTrackerInfo();
//        System.out.println(trackerInfo);
    }



}
