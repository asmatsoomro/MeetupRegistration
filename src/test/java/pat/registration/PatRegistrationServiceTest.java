package pat.registration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


@RunWith(SpringJUnit4ClassRunner.class)
public class PatRegistrationServiceTest {

    @Mock
    RegistrationService registrationService;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_addMember(){
        Person person = new Person("Test", "test",  "dummy street stuttgart", "test@gmx.de","012345678");
        when(registrationService.save(person)).thenReturn(person);
    }


    @Test
    public void test_retrieveAllMembers(){
        Person person = new Person("Test", "test",  "dummy street stuttgart", "test@gmx.de","012345678");
        List<Person> personList = new ArrayList<Person>();
        personList.add(person);
        when(registrationService.getAllMembers()).thenReturn(personList);

        List<Person> result = registrationService.getAllMembers();
        assertEquals(1, result.size());
    }

}
