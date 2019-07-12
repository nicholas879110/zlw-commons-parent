package com.zlw.commons.email;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadFactory;


public class GlobalEmail {
    private static Logger logger = LoggerFactory.getLogger(GlobalEmail.class);
    private final ExecutorService executor = Executors.newFixedThreadPool(4,
            new ThreadFactory() {
                public Thread newThread(Runnable r) {
                    Thread t = Executors.defaultThreadFactory().newThread(r);
                    t.setDaemon(false);
                    return t;
                }
            });
    private String hostName;
    private String smtpPort;
    private String sslSmtpPort;
    private String loginName;
    private String password;

    public GlobalEmail(String hostName, String loginName, String password) {
        this.hostName = hostName;
        this.loginName = loginName;
        this.password = password;
    }

    /**
     * 同步发送邮件
     *
     * @param from
     * @param to
     * @param subject
     * @param content
     * @throws EmailException
     */
    public void send(String from, String to, String subject, String content) throws EmailException {
        logger.debug("{}发送email到{}，标题[{}],内容[{}]", from, to, subject, content);
        Email email = new SimpleEmail();
        email.setHostName(hostName);
        email.setSSLOnConnect(false);
        if (StringUtils.isNotEmpty(loginName)) {
            email.setAuthenticator(new DefaultAuthenticator(loginName, password));
        }
        email.setFrom(from);
        email.setSubject(subject);
        email.setMsg(content);
        email.addTo(to);
        email.send();
    }

    /**
     * 异步发送邮件
     *
     * @param from
     * @param to
     * @param subject
     * @param content
     * @return true代表发送成功，false代表发送失败
     */
    public Future<Boolean> asynSend(final String from, final String subject, final String content, final EmailCallback callback, final String... to) {
        logger.debug("{}发送email到{}，标题[{}],内容[{}]", from, to, subject, content);
        Callable<Boolean> sendEmailTask = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    Email email = new SimpleEmail();
                    email.setHostName(hostName);
                    email.setSSLOnConnect(false);
                    if (StringUtils.isNotEmpty(loginName)) {
                        email.setAuthenticator(new DefaultAuthenticator(loginName, password));
                    }
                    email.setFrom(from);
                    email.setSubject(subject);
                    email.setMsg(content);
                    if (to == null) {
                        throw new EmailException("收件人不能为空!");
                    }
                    for (String s : to) {
                        email.addTo(s);
                    }
                    email.send();
                    callback.whenSuccess();
                    return true;
                } catch (EmailException e) {
                    logger.error("[发送邮件失败]{}发送email到{},标题[{}],内容[{}]", from, to, subject, content);
                    callback.whenFailed();
                    return false;
                }
            }
        };

        FutureTask<Boolean> futureTask = new FutureTask<>(sendEmailTask);
        executor.submit(futureTask);
        return futureTask;
    }

    /**
     * 同步发送邮件，带有附件
     *
     * @param from           发送者
     * @param to             接收者
     * @param subject        标题
     * @param content        发送内容
     * @param attachment     附件路径
     * @param attachmentName 附件名称
     * @throws EmailException 抛出Email异常
     */
    public void send(String from, String subject, String content, String attachment, String attachmentName, String... to) throws EmailException {
        logger.debug("{}发送带附件[{}]的email到{}，标题[{}],内容[{}]", from, attachmentName, to, subject, content);
        MultiPartEmail email = new MultiPartEmail();
        email.setHostName(hostName);
        email.setSSLOnConnect(false);
        if (StringUtils.isNotEmpty(loginName)) {
            email.setAuthenticator(new DefaultAuthenticator(loginName, password));
        }
        email.setFrom(from);
        email.setSubject(subject);
        email.setMsg(content);
        if (to == null) {
            throw new EmailException("收件人不能为空!");
        }
        for (String s : to) {
            email.addTo(s);
        }
        //创建邮件附件可多个
        EmailAttachment attach = new EmailAttachment();//创建附件
        attach.setPath(attachment);//本地附件，绝对路径
        //attachment.setURL(new URL("http://www.baidu.com/moumou附件"));//可添加网络上的附件
        attach.setDisposition(EmailAttachment.ATTACHMENT);
        //attach.setDescription("");//附件描述
        attach.setName(attachmentName);//附件名称
        email.attach(attach);//添加附件到邮件,可添加多个
        email.send();
    }

    /**
     * 异步发送邮件，带有附件
     *
     * @param from           发送者
     * @param to             接收者
     * @param subject        标题
     * @param content        发送内容
     * @param attachment     附件路径
     * @param attachmentName 附件名称
     * @return true代表发送成功，false代表发送失败
     */
    public Future<Boolean> asynSend(final String from, final String subject, final String content, final String attachment, final String attachmentName, final EmailCallback callback, final String... to) {
        logger.debug("{}发送带附件[{}]的email到{}，标题[{}],内容[{}]", from, attachmentName, to, subject, content);
        Callable<Boolean> sendEmailTask = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    MultiPartEmail email = new MultiPartEmail();
                    email.setHostName(hostName);
                    email.setSSLOnConnect(false);
                    if (StringUtils.isNotEmpty(loginName)) {
                        email.setAuthenticator(new DefaultAuthenticator(loginName, password));
                    }
                    email.setFrom(from);
                    email.setSubject(subject);
                    email.setMsg(content);
                    if (to == null) {
                        throw new EmailException("收件人不能为空!");
                    }
                    for (String s : to) {
                        email.addTo(s);
                    }
                    //创建邮件附件可多个
                    EmailAttachment attach = new EmailAttachment();//创建附件
                    attach.setPath(attachment);//本地附件，绝对路径
                    //attachment.setURL(new URL("http://www.baidu.com/moumou附件"));//可添加网络上的附件
                    attach.setDisposition(EmailAttachment.ATTACHMENT);
                    //attach.setDescription("");//附件描述
                    attach.setName(attachmentName);//附件名称
                    email.attach(attach);//添加附件到邮件,可添加多个
                    email.send();
                    callback.whenSuccess();
                    return true;
                } catch (EmailException e) {
                    logger.error("[发送邮件失败]{}发送带附件[{}]的email到{}，标题[{}],内容[{}]", from, attachmentName, to, subject, content);
                    callback.whenFailed();
                    return false;
                }
            }
        };
        FutureTask<Boolean> futureTask = new FutureTask<>(sendEmailTask);
        executor.submit(futureTask);
        return futureTask;
    }

    public void destroy() {
        executor.shutdown();
    }

    public String getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

    public String getSslSmtpPort() {
        return sslSmtpPort;
    }

    public void setSslSmtpPort(String sslSmtpPort) {
        this.sslSmtpPort = sslSmtpPort;
    }
}
