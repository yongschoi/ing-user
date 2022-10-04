package yongs.temp.service;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import yongs.temp.config.MinioConfig;

@Slf4j
@Service
public class MinioService {
	private static final String MINIO_ROOT_USER = "minioadmin";
	private static final String MINIO_ROOT_PASSWORD = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY";

	private MinioClient minioClient;

	@Autowired
	public MinioService(MinioConfig config) {
		minioClient = MinioClient.builder().endpoint("http://" + config.getHost() + ":" + config.getPort())
				.credentials(MINIO_ROOT_USER, MINIO_ROOT_PASSWORD).build();
	}

	public String getObjectUrl(String bucket, String objectName) {
		String url = null;
		try {
			url = minioClient.getPresignedObjectUrl(
					GetPresignedObjectUrlArgs.builder().method(Method.GET).bucket(bucket).object(objectName).build());
		} catch (Exception e) {
			log.error("MinioService.getObjectUrl() ::: ERROR ");
			e.printStackTrace();
		}
		return url;
	}

	public void putObject(String bucket, String objectName, MultipartFile mFile) {
		try (InputStream is = mFile.getInputStream()) {
			minioClient.putObject(
					PutObjectArgs.builder().bucket(bucket).object(objectName).stream(is, mFile.getSize(), -1).build());
		} catch (Exception e) {
			log.error("MinioService.putObject() ::: ERROR ");
			e.printStackTrace();
		}
	}

	public void updateObject(String bucket, String objectName, MultipartFile mFile) {
		try (InputStream is = mFile.getInputStream()) {
			minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(objectName).build());
			minioClient.putObject(
					PutObjectArgs.builder().bucket(bucket).object(objectName).stream(is, mFile.getSize(), -1).build());			
		} catch (Exception e) {
			log.error("MinioService.updateObject() ::: ERROR ");
			e.printStackTrace();
		}
	}
}