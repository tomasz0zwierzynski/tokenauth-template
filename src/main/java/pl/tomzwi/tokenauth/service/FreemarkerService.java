package pl.tomzwi.tokenauth.service;

public interface FreemarkerService {

    String generateActivationMail( String name, String code );

}
