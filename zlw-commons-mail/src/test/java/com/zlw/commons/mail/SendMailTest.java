package com.zlw.commons.mail;

import com.zlw.commons.email.GlobalEmail;
import org.apache.commons.mail.EmailException;

import java.util.concurrent.ExecutionException;


public class SendMailTest {

    public static void main(String[] args) throws EmailException, ExecutionException, InterruptedException {
//        long s = System.currentTimeMillis();
        GlobalEmail globalEmail=new GlobalEmail("10.0.10.30","Assessment","essisNO1.");
        //从spring容器里面获取Go么Mail对象
//        Future<Boolean> f = globalEmail.asynSend("Assessment", "wanglinyong", "test", "this is a test email!!!!");
        globalEmail.send("Assessment", "wanglinyong", "test", "this is a test email!!!!");
//        while (!f.isDone()) {
//            System.out.println("---------->wait!");
//        }
//        System.out.println(f.get() + "---------->" + (System.currentTimeMillis() - s));
    }
}
