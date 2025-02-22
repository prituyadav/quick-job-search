package com.langdb.langDB.routing;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/routing")
public class RoutingController {

    private final RoutingService routingService;

    public RoutingController(RoutingService routingService) {
        this.routingService = routingService;
    }

    @PostMapping("/fallback")
    public ModelTarget getFallbackModel(@RequestBody RoutingConfig config) {
        return routingService.getFallbackModel(config);
    }

    @PostMapping("/optimized")
    public ModelTarget getOptimizedModel(@RequestBody RoutingConfig config) {
        return routingService.getOptimizedModel(config);
    }

    @PostMapping("/percentage")
    public ModelTarget getPercentageModel(@RequestBody RoutingConfig config) {
        return routingService.getPercentageBasedModel(config);
    }

    @PostMapping("/latency")
    public ModelTarget getLatencyModel(@RequestBody RoutingConfig config) {
        List<Integer> responseTimes = Arrays.asList(100, 200, 150); // Example response times
        return routingService.getLatencyBasedModel(config, responseTimes);
    }
}
