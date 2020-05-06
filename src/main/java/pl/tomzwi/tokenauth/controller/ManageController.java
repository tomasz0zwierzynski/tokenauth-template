package pl.tomzwi.tokenauth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "/${security.endpoint.api.prefix}" )
public class ManageController {

    @GetMapping( "/manage" )
    @PreAuthorize("hasRole('ADMIN')")
    public String manage() {
        return "manage";
    }

}
