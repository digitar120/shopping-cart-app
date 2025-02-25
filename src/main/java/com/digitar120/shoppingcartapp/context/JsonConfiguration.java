//

package com.digitar120.shoppingcartapp.context;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This configuration helps to solve an issue in Item queries made from the Cart endpoint.
 * @author Gabriel Pérez (digitar120)
 * @see com.digitar120.shoppingcartapp.persistence.entity.Cart
 * @link <a href="https://stackoverflow.com/questions/52656517/no-serializer-found-for-class-org-hibernate-proxy-pojo-bytebuddy-bytebuddyinterc">Stack Overflow - No serializer found for class org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor</a>
 */
@Configuration
public class JsonConfiguration {
    @Bean
    public Module hibernateModule() {
        return new Hibernate5Module();
    }
}