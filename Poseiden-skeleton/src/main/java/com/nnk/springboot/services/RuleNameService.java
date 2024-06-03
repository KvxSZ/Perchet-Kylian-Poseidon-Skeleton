package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RuleNameService {

    @Autowired
    RuleNameRepository ruleNameRepository;

    public List<RuleName> getRuleNames(){return ruleNameRepository.findAll();}

    public Optional<RuleName> getRuleNameById(Integer id){return ruleNameRepository.findById(id);}

    @Transactional
    public void addRuleName(RuleName ruleName){ruleNameRepository.save(ruleName);}

    @Transactional
    public RuleName updateRuleName(Integer id, RuleName ruleNameDetails) {
        RuleName ruleName = ruleNameRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid rule name Id:" + id));

        ruleName.setName(ruleNameDetails.getName());
        ruleName.setDescription(ruleNameDetails.getDescription());
        ruleName.setJson(ruleNameDetails.getJson());
        ruleName.setTemplate(ruleNameDetails.getTemplate());
        ruleName.setSql(ruleNameDetails.getSql());
        ruleName.setSqlPart(ruleNameDetails.getSqlPart());

        return ruleNameRepository.save(ruleName);
    }

    @Transactional
    public void deleteRuleName(Integer id) {
        RuleName ruleName = ruleNameRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid rule name Id:" + id));
        ruleNameRepository.delete(ruleName);
    }

}
