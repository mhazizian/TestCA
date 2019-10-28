package org.springframework.samples.petclinic.service.userService;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.Rule;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import org.springframework.samples.petclinic.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Role;
import org.springframework.samples.petclinic.repository.UserRepository;
import org.springframework.samples.petclinic.service.UserServiceImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;


class StubRole extends Role {
    public User user;
    public String name;

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}


class StubUser extends User {
    private Set<Role> testRoles;

    public void setRoleForTest(Set<Role> roles) {
        this.testRoles = roles;
    }

    public Set<Role> getRoleForTest() {
        return testRoles;
    }

	@Override
    public Set<Role> getRoles() {
        return testRoles;
    }
}

class StubUserRepository implements UserRepository {
    public User user;

    @Override
    public void save(User user) throws DataAccessException {
        this.user = user;
    }

    public User getUserForTest() {
        return this.user;
    }
}

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
public class UserServiceImplStateTest {
	@Rule
    public ExpectedException expectedEx = ExpectedException.none();


    @Configuration
    static class ContextConfiguration {
    
        @Bean
        public UserRepository userRepository() {
            UserRepository userRepository = new StubUserRepository();
            return userRepository;
        }
    
        @Bean
        public UserServiceImpl userService() {
            UserServiceImpl userService = new UserServiceImpl();
            return userService;
        }
    }
    

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserServiceImpl userService;

    @Test
	public void userWithNullRoleTest() throws Exception {
		expectedEx.expect(Exception.class);
		expectedEx.expectMessage("User must have at least a role set!");
        
        StubUser user = new StubUser();
        user.setRoleForTest(null);

        userService.saveUser(user);
        
        assertEquals(user.getRoleForTest(), null);
    }
    
    @Test
	public void userWithEmptyRoleTest() throws Exception {
		expectedEx.expect(Exception.class);
		expectedEx.expectMessage("User must have at least a role set!");
        
        StubUser user = new StubUser();
        user.setRoleForTest(new HashSet<>());

        userService.saveUser(user);
        
        assertEquals(user.getRoleForTest().size(), 0);
    }

    @Test
	public void standardizeRoleWithNullName() throws Exception {
		expectedEx.expect(NullPointerException.class);
        StubRole role = new StubRole();
        StubUser user = new StubUser();

		HashSet<Role> roles = new HashSet<>();
        roles.add(role);
        
        role.name = null;
        user.setRoleForTest(roles);


        userService.saveUser(user);
	}
    
    @Test
	public void standardizeUserRoleWithWrongNameTest() throws Exception {
        StubRole role = new StubRole();
        StubUser user = new StubUser();

		HashSet<Role> roles = new HashSet<>();
        roles.add(role);
        
        role.name = "testName";
        user.setRoleForTest(roles);


        userService.saveUser(user);
        
        assertEquals(role.name, "ROLE_testName");
    }
    
    @Test
	public void standardizeUserRoleWithRightNameTest() throws Exception {		
        StubRole role = new StubRole();
        StubUser user = new StubUser();

		HashSet<Role> roles = new HashSet<>();
        roles.add(role);
        
        role.name = "ROLE_testName";
        user.setRoleForTest(roles);


        userService.saveUser(user);
        
        assertEquals(role.name, "ROLE_testName");
    }
    
    @Test
	public void getRoleWithNullUserTest() throws Exception {
        StubRole role = new StubRole();
        StubUser user = new StubUser();

		HashSet<Role> roles = new HashSet<>();
        roles.add(role);
        
        role.name = "testName";
        role.user = null;
        user.setRoleForTest(roles);


        userService.saveUser(user);
        assertEquals(role.user, user);
    }
    
    @Test
	public void getRoleWithNonNullUserTest() throws Exception {
        StubRole role = new StubRole();
        StubUser user = new StubUser();

		HashSet<Role> roles = new HashSet<>();
        roles.add(role);
        
        role.name = "testName";
        role.user = user;
        user.setRoleForTest(roles);


        userService.saveUser(user);
        assertEquals(role.user, user);
    }
    
    @Test
	public void shouldsaveAUser() throws Exception {
        StubRole role = new StubRole();
        StubUser user = new StubUser();

		HashSet<Role> roles = new HashSet<>();
        roles.add(role);
        
        role.name = "testName";
        role.user = user;
        user.setRoleForTest(roles);


        userService.saveUser(user);
        assertEquals( ((StubUserRepository)userRepository).getUserForTest(), user);
	}
}