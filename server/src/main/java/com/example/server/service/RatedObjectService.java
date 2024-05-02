package com.example.server.service;

import com.example.server.model.RatedObject;
import com.example.server.repository.RatedObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatedObjectService {

    @Autowired
    RatedObjectRepository ratedObjectRepository;

    void rateObject(int userId, int objectId, RatedObject.Type type) {
        ratedObjectRepository.save(new RatedObject(userId, objectId, type));
    }

    void deleteRatingsOfObject(int objectId) {
        List<RatedObject> objectRatings = ratedObjectRepository.findByObjectId(objectId);
        objectRatings.forEach(x -> ratedObjectRepository.delete(x));
    }

    Optional<RatedObject.Type> getObjectRating(int userId, int objectId) {
        List<RatedObject> rating = ratedObjectRepository.findByUserIdAndObjectId(userId, objectId);
        return Optional.ofNullable(!rating.isEmpty() ? rating.get(0).getType() : null);
    }

    void deleteObjectRating(int userId, int objectId) {
        List<RatedObject> rating = ratedObjectRepository.findByUserIdAndObjectId(userId, objectId);
        ratedObjectRepository.delete(rating.get(0));
    }

    void changeRating(int userId, int objectId) {
        RatedObject rating = ratedObjectRepository.findByUserIdAndObjectId(userId, objectId).get(0);
        rating.setType(rating.getType().equals(RatedObject.Type.LIKE) ? RatedObject.Type.DISLIKE : RatedObject.Type.LIKE);
        ratedObjectRepository.save(rating);
    }

}
