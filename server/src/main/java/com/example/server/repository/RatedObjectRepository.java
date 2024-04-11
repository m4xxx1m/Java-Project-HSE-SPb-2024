package com.example.server.repository;

import com.example.server.model.RatedObject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatedObjectRepository extends JpaRepository<RatedObject, Integer> {

    List<RatedObject> findByObjectId(int objectId);

    List<RatedObject> findByUserIdAndObjectId(int userId, int objectId);
    List<RatedObject> findByUserId(int userId);

}
