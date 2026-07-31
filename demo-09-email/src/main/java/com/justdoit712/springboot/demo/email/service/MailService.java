package com.justdoit712.springboot.demo.email.service;

import jakarta.mail.MessagingException;

/**
 * <p>
 * 邮件发送服务接口
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-30 20:03
 */
public interface MailService {

    /**
     * 发送简单纯文本邮件
     *
     * @param to      收件人地址
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    void sendSimpleMail(String to, String subject, String content);

    /**
     * 发送 HTML 格式的邮件
     *
     * @param to      收件人地址
     * @param subject 邮件主题
     * @param content HTML 格式的邮件内容
     * @throws MessagingException 邮件发送异常
     */
    void sendHtmlMail(String to, String subject, String content) throws MessagingException;

    /**
     * 发送带附件的邮件
     *
     * @param to       收件人地址
     * @param subject  邮件主题
     * @param content  邮件内容
     * @param filePath 附件在本地的绝对路径
     * @throws MessagingException 邮件发送异常
     */
    void sendAttachmentsMail(String to, String subject, String content, String filePath) throws MessagingException;

    /**
     * 发送包含静态资源的邮件 (例如图片)
     *
     * @param to      收件人地址
     * @param subject 邮件主题
     * @param content 邮件内容(必须包含对 rscId 的引用，例如 <img src='cid:rscId'/>)
     * @param rscPath 静态资源路径
     * @param rscId   静态资源 ID
     * @throws MessagingException 邮件发送异常
     */
    void sendResourceMail(String to, String subject, String content, String rscPath, String rscId) throws MessagingException;
}
