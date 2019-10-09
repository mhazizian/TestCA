package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;
import java.util.Set;
import java.util.HashSet;

import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;


public class OwnerTests {

    private Object getField(Object obj, String fieldName) {
        try {
            Field f = obj.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);

            Object result = f.get(obj);
            
            f.setAccessible(false);

            return result;
        } catch (NoSuchFieldException e) {
            assert false : fieldName + " field is missing";
            
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            assert false : "can't access field" + fieldName;
        }

        return null;
    }

    private void setField(Object obj, String fieldName, Object value) {
        try {
            Field f = obj.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);

            f.set(obj, value);
            
            f.setAccessible(false);
        } catch (NoSuchFieldException e) {
            assert false : fieldName + " field is missing";
            
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            assert false : "can't access field" + fieldName;
        }
    }

    @Before
    public void setUp(){
    }

    @Test
    public void getAddressTest() {
        Owner owner = new Owner();
        String testAddress = "this is a test address.";

        setField(owner, "address", testAddress);
        assertEquals(owner.getAddress(), testAddress);
    }

    @Test
    public void setAddressTest() {
        Owner owner = new Owner();
        String testAddress = "this is a test address.";

        owner.setAddress(testAddress);
        assertEquals( (String) getField(owner, "address"), testAddress);
    }

    @Test
    public void setCityTest() {
        Owner owner = new Owner();
        String testCity = "this is a test city: Tehran";

        owner.setCity(testCity);
        assertEquals( (String) getField(owner, "city"), testCity);
    }

    @Test
    public void getCityTest() {
        Owner owner = new Owner();
        String testCity = "this is a test city: Tehran";

        setField(owner, "city", testCity);
        assertEquals(owner.getCity(), testCity);
    }

    @Test
    public void getTelephoneTest() {
        Owner owner = new Owner();
        String testTelephone = "this is a test telephone.";

        setField(owner, "telephone", testTelephone);
        assertEquals(owner.getTelephone(), testTelephone);
    }

    @Test
    public void setTelephoneTest() {
        Owner owner = new Owner();
        String testTelephone = "this is a test telephone.";

        owner.setTelephone(testTelephone);
        assertEquals( (String) getField(owner, "telephone"), testTelephone);
    }

    @Test
    public void getPetsInternalNullTest() throws IllegalAccessException, InvocationTargetException {
        Owner owner = new Owner();
        setField(owner, "pets", null);

        try {
            Method method = Owner.class.getDeclaredMethod("getPetsInternal", null);
            
            method.setAccessible(true);
            Set<Pet> returnValue = (Set<Pet>) method.invoke(owner, null);
            method.setAccessible(false);
            
            assertTrue(returnValue instanceof HashSet<?>);
            assertEquals(returnValue.size(), 0);
        } catch(NoSuchMethodException e) {
            assert false : "getPetsInternal method is missing";
        }
    }

    @Test
    public void getPetsInternalWithValueTest() throws IllegalAccessException, InvocationTargetException {
        Owner owner = new Owner();
        
        HashSet<Pet> pets = new HashSet<>();
        Pet pet = new Pet();
        Pet pet2 = new Pet();
        pets.add(pet);
        setField(owner, "pets", pets);

        try {
            Method method = Owner.class.getDeclaredMethod("getPetsInternal", null);
            
            method.setAccessible(true);
            Set<Pet> returnValue = (Set<Pet>) method.invoke(owner, null);
            method.setAccessible(false);
            
            assertTrue(returnValue instanceof HashSet<?>);
            assertEquals(returnValue.size(), 1);
            assertTrue(returnValue.contains(pet));
            assertFalse(returnValue.contains(pet2));
        } catch(NoSuchMethodException e) {
            assert false : "getPetsInternal method is missing";
        }
    }

    @Test
    public void setPetsInternalTest() throws IllegalAccessException, InvocationTargetException {
        Owner owner = new Owner();
        HashSet<Pet> pets = new HashSet<>();
        Pet pet = new Pet();
        Pet pet2 = new Pet();
        pets.add(pet);
        pets.add(pet2);

        try {
            Method method = Owner.class.getDeclaredMethod("setPetsInternal", Set.class);
            
            method.setAccessible(true);
            Set<Pet> returnValue = (Set<Pet>) method.invoke(owner, pets);
            method.setAccessible(false);
            
            HashSet<Pet> settedPets = (HashSet) getField(owner, "pets");
            
            assertTrue(settedPets instanceof HashSet<?>);
            assertEquals(settedPets.size(), 2);
            assertTrue(settedPets.contains(pet));
            assertTrue(settedPets.contains(pet2));
        } catch(NoSuchMethodException e) {
            assert false : "setPetsInternal method is missing";
        }
    }

    @Test
    public void addPetTest() {
        Owner owner = new Owner();

        Pet pet = new Pet();
        owner.addPet(pet);

        HashSet<Pet> settedPets = (HashSet) getField(owner, "pets");
        assertTrue(settedPets.contains(pet));
    }

    @Test
    public void addPetOwnerTest() {
        Owner owner = new Owner();
        Owner temp = new Owner();

        Pet pet = new Pet();
        pet.setOwner(temp);

        owner.addPet(pet);

        HashSet<Pet> settedPets = (HashSet) getField(owner, "pets");
        assertEquals(pet.getOwner(), owner);
    }

    @Test
    public void getPetsTest() {
        Owner owner = new Owner();

        Pet petA = new Pet();
        petA.setName("pet A");
        
        Pet petB = new Pet();
        petB.setName("pet B");

        Pet petC = new Pet();
        petC.setName("pet C");

        Pet petD = new Pet();
        petD.setName("pet D");


        owner.addPet(petB);
        owner.addPet(petD);
        owner.addPet(petA);
        owner.addPet(petC);
        
        List<Pet> pets = owner.getPets();

        assertEquals(pets.size(), 4);
        assertEquals(pets.get(0), petA);
        assertEquals(pets.get(1), petB);
        assertEquals(pets.get(2), petC);
        assertEquals(pets.get(3), petD);
    }

    @Test
    public void getPetByNameTest() {
        Owner owner = new Owner();
        String petName = "petName";

        Pet pet = new Pet();
        pet.setName(petName);

        owner.addPet(pet);

        Pet result = owner.getPet(petName);
        assertEquals(result, pet);
    }

    @Test
    public void getPetByNameOnInvalidNameTest() {
        Owner owner = new Owner();

        Pet result = owner.getPet("invalidPetName");
        assertNull(result);
    }
}
