package com.sip.amsV3.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;

import com.sip.amsV3.entities.Role;
import com.sip.amsV3.entities.User;
import com.sip.amsV3.repositories.RoleRepository;




@Controller
@RequestMapping("/role/")
public class RoleController {

	private final RoleRepository roleRepository;
	
	@Autowired
	public RoleController (RoleRepository roleRepository) {
		this.roleRepository=roleRepository;
	}
	
	@GetMapping("add")
	//
	public String showAddRole(){
		return ("role/addRole");
	}
	
	@PostMapping("add")
	public String saveAddedRole(@RequestParam(name = "role", required = false) String role){
		Role r = new Role(role);
		Role savedR = roleRepository.save(r);
		System.out.println("New Role" + savedR);
		return ("redirect:list");
	}
	@GetMapping("list")
	public String showListOfRoles(Model model) {
		List<Role> roles = (List<Role>) roleRepository.findAll();
		int nbr = roles.size();
		if(nbr == 0)
			roles = null;
		model.addAttribute("roles", roles);
		model.addAttribute("nbr", nbr);
		return ("role/listRoles");
	}
	
}
