package com.zlw.commons.net;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE;


public class FTPPool {
    private int poolSize = 3;
    private final List<FTPClient> pool = new ArrayList<>(poolSize);
    private boolean showCommond = false;
    private String serverType;
    private boolean binaryTransfer = true;
    private boolean localActive = false;
    private int bufferSize = 1024;
    private String charactorSet = "UTF-8";
    private FTPClientConfig config;
    private String server;
    private int port;
    private String username;
    private String password;

    public FTPClient getFtpClient() throws IOException, LoginException {
        FTPClient ftp=null;
        if (pool.isEmpty()) {
            ftp = createFtpClient();
            pool.add(ftp);
        } else {
            for (int i = 0; i < pool.size(); i++) {
                ftp = pool.get(i);
                if(!ftp.isConnected()){
                    ftp.connect(server,port);
                }
//                else if(ftp.){
//
//                }
            }
        }
        return ftp;
    }


    private FTPClient createFtpClient() throws IOException, LoginException {
        FTPClient ftpClient = new FTPClient();
        if (showCommond) {
            ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));
        }
        ftpClient.connect(server, port);
        int replyCode = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(replyCode)) {
            ftpClient.disconnect();
            throw new ConnectException("FTP connect to server failed!");
        }

        if (!ftpClient.login(username, password)) {
            ftpClient.logout();
            throw new LoginException("Login failed!");
        }

        if (binaryTransfer) {
            ftpClient.setFileType(BINARY_FILE_TYPE);
        } else {
            // in theory this should not be necessary as servers should default to ASCII
            // but they don't all do so - see NET-500
            ftpClient.setFileType(FTP.ASCII_FILE_TYPE);
        }
        if (localActive) {
            ftpClient.enterLocalActiveMode();
        } else {
            ftpClient.enterLocalPassiveMode();
        }

        ftpClient.setBufferSize(bufferSize);//设置上传缓存大小
        ftpClient.setControlEncoding(charactorSet);//设置编码
        return ftpClient;
    }

    private FTPClientConfig getDefaultConfig() {
        if (StringUtils.isNotEmpty(serverType)) {
            config = new FTPClientConfig(serverType);
        } else {
            config = new FTPClientConfig();
        }
//        config.setUnparseableEntries(saveUnparseable);
//        if (defaultDateFormat != null) {
//            config.setDefaultDateFormatStr(defaultDateFormat);
//        }
//        if (recentDateFormat != null) {
//            config.setRecentDateFormatStr(recentDateFormat);
//        }
        return config;
    }
}
