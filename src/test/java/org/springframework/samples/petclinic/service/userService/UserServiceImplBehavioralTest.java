package org.springframework.samples.petclinic.service.userService;

import java.util.HashSet;

import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Role;
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
	public void userWithEmptyRole() throws Exception {
		expectedEx.expect(Exception.class);
		expectedEx.expectMessage("User must have at least a role set!");

		when(user.getRoles()).thenReturn(new HashSet<>());
		
		userServiceImpl.saveUser(user);
		
		verify(user, times(1)).getRoles();
	}

	@Test
	public void standardizeUserRoleTest() throws Exception {
		Role role1 = mock(Role.class);
		Role role2 = mock(Role.class);

		when(role1.getName()).thenReturn("roleName");
		when(role2.getName()).thenReturn("ROLE_role2");



	// 	when(role1.setName(any(String.class))).thenAnswer(i -> {
    //     	String roleName = i.getArgument(0);
    //     	assertEquals("ROLE_roleName", roleName);
    //     	return null;
    // });

		HashSet<Role> roles = new HashSet<>();
		roles.add(role1);
		roles.add(role2);

		when(user.getRoles()).thenReturn(roles);

		// when(role2.getUser()).thenReturn(null);
		userServiceImpl.saveUser(user);


		verify(role1, times(1)).setName(any(String.class));
		verify(role2, times(0)).setName(any(String.class));

		// ArgumentCaptor<String> roleNameCaptor = ArgumentCaptor.forClass(String.class);
		// verify(role1, times(1)).doSomething(captor.capture());

		// // Assert the argument
		// SomeData actual = captor.getValue();
		// assertEquals("Some inner data", actual.innerData);
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