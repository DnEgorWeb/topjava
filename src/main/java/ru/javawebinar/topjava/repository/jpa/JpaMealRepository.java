package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public class JpaMealRepository implements MealRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            User user = em.getReference(User.class, userId);
            meal.setUser(user);
            em.persist(meal);
            em.flush();
            return meal;
        } else {
            return em.merge(meal);
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        Meal meal = em.find(Meal.class, id);
        if (meal == null || meal.getUser().getId() != userId) {
            return false;
        }
        em.remove(meal);
        return true;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = em.find(Meal.class, id);
        if (meal == null || meal.getUser().getId() != userId) {
            return null;
        }
        return meal;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createQuery("SELECT m FROM Meal m WHERE m.user.id = :userId", Meal.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return em.createQuery("SELECT m FROM Meal m WHERE m.user.id = :userId " +
                        "AND m.dateTime >= :start AND m.dateTime < :end ORDER BY m.dateTime DESC", Meal.class)
                .setParameter("userId", userId)
                .setParameter("start", startDateTime)
                .setParameter("end", endDateTime)
                .getResultList();
    }
}