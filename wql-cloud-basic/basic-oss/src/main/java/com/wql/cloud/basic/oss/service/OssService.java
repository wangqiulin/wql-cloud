package com.wql.cloud.basic.oss.service;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface OssService {

	Logger logger = LoggerFactory.getLogger(OssService.class);

	/**
	 * 阿里云上传文件 <a href=
	 * "https://help.aliyun.com/document_detail/84781.html?spm=a2c4g.11186623.6.682.F3YKZO">官方文档</a>
	 *
	 * @param endpoint
	 *            外网访问地址
	 * @param accessKeyId
	 *            用于标识用户
	 * @param accessKeySecret
	 *            是用来验证用户的密钥
	 * @param bucketName
	 *            图片空间名
	 * @param fileName
	 *            文件名
	 * @param file
	 *            文件体
	 * @return String
	 */
	String uploadFile(String endpoint, String accessKeyId, String accessKeySecret, String bucketName, String fileName,
			File file);

	/**
	 * 阿里云上传文件 <a href=
	 * "https://help.aliyun.com/document_detail/84781.html?spm=a2c4g.11186623.6.682.F3YKZO">官方文档</a>
	 *
	 * @param endpoint
	 *            外网访问地址
	 * @param accessKeyId
	 *            用于标识用户
	 * @param accessKeySecret
	 *            是用来验证用户的密钥
	 * @param bucketName
	 *            图片空间名
	 * @param fileName
	 *            文件名
	 * @param inputStream
	 *            文件流
	 * @return
	 */
	String uploadImgFile(String endpoint, String accessKeyId, String accessKeySecret, String bucketName,
			String fileName, InputStream inputStream);

	/**
	 * 阿里云下载文件 <a href=
	 * "https://help.aliyun.com/document_detail/84824.html?spm=a2c4g.11186623.6.692.1Aaskc">官方文档</a>
	 *
	 * @param endpoint
	 *            外网访问地址
	 * @param accessKeyId
	 *            用于标识用户
	 * @param accessKeySecret
	 *            是用来验证用户的密钥
	 * @param bucketName
	 *            图片空间名
	 * @param objectName
	 *            文件名
	 * @param localFile
	 *            本地文件
	 */
	void downloadFile(String endpoint, String accessKeyId, String accessKeySecret, String bucketName, String objectName,
			File localFile);

	/**
	 * 阿里云下载文件 <a href=
	 * "https://help.aliyun.com/document_detail/84842.html?spm=a2c4g.11186623.6.703.YPRQ04">官方文档</a>
	 *
	 * @param endpoint
	 *            外网访问地址
	 * @param accessKeyId
	 *            用于标识用户
	 * @param accessKeySecret
	 *            是用来验证用户的密钥
	 * @param bucketName
	 *            图片空间名
	 * @param objectName
	 *            文件名
	 * @return 文件流
	 */
	InputStream downloadStream(String endpoint, String accessKeyId, String accessKeySecret, String bucketName,
			String objectName);

	/**
	 * 阿里云文件输出 <a href=
	 * "https://help.aliyun.com/document_detail/84842.html?spm=a2c4g.11186623.6.703.YPRQ04">官方文档</a>
	 *
	 * @param endpoint
	 *            外网访问地址
	 * @param accessKeyId
	 *            用于标识用户
	 * @param accessKeySecret
	 *            是用来验证用户的密钥
	 * @param bucketName
	 *            图片空间名
	 * @param objectName
	 *            文件名
	 * @param out
	 *            输出流
	 */
	void write2Stream(String endpoint, String accessKeyId, String accessKeySecret, String bucketName, String objectName,
			OutputStream out);

	/**
	 * * 删除文件 <a href=
	 * "https://help.aliyun.com/document_detail/84842.html?spm=a2c4g.11186623.6.703.YPRQ04">官方文档</a>
	 *
	 * @param endpoint
	 *            外网访问地址
	 * @param accessKeyId
	 *            用于标识用户
	 * @param accessKeySecret
	 *            是用来验证用户的密钥
	 * @param bucketName
	 *            空间名
	 * @param objectNames
	 *            删除文件名
	 * @return 删除失败文件
	 */
	List<String> deleteFile(String endpoint, String accessKeyId, String accessKeySecret, String bucketName,
			String... objectNames);

	/**
	 * * 删除文件 <a href=
	 * "https://help.aliyun.com/document_detail/84842.html?spm=a2c4g.11186623.6.703.YPRQ04">官方文档</a>
	 *
	 * @param accessKeyId
	 *            用于标识用户
	 * @param accessKeySecret
	 *            是用来验证用户的密钥
	 * @param urls
	 *            删除文件url地址 * @return 删除失败文件url
	 */
	List<String> deleteFileByUrl(String accessKeyId, String accessKeySecret, String... urls);

	public static class GroupingByEndpointAndBucketName {

		private String endpoint;

		private String bucketName;

		public GroupingByEndpointAndBucketName(OssUrl ossUrl) {
			if (ossUrl != null) {
				this.endpoint = ossUrl.getEndpoint();
				this.bucketName = ossUrl.getBucketName();
			}
		}

		public String getEndpoint() {
			return endpoint;
		}

		public String getBucketName() {
			return bucketName;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj != null && obj instanceof OssUrl) {
				OssUrl target = (OssUrl) obj;
				return this.getEndpoint().equals(target.getEndpoint())
						&& this.getBucketName().equals(target.getBucketName());
			}
			return false;
		}

		@Override
		public int hashCode() {
			return this.getEndpoint().hashCode() + this.getBucketName().hashCode();
		}
	}

	public static class OssUrl {

		private String url;

		private String endpoint;

		private String objectName;

		private String bucketName;

		public OssUrl(String url) {
			this.url = url;
			try {
				url = URLDecoder.decode(url, "utf-8");
				if (url.contains("?")) {
					// url附带参数截取掉
					url = url.substring(0, url.indexOf("?"));
				}
				int index = url.indexOf(".");
				this.bucketName = url.substring(url.indexOf("://") + 3, index);
				url = url.substring(index + 1, url.length());
				index = url.indexOf("/");
				this.endpoint = url.substring(0, index);
				this.objectName = url.substring(index + 1);
			} catch (Exception e) {
				logger.warn("待删除oss文件url无效->{}", this.url);
			}
		}

		public String getUrl() {
			return url;
		}

		public String getEndpoint() {
			return endpoint;
		}

		public String getObjectName() {
			return objectName;
		}

		public String getBucketName() {
			return bucketName;
		}
	}

}
