package org.springframework.samples.petclinic.model;

import org.junit.*;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import java.util.*;

@RunWith(Theories.class)
public class TheoryTests {
    Owner owner;
    @Before
    public void setUp(){
        this.owner = new Owner();

        Pet petA = new Pet();
        petA.setName("Dog");
        petA.setId(10000);
        owner.addPet(petA);

        Pet petB = new Pet();
        petB.setName("rabbit");
        petB.setId(10001);
        owner.addPet(petB);

        Pet petC = new Pet();
        petC.setName("kitty");
        owner.addPet(petC);

        Pet petD = new Pet();
        petD.setName("Hamster");
        owner.addPet(petD);
    }
    
    @DataPoints
    public static String[] names = {"kitty", "dog", "rabbit", "Hamster", "duck"};

    @DataPoints
    public static boolean[] ignoreFlags = {true, false};

    @Theory
    public void getPetWithNameAndIgnoreTest(String name, boolean ignoreNew) {
        Pet foundPet = this.owner.getPet(name, ignoreNew);
    }
}
