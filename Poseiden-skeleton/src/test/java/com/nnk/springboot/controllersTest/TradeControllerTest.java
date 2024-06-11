package com.nnk.springboot.controllersTest;

import com.nnk.springboot.controllers.TradeController;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.TradeService;
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

class TradeControllerTest {

    @InjectMocks
    private TradeController tradeController;

    @Mock
    private TradeService tradeService;

    @Mock
    private BindingResult bindingResult;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(tradeController).build();
    }

    @Test
    void home() throws Exception {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        Trade trade1 = new Trade("Account1", "Type1", 10.0);
        Trade trade2 = new Trade("Account2", "Type2", 20.0);
        when(tradeService.getTrades()).thenReturn(Arrays.asList(trade1, trade2));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        mockMvc.perform(get("/trade/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/list"))
                .andExpect(model().attributeExists("trades"))
                .andExpect(model().attribute("trades", hasSize(2)))
                .andExpect(model().attribute("trades", is(Arrays.asList(trade1, trade2))));

        verify(tradeService, times(1)).getTrades();
    }

    @Test
    void addUser() throws Exception {
        mockMvc.perform(get("/trade/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"));
    }

    @Test
    void validate() throws Exception {
        Trade trade = new Trade("Account1", "Type1", 10.0);
        when(tradeService.addTrade(any(Trade.class))).thenReturn(trade);

        mockMvc.perform(post("/trade/validate")
                        .flashAttr("trade", trade)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/trade/list"));

        verify(tradeService, times(1)).addTrade(any(Trade.class));
    }


    @Test
    void showUpdateForm() throws Exception {
        Trade trade = new Trade("Account1", "Type1", 10.0);
        when(tradeService.getTradeById(anyInt())).thenReturn(Optional.of(trade));

        mockMvc.perform(get("/trade/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(model().attributeExists("trade"))
                .andExpect(model().attribute("trade", is(trade)));

        verify(tradeService, times(1)).getTradeById(1);
    }

    @Test
    void updateTrade() throws Exception {
        Trade trade = new Trade("Account1", "Type1", 10.0);
        when(bindingResult.hasErrors()).thenReturn(false);

        mockMvc.perform(post("/trade/update/1")
                        .flashAttr("trade", trade)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/trade/list"));

        verify(tradeService, times(1)).updateTrade(anyInt(), any(Trade.class));
    }


    @Test
    void deleteTrade() throws Exception {
        doNothing().when(tradeService).deleteTrade(1);

        mockMvc.perform(get("/trade/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/trade/list"));

        verify(tradeService, times(1)).deleteTrade(1);
    }
}
