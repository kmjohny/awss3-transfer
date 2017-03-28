package org.jkm.awss3transfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.IntegrationComponentScan;

@SpringBootApplication
@IntegrationComponentScan
public class Awss3TransferApplication {

	public static void main(String[] args) {
		SpringApplication.run(Awss3TransferApplication.class, args);
	}
}
