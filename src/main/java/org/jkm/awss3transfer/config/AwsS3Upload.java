package org.jkm.awss3transfer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.aws.outbound.S3MessageHandler;
import org.springframework.messaging.MessageHandler;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Encryption;
import com.amazonaws.services.s3.model.ObjectMetadata;

@Configuration (value = "AwsS3UploadConfig")
public class AwsS3Upload {
  @Value("${aws.s3.sseFolder}")
  private String sseFolder;

  @Value("${aws.s3.cseFolder}")
  private String cseFolder;

  @Value("${aws.s3.rawFolder}")
  private String rawFolder;

  /**
   * MessageHandler instance to upload file to s3 bucket.
   * 
   * @param AmazonS3 Amazon s3 client.
   * @return MessageHandler messageHandler object.
   */
  @Bean
  public MessageHandler s3MessageHandler (AmazonS3 amazonS3) {
    S3MessageHandler s3MessageHandler = new S3MessageHandler(amazonS3, rawFolder);
    return s3MessageHandler;
  }

  /**
   *
   *
   * @param amazonS3Encryption
   * @return
   */
  @Bean
  public MessageHandler s3MessageHandlerWithCSE (AmazonS3Encryption amazonS3Encryption) {
    S3MessageHandler s3MessageHandler = new S3MessageHandler(amazonS3Encryption, cseFolder);
    return s3MessageHandler;
  }
  
  /**
   *
   *
   * @param amazonS3
   * @return
   */
  @Bean
  public MessageHandler s3MessageHandlerWithSSE (AmazonS3 amazonS3) {
    S3MessageHandler s3MessageHandler = new S3MessageHandler(amazonS3, sseFolder);
    s3MessageHandler.setUploadMetadataProvider(
        (metadata, message) -> metadata.setSSEAlgorithm(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION));
    return s3MessageHandler;
  }
}
