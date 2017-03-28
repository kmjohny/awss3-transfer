package org.jkm.awss3transfer.config;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.aws.inbound.S3StreamingMessageSource;
import org.springframework.integration.aws.support.S3RemoteFileTemplate;
import org.springframework.integration.aws.support.S3SessionFactory;
import org.springframework.integration.aws.support.filters.S3PersistentAcceptOnceFileListFilter;
import org.springframework.integration.aws.support.filters.S3RegexPatternFileListFilter;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.filters.ChainFileListFilter;
import org.springframework.integration.file.filters.FileListFilter;
import org.springframework.integration.file.remote.RemoteFileTemplate;
import org.springframework.integration.metadata.SimpleMetadataStore;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Encryption;
import com.amazonaws.services.s3.model.S3ObjectSummary;

@Configuration (value = "AwsS3DownloadConfig")
public class AwsS3Download {
  @Value("${aws.s3.bucket}")
  private String s3Bucket;
  
  @Value("${aws.s3.download.sseFilterRegex}")
  private String sseFilterRegex;

  @Value("${aws.s3.download.cseFilterRegex}")
  private String cseFilterRegex;

  @Value("${aws.s3.download.rawFilterRegex}")
  private String rawFilterRegex;
  
  @Value("${aws.kms.cmkId}")
  private String kmsCmkId;

  /**
   *
   *
   * @param amazonS3
   * @return
   */
  @Bean
  public RemoteFileTemplate<S3ObjectSummary> s3RemoteFileTemplate (AmazonS3 amazonS3) {
    return new S3RemoteFileTemplate (new S3SessionFactory(amazonS3));
  } 

  /**
   *
   *
   * @param amazonS3
   * @return
   */
  @Bean
  public RemoteFileTemplate<S3ObjectSummary> s3RemoteFileTemplateWithCSE (AmazonS3Encryption amazonS3Encryption) {
    return new S3RemoteFileTemplate (new S3SessionFactory(amazonS3Encryption));
  } 

  /**
   *
   *
   * @return
   */
  @Bean
  public FileListFilter<S3ObjectSummary> fileListFilter () {
    ChainFileListFilter<S3ObjectSummary> chainFileListFilter = new ChainFileListFilter<S3ObjectSummary>();
    chainFileListFilter.addFilters(new S3RegexPatternFileListFilter(rawFilterRegex), 
        new S3PersistentAcceptOnceFileListFilter(new SimpleMetadataStore(), "streaming"));
    return chainFileListFilter;
  } 

  /**
   *
   *
   * @return
   */
  @Bean
  public FileListFilter<S3ObjectSummary> fileListFilterWithCSE () {
    ChainFileListFilter<S3ObjectSummary> chainFileListFilter = new ChainFileListFilter<S3ObjectSummary>();
    chainFileListFilter.addFilters(new S3RegexPatternFileListFilter(cseFilterRegex), 
        new S3PersistentAcceptOnceFileListFilter(new SimpleMetadataStore(), "streamingWithCSE"));
    return chainFileListFilter;
  } 
  
  /**
   *
   *
   * @return
   */
  @Bean
  public FileListFilter<S3ObjectSummary> fileListFilterWithSSE () {
    ChainFileListFilter<S3ObjectSummary> chainFileListFilter = new ChainFileListFilter<S3ObjectSummary>();
    chainFileListFilter.addFilters(new S3RegexPatternFileListFilter(sseFilterRegex), 
        new S3PersistentAcceptOnceFileListFilter(new SimpleMetadataStore(), "streamingWithSSE"));
    return chainFileListFilter;
  } 
  
  /**
   *
   *
   * @param amazonS3
   * @return
   */
  @Bean
  public MessageSource<InputStream> s3MessageSource(RemoteFileTemplate<S3ObjectSummary> s3RemoteFileTemplate, 
      FileListFilter<S3ObjectSummary> fileListFilter) {    
    S3StreamingMessageSource messageSource = new S3StreamingMessageSource(s3RemoteFileTemplate);
    messageSource.setRemoteDirectory(s3Bucket);
    messageSource.setFilter(fileListFilter);
    return messageSource;
  }

  /**
   *
   *
   * @param amazonS3
   * @return
   */
  @Bean
  public MessageSource<InputStream> s3MessageSourceWithCSE(RemoteFileTemplate<S3ObjectSummary> s3RemoteFileTemplateWithCSE, 
      FileListFilter<S3ObjectSummary> fileListFilterWithCSE) {    
    S3StreamingMessageSource messageSource = new S3StreamingMessageSource(s3RemoteFileTemplateWithCSE);
    messageSource.setRemoteDirectory(s3Bucket);
    messageSource.setFilter(fileListFilterWithCSE);
    return messageSource;
  }
  
  /**
   *
   *
   * @param amazonS3
   * @return
   */
  @Bean
  public MessageSource<InputStream> s3MessageSourceWithSSE(RemoteFileTemplate<S3ObjectSummary> s3RemoteFileTemplate, 
      FileListFilter<S3ObjectSummary> fileListFilterWithSSE) {    
    S3StreamingMessageSource messageSource = new S3StreamingMessageSource(s3RemoteFileTemplate);
    messageSource.setRemoteDirectory(s3Bucket);
    messageSource.setFilter(fileListFilterWithSSE);
    return messageSource;
  }
}
