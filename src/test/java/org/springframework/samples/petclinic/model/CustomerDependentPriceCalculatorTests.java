package org.springframework.samples.petclinic.model;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.samples.petclinic.model.priceCalculators.CustomerDependentPriceCalculator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomerDependentPriceCalculatorTests {

    @Mock
    PetType rareType;

    @Mock
    PetType commonType;

    @InjectMocks
    CustomerDependentPriceCalculator customerDependentPriceCalculator;


    private List<Pet> youngRarePetsSample;
    private List<Pet> youngCommonPetsSample;
    private List<Pet> oldCommonPetsSample;
    private List<Pet> oldRarePetsSample;

    private Date lessThanInfantYears;
    private Date infantYears;
    private Date moreThanInfantYears;

    private int INFANT_YEARS = 2;
    private double RARE_INFANCY_COEF = 1.4;
    private double COMMON_INFANCY_COEF = 1.2;
    private double BASE_RARE_COEF = 1.2;
    private int DISCOUNT_MIN_SCORE = 10;

    private List<Pet> getManyPetsFromSample(List<Pet> sample, int num) {

        List<Pet> res = new ArrayList<>(sample);

        for(int i = sample.size(); i < num; i++)
            res.add(sample.get(0));

        return res;
    }

    @Before
    public void initPets() {
        when(rareType.getRare()).thenReturn(true);
        when(commonType.getRare()).thenReturn(false);

        youngRarePetsSample = new ArrayList<>();
        youngCommonPetsSample = new ArrayList<>();
        oldCommonPetsSample = new ArrayList<>();
        oldRarePetsSample = new ArrayList<>();

        lessThanInfantYears = DateTime.now().minusYears(INFANT_YEARS - 1).toDate();
        infantYears = DateTime.now().minusYears(INFANT_YEARS).toDate();
        moreThanInfantYears = DateTime.now().minusYears(INFANT_YEARS + 1).toDate();

        Pet youngRarePet1 = mock(Pet.class);
        when(youngRarePet1.getBirthDate()).thenReturn(lessThanInfantYears);
        when(youngRarePet1.getType()).thenReturn(rareType);
        youngRarePetsSample.add(youngRarePet1);

        Pet youngRarePet2 = mock(Pet.class);
        when(youngRarePet2.getBirthDate()).thenReturn(infantYears);
        when(youngRarePet2.getType()).thenReturn(rareType);
        youngRarePetsSample.add(youngRarePet2);

        Pet youngCommonPet1 = mock(Pet.class);
        when(youngCommonPet1.getBirthDate()).thenReturn(lessThanInfantYears);
        when(youngCommonPet1.getType()).thenReturn(commonType);
        youngCommonPetsSample.add(youngCommonPet1);

        Pet youngCommonPet2 = mock(Pet.class);
        when(youngCommonPet2.getBirthDate()).thenReturn(infantYears);
        when(youngCommonPet2.getType()).thenReturn(commonType);
        youngCommonPetsSample.add(youngCommonPet2);

        Pet oldCommonPet1 = mock(Pet.class);
        when(oldCommonPet1.getBirthDate()).thenReturn(moreThanInfantYears);
        when(oldCommonPet1.getType()).thenReturn(commonType);
        oldCommonPetsSample.add(oldCommonPet1);

        Pet oldRarePet1 = mock(Pet.class);
        when(oldRarePet1.getBirthDate()).thenReturn(moreThanInfantYears);
        when(oldRarePet1.getType()).thenReturn(rareType);
        oldRarePetsSample.add(oldRarePet1);
    }

    @Test
    public void Section1RareYoungPetTest() {
        UserType userType = UserType.SILVER;
        double baseCharge = 1000 * 1000 * 1000;
        double basePricePerPet = 1000 * 1000;
        double gottenTotalPrice = customerDependentPriceCalculator
                .calcPrice(youngRarePetsSample, baseCharge, basePricePerPet, userType);

        assertEquals(basePricePerPet * BASE_RARE_COEF * RARE_INFANCY_COEF * youngRarePetsSample.size(), gottenTotalPrice, 0);
    }

    @Test
    public void Section1CommonYoungPetTest() {
        UserType userType = UserType.SILVER;
        double baseCharge = 1000 * 1000 * 1000;
        double basePricePerPet = 1000 * 1000;
        double gottenTotalPrice = customerDependentPriceCalculator
            .calcPrice(youngCommonPetsSample, baseCharge, basePricePerPet, userType);

        assertEquals(basePricePerPet * COMMON_INFANCY_COEF * youngCommonPetsSample.size(), gottenTotalPrice, 0);
    }

    @Test
    public void Section1RareOldPetTest() {
        UserType userType = UserType.SILVER;
        double baseCharge = 1000 * 1000 * 1000;
        double basePricePerPet = 1000 * 1000;
        double gottenTotalPrice = customerDependentPriceCalculator
            .calcPrice(oldRarePetsSample, baseCharge, basePricePerPet, userType);

        assertEquals(basePricePerPet * BASE_RARE_COEF * oldRarePetsSample.size(), gottenTotalPrice, 0);
    }

    @Test
    public void Section1CommonOldPetTest() {
        UserType userType = UserType.SILVER;
        double baseCharge = 1000 * 1000 * 1000;
        double basePricePerPet = 1000 * 1000;
        double gottenTotalPrice = customerDependentPriceCalculator
            .calcPrice(oldCommonPetsSample, baseCharge, basePricePerPet, userType);

        assertEquals(basePricePerPet * oldCommonPetsSample.size(), gottenTotalPrice, 0);
    }

    @Test
    public void Section2RareYoungPetWithGoldUserTypeTest() {
        UserType userType = UserType.GOLD;
        double baseCharge = 1000 * 1000 * 1000;
        double basePricePerPet = 1000 * 1000;
        double gottenTotalPrice = customerDependentPriceCalculator
            .calcPrice(youngRarePetsSample, baseCharge, basePricePerPet, userType);

        double expectedPrice = (basePricePerPet * BASE_RARE_COEF * RARE_INFANCY_COEF * youngRarePetsSample.size()) * userType.discountRate + baseCharge;

        assertEquals(expectedPrice, gottenTotalPrice, 0);
    }

    @Test
    public void Section2RareYoungPetsWithNewUserTypeAndDiscountTest() {
        UserType userType = UserType.NEW;
        int petsCount = 5;
        double baseCharge = 1000 * 1000 * 1000;
        double basePricePerPet = 1000 * 1000;
        double gottenTotalPrice = customerDependentPriceCalculator
            .calcPrice(getManyPetsFromSample(youngRarePetsSample, petsCount), baseCharge, basePricePerPet, userType);

        double expectedPrice = (basePricePerPet * BASE_RARE_COEF * RARE_INFANCY_COEF * petsCount) * userType.discountRate + baseCharge;

        assertEquals(expectedPrice, gottenTotalPrice, 0);
    }

    @Test
    public void Section2RareOldPetsWithNewUserTypeAndDiscountTest() {
        UserType userType = UserType.NEW;
        int petsCount = 10;
        double baseCharge = 1000 * 1000 * 1000;
        double basePricePerPet = 1000 * 1000;
        double gottenTotalPrice = customerDependentPriceCalculator
            .calcPrice(getManyPetsFromSample(oldRarePetsSample, petsCount), baseCharge, basePricePerPet, userType);

        double expectedPrice = (basePricePerPet * BASE_RARE_COEF * petsCount) * userType.discountRate + baseCharge;

        assertEquals(expectedPrice, gottenTotalPrice, 0);
    }

    @Test
    public void Section2CommonOldPetsWithNewUserTypeAndDiscountTest() {
        UserType userType = UserType.NEW;
        int petsCount = 10;
        double baseCharge = 1000 * 1000 * 1000;
        double basePricePerPet = 1000 * 1000;
        double gottenTotalPrice = customerDependentPriceCalculator
            .calcPrice(getManyPetsFromSample(oldCommonPetsSample, petsCount), baseCharge, basePricePerPet, userType);

        double expectedPrice = (basePricePerPet * petsCount) * userType.discountRate + baseCharge;

        assertEquals(expectedPrice, gottenTotalPrice, 0);
    }

    @Test
    public void Section2CommonYoungPetsWithNewUserTypeAndDiscountTest() {
        UserType userType = UserType.NEW;
        int petsCount = 10;
        double baseCharge = 1000 * 1000 * 1000;
        double basePricePerPet = 1000 * 1000;
        double gottenTotalPrice = customerDependentPriceCalculator
            .calcPrice(getManyPetsFromSample(youngCommonPetsSample, petsCount), baseCharge, basePricePerPet, userType);

        double expectedPrice = (basePricePerPet * COMMON_INFANCY_COEF * petsCount) * userType.discountRate + baseCharge;

        assertEquals(expectedPrice, gottenTotalPrice, 0);
    }

    @Test
    public void Section2RareYoungPetsWithNotNewUserTypeAndDiscountTest() {
        UserType userType = UserType.SILVER;
        int petsCount = 5;
        double baseCharge = 1000 * 1000 * 1000;
        double basePricePerPet = 1000 * 1000;
        double gottenTotalPrice = customerDependentPriceCalculator
            .calcPrice(getManyPetsFromSample(youngRarePetsSample, petsCount), baseCharge, basePricePerPet, userType);

        double expectedPrice = ((basePricePerPet * BASE_RARE_COEF * RARE_INFANCY_COEF * petsCount) + baseCharge)
                                * userType.discountRate;

        assertEquals(expectedPrice, gottenTotalPrice, 0);
    }

    @Test
    public void Section2CommonYoungPetsWithNewUserTypeAndNoDiscount9Test() {
        UserType userType = UserType.NEW;
        int petsCount = 9;
        double baseCharge = 1000 * 1000 * 1000;
        double basePricePerPet = 1000 * 1000;
        double gottenTotalPrice = customerDependentPriceCalculator
            .calcPrice(getManyPetsFromSample(youngCommonPetsSample, petsCount), baseCharge, basePricePerPet, userType);

        double expectedPrice = basePricePerPet * COMMON_INFANCY_COEF * petsCount;

        assertEquals(expectedPrice, gottenTotalPrice, 0);
    }

    @Test
    public void Section2CommonYoungPetsWithNewUserTypeAndDiscount11Test() {
        UserType userType = UserType.NEW;
        int petsCount = 9;
        double baseCharge = 1000 * 1000 * 1000;
        double basePricePerPet = 1000 * 1000;
        double gottenTotalPrice = customerDependentPriceCalculator
            .calcPrice(getManyPetsFromSample(youngCommonPetsSample, petsCount), baseCharge, basePricePerPet, userType);

        double expectedPrice = basePricePerPet * COMMON_INFANCY_COEF * petsCount;

        assertEquals(expectedPrice, gottenTotalPrice, 0);
    }
}
