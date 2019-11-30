package org.springframework.samples.petclinic.service.clinicService;

import com.github.mryf323.tractatus.CACC;
import com.github.mryf323.tractatus.ClauseCoverage;
import com.github.mryf323.tractatus.ClauseDefinition;
import com.github.mryf323.tractatus.Valuation;
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

@RunWith(MockitoJUnitRunner.class)
public class ClinicServiceCorrelatedActiveClauseCoverage {

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
    @CACC(
        majorClause = 'a',
        predicate = "a",
        predicateValue = true,
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
    @CACC(
        majorClause = 'a',
        predicate = "a",
        predicateValue = false,
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

    @ClauseDefinition(clause = 'a', def = "vet.isPresent()")
    @CACC(
        majorClause = 'a',
        predicate = "a",
        predicateValue = true,
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
    @CACC(
        majorClause = 'a',
        predicate = "a",
        predicateValue = false,
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

    @ClauseDefinition(clause = 'a', def = "age > 3")
    @ClauseDefinition(clause = 'b', def = "daysFromLastVisit > 364")
    @ClauseDefinition(clause = 'c', def = "daysFromLastVisit > 182")
    @CACC(
        majorClause = 'a',
        predicateValue = true,
        predicate = "ab + ~ad",
        valuations = {
            @Valuation(clause = 'a', valuation = false),
            @Valuation(clause = 'b', valuation = false),
            @Valuation(clause = 'd', valuation = true),
        }
    )
    @Test
    public void testPetNeedVisitWithAgeAsActiveClauseAndFalsePredicate() {
        Date yearsAgo2 = DateTime.now().minusYears(2).toDate();
        Date daysAgo200 = DateTime.now().minusDays(200).toDate();


        Collection<Pet> pets = new ArrayList<>();
        pets.add(pet);
        Collection<Vet> vets = new ArrayList<>();
        vets.add(vet);
        when(petRepository.findByOwner(Mockito.any(Owner.class))).thenReturn(pets);
        Optional<Visit> lastVisitOpt = Optional.of(lastVisit);
        when(pet.getLastVisit()).thenReturn(lastVisitOpt);
        when(pet.getType()).thenReturn(new PetType());

        when(pet.getBirthDate()).thenReturn(yearsAgo2);
        when(lastVisit.getDate()).thenReturn(daysAgo200);

        when(vetRepository.findAll()).thenReturn(vets);
        when(vet.canCurePetTye(Mockito.any(PetType.class))).thenReturn(true);

        clinicService.visitOwnerPets(owner);

        verify(petRepository, times(1)).findByOwner(Mockito.any());
        verify(pet, times(1)).getLastVisit();
        verify(pet, times(1)).getBirthDate();
        verify(lastVisit, times(1)).getDate();
        verify(vet, times(1)).canCurePetTye(Mockito.any(PetType.class));
        verify(visitRepository, times(1)).save(Mockito.any(Visit.class));
    }

    @ClauseDefinition(clause = 'a', def = "age > 3")
    @ClauseDefinition(clause = 'b', def = "daysFromLastVisit > 364")
    @ClauseDefinition(clause = 'c', def = "daysFromLastVisit > 182")
    @CACC(
        majorClause = 'a',
        predicateValue = false,
        predicate = "ab + ~ad",
        valuations = {
            @Valuation(clause = 'a', valuation = true),
            @Valuation(clause = 'b', valuation = false),
            @Valuation(clause = 'd', valuation = true),
        }
    )
    @Test
    public void testPetNeedVisitWithAgeAsActiveClauseAndTruePredicate() {
        Date yearsAgo4 = DateTime.now().minusYears(4).toDate();
        Date daysAgo200 = DateTime.now().minusDays(200).toDate();


        Collection<Pet> pets = new ArrayList<>();
        pets.add(pet);
        Collection<Vet> vets = new ArrayList<>();
        vets.add(vet);
        when(petRepository.findByOwner(Mockito.any(Owner.class))).thenReturn(pets);
        Optional<Visit> lastVisitOpt = Optional.of(lastVisit);
        when(pet.getLastVisit()).thenReturn(lastVisitOpt);
        when(pet.getType()).thenReturn(new PetType());

        when(pet.getBirthDate()).thenReturn(yearsAgo4);
        when(lastVisit.getDate()).thenReturn(daysAgo200);

        when(vetRepository.findAll()).thenReturn(vets);
        when(vet.canCurePetTye(Mockito.any(PetType.class))).thenReturn(true);

        clinicService.visitOwnerPets(owner);

        verify(petRepository, times(1)).findByOwner(Mockito.any());
        verify(pet, times(1)).getLastVisit();
        verify(pet, times(1)).getBirthDate();
        verify(lastVisit, times(1)).getDate();
        verify(vet, times(0)).canCurePetTye(Mockito.any(PetType.class));
        verify(visitRepository, times(0)).save(Mockito.any(Visit.class));
    }

    @ClauseDefinition(clause = 'a', def = "age > 3")
    @ClauseDefinition(clause = 'b', def = "daysFromLastVisit > 364")
    @ClauseDefinition(clause = 'c', def = "daysFromLastVisit > 182")
    @CACC(
        majorClause = 'b',
        predicateValue = false,
        predicate = "ab + ~ad",
        valuations = {
            @Valuation(clause = 'a', valuation = true),
            @Valuation(clause = 'b', valuation = false),
            @Valuation(clause = 'd', valuation = true),
        }
    )
    @Test
    public void testPetNeedVisitWithBAsActiveClauseAndFalsePredicate() {
        Date yearsAgo4 = DateTime.now().minusYears(4).toDate();
        Date daysAgo200 = DateTime.now().minusDays(200).toDate();


        Collection<Pet> pets = new ArrayList<>();
        pets.add(pet);
        Collection<Vet> vets = new ArrayList<>();
        vets.add(vet);
        when(petRepository.findByOwner(Mockito.any(Owner.class))).thenReturn(pets);
        Optional<Visit> lastVisitOpt = Optional.of(lastVisit);
        when(pet.getLastVisit()).thenReturn(lastVisitOpt);
        when(pet.getType()).thenReturn(new PetType());

        when(pet.getBirthDate()).thenReturn(yearsAgo4);
        when(lastVisit.getDate()).thenReturn(daysAgo200);

        when(vetRepository.findAll()).thenReturn(vets);
        when(vet.canCurePetTye(Mockito.any(PetType.class))).thenReturn(true);

        clinicService.visitOwnerPets(owner);

        verify(petRepository, times(1)).findByOwner(Mockito.any());
        verify(pet, times(1)).getLastVisit();
        verify(pet, times(1)).getBirthDate();
        verify(lastVisit, times(1)).getDate();
        verify(vet, times(0)).canCurePetTye(Mockito.any(PetType.class));
        verify(visitRepository, times(0)).save(Mockito.any(Visit.class));
    }

    @ClauseDefinition(clause = 'a', def = "age > 3")
    @ClauseDefinition(clause = 'b', def = "daysFromLastVisit > 364")
    @ClauseDefinition(clause = 'c', def = "daysFromLastVisit > 182")
    @CACC(
        majorClause = 'b',
        predicateValue = true,
        predicate = "ab + ~ad",
        valuations = {
            @Valuation(clause = 'a', valuation = true),
            @Valuation(clause = 'b', valuation = true),
            @Valuation(clause = 'd', valuation = true),
        }
    )
    @Test
    public void testPetNeedVisitWithBAsActiveClauseAndTruePredicate() {
        Date yearsAgo4 = DateTime.now().minusYears(4).toDate();
        Date daysAgo400 = DateTime.now().minusDays(400).toDate();


        Collection<Pet> pets = new ArrayList<>();
        pets.add(pet);
        Collection<Vet> vets = new ArrayList<>();
        vets.add(vet);
        when(petRepository.findByOwner(Mockito.any(Owner.class))).thenReturn(pets);
        Optional<Visit> lastVisitOpt = Optional.of(lastVisit);
        when(pet.getLastVisit()).thenReturn(lastVisitOpt);
        when(pet.getType()).thenReturn(new PetType());

        when(pet.getBirthDate()).thenReturn(yearsAgo4);
        when(lastVisit.getDate()).thenReturn(daysAgo400);

        when(vetRepository.findAll()).thenReturn(vets);
        when(vet.canCurePetTye(Mockito.any(PetType.class))).thenReturn(true);

        clinicService.visitOwnerPets(owner);

        verify(petRepository, times(1)).findByOwner(Mockito.any());
        verify(pet, times(1)).getLastVisit();
        verify(pet, times(1)).getBirthDate();
        verify(lastVisit, times(1)).getDate();
        verify(vet, times(1)).canCurePetTye(Mockito.any(PetType.class));
        verify(visitRepository, times(1)).save(Mockito.any(Visit.class));
    }

    @ClauseDefinition(clause = 'a', def = "age > 3")
    @ClauseDefinition(clause = 'b', def = "daysFromLastVisit > 364")
    @ClauseDefinition(clause = 'c', def = "daysFromLastVisit > 182")
    @CACC(
        majorClause = 'd',
        predicateValue = true,
        predicate = "ab + ~ad",
        valuations = {
            @Valuation(clause = 'a', valuation = false),
            @Valuation(clause = 'b', valuation = true),
            @Valuation(clause = 'd', valuation = true),
        }
    )
    @Test
    public void testPetNeedVisitWithDAsActiveClauseAndTruePredicate() {
        Date yearsAgo2 = DateTime.now().minusYears(2).toDate();
        Date daysAgo400 = DateTime.now().minusDays(400).toDate();


        Collection<Pet> pets = new ArrayList<>();
        pets.add(pet);
        Collection<Vet> vets = new ArrayList<>();
        vets.add(vet);
        when(petRepository.findByOwner(Mockito.any(Owner.class))).thenReturn(pets);
        Optional<Visit> lastVisitOpt = Optional.of(lastVisit);
        when(pet.getLastVisit()).thenReturn(lastVisitOpt);
        when(pet.getType()).thenReturn(new PetType());

        when(pet.getBirthDate()).thenReturn(yearsAgo2);
        when(lastVisit.getDate()).thenReturn(daysAgo400);

        when(vetRepository.findAll()).thenReturn(vets);
        when(vet.canCurePetTye(Mockito.any(PetType.class))).thenReturn(true);

        clinicService.visitOwnerPets(owner);

        verify(petRepository, times(1)).findByOwner(Mockito.any());
        verify(pet, times(1)).getLastVisit();
        verify(pet, times(1)).getBirthDate();
        verify(lastVisit, times(1)).getDate();
        verify(vet, times(1)).canCurePetTye(Mockito.any(PetType.class));
        verify(visitRepository, times(1)).save(Mockito.any(Visit.class));
    }

    @ClauseDefinition(clause = 'a', def = "age > 3")
    @ClauseDefinition(clause = 'b', def = "daysFromLastVisit > 364")
    @ClauseDefinition(clause = 'c', def = "daysFromLastVisit > 182")
    @CACC(
        majorClause = 'd',
        predicateValue = false,
        predicate = "ab + ~ad",
        valuations = {
            @Valuation(clause = 'a', valuation = false),
            @Valuation(clause = 'b', valuation = false),
            @Valuation(clause = 'd', valuation = false),
        }
    )
    @Test
    public void testPetNeedVisitWithDAsActiveClauseAndFalsePredicate() {
        Date yearsAgo2 = DateTime.now().minusYears(2).toDate();
        Date daysAgo100 = DateTime.now().minusDays(100).toDate();


        Collection<Pet> pets = new ArrayList<>();
        pets.add(pet);
        Collection<Vet> vets = new ArrayList<>();
        vets.add(vet);
        when(petRepository.findByOwner(Mockito.any(Owner.class))).thenReturn(pets);
        Optional<Visit> lastVisitOpt = Optional.of(lastVisit);
        when(pet.getLastVisit()).thenReturn(lastVisitOpt);
        when(pet.getType()).thenReturn(new PetType());

        when(pet.getBirthDate()).thenReturn(yearsAgo2);
        when(lastVisit.getDate()).thenReturn(daysAgo100);

        when(vetRepository.findAll()).thenReturn(vets);
        when(vet.canCurePetTye(Mockito.any(PetType.class))).thenReturn(true);

        clinicService.visitOwnerPets(owner);

        verify(petRepository, times(1)).findByOwner(Mockito.any());
        verify(pet, times(1)).getLastVisit();
        verify(pet, times(1)).getBirthDate();
        verify(lastVisit, times(1)).getDate();
        verify(vet, times(0)).canCurePetTye(Mockito.any(PetType.class));
        verify(visitRepository, times(0)).save(Mockito.any(Visit.class));
    }
}
