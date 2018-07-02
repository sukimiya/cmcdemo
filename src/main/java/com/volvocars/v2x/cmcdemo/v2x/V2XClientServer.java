/*
 * cmcdemo
 * Copyright (C) 2018.  e2x.io
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.volvocars.v2x.cmcdemo.v2x;

import com.volvocars.v2x.cmcdemo.car.CarLocationUtils;
import com.volvocars.v2x.cmcdemo.events.V2XAuthorizationSuccessEvent;
import com.volvocars.v2x.cmcdemo.car.vo.BSMFrame;
import com.volvocars.v2x.cmcdemo.car.vo.CarStatusVO;
import com.volvocars.v2x.cmcdemo.car.vo.CarVO;
import com.volvocars.v2x.cmcdemo.repo.CarAuthRepository;
import com.volvocars.v2x.cmcdemo.repo.CarsRepository;
import com.volvocars.v2x.cmcdemo.v2x.msg.AuthReq;
import com.volvocars.v2x.cmcdemo.v2x.msg.AuthResp;
import com.volvocars.v2x.cmcdemo.v2x.vo.CarAuthorizationVO;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.e2x.logger.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.time.Duration;
import java.util.Objects;

public class V2XClientServer implements ApplicationListener<V2XAuthorizationSuccessEvent> {

    private Logger logger = new Logger(this);

    public WebClient client;

    public CarAuthRepository carAuthRepository;
    public CarsRepository carsRepository;

    @Autowired
    public ApplicationEventPublisher publisher;

    @Autowired
    private V2XClientNet netClient;


    public V2XClientServer(CarAuthRepository carAuthRepository,CarsRepository carsRepository) {
        this.carAuthRepository = carAuthRepository;
        this.carsRepository = carsRepository;
    }
    public void renewTokenAndKey(String id, String localmac) throws InterruptedException{
        loginCarById(id,localmac);
    }
    public Mono<AuthResp> loginCarById(String id, String localmac) throws InterruptedException {
        Thread.sleep(1000);
        return checkByCarId(id,localmac).flatMap(carVO -> {
            return Mono.just(new AuthReq(carVO.getAin(),carVO.getEth0_mac(),0));
        }).flatMap(authReq -> client.post().uri("/v2x-auth/v1/auth")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(authReq),AuthReq.class)
                .retrieve()
                .bodyToMono(AuthResp.class)
        );
    }
    public void loginCar(String id,String mac){
        try {
            loginCarById(id,mac).flatMap(resp1 -> {
                return this.onAuthResult(resp1 ,id,mac);
            }).subscribe(carAuthorizationVO -> {
                V2XAuthorizationSuccessEvent event = new V2XAuthorizationSuccessEvent(this,carAuthorizationVO);
                publisher.publishEvent(event);
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public Mono<CarVO> getFirstCar(){
        return carsRepository.findAll()
                .collectList().flatMap(carVOS -> Mono.just(carVOS.get(0)));
    }

    private Mono<CarAuthorizationVO> onAuthResult(AuthResp resp,String id,String mac) {
        logger.info("reviced Object :"+resp.toString());
        CarAuthorizationVO auth = null;
        try {
            auth = expressResp2Auth(resp,id,mac);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Mono.just(auth);
    }

    private CarAuthorizationVO expressResp2Auth(AuthResp resp,String id,String mac) throws Exception {
        CarAuthorizationVO auth = V2XClientUtils.fromAuthResult(resp,id,mac);
        return auth;
    }
    public void uploadBSMPackage(BSMFrame bsmFrame) {
        EventLoopGroup group  = new NioEventLoopGroup();
        Bootstrap bootstrap =new Bootstrap();
        bootstrap.group(group).channel(NioDatagramChannel.class).option(ChannelOption.SO_KEEPALIVE,true);
//        UdpClient.create().doOnConnect(bootstrap1 -> {System.out.print("uploadBSMPackage::doOnConnect");})
//                .doOnDisconnected(connection -> {System.out.print("uploadBSMPackage:"+connection.address().getHostString()+" disconnected");})
//                .host("112.25.66.161").port(28120).connect().timeout(Duration.ofSeconds(1));
    }
    public void onPressJson(){

    }
    public void onBSMSent(Object o){
        logger.info("V2XService onBSMSent");
    }
    public Mono<CarVO> checkByCarId(String id, String localmac){

        return carsRepository.findAllByAin(id)
                .collectList().flatMap(carVOS -> {
                    if (carVOS!=null&&!carVOS.isEmpty())
                        return Mono.just(carVOS.get(0));
                    return carsRepository.save(new CarVO(id,localmac,new CarStatusVO()));
                });

    }

    public void setClient(WebClient client) {
        this.client = client;
    }

    public CarAuthorizationVO getV2XAuth() {
        return tmpV2XAuth;
    }

    private CarAuthorizationVO tmpV2XAuth;
    @Override
    public void onApplicationEvent(V2XAuthorizationSuccessEvent v2XAuthorizationSuccessEvent) {
        Objects.requireNonNull(netClient,"V2XClientNet Null");
        tmpV2XAuth = v2XAuthorizationSuccessEvent.auth;
        //
        Flux.interval(Duration.ofMillis(100))
                .map(this::nextBSMFrame)
                .subscribe(bsmFrame -> {
                    netClient.send(bsmFrame);
                    //logger.info(bsmFrame.toString());
                });

    }
    private int coordsIndex=0;
    private String[] coords = {"314919552,1202865664,0","314841248,1202871680,0","314986496,1202910464,0","314886912,1202922496,0"};
    private BSMFrame nextBSMFrame(long aLong){
        String coordstr = coords[coordsIndex];
        coordsIndex++;
        if(coordsIndex==coords.length) coordsIndex=0;
        return getTestBSM(coordstr);
    }
    private BSMFrame getTestBSM(String s){
        String[] sary = s.split(",");
        BSMFrame rs = new BSMFrame(tmpV2XAuth.carid,CarLocationUtils.pressLocationString(sary[0],sary[1],sary[2]),tmpV2XAuth.accessToken);
        rs.addMsgCnt();
        return rs;
    }

}
