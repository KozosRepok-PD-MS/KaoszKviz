
package hu.kaoszkviz.server.api.component;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


@Component
public class DatabaseScriptInitializer {
    @Value("${application.generate.sqlscript}")
    private boolean generateScript;
    
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String hibernateStatus;
    
    private final String SPLIT_TEXT = "GO";
    
    private Logger logger = LoggerFactory.getLogger(DatabaseScriptInitializer.class);
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    
    @PostConstruct
    public void intializeDatabaseScripts() throws IOException {
        if (!this.generateScript) {
            this.logger.info("SQL scriptek betöltésének kihagyása.");
            return;
        }
        this.logger.info("SQL scriptek betöltése...");
        
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:databaseScripts/*");
        
        for (Resource resource : resources) {
            if (resource.getFilename().equals("initialDatas.sql") && this.skipInitialDatas()) { continue; }
            
            int count = 0;
            int totalCount = 0;
            
            try (InputStream inputStream = resource.getInputStream();) {
                String fileString = new String(inputStream.readAllBytes());
                
                String[] databaseScripts = fileString.contains(this.SPLIT_TEXT) ? fileString.split(this.SPLIT_TEXT) : new String[]{fileString};

                for (String databaseScript : databaseScripts) {
                    if (databaseScript.isBlank()) { continue;}
                    try {
                        this.jdbcTemplate.execute(databaseScript);
                        count++;
                    } catch(Exception ex) {
                        this.logger.warn(ex.getLocalizedMessage());
                    }
                    totalCount++;
                }
                this.logger.info("%s - %d/%d script betöltve".formatted(resource.getFilename(), count, totalCount));
            } catch(Exception ex) {
                this.logger.warn("%s - script betöltése sikertelen".formatted(resource.getFilename()));
            }
        }
        
        this.logger.info("SQL scriptek betöltése vége.");
    }
    
    private boolean skipInitialDatas() {
        return !this.hibernateStatus.equals("create") && !this.hibernateStatus.equals("create-drop");
    }
    
}
