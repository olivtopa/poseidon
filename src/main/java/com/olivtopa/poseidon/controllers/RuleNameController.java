package com.olivtopa.poseidon.controllers;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.olivtopa.poseidon.domain.RuleName;
import com.olivtopa.poseidon.exceptions.DataNotFoundException;
import com.olivtopa.poseidon.repositories.RuleNameRepository;
import com.olivtopa.poseidon.services.RuleNameService;

@Controller
public class RuleNameController {
	
	private static final Logger logger = LoggerFactory.getLogger(RuleNameController.class);	
	
	@Autowired
	private RuleNameService ruleNameService;
	@Autowired 
	RuleNameRepository ruleNameRepository;

    @RequestMapping("/ruleName/list")
    public String home(Model model){
    	logger.info("Rule List page");
        model.addAttribute("ruleNames", ruleNameService.getAllRuleName());
        return "ruleName/list";
    }

    @GetMapping("/ruleName/add")
    public String addRuleForm(RuleName ruleName) {
    	logger.info("display Add Rule form");
        return "ruleName/add";
    }

    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName, BindingResult result, Model model) {
    	logger.info("Adding Rule {}",ruleName);
    	if (!result.hasErrors()) {
			ruleNameService.save(ruleName);
			model.addAttribute("ruleNames", ruleNameService.getAllRuleName());
			logger.info("ruleName added ! : id = {}", ruleName.getId());
			return "redirect:/ruleName/list";
		}
    	logger.info("Adding Rule error ! {}", result);
        return "ruleName/add";
    }

    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
    	logger.info("Display update Rule form");
    	RuleName ruleName = ruleNameService.getRuleById(id);
		model.addAttribute("ruleName", ruleName);
        return "ruleName/update";
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName,
                             BindingResult result, Model model) {
    	if (result.hasErrors()) {
    		logger.info("Rule update error ! {} ", result);
			return "ruleName/update";
		}
    	logger.info("Rule updating ...{}", ruleName);
    	ruleName.setId(id);
    	ruleNameService.save(ruleName);
		model.addAttribute("ruleNames", ruleNameService.getAllRuleName());
		logger.info("ruleName updated ! : {}", ruleName.getId());
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) {
    	logger.info("Deleting rule {}...",id);
    	RuleName ruleName = ruleNameRepository.findById(id)
				.orElseThrow(() -> new DataNotFoundException("Invalid ruleName Id :" + id));
    	ruleNameService.deleteBid(ruleName);
		model.addAttribute("ruleNames", ruleNameService.getAllRuleName());
		logger.info("Rule deleted !");
        return "redirect:/ruleName/list";
    }
}
