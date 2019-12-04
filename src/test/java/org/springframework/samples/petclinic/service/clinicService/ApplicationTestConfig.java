package org.springframework.samples.petclinic.service.clinicService;

import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@TestConfiguration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationTestConfig {

	public ApplicationTestConfig(){
		MockitoAnnotations.initMocks(this);
	}

}
