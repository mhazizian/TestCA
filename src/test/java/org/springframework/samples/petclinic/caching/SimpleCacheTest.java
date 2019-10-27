package org.springframework.samples.petclinic.caching;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SimpleCacheTest {

    @Mock
    EntityManager entityManager;

	@InjectMocks
    SimpleCache simpleCache;
    
    @Test
    public void retrieveObjectNotInCacheTest() { 
        Class clazz = String.class;
        Integer id = new Integer(10);
        when(entityManager.find(Mockito.any(), Mockito.any())).thenReturn("test");

        simpleCache.retrieve(clazz, id);

        verify(entityManager, times(1)).find(Mockito.any(), Mockito.any());
    }

    @Test
    public void retrieveObjectInCacheTest() { 
        Class clazz = String.class;
        Integer id = new Integer(10);
        when(entityManager.find(Mockito.any(), Mockito.any())).thenReturn("test");

        simpleCache.retrieve(clazz, id);
        simpleCache.retrieve(clazz, id);

        verify(entityManager, times(1)).find(Mockito.any(), Mockito.any());
    }

    @Test
    public void evictNotExistedTest() { 
        Class clazz = String.class;
        Integer id = new Integer(10);
        simpleCache.evict(clazz, id);
    }

    @Test
    public void evictExistedTest() { 
        Class clazz = String.class;
        Integer id = new Integer(10);
        when(entityManager.find(Mockito.any(), Mockito.any())).thenReturn("test");
        simpleCache.retrieve(clazz, id);
        simpleCache.evict(clazz, id);
        simpleCache.retrieve(clazz, id);

        verify(entityManager, times(2)).find(Mockito.any(), Mockito.any());
    }
}