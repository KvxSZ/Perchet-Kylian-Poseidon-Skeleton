package com.nnk.springboot.controllersTest;

import com.nnk.springboot.controllers.BidListController;
import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.BidListService;
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

class BidListControllerTest {

    @InjectMocks
    private BidListController bidListController;

    @Mock
    private BidListService bidListService;

    @Mock
    private BindingResult bindingResult;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bidListController).build();
    }

    @Test
    void home() throws Exception {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        BidList bid1 = new BidList("Account1", "Type1", 10.0);
        BidList bid2 = new BidList("Account2", "Type2", 20.0);
        when(bidListService.getAllBids()).thenReturn(Arrays.asList(bid1, bid2));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/list"))
                .andExpect(model().attributeExists("bidLists"))
                .andExpect(model().attribute("bidLists", hasSize(2)))
                .andExpect(model().attribute("bidLists", is(Arrays.asList(bid1, bid2))))
                .andExpect(model().attribute("username",authentication.getName()));


        verify(bidListService, times(1)).getAllBids();
    }

    @Test
    void addBidForm() throws Exception {
        mockMvc.perform(get("/bidList/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"));
    }

    @Test
    void validate() throws Exception {
        BidList bid = new BidList("Account1", "Type1", 10.0);
        when(bindingResult.hasErrors()).thenReturn(false);

        mockMvc.perform(post("/bidList/validate")
                        .flashAttr("bidList", bid)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bidList/list"));

        verify(bidListService, times(1)).addBid(any(BidList.class));
    }


    @Test
    void showUpdateForm() throws Exception {
        BidList bid = new BidList("Account1", "Type1", 10.0);
        when(bidListService.getBidById(anyInt())).thenReturn(Optional.of(bid));

        mockMvc.perform(get("/bidList/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"))
                .andExpect(model().attributeExists("bidList"))
                .andExpect(model().attribute("bidList", is(bid)));

        verify(bidListService, times(1)).getBidById(1);
    }

    @Test
    void updateBid() throws Exception {
        BidList bid = new BidList("Account1", "Type1", 10.0);
        when(bindingResult.hasErrors()).thenReturn(false);

        mockMvc.perform(post("/bidList/update/1")
                        .flashAttr("bidList", bid)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bidList/list"));

        verify(bidListService, times(1)).updateBid(anyInt(), any(BidList.class));
    }


    @Test
    void deleteBid() throws Exception {
        doNothing().when(bidListService).deleteBid(1);

        mockMvc.perform(get("/bidList/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bidList/list"));

        verify(bidListService, times(1)).deleteBid(1);
    }
}
