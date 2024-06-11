package com.nnk.springboot.controllersTest;

import com.nnk.springboot.controllers.RuleNameController;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.RuleNameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.validation.BeanPropertyBindingResult;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RuleNameControllerTest {

    @InjectMocks
    private RuleNameController ruleNameController;

    @Mock
    private RuleNameService ruleNameService;

    private MockMvc mockMvc;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(ruleNameController).build();
    }

    @Test
    void home() throws Exception {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        RuleName rule1 = new RuleName("Rule1", "Description1", "Json1", "Template1", "SQLStr1", "SQLPart1");
        RuleName rule2 = new RuleName("Rule2", "Description2", "Json2", "Template2", "SQLStr2", "SQLPart2");
        when(ruleNameService.getRuleNames()).thenReturn(Arrays.asList(rule1, rule2));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        mockMvc.perform(get("/ruleName/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/list"))
                .andExpect(model().attributeExists("ruleNames"))
                .andExpect(model().attribute("ruleNames", hasSize(2)))
                .andExpect(model().attribute("ruleNames", is(Arrays.asList(rule1, rule2))));

        verify(ruleNameService, times(1)).getRuleNames();
    }

    @Test
    void addRuleForm() throws Exception {
        mockMvc.perform(get("/ruleName/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"));
    }

    @Test
    void validate() throws Exception {
        RuleName ruleName = new RuleName("Rule1", "Description1", "Json1", "Template1", "SQLStr1", "SQLPart1");
        when(bindingResult.hasErrors()).thenReturn(false);

        mockMvc.perform(post("/ruleName/validate")
                        .flashAttr("ruleName", ruleName)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/ruleName/list"));

        verify(ruleNameService, times(1)).addRuleName(any(RuleName.class));
    }

    @Test
    void showUpdateForm() throws Exception {
        RuleName ruleName = new RuleName("Rule1", "Description1", "Json1", "Template1", "SQLStr1", "SQLPart1");
        when(ruleNameService.getRuleNameById(anyInt())).thenReturn(Optional.of(ruleName));

        mockMvc.perform(get("/ruleName/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"))
                .andExpect(model().attributeExists("ruleName"))
                .andExpect(model().attribute("ruleName", is(ruleName)));

        verify(ruleNameService, times(1)).getRuleNameById(1);
    }

    @Test
    void updateRuleName() throws Exception {
        RuleName ruleName = new RuleName("Rule1", "Description1", "Json1", "Template1", "SQLStr1", "SQLPart1");
        when(ruleNameService.updateRuleName(anyInt(), any(RuleName.class))).thenReturn(ruleName);

        mockMvc.perform(post("/ruleName/update/1")
                        .flashAttr("ruleName", ruleName)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/ruleName/list"));

        verify(ruleNameService, times(1)).updateRuleName(anyInt(), any(RuleName.class));
    }


    @Test
    void deleteRuleName() throws Exception {
        doNothing().when(ruleNameService).deleteRuleName(1);

        mockMvc.perform(get("/ruleName/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/ruleName/list"));

        verify(ruleNameService, times(1)).deleteRuleName(1);
    }
}
