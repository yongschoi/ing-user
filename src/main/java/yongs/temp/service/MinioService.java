package yongs.temp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import yongs.temp.config.MinioConfig;

@Service
public class MinioService {	
	private static final String MINIO_ROOT_USER = "minioadmin";
	private static final String MINIO_ROOT_PASSWORD = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY";
	 
	private MinioClient minioClient;
	
	@Autowired 
    public MinioService(MinioConfig config) {
		minioClient = MinioClient.builder()
				.endpoint("http://" + config.getHost() + ":" + config.getPort())
				.credentials(MINIO_ROOT_USER, MINIO_ROOT_PASSWORD)
				.build();
    }
	
	public String getObjectUrl(String bucket, String name) throws Exception {
		return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
			      .method(Method.GET)
			      .bucket(bucket)
			      .object(name)
			      .build());
	}
}