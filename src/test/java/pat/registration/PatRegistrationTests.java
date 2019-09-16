package pat.registration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@EnableJpaRepositories(basePackages = ("pat.registration"))
@EntityScan(basePackages = "pat.registration")
public class PatRegistrationTests {

    @Autowired
    private WebApplicationContext wac;

    MockMvc mockMvc;

    @Autowired
    RegistrationService registrationService;

    @Before
    public void setup () {
        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
        this.mockMvc = builder.build();
    }


    @Test
    public void test_addNewMember() throws Exception {

        ResultMatcher ok = MockMvcResultMatchers.status()
                .is2xxSuccessful();

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/register")
                .param("name", "Test")
                .param("password", "Test")
                .param("address", "Test")
                .param("email", "test@gmx.de")
                .param("phone", "+49179855444");;

        this.mockMvc.perform(builder)
                .andExpect(ok);
    }

    @Test(expected = NestedServletException.class)
    public void test_addNewMemberInvalidValues() throws Exception {

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/register")
                .param("name", "")
                .param("password", "")
                .param("address", "Test")
                .param("email", "test@gmx.de")
                .param("phone", "Invalid");;

        this.mockMvc.perform(builder);

    }

    @Test(expected = NestedServletException.class)
    public void test_addNewMember_InvalidName() throws Exception {

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/register")
                .param("name", "Test12345")
                .param("password", "")
                .param("address", "Test")
                .param("email", "test@gmx.de")
                .param("phone", "0179855444");;

        this.mockMvc.perform(builder);

    }

    @Test(expected = NestedServletException.class)
    public void test_addNewMember_InvalidEmail() throws Exception {

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/register")
                .param("name", "Test12345")
                .param("password", "test")
                .param("address", "Test")
                .param("email", "test#de")
                .param("phone", "0179855444");;

        this.mockMvc.perform(builder);

    }

    @Test(expected = NestedServletException.class)
    public void test_addNewMember_InvalidPhoneNumber() throws Exception {

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/register")
                .param("name", "Test12345")
                .param("password", "test")
                .param("address", "Test")
                .param("email", "test#de")
                .param("phone", "InvalidPhone");;

        this.mockMvc.perform(builder);

    }


    @After
    public void clearMemberCache(){
        registrationService.removeAllMembers();
    }



}
