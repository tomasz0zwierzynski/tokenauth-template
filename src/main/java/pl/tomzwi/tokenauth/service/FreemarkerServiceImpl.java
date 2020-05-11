package pl.tomzwi.tokenauth.service;

import freemarker.template.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class FreemarkerServiceImpl implements FreemarkerService {

    private final Configuration configuration;

    public FreemarkerServiceImpl() {
        configuration = new Configuration( new Version(2, 3, 30));
        configuration.setClassForTemplateLoading( this.getClass(), "/templates/");
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }

    @Override
    public String generateActivationMail(String name, String code) {

        Map<String, Object> input = new HashMap<>();
        input.put( "name", name );
        input.put( "code", code );

        try {
            Template template = configuration.getTemplate("active-code.ftl");

            Writer writer = new StringWriter();
            template.process(input, writer);

            return writer.toString();
        } catch (IOException | TemplateException e) {
            log.error( "Error while generating registration email", e );
        }

        return "Error!";
    }
}
