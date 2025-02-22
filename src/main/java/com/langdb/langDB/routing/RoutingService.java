package com.langdb.langDB.routing;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class RoutingService {

    public ModelTarget getFallbackModel(RoutingConfig config) {
        if ("fallback".equals(config.getType())) {
            return config.getTargets().stream().findFirst().orElse(null);
        }
        return null;
    }

    public ModelTarget getOptimizedModel(RoutingConfig config) {
        if ("optimized".equals(config.getType())) {
            return config.getTargets().stream()
                    .min(Comparator.comparingInt(
                            ModelTarget::getMaxTokens)) // Example: Selects model with lowest maxTokens
                    .orElse(null);
        }
        return null;
    }

    public ModelTarget getPercentageBasedModel(RoutingConfig config) {
        if ("percentage".equals(config.getType())) {
            double random = Math.random();
            double cumulativeProbability = 0.0;
            for (ModelTarget target : config.getTargets()) {
                cumulativeProbability += 0.5; // Example: Assigning equal probability
                if (random <= cumulativeProbability) {
                    return target;
                }
            }
        }
        return null;
    }

    public ModelTarget getLatencyBasedModel(RoutingConfig config, List<Integer> responseTimes) {
        if ("latency".equals(config.getType()) && !responseTimes.isEmpty()) {
            return config.getTargets()
                    .get(responseTimes.indexOf(responseTimes.stream().min(Integer::compare).orElse(0)));
        }
        return null;
    }
}
