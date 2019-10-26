package org.springframework.samples.petclinic.service.userService;

import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.Mockito;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.repository.UserRepository;
import org.springframework.samples.petclinic.service.UserServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplBehavioralTest {

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Mock
	UserRepository userRepository;

    @Mock
    User user;

	@InjectMocks
	UserServiceImpl userServiceImpl;

	@Before
	public void setup() {
    //    Mockito.when(userRepository.save(user)).do
	}

	@Test
	public void userWithNullRole() throws Exception {
		expectedEx.expect(Exception.class);
		expectedEx.expectMessage("User must have at least a role set!");

		when(user.getRoles()).thenReturn(null);
		
		userServiceImpl.saveUser(user);
		
		verify(user, times(1)).getRoles();
	}
	
	@Test
	public void shouldsaveAUser() {
        
// 	when(userRepository.save(any(User.class))).thenAnswer(i -> {
//     User user = i.getArgument(0);
//     userMap.add(user.getId(), user);
//     return null;
// });
	}
}