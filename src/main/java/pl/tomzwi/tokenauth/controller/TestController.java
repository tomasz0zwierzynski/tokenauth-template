package pl.tomzwi.tokenauth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    public JavaMailSender emailSender;

    @GetMapping( "/test" )
    public String test() {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo( "tomasz0zwierzynski@gmail.com" );
        message.setSubject("temat");
        message.setText("tresc");

        emailSender.send( message );
        return "OK";
    }

}
