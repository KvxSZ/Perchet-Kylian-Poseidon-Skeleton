package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CurveService {

    @Autowired
    private CurvePointRepository curvePointRepository;

    public List<CurvePoint> getAllCurvePoints() {
        return curvePointRepository.findAll();
    }

    public Optional<CurvePoint> getCurvePointById(Integer id) {
        return curvePointRepository.findById(id);
    }

    public void addCurvePoint(CurvePoint curvePoint) {
        curvePointRepository.save(curvePoint);
    }

    @Transactional
    public void updateCurvePoint(Integer id, CurvePoint curvePointDetails) {
        CurvePoint curvePoint = curvePointRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid CurvePoint Id: " + id));

        curvePoint.setCurveId(curvePointDetails.getCurveId());
        curvePoint.setAsOfDate(curvePointDetails.getAsOfDate());
        curvePoint.setTerm(curvePointDetails.getTerm());
        curvePoint.setValue(curvePointDetails.getValue());
        curvePoint.setCreationDate(curvePointDetails.getCreationDate());

        curvePointRepository.save(curvePoint);
    }

    @Transactional
    public void deleteCurvePoint(Integer id) {
        CurvePoint curvePoint = curvePointRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid CurvePoint Id: " + id));
        curvePointRepository.delete(curvePoint);
    }

}
