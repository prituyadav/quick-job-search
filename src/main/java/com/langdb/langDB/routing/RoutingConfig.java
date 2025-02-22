package com.langdb.langDB.routing;

import lombok.Data;
import java.util.List;

@Data
public class RoutingConfig {
    private String type;
    private List<ModelTarget> targets;
}
