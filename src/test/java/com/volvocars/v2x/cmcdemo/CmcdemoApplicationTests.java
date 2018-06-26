package com.volvocars.v2x.cmcdemo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CmcdemoApplicationTests {

	@Test
	public void contextLoads() {
	    String username = "admin";
	    String password = "{YWRtaW46VnJh.}";
	    try {
            WebClient.create().get().uri("http://127.0.0.1:8080/nest/hello")
                    .header("Authorization", "Basic " + Base64Utils.encodeToString((username + ":" + password).getBytes("utf-8")))
                    .retrieve().bodyToMono(String.class)
                    .flatMap(s -> {
                        System.out.println("result->" + s);
                        return Mono.just(s);
                    }).block();
        }catch (UnsupportedEncodingException uee){
	        uee.printStackTrace();
        }
    }

}
