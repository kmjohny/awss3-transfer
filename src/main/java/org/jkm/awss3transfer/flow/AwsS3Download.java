package org.jkm.awss3transfer.flow;

import java.io.File;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.file.FileHeaders;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.transformer.StreamTransformer;

@Configuration (value="AwsS3DownloadFlow")
public class AwsS3Download {

	@Value("${awss3.transfer.targetDirectory}")
	private File targetDirectory;

  @Autowired
  private MessageSource<InputStream> s3MessageSource;

  @Autowired
  private MessageSource<InputStream> s3MessageSourceWithCSE;
  
  @Autowired
  private MessageSource<InputStream> s3MessageSourceWithSSE;
 
  @Bean
  public IntegrationFlow s3DownloadIntegrationFlow () {
    return IntegrationFlows
							.from(s3MessageSource, e -> e.poller(Pollers.fixedDelay(1000)))
              .enrichHeaders(h -> h.headerExpression(FileHeaders.FILENAME, "headers['file_remoteFile']"))
              .transform(new StreamTransformer())
              .handleWithAdapter(a -> a.fileGateway(targetDirectory))
              .log(LoggingHandler.Level.INFO, "org.jkm.awss3transfer.flow.awss3download.raw.logger")
              .channel(new QueueChannel())
              .get();
  }
  

  @Bean
  public IntegrationFlow s3DownloadIntegrationWithCSEFlow () {
    return IntegrationFlows
							.from(s3MessageSourceWithCSE, e -> e.poller(Pollers.fixedDelay(1000)))
              .enrichHeaders(h -> h.headerExpression(FileHeaders.FILENAME, "headers['file_remoteFile']"))
              .transform(new StreamTransformer())
              .handleWithAdapter(a -> a.fileGateway(targetDirectory))
              .log(LoggingHandler.Level.INFO, "org.jkm.awss3transfer.cse.flow.awss3download.cse.logger")
              .channel(new QueueChannel())
              .get();
  }
  
  
  @Bean
  public IntegrationFlow s3DownloadIntegrationWithSSEFlow () {
    return IntegrationFlows
							.from(s3MessageSourceWithSSE, e -> e.poller(Pollers.fixedDelay(1000)))
              .enrichHeaders(h -> h.headerExpression(FileHeaders.FILENAME, "headers['file_remoteFile']"))
              .transform(new StreamTransformer())
              .handleWithAdapter(a -> a.fileGateway(targetDirectory))
              .log(LoggingHandler.Level.INFO, "org.jkm.awss3transfer.sse.flow.awss3download.sse.logger")
              .channel(new QueueChannel())
              .get();
  }
}
