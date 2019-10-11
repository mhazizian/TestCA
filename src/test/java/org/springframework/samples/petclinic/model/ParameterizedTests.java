package org.springframework.samples.petclinic.model;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import static org.junit.Assert.*;
import java.util.*;

@RunWith(Parameterized.class)
public class ParameterizedTests {
    private Owner owner;
    private ArrayList<String> resultPetNames;

    public ParameterizedTests(ArrayList<String> petNames, ArrayList<String> resultPetNames){
        this.owner = new Owner();
        for (String name : petNames) {
            Pet newPet = new Pet();
            newPet.setName(name);
            this.owner.addPet(newPet);
        }
        this.resultPetNames = resultPetNames;
    }

    @Parameters
    public static Collection<Object[]> parameters(){
        return Arrays.asList(
            new Object[][]{
                {
                    new ArrayList<String>(Arrays.asList("PetA", "PetB", "PetC")),
                    new ArrayList<String>(Arrays.asList("PetA", "PetB", "PetC"))
                },
                {
                    new ArrayList<String>(Arrays.asList("PetB", "PetA", "PetC")),
                    new ArrayList<String>(Arrays.asList("PetA", "PetB", "PetC"))
                },
                {
                    new ArrayList<String>(Arrays.asList("PetC", "PetB", "PetA")),
                    new ArrayList<String>(Arrays.asList("PetA", "PetB", "PetC"))
                }
            }
        );
    }

    @Test
    public void getPetsTest() {
        List<Pet> pets = this.owner.getPets();
        assertEquals(this.resultPetNames.size(), pets.size());
        for (int i = 0; i < this.resultPetNames.size(); i++)
            assertEquals(this.resultPetNames.get(i), pets.get(i).getName());
    }
}