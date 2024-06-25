package com.example.server.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class SimilarityThresholdService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void setSimilarityThreshold(double threshold) {
        entityManager.createNativeQuery("SET pg_trgm.similarity_threshold = :threshold")
                .setParameter("threshold", threshold)
                .executeUpdate();
    }
}