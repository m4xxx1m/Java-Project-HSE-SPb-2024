package com.example.server.service;

import com.example.server.model.SavedObject;
import com.example.server.repository.SavedObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class SavedObjectService {
    @Autowired
    SavedObjectRepository SavedObjectRepository;

    public void saveObject(int userId, int objectId, SavedObject.Type type) {
        List<SavedObject> userSavedObjects = SavedObjectRepository.findByUserId(userId);
        Optional<SavedObject> existingSaving = userSavedObjects.stream()
                .filter(object -> object.getObjectId() == objectId)
                .findFirst();
        if (existingSaving.isPresent()) {
            SavedObjectRepository.deleteById(existingSaving.orElse(null).getId());
        } else {
            SavedObjectRepository.save(new SavedObject(userId, objectId, type));
        }
    }

    public List<Integer> getSavedObjectByUserId(int userId, SavedObject.Type type) {
        List<SavedObject> userSavedObjects = SavedObjectRepository.findByUserId(userId);
        return userSavedObjects.stream()
                .filter(object -> object.getType() == type)
                .map(SavedObject::getObjectId)
                .collect(Collectors.toList());
    }

    public void deleteSavedObjectForAllUsers(int objectId) {
        List<SavedObject> objectSavings = SavedObjectRepository.findByObjectId(objectId);
        for (SavedObject objectSaving : objectSavings) {
            SavedObjectRepository.deleteById(objectSaving.getId());
        }
    }
}
