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
    private Owner owner;
    public ArrayList<String> oldPets;
    public ArrayList<String> newPets;

    @Before
    public void setUp(){
        this.owner = new Owner();

        Pet petA = new Pet();
        petA.setName("Dog");
        petA.setId(10000);
        owner.addPet(petA);
        this.oldPets.add("Dog");

        Pet petB = new Pet();
        petB.setName("rabbit");
        petB.setId(10001);
        owner.addPet(petB);
        this.oldPets.add("rabbit");

        Pet petC = new Pet();
        petC.setName("kitty");
        owner.addPet(petC);
        this.newPets.add("kitty");

        Pet petD = new Pet();
        petD.setName("Hamster");
        owner.addPet(petD);
        this.newPets.add("Hamster");
    }
    
    @DataPoints
    public static String[] names = {"kitty", "dog", "rabbit", "Hamster", "duck"};

    @DataPoints
    public static boolean[] ignoreFlags = {true, false};

    @Theory
    public void getPetWithNameAndIgnoreTest(String name, boolean ignoreNew) {
        Pet foundPet = this.owner.getPet(name, ignoreNew);
        name = name.toLowerCase();
        if (this.oldPets.contains(name)){
            assertTrue(name + "has been found.", foundPet!=null);
        } else if (this.newPets.contains(name)) {
            if (ignoreNew)
                assertTrue("New pet should not be found.", foundPet==null);
            else
                assertTrue(name + "has been found with false flag.", foundPet!=null);
        } else assertTrue("This pet should not found.", foundPet==null);
    }
}
