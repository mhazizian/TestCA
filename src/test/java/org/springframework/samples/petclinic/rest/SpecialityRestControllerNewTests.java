package org.springframework.samples.petclinic.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.service.clinicService.ApplicationTestConfig;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(SpecialtyRestController.class)
@ContextConfiguration(classes= ApplicationTestConfig.class)
public class SpecialityRestControllerNewTests {
    @Autowired
    private SpecialtyRestController specialtyRestController;

    @MockBean
    private ClinicService clinicService;

    private MockMvc mockMvc;

    @Before
    public void initSpecialtys(){
        this.mockMvc = MockMvcBuilders.standaloneSetup(specialtyRestController)
            .setControllerAdvice(new ExceptionControllerAdvice())
            .build();
    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    public void testUpdateSpecialtyWithInvalidSpecialityObj() throws Exception {
        Specialty specialty = new Specialty();
        specialty.setId(1);
        specialty.setName("testSpeciality");
        given(this.clinicService.findSpecialtyById(1)).willReturn(specialty);

        this.mockMvc.perform(put("/api/specialties/1")
            .content("{}").accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().contentType("application/json;charset=ISO-8859-1"))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    public void testUpdateSpecialtyWithNoContent() throws Exception {
        given(this.clinicService.findSpecialtyById(1)).willReturn(null);

        this.mockMvc.perform(put("/api/specialties/1")
            .accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    public void testUpdateSpecialtyWithInvalidSpecialityId() throws Exception {
        given(this.clinicService.findSpecialtyById(2)).willReturn(null);

        Specialty newSpecialty = new Specialty();
        newSpecialty.setId(2);
        newSpecialty.setName("newName");
        ObjectMapper mapper = new ObjectMapper();
        String newSpecialtyAsJSON = mapper.writeValueAsString(newSpecialty);

        this.mockMvc.perform(put("/api/specialties/20")
            .content(newSpecialtyAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNotFound());
    }

//    @Test
//    public void testUpdateSpecialtyWithWrongPermission() throws Exception {
//        Specialty specialty = new Specialty();
//        specialty.setId(3);
//        specialty.setName("testSpeciality");
//
//        given(this.clinicService.findSpecialtyById(3)).willReturn(specialty);
//        Specialty newSpecialty = new Specialty();
//        newSpecialty.setId(3);
//        newSpecialty.setName("NewName");
//        ObjectMapper mapper = new ObjectMapper();
//        String newSpecialtyAsJSON = mapper.writeValueAsString(newSpecialty);
//
//        this.mockMvc.perform(put("/api/specialties/3")
//            .content(newSpecialtyAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(status().isBadRequest());
//    }


    @Test
    @WithMockUser(roles="VET_ADMIN")
    public void testUpdateSpecialtyWithChangeName() throws Exception {
        Specialty specialty = new Specialty();
        specialty.setId(3);
        specialty.setName("testSpeciality");

        given(this.clinicService.findSpecialtyById(3)).willReturn(specialty);
        Specialty newSpecialty = new Specialty();
        newSpecialty.setId(3);
        newSpecialty.setName("NewName");
        ObjectMapper mapper = new ObjectMapper();
        String newSpecialtyAsJSON = mapper.writeValueAsString(newSpecialty);

        this.mockMvc.perform(put("/api/specialties/3")
            .content(newSpecialtyAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(status().isNoContent());

        this.mockMvc.perform(get("/api/specialties/3")
            .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
//            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.id").value(3))
            .andExpect(jsonPath("$.name").value("NewName"));
    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    public void testUpdateSpecialtyWithChangeId() throws Exception {
        Specialty specialty = new Specialty();
        specialty.setId(3);
        specialty.setName("testSpeciality");

        given(this.clinicService.findSpecialtyById(3)).willReturn(specialty);
        Specialty newSpecialty = new Specialty();
        newSpecialty.setId(300);
        newSpecialty.setName("NewName");
        ObjectMapper mapper = new ObjectMapper();
        String newSpecialtyAsJSON = mapper.writeValueAsString(newSpecialty);

        this.mockMvc.perform(put("/api/specialties/3")
            .content(newSpecialtyAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(status().isNoContent());

        this.mockMvc.perform(get("/api/specialties/3")
            .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.id").value(3))
            .andExpect(jsonPath("$.name").value("NewName"));
    }
}
