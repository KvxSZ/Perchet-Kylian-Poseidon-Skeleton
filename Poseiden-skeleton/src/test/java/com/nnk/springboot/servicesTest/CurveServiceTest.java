package com.nnk.springboot.servicesTest;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.services.CurveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurveServiceTest {

    @InjectMocks
    private CurveService curveService;

    @Mock
    private CurvePointRepository curvePointRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCurvePoints() {
        CurvePoint curve1 = new CurvePoint(1, 10.0, 20.0);
        CurvePoint curve2 = new CurvePoint(2, 15.0, 25.0);
        List<CurvePoint> curvePoints = Arrays.asList(curve1, curve2);

        when(curvePointRepository.findAll()).thenReturn(curvePoints);

        List<CurvePoint> result = curveService.getAllCurvePoints();

        assertEquals(2, result.size());
        verify(curvePointRepository, times(1)).findAll();
    }

    @Test
    void getCurvePointById() {
        CurvePoint curve = new CurvePoint();
        curve.setTerm(10.0);
        when(curvePointRepository.findById(1)).thenReturn(Optional.of(curve));

        Optional<CurvePoint> result = curveService.getCurvePointById(1);

        assertTrue(result.isPresent());
        assertEquals(10.0, result.get().getTerm());
        verify(curvePointRepository, times(1)).findById(1);
    }

    @Test
    void addCurvePoint() {
        CurvePoint curve = new CurvePoint(1, 10.0, 20.0);

        curveService.addCurvePoint(curve);

        verify(curvePointRepository, times(1)).save(curve);
    }

    @Test
    void updateCurvePoint() {
        CurvePoint existingCurve = new CurvePoint();
        existingCurve.setId(1);
        existingCurve.setTerm(10.0);
        existingCurve.setValue(20.0);
        CurvePoint updatedCurve = new CurvePoint(1, 15.0, 25.0);
        updatedCurve.setTerm(15.0);
        updatedCurve.setValue(25.0);

        when(curvePointRepository.findById(1)).thenReturn(Optional.of(existingCurve));

        curveService.updateCurvePoint(1, updatedCurve);

        verify(curvePointRepository, times(1)).save(existingCurve);
        assertEquals(15.0, existingCurve.getTerm());
        assertEquals(25.0, existingCurve.getValue());
    }

    @Test
    void deleteCurvePoint() {
        CurvePoint curve = new CurvePoint(1, 10.0, 20.0);
        curve.setId(1);
        when(curvePointRepository.findById(1)).thenReturn(Optional.of(curve));

        curveService.deleteCurvePoint(1);

        verify(curvePointRepository, times(1)).delete(curve);
    }

    @Test
    void updateCurvePointNotFound() {
        CurvePoint updatedCurve = new CurvePoint(1, 15.0, 25.0);

        when(curvePointRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            curveService.updateCurvePoint(1, updatedCurve);
        });

        String expectedMessage = "Invalid CurvePoint Id: 1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void deleteCurvePointNotFound() {
        when(curvePointRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            curveService.deleteCurvePoint(1);
        });

        String expectedMessage = "Invalid CurvePoint Id: 1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
