package org.jkm.awss3transfer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3Encryption;
import com.amazonaws.services.s3.AmazonS3EncryptionClientBuilder;
import com.amazonaws.services.s3.model.EncryptionMaterialsProvider;
import com.amazonaws.services.s3.model.KMSEncryptionMaterialsProvider;

@Configuration (value = "AwsS3Config")
public class AwsS3 {
  @Value("${aws.kms.cmkId}")
  private String kmsCmkId;

  /**
   *
   *
   * @return
   */
  @Bean
  public AmazonS3 amazonS3() {
    AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
                        .withRegion(Regions.US_EAST_1)
                        .build();
    return amazonS3;
  } 

  /**
   *
   *
   * @return
   */
  @Bean
  public EncryptionMaterialsProvider encryptionMaterialsProvider() {
    return new KMSEncryptionMaterialsProvider(kmsCmkId);
  }

  /**
   *
   *
   * @param encryptionMaterialsProvider
   * @return
   */
  @Bean
  public AmazonS3Encryption amazonS3Encryption(EncryptionMaterialsProvider encryptionMaterialsProvider) {
    AmazonS3Encryption amazonS3Encryption = AmazonS3EncryptionClientBuilder.standard()
                        .withRegion(Regions.US_EAST_1)
                        .withEncryptionMaterials(encryptionMaterialsProvider)
                        .build();
    return amazonS3Encryption;
  } 
}
