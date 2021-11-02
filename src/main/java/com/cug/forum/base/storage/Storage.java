package com.cug.forum.base.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface Storage {

	/**
	 * 存储图片
	 * @param file
	 * @param basePath
	 * @return
	 * @throws IOException
	 */
	String store(MultipartFile file, String basePath) throws Exception;

	/**
	 * 存储压缩图片
	 * @param file
	 * @param basePath
	 * @return
	 * @throws IOException
	 */
	String storeScale(MultipartFile file, String basePath, int maxWidth) throws Exception;

	/**
	 * 存储压缩图片
	 * @param file
	 * @param basePath
	 * @return
	 * @throws IOException
	 */
	String storeScale(MultipartFile file, String basePath, int width, int height) throws Exception;

	/**
	 * 存储路径
	 * @param storePath
	 */
	void deleteFile(String storePath);

	String writeToStore(byte[] bytes, String pathAndFileName) throws Exception;
}
