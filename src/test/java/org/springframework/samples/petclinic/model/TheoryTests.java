package org.springframework.samples.petclinic.model;

import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import java.util.*;

@RunWith(Theories.class)
public class TheoryTests {
    public ArrayList<String> oldPets = new ArrayList<>(
        Arrays.asList("dog", "rabbit")
    );
    public ArrayList<String> newPets = new ArrayList<>(
        Arrays.asList("kitty", "hamster")
    );

    @DataPoint
    public static Owner getOwner(){
        Owner owner = new Owner();

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
        return owner;
    }
    
    @DataPoints
    public static String[] names = {"kitty", "dog", "rabbit", "Hamster", "duck"};

    @DataPoints
    public static boolean[] ignoreFlags = {true, false};

    @Theory
    public void getPetWithNameAndIgnoreTest(Owner owner, String name, boolean ignoreNew) {

        Pet foundPet = owner.getPet(name, ignoreNew);
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
