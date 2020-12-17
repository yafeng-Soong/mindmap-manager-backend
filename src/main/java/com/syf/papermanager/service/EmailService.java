package com.syf.papermanager.service;

import com.syf.papermanager.utils.redis.UserKeyPrefix;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.internet.MimeMessage;
import java.net.InetAddress;
import java.util.UUID;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.service
 * @description:
 * @author: songyafeng
 * @create_time: 2020/12/15 18:48
 */
@Slf4j
@Service
public class EmailService {
    @Resource
    private JavaMailSender javaMailSender;

    @Resource
    private TemplateEngine templateEngine;
    @Resource
    private RedisService redisService;

    //读取配置文件中的参数
    @Value("${spring.mail.username}")
    private String sender;
    @Value("${back.address}")
    private String address;
    @Value("${server.port}")
    private int serverPort;
    @Value("${front.address}")
    private String frontAddress;
    @Value("${front.port}")
    private int frontPort;

    /**
     * 发送模板邮件
     * @param email 接收方的email
     * @param template 模板的名称，有激活模板和找回密码两种，/resource/templates下
     * @param subject 邮件主题
     */

    public void sendTemplateMail(String email,String template,String subject) throws Exception {
        String baseUrl = address + ":" + serverPort;
        String frontUrl = frontAddress + ":" + frontPort;
        MimeMessage message = javaMailSender.createMimeMessage();
        //为了防止接收方丢入垃圾箱，可以在header里加上Outlook
        message.addHeader("X-Mailer","Microsoft Outlook Express 6.00.2900.2869");
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        //指定源、目的地、主题
        helper.setFrom(sender);
        helper.setTo(email);
        helper.setSubject(subject);

        Context context = new Context();
        //设置模板中的变量
        context.setVariable("email", email);
        context.setVariable("baseUrl", baseUrl);
        context.setVariable("frontUrl", frontUrl);
        //生成UUID作为tmpKey
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        redisService.set(UserKeyPrefix.TOKEN, token, email);
        context.setVariable("token", token);
        //指定template模板
        String emailContent = templateEngine.process(template, context);
        helper.setText(emailContent, true);
        javaMailSender.send(message);
    }
}
