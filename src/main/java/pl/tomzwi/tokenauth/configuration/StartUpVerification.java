package pl.tomzwi.tokenauth.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.tomzwi.tokenauth.exception.RoleNotFoundException;
import pl.tomzwi.tokenauth.service.RoleService;

import java.util.List;

@Component
@Slf4j
public class StartUpVerification {

    @Value("#{'${security.roles}'.split(',\\s*')}")
    List<String> roles;

    @Autowired
    private RoleService roleService;

    @EventListener
    public void onApplicationEvent( ContextRefreshedEvent event ) {
        log.info("Verifying database roles");

        log.info( "Defined roles in configuration: " + roles.toString() );
        roles.forEach( role -> {
            try {
                roleService.getRoleByName(role);
            } catch ( RoleNotFoundException ex ) {
                log.warn("Role " + role + " not found, creating...");
                roleService.addRole( role );
            }
        });
    }

}
