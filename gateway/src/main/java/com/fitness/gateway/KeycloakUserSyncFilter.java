package com.fitness.gateway;

import com.fitness.gateway.user.RegisterRequest;
import com.fitness.gateway.user.UserService;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class KeycloakUserSyncFilter implements WebFilter {
    private final UserService userService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain filterChain){
//        String userId = exchange.getRequest().getHeaders().getFirst("X-USER-ID");
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        RegisterRequest registerRequest = getUserDetails(token);
        String keycloakId = registerRequest.getKeyCloakId();

        if(keycloakId != null && token != null){
            return userService.validateUser(keycloakId)
                    .flatMap( exist ->{
                        if(!exist){
                            // Register user

                            if(registerRequest != null){
                                return userService.registerUser(registerRequest)
                                        .then(Mono.empty());
                            } else
                                 return Mono.empty();
                        } else{
                            log.info("User already exists, skipping sync: {}", keycloakId);
                            return Mono.empty();
                        }
                    })
                    .then(Mono.defer(() -> {
                        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                                .header("X-USER-ID", keycloakId)
                                .build();

                        return filterChain.filter(exchange.mutate().request(mutatedRequest).build());
                    }));
        }
        return filterChain.filter(exchange);
    }

    private RegisterRequest getUserDetails(String token) {
        try{
            String tokenWithoutBearer = token.substring(7);
            SignedJWT signedJWT = SignedJWT.parse(tokenWithoutBearer);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setEmail(claims.getStringClaim("email"));
            registerRequest.setPassword("dummy_password");
            registerRequest.setKeyCloakId(claims.getStringClaim("sub"));
            registerRequest.setFirstName(claims.getStringClaim("given_name"));
            registerRequest.setLastName(claims.getStringClaim("family_name"));

            return registerRequest;

        } catch(Exception e){
            log.error("Error while fetching user details from token: ", e);
            return null;
        }
    }
}
