package com.hostel.service.impl;

import com.hostel.entity.CommercialPlan;
import com.hostel.entity.Student;
import com.hostel.repository.CommercialPlanRepository;
import com.hostel.repository.StudentRepository;
import com.hostel.service.CommercialPlanService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link CommercialPlanService}.
 *
 * <p>Contains all business logic for creating, updating, and
 * retrieving {@link CommercialPlan} records. Repository details are
 * not exposed beyond this class.</p>
 */
@Service
public class CommercialPlanServiceImpl implements CommercialPlanService {

    private final CommercialPlanRepository commercialPlanRepository;
    private final StudentRepository studentRepository;

    /**
     * Constructs a new {@code CommercialPlanServiceImpl}.
     *
     * @param commercialPlanRepository the commercial plan repository
     * @param studentRepository        the student repository, used to
     *                                 resolve the owning student when
     *                                 creating a commercial plan
     */
    public CommercialPlanServiceImpl(CommercialPlanRepository commercialPlanRepository,
                                      StudentRepository studentRepository) {
        this.commercialPlanRepository = commercialPlanRepository;
        this.studentRepository = studentRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public CommercialPlan createCommercialPlan(Long studentId, CommercialPlan plan) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        plan.setStudent(student);
        return commercialPlanRepository.save(plan);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public CommercialPlan updateCommercialPlan(Long planId, CommercialPlan plan) {
        CommercialPlan existingPlan = findCommercialPlanById(planId);
        existingPlan.setCommercialAmount(plan.getCommercialAmount());
        existingPlan.setBookingAmount(plan.getBookingAmount());
        existingPlan.setMoveinAmount(plan.getMoveinAmount());
        existingPlan.setInstallmentCount(plan.getInstallmentCount());
        return commercialPlanRepository.save(existingPlan);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public CommercialPlan findCommercialPlanById(Long planId) {
        return commercialPlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Commercial plan not found with id: " + planId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public CommercialPlan findCommercialPlanByStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        return commercialPlanRepository.findByStudent(student)
                .orElseThrow(() -> new RuntimeException("Commercial plan not found for student id: " + studentId));
    }
}
