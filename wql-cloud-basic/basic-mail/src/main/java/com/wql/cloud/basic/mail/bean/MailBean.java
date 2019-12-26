package com.wql.cloud.basic.mail.bean;

import java.io.Serializable;

public class MailBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 发送人邮件
	 */
	private String sender;

	/**
	 * 发件人昵称
	 */
	private String nickName;

	/**
	 * 邮件接收人
	 */
	private String[] recipient;

	/**
	 * 邮件主题
	 */
	private String subject;

	/**
	 * 邮件内容
	 */
	private String content;

	
	private String filePath;
	
	
	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String[] getRecipient() {
		return recipient;
	}

	public void setRecipient(String[] recipient) {
		this.recipient = recipient;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
