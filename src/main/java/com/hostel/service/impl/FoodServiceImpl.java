package com.hostel.service.impl;

import com.hostel.entity.Food;
import com.hostel.entity.Student;
import com.hostel.repository.FoodRepository;
import com.hostel.repository.StudentRepository;
import com.hostel.service.FoodService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link FoodService}.
 *
 * <p>Contains all business logic for creating, updating, and
 * retrieving {@link Food} records. Repository details are not exposed
 * beyond this class.</p>
 */
@Service
public class FoodServiceImpl implements FoodService {

    private final FoodRepository foodRepository;
    private final StudentRepository studentRepository;

    /**
     * Constructs a new {@code FoodServiceImpl}.
     *
     * @param foodRepository    the food repository
     * @param studentRepository the student repository, used to resolve
     *                          the owning student when creating a food
     *                          record
     */
    public FoodServiceImpl(FoodRepository foodRepository, StudentRepository studentRepository) {
        this.foodRepository = foodRepository;
        this.studentRepository = studentRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Food createFood(Long studentId, Food food) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        food.setStudent(student);
        return foodRepository.save(food);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Food updateFood(Long foodId, Food food) {
        Food existingFood = findFoodById(foodId);
        existingFood.setLunchIncluded(food.getLunchIncluded());
        existingFood.setLunchAmount(food.getLunchAmount());
        existingFood.setAlacarteAmount(food.getAlacarteAmount());
        return foodRepository.save(existingFood);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Food findFoodById(Long foodId) {
        return foodRepository.findById(foodId)
                .orElseThrow(() -> new RuntimeException("Food record not found with id: " + foodId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Food findFoodByStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        return foodRepository.findByStudent(student)
                .orElseThrow(() -> new RuntimeException("Food record not found for student id: " + studentId));
    }
}
