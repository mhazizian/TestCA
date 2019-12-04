package org.springframework.samples.petclinic.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.service.clinicService.ApplicationTestConfig;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
//@WebMvcTest(SpecialtyRestController.class)
@WebAppConfiguration
@ContextConfiguration(classes= ApplicationTestConfig.class)
public class SpecialityRestControllerNewTests {
    @Autowired
    private SpecialtyRestController specialtyRestController;

    @MockBean
    private ClinicService clinicService;

    private MockMvc mockMvc;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Before
    public void initSpecialties(){
        this.mockMvc = MockMvcBuilders.standaloneSetup(specialtyRestController)
            .setControllerAdvice(new ExceptionControllerAdvice()).apply(documentationConfiguration(this.restDocumentation))
            .alwaysDo(document("{method-name}",
                preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
            .build();
    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    public void testUpdateSpecialtySuccess() throws Exception {
        Specialty specialty = new Specialty();
        specialty.setId(1);
        specialty.setName("testSpeciality");
        given(this.clinicService.findSpecialtyById(2)).willReturn(specialty);
        Specialty newSpecialty = new Specialty();
        specialty.setId(2);
        newSpecialty.setName("surgery I");
        ObjectMapper mapper = new ObjectMapper();
        String newSpecialtyAsJSON = mapper.writeValueAsString(newSpecialty);
        this.mockMvc.perform(put("/api/specialties/2")
            .content(newSpecialtyAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(status().isNoContent());

        this.mockMvc.perform(get("/api/specialties/2")
            .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.id").value(2))
            .andExpect(jsonPath("$.name").value("surgery I"));
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
    @WithMockUser(roles = "VET_ADMIN" )
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

    @Test
    @WithMockUser(roles="OWNER_ADMIN")
    public void testUpdateSpecialtyWithWrongPermission() throws Exception {
        Specialty specialty = new Specialty();
        specialty.setId(1);
        specialty.setName("testSpeciality");
        given(this.clinicService.findSpecialtyById(2)).willReturn(specialty);
        Specialty newSpecialty = new Specialty();
        specialty.setId(2);
        newSpecialty.setName("surgery I");
        ObjectMapper mapper = new ObjectMapper();
        String newSpecialtyAsJSON = mapper.writeValueAsString(newSpecialty);
        this.mockMvc.perform(put("/api/specialties/2")
            .content(newSpecialtyAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().contentType("application/json;charset=ISO-8859-1"))
            .andExpect(status().isBadRequest());
    }


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
            .andExpect(status().isNoContent());

        this.mockMvc.perform(get("/api/specialties/3")
            .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
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
