<<<<<<< HEAD:src/main/java/com/system/wood/gloabal/config/Swagger2Config.java
package com.system.wood.gloabal.config;
=======
package com.system.wood.global.config;
>>>>>>> e8f21e559ae94ca08171988ed533609be3f53a56:src/main/java/com/system/wood/global/config/Swagger2Config.java

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class Swagger2Config {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.system.wood"))
                .paths(PathSelectors.any())
                .build();
    }

}
