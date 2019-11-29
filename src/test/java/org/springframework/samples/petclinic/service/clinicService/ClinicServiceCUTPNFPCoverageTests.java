package org.springframework.samples.petclinic.service.clinicService;

import com.github.mryf323.tractatus.*;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.samples.petclinic.model.*;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.samples.petclinic.service.ClinicServiceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class ClinicServiceCUTPNFPCoverageTests {

    @Mock
    PetRepository petRepository;

    @Mock
    VetRepository vetRepository;

    @Mock
    VisitRepository visitRepository;

    @Mock
    Owner owner;

    @Mock
    Pet pet;

    @Mock
    Vet vet;

    @Mock
    Visit lastVisit;

    @InjectMocks
    ClinicServiceImpl clinicService;

    @ClauseDefinition(clause = 'a', def = "last.isPresent()")
    @UniqueTruePoint(
        predicate = "a",
        cnf = "a",
        implicant = "a",
        valuations = {
            @Valuation(clause = 'a', valuation = true)
        }
    )
    @Test
    public void testLastVisitIsPresent() {
        Collection<Pet> pets = new ArrayList<>();
        pets.add(pet);
        when(petRepository.findByOwner(Mockito.any(Owner.class))).thenReturn(pets);
        Optional<Visit> lastVisitOpt = Optional.of(lastVisit);
        when(pet.getLastVisit()).thenReturn(lastVisitOpt);

        clinicService.visitOwnerPets(owner);

        verify(petRepository, times(1)).findByOwner(Mockito.any());
        verify(pet, times(1)).getLastVisit();
        verify(pet, times(1)).getBirthDate();
    }

    @ClauseDefinition(clause = 'a', def = "last.isPresent()")
    @NearFalsePoint(
        predicate = "a",
        cnf = "a",
        implicant = "a",
        clause = 'a',
        valuations = {
            @Valuation(clause = 'a', valuation = false)
        }
    )
    @Test(expected = ClinicServiceImpl.VisitException.class)
    public void testLastVisitIsNotPresent() {
        Collection<Pet> pets = new ArrayList<>();
        pets.add(pet);
        when(petRepository.findByOwner(Mockito.any(Owner.class))).thenReturn(pets);
        Optional<Visit> lastVisitOpt = Optional.empty();
        when(pet.getLastVisit()).thenReturn(lastVisitOpt);

        clinicService.visitOwnerPets(owner);

        verify(petRepository, times(1)).findByOwner(Mockito.any());
        verify(pet, times(1)).getLastVisit();
        verify(pet, times(0)).getBirthDate();
    }

    @ClauseDefinition(clause = 'a', def = "notVisited.size() > 0")
    @NearFalsePoint(
        predicate = "a",
        cnf = "a",
        implicant = "a",
        clause = 'a',
        valuations = {
            @Valuation(clause = 'a', valuation = false)
        }
    )
    @Test
    public void testNotHavingNotVisitedPet() {
        Collection<Pet> pets = new ArrayList<>();
        pets.add(pet);
        when(petRepository.findByOwner(Mockito.any(Owner.class))).thenReturn(pets);
        Optional<Visit> lastVisitOpt = Optional.of(lastVisit);
        when(pet.getLastVisit()).thenReturn(lastVisitOpt);

        clinicService.visitOwnerPets(owner);

        verify(petRepository, times(1)).findByOwner(Mockito.any());
        verify(pet, times(1)).getLastVisit();
        verify(pet, times(1)).getBirthDate();
    }

    @ClauseDefinition(clause = 'a', def = "notVisited.size() > 0")
    @UniqueTruePoint(
        predicate = "a",
        cnf = "a",
        implicant = "a",
        valuations = {
            @Valuation(clause = 'a', valuation = true)
        }
    )
    @Test(expected = ClinicServiceImpl.VisitException.class)
    public void testHavingNotVisitedPet() {
        Collection<Pet> pets = new ArrayList<>();
        pets.add(pet);
        when(petRepository.findByOwner(Mockito.any(Owner.class))).thenReturn(pets);
        Optional<Visit> lastVisitOpt = Optional.empty();
        when(pet.getLastVisit()).thenReturn(lastVisitOpt);

        clinicService.visitOwnerPets(owner);

        verify(petRepository, times(1)).findByOwner(Mockito.any());
        verify(pet, times(1)).getLastVisit();
        verify(pet, times(0)).getBirthDate();
    }

    @ClauseDefinition(clause = 'a', def = "vet.isPresent()")
    @UniqueTruePoint(
        predicate = "a",
        cnf = "a",
        implicant = "a",
        valuations = {
            @Valuation(clause = 'a', valuation = true)
        }
    )
    @Test
    public void testVetIsPresent() {
        Collection<Pet> pets = new ArrayList<>();
        pets.add(pet);
        Collection<Vet> vets = new ArrayList<>();
        vets.add(vet);
        when(petRepository.findByOwner(Mockito.any(Owner.class))).thenReturn(pets);
        Optional<Visit> lastVisitOpt = Optional.empty();
        when(pet.getLastVisit()).thenReturn(lastVisitOpt);
        when(pet.getType()).thenReturn(new PetType());
        when(vetRepository.findAll()).thenReturn(vets);
        when(vet.canCurePetTye(Mockito.any(PetType.class))).thenReturn(true);

        clinicService.visitOwnerPets(owner);

        verify(petRepository, times(1)).findByOwner(Mockito.any());
        verify(pet, times(1)).getLastVisit();
        verify(pet, times(0)).getBirthDate();
        verify(visitRepository, times(1)).save(Mockito.any(Visit.class));
    }

    @ClauseDefinition(clause = 'a', def = "vet.isPresent()")
    @NearFalsePoint(
        predicate = "a",
        cnf = "a",
        implicant = "a",
        clause = 'a',
        valuations = {
            @Valuation(clause = 'a', valuation = false)
        }
    )
    @Test(expected = ClinicServiceImpl.VisitException.class)
    public void testVetIsNotPresent() {
        Collection<Pet> pets = new ArrayList<>();
        pets.add(pet);
        Collection<Vet> vets = new ArrayList<>();
        vets.add(vet);
        when(petRepository.findByOwner(Mockito.any(Owner.class))).thenReturn(pets);
        Optional<Visit> lastVisitOpt = Optional.empty();
        when(pet.getLastVisit()).thenReturn(lastVisitOpt);
        when(pet.getType()).thenReturn(new PetType());
        when(vetRepository.findAll()).thenReturn(vets);
        when(vet.canCurePetTye(Mockito.any(PetType.class))).thenReturn(false);

        clinicService.visitOwnerPets(owner);

        verify(petRepository, times(1)).findByOwner(Mockito.any());
        verify(pet, times(1)).getLastVisit();
        verify(pet, times(0)).getBirthDate();
        verify(visitRepository, times(0)).save(Mockito.any(Visit.class));
    }

//    @ClauseDefinition(clause = 'a', def = "age > 3")
//    @ClauseDefinition(clause = 'b', def = "daysFromLastVisit > 364")
//    @ClauseDefinition(clause = 'c', def = "age <= 3")
//    @ClauseDefinition(clause = 'd', def = "daysFromLastVisit > 182")
//    @ClauseCoverage(
//        predicate = "ab + cd",
//        valuations = {
//            @Valuation(clause = 'a', valuation = true),
//            @Valuation(clause = 'b', valuation = true),
//            @Valuation(clause = 'c', valuation = false),
//            @Valuation(clause = 'd', valuation = true),
//        }
//    )
//    @Test(expected = ClinicServiceImpl.VisitException.class)
//    public void testPetNeedVisitWith4YearsAgeAnd365DaysFromLastVisit() {
//        Date yearsAgo4 = DateTime.now().minusYears(4).toDate();
//        Date daysAgo365 = DateTime.now().minusDays(365).toDate();
//
//        Collection<Pet> pets = new ArrayList<>();
//        pets.add(pet);
//        when(petRepository.findByOwner(Mockito.any(Owner.class))).thenReturn(pets);
//        Optional<Visit> lastVisitOpt = Optional.of(lastVisit);
//        when(pet.getLastVisit()).thenReturn(lastVisitOpt);
//
//        when(pet.getBirthDate()).thenReturn(yearsAgo4);
//        when(lastVisit.getDate()).thenReturn(daysAgo365);
//
//        clinicService.visitOwnerPets(owner);
//
//        verify(petRepository, times(1)).findByOwner(Mockito.any());
//        verify(pet, times(1)).getLastVisit();
//        verify(pet, times(1)).getBirthDate();
//    }
}
