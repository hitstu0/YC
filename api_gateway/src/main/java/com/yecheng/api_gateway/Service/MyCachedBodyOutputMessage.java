package com.yecheng.api_gateway.Service;

import java.util.function.Supplier;

import org.reactivestreams.Publisher;
import org.springframework.cglib.core.internal.Function;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class MyCachedBodyOutputMessage implements ReactiveHttpOutputMessage {
    private final DataBufferFactory bufferFactory;
    private final HttpHeaders httpHeaders;
    private boolean cached = false;
    private Flux<DataBuffer> body = Flux.error(new IllegalStateException("The body is not set. Did handling complete with success?"));

    public MyCachedBodyOutputMessage(ServerWebExchange exchange, HttpHeaders httpHeaders) {
        this.bufferFactory = exchange.getResponse().bufferFactory();
        this.httpHeaders = httpHeaders;
    }

    public boolean isCommitted() {
        return false;
    }

    public boolean isCached() {
        return this.cached;
    }

    public HttpHeaders getHeaders() {
        return this.httpHeaders;
    }

    public DataBufferFactory bufferFactory() {
        return this.bufferFactory;
    }

    public Flux<DataBuffer> getBody() {
        return this.body;
    }


    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        this.body = Flux.from(body);
        this.cached = true;
        return Mono.empty();
    }

    public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
        return this.writeWith(Flux.from(body).flatMap((p) -> {
            return p;
        }));
    }

    public Mono<Void> setComplete() {
        return this.writeWith(Flux.empty());
    }

    @Override
    public void beforeCommit(Supplier<? extends Mono<Void>> action) {
        // TODO Auto-generated method stub
        
    }
}
