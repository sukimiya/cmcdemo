package com.volvocars.v2x.cmcdemo;

import com.volvocars.v2x.cmcdemo.events.V2XAuthorizationSuccessEvent;
import com.volvocars.v2x.cmcdemo.repo.CarAuthRepository;
import com.volvocars.v2x.cmcdemo.repo.CarsRepository;
import com.volvocars.v2x.cmcdemo.v2x.V2XClientNet;
import com.volvocars.v2x.cmcdemo.v2x.V2XClientServer;
import com.volvocars.v2x.cmcdemo.v2x.V2XClientUtils;
import com.volvocars.v2x.cmcdemo.v2x.msg.AuthResp;
import com.volvocars.v2x.cmcdemo.v2x.vo.CarAuthorizationVO;
import io.e2x.logger.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.util.stream.Stream;


@SpringBootApplication
public class CmcdemoApplication {




    private Logger logger = new Logger(this);

    @Autowired
    private ApplicationContext publisher;


    @Bean
    WebClient getWebClient(){
        return WebClient.builder()
                .baseUrl("http://112.25.66.162:8003")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                .build();
    }
    @Bean
    CommandLineRunner AuthToV2X(CarAuthRepository carAuthRepository, CarsRepository carsRepository){

        V2XClientServer v2XClientServer = getV2XClientServer(carAuthRepository,carsRepository);
        v2XClientServer.setClient(getWebClient());
        Mono<AuthResp> authRespMono = null;
        v2XClientServer.loginCar("0065766033703042","D8-C7-71-4B-7A-4C");
        return args-> Stream.of("Finished Start Auth");

    }
    private V2XClientServer v2XClientServer;
    @Bean
    public V2XClientServer getV2XClientServer(CarAuthRepository carAuthRepository, CarsRepository carsRepository){
        return v2XClientServer = new V2XClientServer(carAuthRepository,carsRepository);
    }
    private V2XClientNet v2XClientNet;
    @Bean
    public V2XClientNet getV2XClientNet(){
        if(v2XClientNet==null)v2XClientNet = new V2XClientNet("112.25.66.161",28120,v2XClientServer);
        return v2XClientNet;
    }
    @Autowired
    public ApplicationEventPublisher applicationEventPublisher;
	public static void main(String[] args) {

        SpringApplication.run(CmcdemoApplication.class, args);

	}
}