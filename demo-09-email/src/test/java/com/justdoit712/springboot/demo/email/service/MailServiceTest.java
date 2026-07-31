package com.justdoit712.springboot.demo.email.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;

/**
 * <p>
 * 邮件发送服务测试类
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-30 20:04
 */
@SpringBootTest
public class MailServiceTest {

    @Autowired
    private MailService mailService;
    
    @Autowired
    private TemplateEngine templateEngine;
    
    @Autowired
    private ApplicationContext context;

    /**
     * 测试发送简单邮件
     * 注意：由于 application.yml 中的邮箱配置为占位符，默认加上 @Disabled。
     * 配置好真实的邮箱账号后，可以把 @Disabled 去掉并运行。
     */
    @Test
    @Disabled("需要先在 application.yml 中配置真实的 SMTP 账号信息")
    public void testSendSimpleMail() {
        String to = "test-target@qq.com"; // 修改为接收邮件的测试邮箱
        mailService.sendSimpleMail(to, "这是一封简单文本邮件", "测试 Spring Boot 3 发送简单邮件！\n\nJDK 25 真香。");
    }

    /**
     * 测试发送 HTML 邮件（结合 Thymeleaf 模板）
     */
    @Test
    @Disabled("需要先在 application.yml 中配置真实的 SMTP 账号信息")
    public void testSendHtmlMail() throws MessagingException {
        String to = "test-target@qq.com"; // 修改为接收邮件的测试邮箱
        
        // 构造 Thymeleaf 上下文数据
        Context contextData = new Context();
        contextData.setVariable("username", "Justin");
        
        // 渲染 welcome.html 模板内容
        String emailContent = templateEngine.process("welcome", contextData);
        
        mailService.sendHtmlMail(to, "这是一封 HTML 模板邮件", emailContent);
    }

    /**
     * 测试发送带附件的邮件
     */
    @Test
    @Disabled("需要先在 application.yml 中配置真实的 SMTP 账号信息")
    public void testSendAttachmentsMail() throws MessagingException {
        String to = "test-target@qq.com"; // 修改为接收邮件的测试邮箱
        
        // 生成一个临时的测试附件
        File tempFile = FileUtil.writeUtf8String("这是附件里的文本内容，测试文件上传功能。", FileUtil.getTmpDir() + File.separator + "test-attachment.txt");
        
        mailService.sendAttachmentsMail(to, "这是一封带附件的邮件", "请查看附件内容", tempFile.getAbsolutePath());
    }

    /**
     * 测试发送带静态资源的邮件（图片）
     */
    @Test
    @Disabled("需要先在 application.yml 中配置真实的 SMTP 账号信息")
    public void testSendResourceMail() throws MessagingException {
        String to = "test-target@qq.com"; // 修改为接收邮件的测试邮箱
        String rscId = IdUtil.fastUUID();
        String content = "<html><body>这是带静态资源的邮件内容，图片如下：<br/><img src=\'cid:" + rscId + "\'/></body></html>";
        
        // 找一个现有的图片做测试，也可以临时生成或写入一个图片
        // 这里只是为了演示，请保证图片路径有效
        String imgPath = "d:/test-image.png"; 
        
        if (FileUtil.exist(imgPath)) {
            mailService.sendResourceMail(to, "这是一封带图片的静态资源邮件", content, imgPath, rscId);
        } else {
            System.out.println("找不到图片: " + imgPath + "，略过发送静态资源邮件测试。");
        }
    }
}
