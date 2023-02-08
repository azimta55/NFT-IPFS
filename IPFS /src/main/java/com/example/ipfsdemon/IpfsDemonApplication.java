package com.example.ipfsdemon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.ipfs.payload,com.ipfs.property"})
public class IpfsDemonApplication {

    public static void main(String[] args) {
        SpringApplication.run(IpfsDemonApplication.class, args);
    }
//Azim T.A
}
