package com.marcoscl.sbibe.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.marcoscl.sbibe.services.exceptions.ArquivoException;

@Service
public class S3Service {

	private Logger LOG = LoggerFactory.getLogger(S3Service.class);

	@Autowired
	private AmazonS3 s3cliente;

	@Value("${s3.bucket}")
	private String nomeBucket;

	public URI uploadArquivo(MultipartFile arquivoMultiPart) {
		try {
			String nomeArquivo = arquivoMultiPart.getOriginalFilename();
			InputStream is = arquivoMultiPart.getInputStream();
			String tipoConteudo = arquivoMultiPart.getContentType();
			return uploadArquivo(is, nomeArquivo, tipoConteudo);
		} catch (IOException e) {
			throw new ArquivoException("Erro de IO: " + e.getMessage());
		}
	}

	public URI uploadArquivo(InputStream is, String nomeArquivo, String tipoConteudo) {
		try {
			ObjectMetadata metaDado = new ObjectMetadata();
			metaDado.setContentType(tipoConteudo);
			LOG.info("Iniciando upload");
			s3cliente.putObject(nomeBucket, nomeArquivo, is, metaDado);
			LOG.info("Finalizado upload");
			return s3cliente.getUrl(nomeBucket, nomeArquivo).toURI();
		} catch (URISyntaxException e) {
			throw new ArquivoException("Erro ao converter URL para URI");
		}
	}

}
