package com.example.server.repository;

import com.example.server.model.SavedObject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavedObjectRepository extends JpaRepository<SavedObject, Integer> {
    List<SavedObject> findByObjectId(int objectId);
    List<SavedObject> findByUserId(int userId);
}
