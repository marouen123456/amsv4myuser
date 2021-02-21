package com.sip.amsV3.controllers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sip.amsV3.entities.Role;
import com.sip.amsV3.entities.User;
import com.sip.amsV3.repositories.RoleRepository;
import com.sip.amsV3.repositories.UserRepository;

@Controller
@RequestMapping("/accounts/")
public class AccountController {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	public AccountController(UserRepository userRepository, RoleRepository roleRepository) {
		this.userRepository=userRepository;
		this.roleRepository=roleRepository;
	}
	
	@GetMapping("list")
	public String showAccountsList(Model model) {
		List<User> accountsList = (List<User>)userRepository.findAll();
		int accountsNumber=accountsList.size();
		if(accountsNumber == 0)
			accountsList=null;
		List<Role> roleList = (List<Role>)roleRepository.findAll();
		
			model.addAttribute("accountsNumber", accountsNumber);
			model.addAttribute("accountsList", accountsList);
			model.addAttribute("roleList", roleList);
		return ("accounts/accountsList");
	}
	
	@GetMapping("activate/{id}/{email}")
	public String activateAccount(@PathVariable("id") int id, 
								@PathVariable("email") String email) {
		sendEmail(email, true);
		User user = userRepository.findById(id);
		user.setActive(1);
		userRepository.saveAndFlush(user);
		return "redirect:../../list";
	}
	
	@GetMapping("disactivate/{id}/{email}")
	//@ResponseBody
	public String disactivateAccount(@PathVariable("id") int id,
								@PathVariable("email") String email) {
		sendEmail(email, false);
		User user = userRepository.findById(id);
		user.setActive(0);
		userRepository.save(user);
		return ("redirect:../../list");
	}
	
	@PostMapping("editRole")
	//@ResponseBody
	public String editAccountRole(@RequestParam("id") int id, 
								@RequestParam("newRole") String newRole) {
		User user = userRepository.findById(id);
		Role userRole = roleRepository.findByRole(newRole);
		user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		userRepository.saveAndFlush(user);
		return ("redirect:list");
	}
	
	
	void sendEmail(String email, boolean state) {
		 SimpleMailMessage msg = new SimpleMailMessage();
		 msg.setTo(email);
		 if(state == true)
		 {
		 msg.setSubject("Account Has Been Activated");
		 msg.setText("Hello, Your account has been activated. " + "You can log in : http://127.0.0.1:81/login"
		 + " \n Best Regards!");
		 }
		 else
		 {
		 msg.setSubject("Account Has Been disactivated");
		 msg.setText("Hello, Your account has been disactivated.");
		 }
		 javaMailSender.send(msg);
		 }

}

