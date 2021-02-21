package com.sip.amsV3.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sip.amsV3.entities.Article;
import com.sip.amsV3.entities.Provider;
import com.sip.amsV3.repositories.ProviderRepository;
@Controller
@RequestMapping("/provider/")
public class ProviderController {
	public static String uploadDirectoryProvider = System.getProperty("user.dir")+"/src/main/resources/static/uploads";
	
	private final ProviderRepository providerRepository;
    @Autowired
    public ProviderController(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }
    
    @GetMapping("list")
    public String listProviders(Model model) {
        model.addAttribute("providers", providerRepository.findAll());
        return "provider/listProvider";
    }
    
    @GetMapping("add")
    public String showAddProviderForm(Provider provider) {
        return "provider/addProvider";
    }
    
    @PostMapping("add")
    public String addProvider(@Valid Provider provider, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "provider/addProvider";
        }
        providerRepository.save(provider);
        return "redirect:list";
    }
    
    @GetMapping("delete/{id}")
    public String deleteProvider(@PathVariable("id") long id, Model model) {
        Provider provider = providerRepository.findById(id)
            .orElseThrow(()-> new IllegalArgumentException("Invalid provider Id:" + id));
        providerRepository.delete(provider);
        model.addAttribute("providers", providerRepository.findAll());
        return "provider/listProvider";
    }
    
    
    @GetMapping("edit/{id}")
    public String showProviderFormToUpdate(@PathVariable("id") long id, Model model) {
        Provider provider = providerRepository.findById(id)
            .orElseThrow(()->new IllegalArgumentException("Invalid provider Id:" + id));
        model.addAttribute("provider", provider);
        return "provider/updateProvider";
    }
    @PostMapping("update/{id}")
    public String updateProvider(@PathVariable("id") long id, @Valid Provider provider, BindingResult result,
        Model model) {
        if (result.hasErrors()) {
            provider.setId(id);
            return "provider/updateProvider";
        }
        providerRepository.save(provider);
        model.addAttribute("providers", providerRepository.findAll());
        return "provider/listProvider";
    }
    
    @GetMapping("show/{id}")
	public String showProvider(@PathVariable("id") long id, Model model) {
		Provider provider = providerRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid provider Id:" + id));
		List<Article> articles = providerRepository.findArticlesByProvider(id);
		for (Article a : articles)
			System.out.println("Article = " + a.getLabel());
		
		model.addAttribute("articles", articles);
		model.addAttribute("provider", provider);
		return "provider/showProvider";
	}


}

