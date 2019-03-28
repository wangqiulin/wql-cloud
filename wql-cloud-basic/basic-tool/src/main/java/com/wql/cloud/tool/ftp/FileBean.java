package com.wql.cloud.tool.ftp;

public class FileBean {
	/**
	 * 文件名
	 */
	private String name;
	/**
	 * 文件原始名
	 */
	private String originalFilename;
	/**
	 * 类型
	 */
	private String contentType;

	/**
	 * 文件类型
	 */
	private String fileType;
	/**
	 * 文件大小
	 */
	private Long size;
	/**
	 * 文件内容
	 */
	private byte[] bytes;

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOriginalFilename() {
		return originalFilename;
	}

	public void setOriginalFilename(String originalFilename) {
		this.originalFilename = originalFilename;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
}
