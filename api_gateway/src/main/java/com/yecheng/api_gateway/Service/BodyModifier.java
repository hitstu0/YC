package com.yecheng.api_gateway.Service;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;

import com.alibaba.fastjson.util.BiFunction;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;

import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;


@Component
public class BodyModifier implements GlobalFilter, Ordered {
    private Logger logger = LoggerFactory.getLogger(BodyModifier.class);

    @Autowired
    private RsaService rsaService;

    @Override
    public int getOrder() {
        return -1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerRequest serverRequest = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());
        Mono<String> modifiedBody = serverRequest.bodyToMono(String.class).flatMap(originalBody -> modifyBody()
                .apply(exchange,originalBody));

        BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);

        HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());
        headers.remove("Content-Length");

        MyCachedBodyOutputMessage outputMessage = new MyCachedBodyOutputMessage(exchange, headers);
        return bodyInserter.insert(outputMessage, new BodyInserterContext()).then(Mono.defer(() -> {
            ServerHttpRequest decorator = decorate(exchange, headers, outputMessage);
            return chain.filter(exchange.mutate().request(decorator).build());
        })).onErrorResume((throwable) -> {
            return release(exchange, outputMessage,(Throwable) throwable);
        });
    }

    private BiFunction<ServerWebExchange, String, Mono<String>> modifyBody() {
        return new BiFunction<ServerWebExchange, String, Mono<String>>() {
            @Override
            public Mono<String> apply(ServerWebExchange serverWebExchange, String raw) {
                try {
                    String decrypt = rsaService.doDecryption(raw);
                    return Mono.just(decrypt);
                } catch (Exception e) {
                    logger.error("服务器异常",e);
                    return Mono.empty();
                }
            }
        };
    }


    protected Mono<Void> release(ServerWebExchange exchange, MyCachedBodyOutputMessage outputMessage, Throwable throwable) {
        return outputMessage.isCached() ? outputMessage.getBody().map(DataBufferUtils::release).then(Mono.error(throwable)) : Mono.error(throwable);
    }

    ServerHttpRequestDecorator decorate(ServerWebExchange exchange, HttpHeaders headers, MyCachedBodyOutputMessage outputMessage) {
        return new ServerHttpRequestDecorator(exchange.getRequest()) {
            public HttpHeaders getHeaders() {
                long contentLength = headers.getContentLength();
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.putAll(headers);
                if (contentLength > 0L) {
                    httpHeaders.setContentLength(contentLength);
                } else {
                    httpHeaders.set("Transfer-Encoding", "chunked");
                }

                return httpHeaders;
            }

            public Flux<DataBuffer> getBody() {
                return outputMessage.getBody();
            }
        };
    }
}



