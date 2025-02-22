package com.langdb.langDB.routing;

import lombok.Data;

@Data
public class ModelTarget {
    private String model;
    private Double temperature;
    private Integer maxTokens;
    private Double topP;
    private Double frequencyPenalty;
    private Double presencePenalty;
}
