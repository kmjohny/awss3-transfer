package org.jkm.awss3transfer.flow;

import java.io.File;

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
import org.springframework.social.twitter.api.Tweet;

@Configuration (value="TwitterFlow")
public class Twitter {

  @Value("${awss3.transfer.sourceDirectory}")
	private File sourceDirectory;

  @Autowired
  private MessageSource<Tweet> searchReceivingMessageSource;
 
  @Bean
  public IntegrationFlow twitterSearchIntegrationFlow () {
    return IntegrationFlows
							.from(searchReceivingMessageSource, e -> e.poller(Pollers.fixedDelay(1000)))
              .enrichHeaders(h -> h.headerExpression(FileHeaders.FILENAME, "payload.id + '.json'"))
              .<Tweet, String> transform(t -> t.getText())
              .handleWithAdapter(a -> a.fileGateway(sourceDirectory))
              .log(LoggingHandler.Level.INFO, "org.jkm.awss3transfer.flow.twitter.raw.logger")
              .channel(new QueueChannel())
              .get();
  }
}
