package org.springframework.samples.petclinic.service.userService;

import java.util.HashSet;

import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

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

	private void mockSavableUser() {
		Role role = mock(Role.class);
		
		HashSet<Role> roles = new HashSet<>();
		roles.add(role);
		
		when(role.getName()).thenReturn("ROLE_role");
		when(user.getRoles()).thenReturn(roles);
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
	public void standardizeUserRoleWithRightNameTest() throws Exception {		
		Role role2 = mock(Role.class);
		
		HashSet<Role> roles = new HashSet<>();
		roles.add(role2);
		
		when(role2.getName()).thenReturn("ROLE_role2");
		when(user.getRoles()).thenReturn(roles);

		userServiceImpl.saveUser(user);

		verify(role2, times(0)).setName(any(String.class));
	}


	@Test
	public void standardizeUserRoleWithWrongNameTest() throws Exception {
		Role role1 = mock(Role.class);
		
		HashSet<Role> roles = new HashSet<>();
		roles.add(role1);
		
		when(role1.getName()).thenReturn("roleName");
		when(user.getRoles()).thenReturn(roles);

		userServiceImpl.saveUser(user);

		ArgumentCaptor<String> roleNameCaptor = ArgumentCaptor.forClass(String.class);
		verify(role1, times(1)).setName(roleNameCaptor.capture());
		assertEquals("ROLE_roleName", roleNameCaptor.getValue());
	}
	
	@Test
	public void getRoleNullUserTest() throws Exception {
		Role role = mock(Role.class);
		ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

		HashSet<Role> roles = new HashSet<>();
		roles.add(role);
		
		when(role.getName()).thenReturn("ROLE_role");
		when(user.getRoles()).thenReturn(roles);
		when(role.getUser()).thenReturn(null);

		userServiceImpl.saveUser(user);

		verify(role, times(1)).getUser();
		verify(role, times(1)).setUser(userCaptor.capture());
		assertEquals(userCaptor.getValue(), user);
		
	}


	public void shouldsaveAUser() throws Exception {
		mockSavableUser();
		ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

		userServiceImpl.saveUser(user);

		verify(userRepository, times(1)).save(userCaptor.capture());
		assertEquals(userCaptor.getValue(), user);
	}
}