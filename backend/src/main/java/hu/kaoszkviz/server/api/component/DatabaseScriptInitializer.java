
package hu.kaoszkviz.server.api.component;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


@Component
public class DatabaseScriptInitializer {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    
    @PostConstruct
    public void intializeDatabaseScripts() throws IOException {

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:");
        
        
        
    }
    
}
