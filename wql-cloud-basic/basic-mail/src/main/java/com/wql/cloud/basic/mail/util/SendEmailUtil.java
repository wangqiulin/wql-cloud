package com.wql.cloud.basic.mail.util;

import java.io.File;
import java.io.UnsupportedEncodingException;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.wql.cloud.basic.mail.bean.MailBean;

@Component
public class SendEmailUtil {

	private static final Logger logger = LoggerFactory.getLogger(SendEmailUtil.class);

	@Autowired
	private JavaMailSender javaMailSender;

	/**
	 * 发送一个简单格式的邮件
	 * 
	 * @param mailBean
	 */
	public void sendSimpleMail(MailBean mailBean) {
		try {
			SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
			// 邮件发送人
			simpleMailMessage.setFrom(mailBean.getSender());
			// 邮件接收人
			simpleMailMessage.setTo(mailBean.getRecipient());
			// 邮件主题
			simpleMailMessage.setSubject(mailBean.getSubject());
			// 邮件内容
			simpleMailMessage.setText(mailBean.getContent());
			javaMailSender.send(simpleMailMessage);
		} catch (Exception e) {
			logger.error("邮件发送失败", e);
		}
	}

	/**
	 * 发送一个HTML格式的邮件
	 * 
	 * @param mailBean
	 */
	public void sendHtmlMail(MailBean mailBean) {
		MimeMessage mimeMailMessage = null;
		try {
			mimeMailMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, true);
			// 邮件发送人
			if(StringUtils.isBlank(mailBean.getNickName())) {
				mimeMessageHelper.setFrom(mailBean.getSender());
			} else {
				String nickName = getNick(mailBean.getNickName());
				mimeMessageHelper.setFrom(new InternetAddress(nickName+" <"+mailBean.getSender()+">") );
			}
			mimeMessageHelper.setTo(mailBean.getRecipient());
			mimeMessageHelper.setSubject(mailBean.getSubject());
			StringBuilder sb = new StringBuilder();
			sb.append("<h1>SpirngBoot测试邮件HTML</h1>").append("\"<p style='color:#F00'>你是真的太棒了！</p>")
					.append("<p style='text-align:right'>右对齐</p>");
			mimeMessageHelper.setText(sb.toString(), true);
			javaMailSender.send(mimeMailMessage);
		} catch (Exception e) {
			logger.error("邮件发送失败", e);
		}
	}

	
	/**
	 *  发送带附件格式的邮件
	 *  
	 * @param mailBean
	 */
	public void sendAttachmentMail(MailBean mailBean) {
        MimeMessage mimeMailMessage = null;
        try {
            mimeMailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, true);
            // 邮件发送人
			if(StringUtils.isBlank(mailBean.getNickName())) {
				mimeMessageHelper.setFrom(mailBean.getSender());
			} else {
				String nickName = getNick(mailBean.getNickName());
				mimeMessageHelper.setFrom(new InternetAddress(nickName+" <"+mailBean.getSender()+">") );
			}
            mimeMessageHelper.setTo(mailBean.getRecipient());
            mimeMessageHelper.setSubject(mailBean.getSubject());
            mimeMessageHelper.setText(mailBean.getContent());
            //文件路径
            FileSystemResource file = new FileSystemResource(new File("src/main/resources/static/image/mail.png"));
            mimeMessageHelper.addAttachment("mail.png", file);
            javaMailSender.send(mimeMailMessage);
        } catch (Exception e) {
            logger.error("邮件发送失败", e);
        }
    }
	
	
	/**
	 * 发送带静态资源的邮件
	 * 
	 * @param mailBean
	 */
	public void sendInlineMail(MailBean mailBean) {
        MimeMessage mimeMailMessage = null;
        try {
            mimeMailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, true);
            // 邮件发送人
            if(StringUtils.isBlank(mailBean.getNickName())) {
				mimeMessageHelper.setFrom(mailBean.getSender());
			} else {
				String nickName = getNick(mailBean.getNickName());
				mimeMessageHelper.setFrom(new InternetAddress(nickName+" <"+mailBean.getSender()+">") );
			}
            mimeMessageHelper.setTo(mailBean.getRecipient());
            mimeMessageHelper.setSubject(mailBean.getSubject());
            mimeMessageHelper.setText("<html><body>带静态资源的邮件内容，这个一张IDEA配置的照片:<img src='cid:picture' /></body></html>", true);
            //文件路径
            FileSystemResource file = new FileSystemResource(new File("src/main/resources/static/image/mail.png"));
            mimeMessageHelper.addInline("picture", file);
            javaMailSender.send(mimeMailMessage);
        } catch (Exception e) {
            logger.error("邮件发送失败", e);
        }
    }
	
	
	/**
	 * 设置昵称
	 * @param nickName
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String getNick(String nickName) throws UnsupportedEncodingException {
		if(StringUtils.isNotBlank(nickName)) {
			return javax.mail.internet.MimeUtility.encodeText(nickName); 
		}
		return null;
	}
	
	
}
