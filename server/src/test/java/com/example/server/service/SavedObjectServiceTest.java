package com.example.server.service;

import com.example.server.model.SavedObject;
import com.example.server.repository.SavedObjectRepository;
import com.example.server.service.SavedObjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
public class SavedObjectServiceTest {

    @Mock
    private SavedObjectRepository savedObjectRepository;

    @InjectMocks
    private SavedObjectService savedObjectService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveObject() {
        savedObjectService.saveObject(1, 1, SavedObject.Type.POST);
        verify(savedObjectRepository, times(1)).save(any(SavedObject.class));
    }

    @Test
    public void testGetSavedObjectByUserId() {
        when(savedObjectRepository.findByUserId(1)).thenReturn(Arrays.asList(new SavedObject(), new SavedObject()));
        savedObjectService.getSavedObjectByUserId(1, SavedObject.Type.POST);
        verify(savedObjectRepository, times(1)).findByUserId(1);
    }

    @Test
    public void testDeleteSavedObjectForAllUsers() {
        when(savedObjectRepository.findByObjectId(1)).thenReturn(Arrays.asList(new SavedObject(), new SavedObject()));
        savedObjectService.deleteSavedObjectForAllUsers(1);
        verify(savedObjectRepository, times(2)).deleteById(anyInt());
    }
}