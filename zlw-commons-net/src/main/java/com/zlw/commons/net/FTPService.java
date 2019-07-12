package com.zlw.commons.net;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class FTPService {
    private static ExecutorService executorService = Executors.newCachedThreadPool();//FTP后台心跳线程
    public Logger logger = LoggerFactory.getLogger(FTPService.class);
    private String userName = "ftpUser";         //FTP 登录用户名
    private String password = "ftp@admin";         //FTP 登录密码
    private String host = "10.128.31.132";                     //FTP 服务器地址IP地址
    private int port;                        //FTP 端口
    private Properties property = null;    //属性集
    private String configFile = "";    //配置文件的路径名
    private FTPClient ftpClient = null; //FTP 客户端代理
    private String ftpServerType;
    private String basePath;//应用系统基础路径
    private String ftpBasePath = "/home/ftpUser";//上传服务基础路径
    private String encoding = "UTF-8";

    /**
     * 下载文件
     *
     * @param remoteFileName 服务器上的文件名
     * @param outputStream   本地输出流
     * @return true 下载成功，false 下载失败
     */
    public boolean downloadFile(String remoteFileName, OutputStream outputStream) {
        boolean flag = false;
        // 下载文件
        BufferedOutputStream buffOut = null;
        try {
            logger.debug("Downloading by ftp:" + remoteFileName);
            buffOut = new BufferedOutputStream(outputStream);
            String remotePath = contractPath(ftpBasePath, basePath);
            remotePath = contractPath(remotePath, remoteFileName);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            flag = ftpClient.retrieveFile(getPath(remotePath), buffOut);
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("本地文件下载失败！", e);
            throw new FtpException("本地文件下载失败", e);
        } finally {
            try {
                if (buffOut != null)
                    buffOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * 删除一个文件
     */
    private boolean deleteFile(String filename) {
        boolean flag = true;
        try {
            flag = ftpClient.deleteFile(filename);
            if (flag) {
                System.out.println("删除文件成功！");
            } else {
                System.out.println("删除文件失败！");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除目录
     */
    private void deleteDirectory(String pathname) {
        try {
            File file = new File(pathname);
            if (file.isDirectory()) {
                File file2[] = file.listFiles();
            } else {
                deleteFile(pathname);
            }
            ftpClient.removeDirectory(pathname);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * 删除空目录
     */
    private void deleteEmptyDirectory(String pathname) {
        try {
            ftpClient.removeDirectory(pathname);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * 列出服务器上文件和目录
     *
     * @param regStr --匹配的正则表达式
     */
    private void listRemoteFiles(String regStr) {
        connectServer();
        try {
            String files[] = ftpClient.listNames(regStr);
            if (files == null || files.length == 0)
                System.out.println("没有任何文件!");
            else {
                for (int i = 0; i < files.length; i++) {
                    System.out.println(files[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 列出Ftp服务器上的所有文件和目录
     */
    private void listRemoteAllFiles() {
        try {
            String[] names = ftpClient.listNames();
            for (int i = 0; i < names.length; i++) {
                System.out.println(names[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭连接
     */
    public void closeConnect() {
        try {
            if (ftpClient != null) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
            executorService.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置配置文件
     *
     * @param configFile
     */
    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

    /**
     * 设置传输文件的类型[文本文件或者二进制文件]
     *
     * @param fileType--BINARY_FILE_TYPE、ASCII_FILE_TYPE
     */
    public void setFileType(int fileType) {
        try {
            ftpClient.setFileType(fileType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置参数
     *
     * @param configFile --参数的配置文件
     */
    private void setArg(String configFile) {
        property = new Properties();
        BufferedInputStream inBuff;
        try {
            File file = new File(configFile);
            inBuff = new BufferedInputStream(new FileInputStream(file));
            property.load(inBuff);
            userName = property.getProperty("username");
            password = property.getProperty("password");
            host = property.getProperty("ip");
            port = Integer.parseInt(property.getProperty("port"));
        } catch (FileNotFoundException e1) {
            logger.warn("配置文件 " + configFile + " 不存在！");
        } catch (IOException e) {
            logger.warn("配置文件 " + configFile + " 无法读取！");
        }
    }

    /**
     * 连接到服务器
     *
     * @return true 连接服务器成功，false 连接服务器失败
     */
    public boolean connectServer() {
        logger.info("Connect server:" + this.host + ":" + this.port);
        boolean flag = true;
        int reply;
        try {
            setArg(configFile);
            ftpClient = new FTPClient();
            ftpClient.setControlEncoding(this.encoding);
            if (port > 0) {
                ftpClient.setDefaultPort(port);
            }
            ftpClient.connect(host);
            ftpClient.login(userName, password);
            reply = ftpClient.getReplyCode();
            ftpClient.setDataTimeout(120000);
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                logger.debug("FTP 服务拒绝连接！");
                flag = false;
            }
            ftpServerType = ftpClient.getSystemType();
//            int x = ftpClient.pasv();//启用被动模式
//            logger.debug("启用被动模式[" + x + "]：ReplyCode:" + ftpClient.getReplyCode() + ",ReplyString:" + ftpClient.getReplyString());
            ftpClient.configure(new FTPClientConfig(ftpServerType));
        } catch (SocketException e) {
            flag = false;
            e.printStackTrace();
            logger.error("登录ftp服务器 " + host + " 失败,连接超时！");
        } catch (IOException e) {
            flag = false;
            e.printStackTrace();
            logger.error("登录ftp服务器 " + host + " 失败，FTP服务器无法打开！");
        }
        logger.info("FTP 连接成功. ftp server:" + ftpClient.getRemoteAddress() + ":" + ftpClient.getRemotePort());
        return flag;
    }

    /**
     * 进入到服务器的某个目录下
     *
     * @param directory
     */
    private void changeWorkingDirectory(String directory) {
        try {
            connectServer();
            ftpClient.changeWorkingDirectory(directory);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void alive() {
        Runnable hearBeatThread = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    FTPService.this.hearBeat();
                }
            }
        };
        executorService.execute(hearBeatThread);
    }

    private void hearBeat() {
        try {
            ftpClient.noop();
            logger.info("Ftp is alive at " + ftpClient.getRemoteAddress() + ":" + ftpClient.getRemotePort());
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Ftp connection has bean disconnected!");
            connectServer();
        }
    }

    /**
     * 返回到上一层目录
     */
    private void changeToParentDirectory() {
        try {
            connectServer();
            ftpClient.changeToParentDirectory();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void initial() {
        if (connectServer()) {
            alive();
        } else {
            logger.error("启动ftp服务失败");
        }
    }

    /**
     * 重命名文件
     *
     * @param oldFileName --原文件名
     * @param newFileName --新文件名
     */
    private void renameFile(String oldFileName, String newFileName) {
        try {
            connectServer();
            ftpClient.rename(oldFileName, newFileName);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * 转码[ISO-8859-1 -> GBK] 不同的平台需要不同的转码
     *
     * @param obj
     * @return ""
     */
    private String iso8859togbk(Object obj) {
        try {
            if (obj == null)
                return "";
            else
                return new String(obj.toString().getBytes("iso-8859-1"), "GBK");
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 在服务器上创建一个文件夹
     *
     * @param dir 文件夹名称，不能含有特殊字符，如 \ 、/ 、: 、* 、?、 "、 <、>...
     */
    private boolean makeDirectory(String dir) {
        connectServer();
        boolean flag = true;
        try {
            // System.out.println("dir=======" dir);
            flag = ftpClient.makeDirectory(dir);
            if (flag) {
                System.out.println("make Directory " + dir + " succeed");

            } else {

                System.out.println("make Directory " + dir + " false");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public void uploadDirectory(final Path localDir, Path distDir) throws FileNotFoundException {
        if (!Files.isDirectory(localDir, LinkOption.NOFOLLOW_LINKS)) {
            throw new FileNotFoundException("文件目录不存在：" + localDir.toString());
        }
        try {
            final Path workingDir = distDir.resolve(localDir.getFileName().toString());
            ftpClient.makeDirectory(getPath(workingDir));//创建上传目录
            ftpClient.changeWorkingDirectory(getPath(workingDir));
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                logger.error("创建远程目录[]失败", getPath(workingDir));
                throw new FileNotFoundException("创建远程目录失败");
            }
            Files.walkFileTree(localDir, new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    Path localRelativePath = localDir.relativize(dir);
                    ftpClient.makeDirectory(getPath(workingDir.resolve(localRelativePath)));
                    ftpClient.changeWorkingDirectory(getPath(workingDir));
                    if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                        return FileVisitResult.TERMINATE;
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path workingPath = file.getParent();
                    String remoteDir;
                    if (Files.isDirectory(workingPath)) {
                        String relativeDir = getPath(localDir.relativize(workingPath));
                        remoteDir = getPath(workingDir.resolve(relativeDir));
                    } else {
                        remoteDir = getPath(workingDir);
                    }
                    ftpClient.changeWorkingDirectory(remoteDir);
                    uploadFile(file, remoteDir, file.getFileName().toString());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("本地文件上传失败！", e);
        }
    }

    /**
     * 上传单个文件，并重命名
     *
     * @param stream 本地文件输出流
     * @param rename 新的文件名
     * @return 返回文件夹和目录名称 如：20161105/file.txt
     */
    public String uploadFileToDailyFolder(InputStream stream, String rename) {
        String path = DateFormatUtils.format(new Date(), "yyyyMMdd");
        boolean isSuccess = uploadFile(stream, path, rename);
        if (!isSuccess) {
            throw new RuntimeException("上传文件失败");
        }
        return contractPath(path, rename);
    }

    /**
     * 上传单个文件，并重命名
     *
     * @param stream 本地文件输出流
     * @param rename 新的文件名
     * @return true 上传成功，false 上传失败
     */
    public boolean uploadFile(InputStream stream, String rename) throws FileNotFoundException {
        return uploadFile(stream, null, rename);
    }

    /**
     * 上传单个文件，并重命名
     *
     * @param localFileStr 本地文件路径
     * @param distDirStr   目标文件夹
     * @param rename       新的文件名
     * @return true 上传成功，false 上传失败
     */
    public boolean uploadFile(String localFileStr, String distDirStr, String rename) throws FileNotFoundException {
        boolean flag = false;
        InputStream input;
        Path localFile = Paths.get(localFileStr);
        if (!Files.isRegularFile(localFile)) {
            throw new FileNotFoundException("找不到上传文件.");
        }
        if (StringUtils.isEmpty(rename)) {
            rename = localFile.getFileName().toString();
        }
        try {
            input = Files.newInputStream(localFile, StandardOpenOption.READ);
            uploadFile(input, distDirStr, rename);
        } catch (Exception e) {
            logger.error("上传文件[" + localFile + "]到[" + distDirStr + File.separator + rename + "] 失败.", e);
        }
        return flag;
    }

    /**
     * 上传单个文件，并重命名
     *
     * @param input      本地文件输入流
     * @param distDirStr 目标文件夹
     * @param fileName   新的文件名
     * @return true 上传成功，false 上传失败
     */
    public boolean uploadFile(InputStream input, String distDirStr, String fileName) {
        boolean flag = false;
        if (input == null) {
            throw new NullPointerException("上传文件流不能为空.");
        }
        if (StringUtils.isEmpty(fileName)) {
            throw new NullPointerException("文件名不能为空.");
        }
        String workingDir = "";
        if (StringUtils.isNotEmpty(ftpBasePath)) {
            workingDir = ftpBasePath;
        }
        if (StringUtils.isNotEmpty(basePath)) {
            workingDir = contractPath(workingDir, basePath);
        }
        workingDir = getPath(contractPath(workingDir, distDirStr));
        try {
            ftpClient.makeDirectory(workingDir);
            logger.debug("makeDirectory ReplyCode:" + ftpClient.getReply() + ",ReplyString:" + ftpClient.getReplyString());
            ftpClient.changeWorkingDirectory(workingDir);
            logger.debug("changeWorkingDirectory ReplyCode:" + ftpClient.getReply() + ",ReplyString:" + ftpClient.getReplyString());
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            flag = ftpClient.storeFile(fileName, input);
            if (flag) {
                logger.info("上传文件[" + fileName + "]到[" + distDirStr + File.separator + fileName + "] 成功.");
            } else {
                logger.info("上传失败" + ftpClient.printWorkingDirectory() + " replyCode[" + ftpClient.getReplyCode() + "],replyString[" + ftpClient.getReplyString() + "].");
            }
            input.close();
        } catch (Exception e) {
            logger.error("上传文件[" + fileName + "]到[" + distDirStr + File.separator + fileName + "] 失败.", e);
        }
        return flag;
    }

    /**
     * 上传单个文件，并重命名
     *
     * @param inputFilePath 本地文件输入流
     * @param distDirStr    目标文件夹
     * @param rename        新的文件名
     * @return true 上传成功，false 上传失败
     */
    public boolean uploadFile(Path inputFilePath, String distDirStr, String rename) throws FileNotFoundException {
        boolean flag = false;
        if (!Files.isRegularFile(inputFilePath)) {
            throw new FileNotFoundException("找不到上传文件流.");
        }
        if (StringUtils.isEmpty(rename)) {
            rename = inputFilePath.getFileName().toString();
        }
        try {
            uploadFile(Files.newInputStream(inputFilePath), distDirStr, rename);
        } catch (Exception e) {
            logger.error("上传文件[" + inputFilePath.getFileName().toString() + "]到[" + distDirStr + File.separator + rename + "] 失败.", e);
        }
        return flag;
    }

    private boolean isUnixSystem() {
        return ftpServerType != null && ftpServerType.toUpperCase().contains("UNIX");
    }

    private String getPath(Path path) {
        return getPath(path.toString());
    }

    private String getPath(String path) {
        if (StringUtils.isNotEmpty(path)) {
            if (isUnixSystem()) {
                path = path.replace("\\", getSystemSeparator());
            } else {
                path = path.replace("/", getSystemSeparator());
            }
        }
        return path;
    }

    private String getSystemSeparator() {
        if (isUnixSystem()) {
            return "/";
        } else {
            return "\\";
        }
    }

    private String contractPath(String basePath, String relativePath) {
        if (StringUtils.isEmpty(basePath)) {
            return relativePath;
        }
        if (StringUtils.isEmpty(relativePath)) {
            return basePath;
        }
        while (relativePath.startsWith("/") || relativePath.startsWith("\\")) {
            relativePath = relativePath.substring(1);
        }

        if (basePath.endsWith("/") || basePath.endsWith("\\")) {
            return basePath + relativePath;
        } else {
            return basePath + getSystemSeparator() + relativePath;
        }
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void setFtpBasePath(String ftpBasePath) {
        this.ftpBasePath = ftpBasePath;
    }
}
