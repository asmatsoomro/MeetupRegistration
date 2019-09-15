package pat.registration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/registration")
@Component
public class RegistrationResource {

    @Autowired
    RegistrationRepository registrationRepository;

    // Extend the current resource to receive and store the data in memory.
    // Return a success information to the user including the entered information.
    // In case of the address split the information into a better format/structure
    // for better handling later on.
    public Response save(Person person) {

        registrationRepository.save(person);

        return Response.ok().build();
    }

    public List<Person> getAllMembers() {
        return registrationRepository.findAll();
    }

    public void removeAllMembers(){
        registrationRepository.deleteAll();
    }

}
