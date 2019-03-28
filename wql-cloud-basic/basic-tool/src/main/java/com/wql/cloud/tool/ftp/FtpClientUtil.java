package com.wql.cloud.tool.ftp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FtpClientUtil {

	/**
	 * Description: 向FTP服务器上传文件
	 * 
	 * @Version1.0
	 * 
	 * @param url
	 *            FTP服务器hostname
	 * @param port
	 *            FTP服务器端口
	 * @param username
	 *            FTP登录账号
	 * @param password
	 *            FTP登录密码
	 * @param path
	 *            FTP服务器保存目录
	 * @param filename
	 *            上传到FTP服务器上的文件名
	 * @param input
	 *            输入流
	 * @return 成功返回true，否则返回false
	 */
	public boolean uploadFile(String url, // FTP服务器hostname
			int port, // FTP服务器端口
			String username, // FTP登录账号
			String password, // FTP登录密码
			String path, // FTP服务器保存目录
			String filename, // 上传到FTP服务器上的文件名
			String encoding, // 文件编码格式
			InputStream input // 输入流
	) {
		boolean isSuccess = false;
		FTPClient ftp = null;
		try {
			ftp = createFtp(url, port, username, password, path, encoding);
			if (null == ftp) {
				return isSuccess;
			}
			isSuccess = ftp.storeFile(new String(filename.getBytes("UTF-8"), "iso-8859-1"), input);
			input.close();
			ftp.logout();
			isSuccess = true;
		} catch (IOException e) {
			// logger.error("FtpClientUtil.uploadFile", e);
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
					// logger.error("FtpClientUtil.uploadFile", ioe);
				}
			}
		}
		return isSuccess;
	}

	public FTPClient createFtp(String url, // FTP服务器hostname
			int port, // FTP服务器端口
			String username, // FTP登录账号
			String password, // FTP登录密码
			String path, // FTP服务器保存目录
			String encoding// 文件编码格式
	) {
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			// 连接FTP服务器，如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.connect(url, port);
			// if (!ftp.isRemoteVerificationEnabled()) {
			// 登录
			if (!ftp.login(username, password)) {
				// logger.error("FtpClientUtil.createFtp", "==ftp login faile==" + username);
				return null;
			}
			// }
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return null;
			}
			if (StringUtils.isBlank(encoding)) {
				encoding = "UTF-8";
			}
			ftp.setControlEncoding(encoding);
			// 指定文件类型，解决下载后文件大小变化的问题
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			if (!ftp.changeWorkingDirectory(path)) {
				String[] paths = path.split("/");
				for (String string : paths) {
					if (!"".equals(string)) {
						if (!ftp.changeWorkingDirectory(string)) {
							if (!ftp.makeDirectory(string)) {
								ftp.disconnect();
								// logger.error("createFtp", "==ftp changeWorkingDirectory faile==" + path +
								// "==" + string);
								return null;
							} else {
								if (!ftp.changeWorkingDirectory(string)) {
									ftp.disconnect();
									// logger.error("createFtp", "==ftp changeWorkingDirectory faile==" + path +
									// "==" + string);
									return null;
								}
							}
						}
					}
				}
			}
		} catch (IOException e) {
			// logger.error("createFtp", "==ftp createFtp==", e);
		}
		return ftp;
	}

	/**
	 * Description: 向FTP服务器上传文件
	 * 
	 * @Version1.0
	 * 
	 * @param url
	 *            FTP服务器hostname
	 * @param port
	 *            FTP服务器端口
	 * @param username
	 *            FTP登录账号
	 * @param password
	 *            FTP登录密码
	 * @param path
	 *            FTP服务器保存目录
	 * @param filename
	 *            上传到FTP服务器上的文件名
	 * @param encoding
	 *            文件编码格式
	 * @param content
	 *            字节
	 * @return 成功返回true，否则返回false
	 */
	public boolean uploadFile(String url, // FTP服务器hostname
			int port, // FTP服务器端口
			String username, // FTP登录账号
			String password, // FTP登录密码
			String path, // FTP服务器保存目录
			String filename, // 上传到FTP服务器上的文件名
			String encoding, // 文件编码格式
			byte[] content // 输入字节
	) {
		InputStream input = new ByteArrayInputStream(content);
		return uploadFile(url, port, username, password, path, filename, encoding, input);
	}

	/**
	 * Description: 向FTP服务器批量上传文件
	 * 
	 * @Version1.0
	 * 
	 * @param url
	 *            FTP服务器hostname
	 * @param port
	 *            FTP服务器端口
	 * @param username
	 *            FTP登录账号
	 * @param password
	 *            FTP登录密码
	 * @param path
	 *            FTP服务器保存目录
	 * @param encoding
	 *            文件编码格式
	 * @param fileBeanList
	 *            //文件信息集合
	 * @return 成功返回true，否则返回false
	 */
	public boolean uploadFileList(String url, // FTP服务器hostname
			int port, // FTP服务器端口
			String username, // FTP登录账号
			String password, // FTP登录密码
			String path, // FTP服务器保存目录
			String encoding, // 文件编码格式
			List<FileBean> fileBeanList// 文件信息集合
	) {
		if (null == fileBeanList || fileBeanList.isEmpty()) {
			return false;
		}
		boolean success = false;
		FTPClient ftp = createFtp(url, port, username, password, path, encoding);
		if (null == ftp) {
			return success;
		}
		try {
			InputStream input = null;
			success = true;
			for (FileBean fileBean : fileBeanList) {
				input = new ByteArrayInputStream(fileBean.getBytes());
				if (!ftp.storeFile(fileBean.getName(), input)) {
					success = false;
				}
				input.close();
			}
			ftp.logout();
		} catch (IOException e) {
			// logger.error("uploadFileList", "==ftp uploadFileList==", e);
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
					// logger.error("uploadFileList", "==ftp disconnect==", ioe);
				}
			}
		}
		return success;
	}

	/**
	 * Description: 从FTP服务器下载文件
	 * 
	 * @Version1.0
	 * 
	 * @param url
	 *            FTP服务器hostname
	 * @param port
	 *            FTP服务器端口
	 * @param username
	 *            FTP登录账号
	 * @param password
	 *            FTP登录密码
	 * @param remotePath
	 *            FTP服务器上的相对路径
	 * @param fileName
	 *            要下载的文件名
	 * @param encoding
	 *            文件编码
	 * @return
	 */
	public byte[] downFile(String url, // FTP服务器hostname
			int port, // FTP服务器端口
			String username, // FTP登录账号
			String password, // FTP登录密码
			String remotePath, // FTP服务器上的相对路径
			String fileName, // 要下载的文件名
			String encoding// 文件编码
	) {
		FTPClient ftp = createFtp(url, port, username, password, remotePath, encoding);
		try {
			FTPFile[] fs = ftp.listFiles();
			InputStream input = null;
			for (FTPFile ff : fs) {
				if (ff.getName().equals(fileName)) {
					input = ftp.retrieveFileStream(ff.getName());
					break;
				}
			}
			byte[] imgdata = null;
			if (null != input) {
				ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
				int ch;
				while ((ch = input.read()) != -1) {
					bytestream.write(ch);
				}
				imgdata = bytestream.toByteArray();
				bytestream.close();
				input.close();
			}
			ftp.logout();
			return imgdata;
		} catch (IOException e) {
			// logger.error("downFile", "==ftp downFile==", e);
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
					// logger.error("downFile", "==ftp disconnect==", ioe);
				}
			}
		}
		return null;
	}
}
