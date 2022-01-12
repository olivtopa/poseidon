package com.olivtopa.poseidon.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.olivtopa.poseidon.domain.RuleName;
import com.olivtopa.poseidon.repositories.RuleNameRepository;
import com.olivtopa.poseidon.services.RuleNameService;

@Controller
public class RuleNameController {
	@Autowired
	private RuleNameService ruleNameService;
	@Autowired RuleNameRepository ruleNameRepository;

    @RequestMapping("/ruleName/list")
    public String home(Model model)
    {
        ruleNameService.getAllRuleName();
        return "ruleName/list";
    }

    @GetMapping("/ruleName/add")
    public String addRuleForm(RuleName ruleName) {
        return "ruleName/add";
    }

    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName, BindingResult result, Model model) {
    	if (!result.hasErrors()) {
			ruleNameService.save(ruleName);
			model.addAttribute("rueleName", ruleNameService.getAllRuleName());
		}
        return "ruleName/add";
    }

    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
    	RuleName ruleName = ruleNameRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid RuleName Id :" + id));
		model.addAttribute("ruleName", ruleName);
        return "ruleName/update";
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName,
                             BindingResult result, Model model) {
    	if (result.hasErrors()) {
			return "ruleName/update";
		}
    	ruleName.setId(id);
		model.addAttribute("ruleName", ruleName);
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) {
    	RuleName ruleName = ruleNameRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid ruleName Id :" + id));
    	ruleNameService.deleteBid(ruleName);
		model.addAttribute("ruleName", ruleNameService.getAllRuleName());
        return "redirect:/ruleName/list";
    }
}
