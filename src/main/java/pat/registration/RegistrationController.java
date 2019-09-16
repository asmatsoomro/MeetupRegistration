package pat.registration;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

@RestController
public class RegistrationController {

    @Autowired
    private VelocityEngine velocityEngine;

    @Autowired
    private RegistrationService registrationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationController.class);

    @RequestMapping("/registration.html")
    public String registration() {

        Template template = velocityEngine.getTemplate("templates/registration.vm");
        VelocityContext context = new VelocityContext();
        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        return writer.toString();
    }

    @RequestMapping(value = {"/register", "/Register"}, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ExceptionHandler(IllegalArgumentException.class)
    public String addNewMember(HttpServletRequest servletRequest) throws IllegalArgumentException {

        LOGGER.info("Adding a new member to the repository");
        String name = servletRequest.getParameter("name");
        String password = servletRequest.getParameter("password");
        String address = servletRequest.getParameter("address");
        String email = servletRequest.getParameter("email");
        String phone = servletRequest.getParameter("phone");
        if (isNameInvalid(name) || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(address) || isEmailAddressInValid(email) || isPhoneNumberInvalid(phone)) {

            throw new IllegalArgumentException("One or more input values are invalid");
        }

        Person person = new Person();
        person.setName(servletRequest.getParameter("name"));
        person.setAddress(servletRequest.getParameter("address"));
        person.setEmail(servletRequest.getParameter("email"));

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        person.setPassword(hashedPassword);
        person.setPhone(servletRequest.getParameter("phone"));

        registrationService.save(person);

        Template template = velocityEngine.getTemplate("templates/registrationSuccessful.vm");
        VelocityContext context = new VelocityContext();
        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        return writer.toString();
    }

    private boolean isNameInvalid(String name) {
        return StringUtils.isEmpty(name) || Pattern.compile("[0-9]").matcher(name).find();
    }

    private boolean isEmailAddressInValid(String email){
        String regex = "^(.+)@(.+)$";

        Pattern pattern = Pattern.compile(regex);
        return StringUtils.isEmpty(email) || !pattern.matcher(email).find();
    }

    private boolean isPhoneNumberInvalid(String number) {
        String regex = "^\\+(?:[0-9] ?){6,14}[0-9]$";
        Pattern pattern = Pattern.compile(regex);
        return StringUtils.isEmpty(number) || !pattern.matcher(number).find();
    }

    @RequestMapping(value = {"/person", "/Person"}, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getAllMembers()
            throws IOException, ExecutionException, InterruptedException {
        LOGGER.info("Received a request to get all members");
        List<Person> memberList = registrationService.getAllMembers();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(memberList);

    }


}