
package hu.kaoszkviz.server.api.component;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


@Component
public class DatabaseScriptInitializer {
    private Logger logger = LoggerFactory.getLogger(DatabaseScriptInitializer.class);
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    
    
    @PostConstruct
    public void intializeDatabaseScripts() throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:databaseScripts/*");
        
        for (Resource resource : resources) {
            int count = 0;
            int totalCount = 0;
            
            try (InputStream inputStream = resource.getInputStream();) {
                String fileString = new String(inputStream.readAllBytes());
                
                String[] databaseScripts = fileString.split("GO");
                
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
        
    }

    
}
