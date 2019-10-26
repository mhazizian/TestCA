package org.springframework.samples.petclinic.service;

import org.apache.catalina.User;
import org.aspectj.lang.annotation.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.samples.petclinic.repository.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplBehavioralTest {

	@Mock
	UserRepository userRepository;

    @Mock
    User user;

	@InjectMocks
	UserServiceImpl userServiceImpl;

	@Before
	public void setup() {
       Mockito.when(userRepository.save(user)).do
	}
	
	@Test
	public void shouldsaveAUser() {
        
	}
}