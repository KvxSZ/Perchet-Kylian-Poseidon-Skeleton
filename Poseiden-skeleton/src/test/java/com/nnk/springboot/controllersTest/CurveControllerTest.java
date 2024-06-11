package com.nnk.springboot.controllersTest;

import com.nnk.springboot.controllers.CurveController;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.CurveService;
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

class CurveControllerTest {

    @InjectMocks
    private CurveController curveController;

    @Mock
    private CurveService curveService;

    @Mock
    private BindingResult bindingResult;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(curveController).build();
    }

    @Test
    void home() throws Exception {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        CurvePoint curve1 = new CurvePoint(1, 10, 10.0, 30.0);
        CurvePoint curve2 = new CurvePoint(2, 20, 20.0, 40.0);
        when(curveService.getAllCurvePoints()).thenReturn(Arrays.asList(curve1, curve2));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        mockMvc.perform(get("/curvePoint/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/list"))
                .andExpect(model().attributeExists("curvePoints"))
                .andExpect(model().attribute("curvePoints", hasSize(2)))
                .andExpect(model().attribute("curvePoints", is(Arrays.asList(curve1, curve2))))
                .andExpect(model().attribute("username",authentication.getName()));;

        verify(curveService, times(1)).getAllCurvePoints();
    }

    @Test
    void addBidForm() throws Exception {
        mockMvc.perform(get("/curvePoint/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"));
    }

    @Test
    void validate() throws Exception {
        CurvePoint curvePoint = new CurvePoint(10, 10.0, 30.0);
        when(bindingResult.hasErrors()).thenReturn(false);

        mockMvc.perform(post("/curvePoint/validate")
                        .flashAttr("curvePoint", curvePoint)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/curvePoint/list"));

        verify(curveService, times(1)).addCurvePoint(any(CurvePoint.class));
    }


    @Test
    void showUpdateForm() throws Exception {
        CurvePoint curvePoint = new CurvePoint(10, 10.0, 30.0);
        when(curveService.getCurvePointById(anyInt())).thenReturn(Optional.of(curvePoint));

        mockMvc.perform(get("/curvePoint/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"))
                .andExpect(model().attributeExists("curvePoint"))
                .andExpect(model().attribute("curvePoint", is(curvePoint)));

        verify(curveService, times(1)).getCurvePointById(1);
    }

    @Test
    void updateCurvePoint() throws Exception {
        CurvePoint curvePoint = new CurvePoint(10, 10.0, 30.0);
        when(bindingResult.hasErrors()).thenReturn(false);

        mockMvc.perform(post("/curvePoint/update/1")
                        .flashAttr("curvePoint", curvePoint)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/curvePoint/list"));

        verify(curveService, times(1)).updateCurvePoint(anyInt(), any(CurvePoint.class));
    }


    @Test
    void deleteCurvePoint() throws Exception {
        doNothing().when(curveService).deleteCurvePoint(1);

        mockMvc.perform(get("/curvePoint/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/curvePoint/list"));

        verify(curveService, times(1)).deleteCurvePoint(1);
    }
}


