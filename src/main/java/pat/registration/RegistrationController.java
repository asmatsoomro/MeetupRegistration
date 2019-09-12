package pat.registration;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class RegistrationController {

    @Autowired
    private VelocityEngine velocityEngine;

    @Autowired
    private RegistrationResource registrationResource;

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
    public String addNewMember(Model model, POST post, HttpServletRequest servletRequest) {

        LOGGER.info("Adding a new member to the repository");
        String name = servletRequest.getParameter("name");
        String password = servletRequest.getParameter("password");
        String address = servletRequest.getParameter("address");
        String email = servletRequest.getParameter("email");
        String phone = servletRequest.getParameter("phone");
        if (servletRequest == null || StringUtils.isEmpty(name) || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(address) || StringUtils.isEmpty(email) || StringUtils.isEmpty(phone)) {
            Template template = velocityEngine.getTemplate("templates/registrationFailure.vm");
            VelocityContext context = new VelocityContext();
            StringWriter writer = new StringWriter();
            template.merge(context, writer);

            return writer.toString();
        }

        Person person = new Person();
        person.setName(servletRequest.getParameter("name"));
        person.setAddress(servletRequest.getParameter("address"));
        person.setEmail(servletRequest.getParameter("email"));
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(servletRequest.getParameter("password"));



        person.setPassword(hashedPassword);
        person.setPhone(servletRequest.getParameter("phone"));

        registrationResource.save(person);

        Template template = velocityEngine.getTemplate("templates/registrationSuccessful.vm");
        VelocityContext context = new VelocityContext();
        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        return writer.toString();
    }

    @RequestMapping(value = {"/person", "/Person"}, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getAllMembers()
            throws IOException, ExecutionException, InterruptedException {
        LOGGER.info("Received a request to get all members");
        List<Person> memberList = registrationResource.getAllMembers();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(memberList);

    }
}