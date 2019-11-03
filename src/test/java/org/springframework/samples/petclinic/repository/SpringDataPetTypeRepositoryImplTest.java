package org.springframework.samples.petclinic.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.repository.springdatajpa.SpringDataPetTypeRepositoryImpl;

import javax.persistence.*;

import java.util.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SpringDataPetTypeRepositoryImplTest {

    @Mock
    EntityManager em;

    @Mock
    PetType petType;

    @InjectMocks
    SpringDataPetTypeRepositoryImpl springDataPetTypeRepository;

    @Test
    public void testDeleteWithMerge() {

        when(em.contains(petType)).thenReturn(false);
        when(petType.getId()).thenReturn(1);
        when(em.createQuery(Mockito.anyString())).thenReturn(new Query() {
            @Override
            public List getResultList() {
                return new ArrayList<Pet>();
            }

            @Override
            public Object getSingleResult() {
                return null;
            }

            @Override
            public int executeUpdate() {
                return 0;
            }

            @Override
            public Query setMaxResults(int maxResult) {
                return null;
            }

            @Override
            public int getMaxResults() {
                return 0;
            }

            @Override
            public Query setFirstResult(int startPosition) {
                return null;
            }

            @Override
            public int getFirstResult() {
                return 0;
            }

            @Override
            public Query setHint(String hintName, Object value) {
                return null;
            }

            @Override
            public Map<String, Object> getHints() {
                return null;
            }

            @Override
            public <T> Query setParameter(Parameter<T> param, T value) {
                return null;
            }

            @Override
            public Query setParameter(Parameter<Calendar> param, Calendar value, TemporalType temporalType) {
                return null;
            }

            @Override
            public Query setParameter(Parameter<Date> param, Date value, TemporalType temporalType) {
                return null;
            }

            @Override
            public Query setParameter(String name, Object value) {
                return null;
            }

            @Override
            public Query setParameter(String name, Calendar value, TemporalType temporalType) {
                return null;
            }

            @Override
            public Query setParameter(String name, Date value, TemporalType temporalType) {
                return null;
            }

            @Override
            public Query setParameter(int position, Object value) {
                return null;
            }

            @Override
            public Query setParameter(int position, Calendar value, TemporalType temporalType) {
                return null;
            }

            @Override
            public Query setParameter(int position, Date value, TemporalType temporalType) {
                return null;
            }

            @Override
            public Set<Parameter<?>> getParameters() {
                return null;
            }

            @Override
            public Parameter<?> getParameter(String name) {
                return null;
            }

            @Override
            public <T> Parameter<T> getParameter(String name, Class<T> type) {
                return null;
            }

            @Override
            public Parameter<?> getParameter(int position) {
                return null;
            }

            @Override
            public <T> Parameter<T> getParameter(int position, Class<T> type) {
                return null;
            }

            @Override
            public boolean isBound(Parameter<?> param) {
                return false;
            }

            @Override
            public <T> T getParameterValue(Parameter<T> param) {
                return null;
            }

            @Override
            public Object getParameterValue(String name) {
                return null;
            }

            @Override
            public Object getParameterValue(int position) {
                return null;
            }

            @Override
            public Query setFlushMode(FlushModeType flushMode) {
                return null;
            }

            @Override
            public FlushModeType getFlushMode() {
                return null;
            }

            @Override
            public Query setLockMode(LockModeType lockMode) {
                return null;
            }

            @Override
            public LockModeType getLockMode() {
                return null;
            }

            @Override
            public <T> T unwrap(Class<T> cls) {
                return null;
            }
        });
        springDataPetTypeRepository.delete(petType);
        verify(em, times(1)).merge(petType);
        verify(em, times(1)).createQuery("SELECT pet FROM Pet pet WHERE type_id=1");
        verify(em, times(1)).createQuery("DELETE FROM PetType pettype WHERE id=1");
    }
}
