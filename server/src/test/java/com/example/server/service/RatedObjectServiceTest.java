package com.example.server.service;


import com.example.server.model.RatedObject;
import com.example.server.repository.RatedObjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
public class RatedObjectServiceTest {

    @InjectMocks
    private RatedObjectService ratedObjectService;

    @Mock
    private RatedObjectRepository ratedObjectRepository;

    @Test
    public void testRateObject() {
        ratedObjectService.rateObject(1, 1, RatedObject.Type.LIKE);
        verify(ratedObjectRepository, times(1)).save(any(RatedObject.class));
    }

    @Test
    public void testDeleteRatingsOfObject() {
        when(ratedObjectRepository.findByObjectId(1)).thenReturn(Arrays.asList(new RatedObject(), new RatedObject()));
        ratedObjectService.deleteRatingsOfObject(1);
        verify(ratedObjectRepository, times(2)).delete(any(RatedObject.class));
    }

    @Test
    public void testGetObjectRating() {
        when(ratedObjectRepository.findByUserIdAndObjectId(1, 1)).thenReturn(Collections.singletonList(new RatedObject()));
        Optional<RatedObject.Type> rating = ratedObjectService.getObjectRating(1, 1);
        verify(ratedObjectRepository, times(1)).findByUserIdAndObjectId(1, 1);
    }

    @Test
    public void testDeleteObjectRating() {
        when(ratedObjectRepository.findByUserIdAndObjectId(1, 1)).thenReturn(Collections.singletonList(new RatedObject()));
        ratedObjectService.deleteObjectRating(1, 1);
        verify(ratedObjectRepository, times(1)).delete(any(RatedObject.class));
    }

    @Test
    public void testChangeRating() {
        RatedObject ratedObject = new RatedObject();
        ratedObject.setType(RatedObject.Type.LIKE);
        when(ratedObjectRepository.findByUserIdAndObjectId(1, 1)).thenReturn(Collections.singletonList(ratedObject));
        ratedObjectService.changeRating(1, 1);
        verify(ratedObjectRepository, times(1)).save(any(RatedObject.class));
    }

}
