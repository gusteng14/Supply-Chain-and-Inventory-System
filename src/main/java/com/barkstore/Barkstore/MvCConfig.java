package com.barkstore.Barkstore;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvCConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = "product-photos";
        Path productPhotosDir = Paths.get(location);
        String productPhotosPath = productPhotosDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/"+location+"/**")
                .addResourceLocations("file:"+productPhotosPath+"/");
    }
}
