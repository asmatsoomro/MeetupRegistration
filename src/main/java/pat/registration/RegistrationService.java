package pat.registration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class RegistrationService {

    @Autowired
    RegistrationRepository registrationRepository;

    // Extend the current resource to receive and store the data in memory.
    // Return a success information to the user including the entered information.
    // In case of the address split the information into a better format/structure
    // for better handling later on.
    public Person save(Person person) {

        return registrationRepository.save(person);
    }

    public List<Person> getAllMembers() {
        return registrationRepository.findAll();
    }

    public void removeAllMembers(){
        registrationRepository.deleteAll();
    }

}
