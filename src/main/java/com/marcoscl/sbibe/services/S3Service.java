package com.marcoscl.sbibe.services;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class S3Service {
	
	private Logger LOG = LoggerFactory.getLogger(S3Service.class);
	
	@Autowired
	private AmazonS3 s3cliente;
	
	@Value("${s3.bucket}")
	private String nomeBucket;
	
	public void uploadArquivo(String caminhoLocalArquivo) {
		try {
			File arquivo = new File(caminhoLocalArquivo);
			LOG.info("Iniciando upload");
			s3cliente.putObject(new PutObjectRequest(nomeBucket, "teste.png", arquivo));
			LOG.info("Finalizado upload");
		} catch (AmazonServiceException e) {
			LOG.info("AmazonServiceException: " + e.getErrorMessage());
			LOG.info("Status code: " + e.getErrorCode());
		} catch (AmazonClientException e) {
			LOG.info("AmazonClientException: " + e.getMessage());
		}
	}

}
