package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;
import java.util.Set;

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
    @Ignore
    public void shouldNotValidateWhenFirstNameEmpty() {
        
        Person person = new Person();
        person.setFirstName("");
        person.setLastName("smith");

        // Validator validator = createValidator();
        // Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

        // assertThat(constraintViolations.size()).isEqualTo(1);
        // ConstraintViolation<Person> violation = constraintViolations.iterator().next();
        // assertThat(violation.getPropertyPath().toString()).isEqualTo("firstName");
        // assertThat(violation.getMessage()).isEqualTo("must not be empty");
    }

}
