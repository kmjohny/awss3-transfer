package org.jkm.awss3transfer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.twitter.inbound.SearchReceivingMessageSource;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

@Configuration (value="TwitterConfig")
public class Twitter {
  /** Twitter consumer key. */
  @Value("${twitter.oauth.consumerKey}")
  private transient String consumerKey;

  /** Twitter consumer Secret. */
  @Value("${twitter.oauth.consumerSecret}")
  private transient String consumerSecret;

  /** Twitter access token. */
  @Value("${twitter.oauth.accessToken}")
  private transient String accessToken;

  /** Twitter access token secret. */
  @Value("${twitter.oauth.accessTokenSecret}")
  private transient String accessTokenSecret;

  @Value("${awss3.transfer.twitter.query}")
  private transient String twitterQuery;

  /**
   * Return a twitter template bean.
   *
   * @return TwitterTemplate twitter template object.
   */
  @Bean
  public TwitterTemplate twitterTemplate() {
    return new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);
  }

  /**
   * Return a twitter search inbound adapter.
   *
   * @return SearchReceivingMessageSource Twitter search inbound adapter.
   */
  @Bean
  public MessageSource<Tweet> searchReceivingMessageSource() {
    final SearchReceivingMessageSource searchReceiveMessageSource =
        new SearchReceivingMessageSource(twitterTemplate(), "metadataKey");
    searchReceiveMessageSource.setQuery(twitterQuery);
    return searchReceiveMessageSource;
  }
}
