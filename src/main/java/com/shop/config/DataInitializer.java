package com.shop.config;

import com.shop.model.Product;
import com.shop.model.User;
import com.shop.repository.ProductRepository;
import com.shop.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner init(UserRepository users, ProductRepository products, PasswordEncoder encoder){
        return args -> {
            if(users.findByUsername("admin").isEmpty()){
                User a=new User();
                a.setUsername("admin");
                a.setPassword(encoder.encode("admin123"));
                a.setFullName("Quản trị viên");
                a.setRole("ADMIN");
                users.save(a);
            }
            if(products.count()==0){
                products.save(product("Áo thun basic",199000,"Áo thun cotton form đẹp","https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=600","Thời trang"));
                products.save(product("Giày sneaker",799000,"Giày sneaker nam nữ","https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=600","Giày"));
                products.save(product("Ba lô thời trang",349000,"Ba lô đi học, đi làm","https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=600","Phụ kiện"));
                products.save(product("Tai nghe Bluetooth",599000,"Tai nghe không dây","https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600","Điện tử"));
            }
        };
    }

    private Product product(String n,double p,String d,String i,String c){
        Product x=new Product(); x.setName(n); x.setPrice(p); x.setQuantity(50); x.setDescription(d); x.setImage(i); x.setCategory(c); return x;
    }
}
