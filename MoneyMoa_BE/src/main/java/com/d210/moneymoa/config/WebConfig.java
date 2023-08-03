// package com.d210.moneymoa.config;

// import com.google.gson.Gson;
// import com.google.gson.GsonBuilder;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.converter.HttpMessageConverter;
// import org.springframework.web.client.RestTemplate;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;
// import org.springframework.web.servlet.config.annotation.EnableWebMvc;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// import javax.annotation.PostConstruct;
// import java.util.List;

// //@EnableWebMvc
// @Configuration
// public class WebConfig implements WebMvcConfigurer {

//      @Override
//      public void addCorsMappings(CorsRegistry registry) {
//          registry.addMapping("/**")
//                  .allowedOrigins("http://localhost:5173","http://127.0.0.1:5173")
//                  .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS", "PATCH")
//                  .allowedHeaders("*")
//                  .allowCredentials(true)
//                  .exposedHeaders("Authorization", "RefreshToken");
//      }
//  }



// @EnableWebMvc
// @Configuration
// public class WebConfig implements WebMvcConfigurer {

//     @Override
//     public void addCorsMappings(CorsRegistry registry) {
//         registry.addMapping("/**")
//             .allowedOriginPatterns("*") 
//             .allowedHeaders("*")
//             .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS" , "PATCH")
//             .exposedHeaders("Authorization", "RefreshToken")
//             .allowCredentials(true);
//     }

// }

      // registry.addMapping("/**")
        //         .allowedOriginPatterns("*")
        //         // .allowedOriginPatterns("http://localhost:5173")
        //         // .allowedOriginPatterns("http://http://i9d210.p.ssafy.io")
        //         .exposedHeaders("*");
