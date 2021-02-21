package com.sip.amsV3.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.sip.amsV3.entities.Article;
import com.sip.amsV3.entities.Provider;
import com.sip.amsV3.repositories.ArticleRepository;
import com.sip.amsV3.repositories.ProviderRepository;

@Controller
@RequestMapping("/article/")
public class ArticleController {

	public static String uploadDirectory = System.getProperty("user.dir")+"/src/main/resources/static/uploads";
	
	private final ArticleRepository articleRepository;
	private final ProviderRepository providerRepository;

	public class ImageUtil{
		public String getImageData(byte[] byteData) {
			return Base64.getMimeEncoder().encodeToString(byteData);
		}
	}
	
	@Autowired
	public ArticleController(ArticleRepository articleRepository, ProviderRepository providerRepository) {
		this.articleRepository = articleRepository;
		this.providerRepository = providerRepository;
	}
	
	@GetMapping("list")
	public String listProviders(Model model) {
		
		List<Article> la = (List<Article>) articleRepository.findAll();

		if (la.size() == 0)
			la = null;

		model.addAttribute("articles", la);
		return "article/listArticles";
	}

	@GetMapping("add")
	public String showAddArticleForm(Model model) {

		model.addAttribute("providers", providerRepository.findAll());
		model.addAttribute("article", new Article());
		return "article/addArticle";
	}

	@PostMapping("add")
	// @ResponseBody
	public String addArticle(@Valid Article article, BindingResult result,
			@RequestParam(name = "providerId", required = false) Long p,
			@RequestParam("imageFile") MultipartFile imageFile,
			@RequestParam("files") MultipartFile file, Model model) {
		
		if (result.hasErrors()) {
			model.addAttribute("providers", providerRepository.findAll());
			System.out.println(article);
			
			return "article/addArticle";
		}
		
		Provider provider = providerRepository.findById(p)
				.orElseThrow(() -> new IllegalArgumentException("Invalid provider Id:" + p));
		
		article.setProvider(provider);
		
		/// upload part by BLOB command, store image in the DB
		
		Path imgPath = Paths.get(uploadDirectory, imageFile.getOriginalFilename());
		//System.out.println(imgPath);
		//System.out.println(imgPath.toString());
		
		File file1 = new File (imgPath.toString());
		byte[] imInBytes = new byte[(int) file1.length()];
		
		article.setImage(imInBytes);
				
		/// part upload by file path
    	
    	StringBuilder fileName = new StringBuilder();
    	Random rand = new Random();
		int x = rand.nextInt(6) + 5;
		
		fileName.append(x).append(file.getOriginalFilename());
		
    	Path fileNameAndPath = Paths.get(uploadDirectory, fileName.toString());
    	try {
			Files.write(fileNameAndPath, file.getBytes());
		} 
    	catch (IOException e) {
			e.printStackTrace();
		}
    	
		 article.setPicture(fileName.toString());

		
		articleRepository.save(article);
		return "redirect:list";

		// return article.getLabel() + " " +article.getPrice() + " " + p.toString();
	}

	@GetMapping("delete/{id}")
	public String deleteProvider(@PathVariable("id") long id, Model model) {
		Article artice = articleRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid provider Id:" + id));
		
		articleRepository.delete(artice);
		return "redirect:../list";
	}

	@GetMapping("edit/{id}")
	public String showArticleFormToUpdate(@PathVariable("id") long id, Model model) {
		Article article = articleRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid provider Id:" + id));

		model.addAttribute("article", article);
		model.addAttribute("providers", providerRepository.findAll());
		model.addAttribute("idProvider", article.getProvider().getId());

		return "article/updateArticle";
	}

	@PostMapping("edit")
	public String updateArticle(@Valid Article article, BindingResult result, Model model,
			@RequestParam(name = "providerId", required = false) Long p) {
		if (result.hasErrors()) {
			
			return "article/updateArticle";
		}

		Provider provider = providerRepository.findById(p)
				.orElseThrow(() -> new IllegalArgumentException("Invalid provider Id:" + p));
		article.setProvider(provider);

		articleRepository.save(article);
		return "redirect:list";
	}
	
	@GetMapping("show/{id}")
    public String showArticleDetails(@PathVariable("id") long id, Model model) {
		
    	Article article = articleRepository.findById(id)
            .orElseThrow(()->new IllegalArgumentException("Invalid provider Id:" + id));
    	InputStream is = new ByteArrayInputStream(article.getImage());
    	/*
    	 * show images using two different ways
    	 */
    	model.addAttribute("image", Base64.getEncoder().encodeToString(article.getImage()));
    	model.addAttribute("is", is);
    	//System.out.println(Base64.getEncoder().encodeToString(article.getImage()));
    	//System.out.println(article.getImage());
    	//model.addAttribute("image", fileInputStream);
    	  model.addAttribute("article", article);
    	  System.out.println(is);
        return "article/showArticle";
    }

	@GetMapping("display/{id}")
	void displayImage (@PathVariable("id") Long id, HttpServletResponse response, Article article) 
			throws ServletException, IOException {
		//log.info("Id: " + id);
		article = articleRepository.findById(id)
				.orElseThrow(()->new IllegalArgumentException("Invalid provider Id:" + id));
		//response.setContentType("image/jpg");
		//response.getOutputStream().write(article.getImage());
		//response.getOutputStream().close();
		InputStream is = new ByteArrayInputStream(article.getImage());
		IOUtils.copy(is, response.getOutputStream());
		//System.out.println(is.toString());
		//System.out.println(response);
		//System.out.println("Display void is working" + id);
		
	}
	
	
}


