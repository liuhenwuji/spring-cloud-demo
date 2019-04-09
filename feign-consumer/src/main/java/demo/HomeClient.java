package demo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value ="eureka-provider",fallbackFactory = HystrixClientFallbackFactory.class)
public interface  HomeClient {
    @GetMapping("/")
    String consumer();
}