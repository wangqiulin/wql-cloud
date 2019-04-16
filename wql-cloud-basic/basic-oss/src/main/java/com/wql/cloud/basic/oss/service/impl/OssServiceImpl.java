package com.wql.cloud.basic.oss.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectRequest;
import com.wql.cloud.basic.oss.service.OssService;

@Service
public class OssServiceImpl implements OssService {

	private static final Logger logger = LoggerFactory.getLogger(OssServiceImpl.class);

	private static final Random RANDOM = new Random();

	@Override
	public String uploadFile(String endpoint, String accessKeyId, String accessKeySecret, String bucketName,
			String fileName, File file) {
		OSS ossClient = getOSSClient(endpoint, accessKeyId, accessKeySecret, bucketName);
		try {
			ossClient.putObject(new PutObjectRequest(bucketName, fileName, file));
			return getUrl(ossClient, bucketName, fileName);
		} catch (OSSException oe) {
			logger.warn("文件上传失败", oe);
		} catch (ClientException ce) {
			logger.warn("ossClient异常", ce);
		} finally {
			ossClient.shutdown();
		}
		return "";
	}

	@Override
	public String uploadImgFile(String endpoint, String accessKeyId, String accessKeySecret, String bucketName,
			String fileName, InputStream inputStream) {
		OSS ossClient = getOSSClient(endpoint, accessKeyId, accessKeySecret, bucketName);
		try {

			String filePrefix = getFilePrefixIgnoreName(fileName);
			String fileSuffix = getFileSuffix(fileName);
			fileName = filePrefix.concat(randomName()).concat(fileSuffix);
			ossClient.putObject(bucketName, fileName, inputStream);
			return getUrl(ossClient, bucketName, fileName);
		} catch (OSSException oe) {
			logger.warn("文件上传失败", oe);
		} catch (ClientException ce) {
			logger.warn("ossClient异常", ce);
		} finally {
			ossClient.shutdown();
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.warn("oss文件上传流");
				}
			}
		}
		return "";
	}

	@Override
	public void downloadFile(String endpoint, String accessKeyId, String accessKeySecret, String bucketName,
			String objectName, File localFile) {
		OSS ossClient = getOSSClient(endpoint, accessKeyId, accessKeySecret, bucketName);
		try {
			// 创建OSSClient实例。
			GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, objectName);
			// 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建。
			ossClient.getObject(getObjectRequest, localFile);
		} catch (OSSException oe) {
			logger.warn("文件下载失败", oe);
		} catch (ClientException ce) {
			logger.warn("ossClient异常", ce);
		} finally {
			ossClient.shutdown();
		}
	}

	@Override
	public InputStream downloadStream(String endpoint, String accessKeyId, String accessKeySecret, String bucketName,
			String objectName) {
		OSS ossClient = getOSSClient(endpoint, accessKeyId, accessKeySecret, bucketName);
		try {
			// 创建OSSClient实例。
			GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, objectName);
			// 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建。
			OSSObject object = ossClient.getObject(getObjectRequest);
			try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
				byte[] buffer = new byte[2048];
				int bytesRead;
				while ((bytesRead = object.getObjectContent().read(buffer)) != -1) {
					bos.write(buffer, 0, bytesRead);
				}
				return new ByteArrayInputStream(bos.toByteArray());
			}
		} catch (OSSException oe) {
			logger.warn("文件下载失败", oe);
		} catch (ClientException ce) {
			logger.warn("ossClient异常", ce);
		} catch (IOException e) {
			logger.warn("阿里云文件下载io异常", e);
		} finally {
			ossClient.shutdown();
		}
		return null;
	}

	@Override
	public void write2Stream(String endpoint, String accessKeyId, String accessKeySecret, String bucketName,
			String objectName, OutputStream out) {
		Assert.notNull(out, "输出流不能为空");
		OSS ossClient = getOSSClient(endpoint, accessKeyId, accessKeySecret, bucketName);
		try {
			// 创建OSSClient实例。
			GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, objectName);
			// 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建。
			OSSObject object = ossClient.getObject(getObjectRequest);
			byte[] buffer = new byte[2048];
			int bytesRead;
			while ((bytesRead = object.getObjectContent().read(buffer)) != -1) {
				out.write(buffer,0,bytesRead);
			}
		} catch (OSSException oe) {
			logger.warn("文件输出失败", oe);
		} catch (ClientException ce) {
			logger.warn("ossClient异常", ce);
		} catch (IOException e) {
			logger.warn("阿里云文件输出io异常", e);
		} finally {
			ossClient.shutdown();
		}
	}

	@Override
	public List<String> deleteFile(String endpoint, String accessKeyId, String accessKeySecret, String bucketName,
			String... objectNames) {
		OSS ossClient = getOSSClient(endpoint, accessKeyId, accessKeySecret, bucketName);
		try {
			if (objectNames == null) {
				return new ArrayList<>();
			}
			// 单文件删除
			if (objectNames.length == 1) {
				try {
					ossClient.deleteObject(bucketName, objectNames[0]);
				} catch (OSSException oe) {
					logger.warn("单文件删除失败objectName->{}", objectNames[0], oe);
					return Arrays.asList(objectNames);
				}
			} else {
				try {
					// 多文件删除
					return batchDeleteByGroup(ossClient, bucketName, Arrays.asList(objectNames));
				} catch (OSSException oe) {
					logger.warn("文件删除失败", oe);
				}
			}
		} catch (ClientException ce) {
			logger.warn("文件删除失败ossClient异常", ce);
		} finally {
			ossClient.shutdown();
		}
		return new ArrayList<>();
	}

	@Override
	public List<String> deleteFileByUrl(String accessKeyId, String accessKeySecret, String... urls) {
		if (urls == null) {
			return new ArrayList<>();
		}
		try {
			// 单文件
			if (urls.length == 1) {
				OssUrl ossUrl = new OssUrl(urls[0]);
				OSS ossClient = getOSSClient(ossUrl.getEndpoint(), accessKeyId, accessKeySecret,
						ossUrl.getBucketName());
				try {
					ossClient.deleteObject(ossUrl.getBucketName(), ossUrl.getObjectName());
				} catch (OSSException oe) {
					logger.warn("单文件删除失败url->{}", ossUrl.getUrl(), oe);
					return Arrays.asList(ossUrl.getUrl());
				} finally {
					ossClient.shutdown();
				}
			} else {
				// 多文件
				List<String> result = new ArrayList<>();
				Stream.of(urls).map(OssUrl::new).filter(o -> o != null)
						.collect(Collectors.groupingBy(GroupingByEndpointAndBucketName::new)).forEach((k, v) -> {
							String endpoint = k.getEndpoint();
							String bucketName = k.getBucketName();
							OSS ossClient = getOSSClient(endpoint, accessKeyId, accessKeySecret, bucketName);
							List<String> objectNameList = v.stream().map(OssUrl::getObjectName)
									.collect(Collectors.toList());
							result.addAll(batchDeleteByGroup(ossClient, bucketName, objectNameList));
						});
				return result;
			}
		} catch (ClientException ce) {
			logger.warn("文件删除失败ossClient异常", ce);
		}
		return new ArrayList<>();
	}

	/**
	 * 获得url链接
	 *
	 * @param key
	 * @return
	 */
	private String getUrl(OSS ossClient, String bucketName, String key) {
		// 设置URL过期时间为10年 3600l* 1000*24*365*10
		Date expiration = new Date(System.currentTimeMillis() + 3600 * 1000 * 24 * 365 * 10);
		// 生成URL
		URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
		String urlStr = "";
		if (url != null) {
			urlStr = url.toString();
			int index = urlStr.indexOf("?");
			String lastUrl = "";
			if (index > 0) {
				lastUrl = urlStr.substring(0, index);
			} else {
				lastUrl = urlStr;
			}

			return toHttpsUrl(lastUrl);
		}
		return null;
	}

	/**
	 * 获取ossClient 并且判断是否存在该bucket,如果不存在则创建
	 *
	 * @param endpoint
	 * @param accessKeyId
	 * @param accessKeySecret
	 * @param bucketName
	 */
	private OSS getOSSClient(String endpoint, String accessKeyId, String accessKeySecret, String bucketName) {
		OSS ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		if (!ossClient.doesBucketExist(bucketName)) {
			ossClient.createBucket(bucketName);
			CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
			createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
			ossClient.createBucket(createBucketRequest);
		}
		return ossClient;
	}

	/**
	 * 获取文件前缀并且忽略文件名
	 *
	 * @param fileName
	 * @return
	 */
	private String getFilePrefixIgnoreName(String fileName) {
		if (fileName.contains("/")) {
			return fileName.substring(0, fileName.lastIndexOf("/") + 1);
		}
		return "";
	}

	/**
	 * 获取文件后缀
	 *
	 * @param fileName
	 * @return
	 */
	private String getFileSuffix(String fileName) {
		if (fileName.contains(".")) {
			return fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
		}
		return "";

	}

	/**
	 * 生成随机名字
	 *
	 * @return
	 */
	private String randomName() {
		return String.valueOf(RANDOM.nextInt(10000)).concat(String.valueOf(System.currentTimeMillis()));
	}

	/**
	 * 批量删除文件,数量大于1000分组
	 *
	 * @param ossClient
	 * @param bucketName
	 * @param objectNameList
	 * @return
	 */
	private List<String> batchDeleteByGroup(OSS ossClient, String bucketName, List<String> objectNameList) {
		if (objectNameList != null) {
			// 阿里云批量删除最多支持1000
			if (objectNameList.size() <= 1000) {
				return batchDelete(ossClient, bucketName, objectNameList);
			} else {
				List<List<String>> list = new ArrayList<>();
				// 每1000数据分组
				for (int i = 0; i < objectNameList.size(); i++) {
					List<String> groupObjectNameList = null;
					if (i % 1000 == 0) {
						groupObjectNameList = new ArrayList<>();
					}
					groupObjectNameList.add(objectNameList.get(i));
					if (groupObjectNameList.size() == 1000 || i == objectNameList.size() - 1) {
						list.add(groupObjectNameList);
					}
				}
				List<String> result = new ArrayList<>();
				for (List<String> groupObjectNameList : list) {
					result.addAll(batchDelete(ossClient, bucketName, groupObjectNameList));
				}
				return result;
			}
		}
		return new ArrayList<>();
	}

	/**
	 * 批量删除文件
	 *
	 * @param ossClient
	 * @param bucketName
	 * @param objectNameList
	 * @return
	 */
	private List<String> batchDelete(OSS ossClient, String bucketName, List<String> objectNameList) {
		try {
			if (objectNameList != null) {
				DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName);
				// 返回模式。true表示简单模式(返回删除失败文件)，false表示详细模式(返回删除成功文件)。默认为详细模式。
				deleteObjectsRequest.setQuiet(true);
				deleteObjectsRequest.withKeys(objectNameList);
				DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(deleteObjectsRequest);
				return deleteObjectsResult.getDeletedObjects();
			}
		} catch (OSSException oe) {
			logger.warn("文件删除失败", oe);
		}
		return new ArrayList<>();
	}

	/**
	 * http的url转换成https
	 * 
	 * @param httpUrl
	 * @return
	 */
	private String toHttpsUrl(String httpUrl) {
		String httpPrefix = "http://";
		if (StringUtils.isNotEmpty(httpUrl) && httpUrl.contains(httpPrefix)) {
			String httpsPrefix = "https://";
			return httpUrl.replaceFirst(httpPrefix, httpsPrefix);
		}
		return httpUrl;
	}

}
