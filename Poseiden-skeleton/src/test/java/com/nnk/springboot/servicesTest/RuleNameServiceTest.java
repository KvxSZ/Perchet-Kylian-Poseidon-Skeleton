package com.nnk.springboot.servicesTest;

import com.nnk.springboot.services.RuleNameService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RuleNameServiceTest {

    @InjectMocks
    private RuleNameService ruleNameService;

    @Mock
    private RuleNameRepository ruleNameRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getRuleNames() {
        RuleName rule1 = new RuleName("Rule1", "Description1", "Json1", "Template1", "SqlStr1", "SqlPart1");
        RuleName rule2 = new RuleName("Rule2", "Description2", "Json2", "Template2", "SqlStr2", "SqlPart2");
        List<RuleName> rules = Arrays.asList(rule1, rule2);

        when(ruleNameRepository.findAll()).thenReturn(rules);

        List<RuleName> result = ruleNameService.getRuleNames();

        assertEquals(2, result.size());
        verify(ruleNameRepository, times(1)).findAll();
    }

    @Test
    void getRuleNameById() {
        RuleName rule = new RuleName();
        rule.setName("Rule1");
        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(rule));

        Optional<RuleName> result = ruleNameService.getRuleNameById(1);

        assertTrue(result.isPresent());
        assertEquals("Rule1", result.get().getName());
        verify(ruleNameRepository, times(1)).findById(1);
    }

    @Test
    void addRuleName() {
        RuleName rule = new RuleName("Rule1", "Description1", "Json1", "Template1", "SqlStr1", "SqlPart1");

        ruleNameService.addRuleName(rule);

        verify(ruleNameRepository, times(1)).save(rule);
    }

    @Test
    void updateRuleName() {
        RuleName existingRule = new RuleName();
        existingRule.setId(1);
        existingRule.setName("Rule1");
        existingRule.setDescription("Description1");
        existingRule.setJson("Json1");
        existingRule.setTemplate("Template1");
        existingRule.setSql("SqlStr1");
        existingRule.setSqlPart("SqlPart1");
        RuleName updatedRule = new RuleName("UpdatedRule", "UpdatedDescription", "UpdatedJson", "UpdatedTemplate", "UpdatedSqlStr", "UpdatedSqlPart");
        updatedRule.setName("UpdatedRule");
        updatedRule.setDescription("UpdatedDescription");
        updatedRule.setJson("UpdatedJson");
        updatedRule.setTemplate("UpdatedTemplate");
        updatedRule.setSql("UpdatedSqlStr");
        updatedRule.setSqlPart("UpdatedSqlPart");


        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(existingRule));

        ruleNameService.updateRuleName(1, updatedRule);

        verify(ruleNameRepository, times(1)).save(existingRule);
        assertEquals("UpdatedRule", existingRule.getName());
        assertEquals("UpdatedDescription", existingRule.getDescription());
        assertEquals("UpdatedJson", existingRule.getJson());
        assertEquals("UpdatedTemplate", existingRule.getTemplate());
        assertEquals("UpdatedSqlStr", existingRule.getSql());
        assertEquals("UpdatedSqlPart", existingRule.getSqlPart());
    }

    @Test
    void deleteRuleName() {
        RuleName rule = new RuleName("Rule1", "Description1", "Json1", "Template1", "SqlStr1", "SqlPart1");
        rule.setId(1);
        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(rule));

        ruleNameService.deleteRuleName(1);

        verify(ruleNameRepository, times(1)).delete(rule);
    }

    @Test
    void updateRuleNameNotFound() {
        RuleName updatedRule = new RuleName("UpdatedRule", "UpdatedDescription", "UpdatedJson", "UpdatedTemplate", "UpdatedSqlStr", "UpdatedSqlPart");

        when(ruleNameRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ruleNameService.updateRuleName(1, updatedRule);
        });

        String expectedMessage = "Invalid rule name Id:1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void deleteRuleNameNotFound() {
        when(ruleNameRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ruleNameService.deleteRuleName(1);
        });

        String expectedMessage = "Invalid rule name Id:1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
