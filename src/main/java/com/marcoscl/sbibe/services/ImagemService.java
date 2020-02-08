package com.marcoscl.sbibe.services;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.marcoscl.sbibe.services.exceptions.ArquivoException;

@Service
public class ImagemService {
	
	public BufferedImage getJpgImagemArquivo(MultipartFile uploadedFile) {
		String extensao = FilenameUtils.getExtension(uploadedFile.getOriginalFilename());
		if(!"png".equals(extensao) && !"jpg".equals(extensao)) {
			throw new ArquivoException("Somente imagens PNG e JPG são permitidas");
		}
		
		try {
			BufferedImage imagem = ImageIO.read(uploadedFile.getInputStream());
			if ("png".equals(extensao)) {
				imagem = pngParaJpg(imagem);
			}
			return imagem;
		} catch (IOException e) {
			throw new ArquivoException("Erro ao ler arquivo");
		}
		
	}

	public BufferedImage pngParaJpg(BufferedImage imagem) {
		BufferedImage imagemJpg = new BufferedImage(imagem.getWidth(), imagem.getHeight(), BufferedImage.TYPE_INT_RGB);
		imagemJpg.createGraphics().drawImage(imagem, 0, 0, Color.WHITE, null); // Color.WHITE : preencher o fundo com o branco
		return imagemJpg;
	}
	
	public InputStream getInputStream(BufferedImage imagem, String extensao) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ImageIO.write(imagem, extensao, outputStream);
			return new ByteArrayInputStream(outputStream.toByteArray());
		} catch (IOException e) {
			throw new ArquivoException("Erro ao ler arquivo");
		}
	}
	
//	Recortar imagem
	public BufferedImage cortarQuadrado(BufferedImage origemImagem) {
		int minimo = (origemImagem.getHeight() <= origemImagem.getWidth()) ? origemImagem.getHeight() : origemImagem.getWidth();
		return Scalr.crop(origemImagem, (origemImagem.getWidth()/2) - (minimo/2), (origemImagem.getHeight()/2) - (minimo/2), minimo, minimo);
	}
	
	public BufferedImage redimensionar(BufferedImage origemImagem, int tamanho) {
		return Scalr.resize(origemImagem, Scalr.Method.ULTRA_QUALITY, tamanho);
	}
	
}
