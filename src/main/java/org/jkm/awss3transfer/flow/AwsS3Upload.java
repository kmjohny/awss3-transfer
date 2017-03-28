package org.jkm.awss3transfer.flow;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.messaging.MessageHandler;

@Configuration (value="AwsS3UploadFlow")
public class AwsS3Upload {

	@Value("${awss3.transfer.sourceDirectory}")
	private File sourceDirectory;

  @Value("${aws.s3.upload.filenameFilterRegex}")
  private String filenameFilterRegex;
  
  @Autowired
  private MessageHandler s3MessageHandler;

  @Autowired
  private MessageHandler s3MessageHandlerWithSSE;
  
  @Autowired
  private MessageHandler s3MessageHandlerWithCSE;

  @Bean
  public IntegrationFlow s3UploadIntegrationFlow () {
    return IntegrationFlows
							.from(s -> s.file(sourceDirectory)
														.regexFilter(filenameFilterRegex),	
										e -> e.poller(Pollers.fixedDelay(1000)))
              .publishSubscribeChannel(c -> c
                  .subscribe(s -> s.handle(s3MessageHandler))
                  .subscribe(s -> s.handle(s3MessageHandlerWithSSE))
                  .subscribe(s -> s.handle(s3MessageHandlerWithCSE)))
              .log(LoggingHandler.Level.INFO, "org.jkm.awss3transfer.flow.awss3upload.logger")
              .get();
  }
}
