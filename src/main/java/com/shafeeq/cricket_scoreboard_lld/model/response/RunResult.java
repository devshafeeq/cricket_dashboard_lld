package com.shafeeq.cricket_scoreboard_lld.model.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RunResult {
    
    @Builder.Default
    private List<String> results = new ArrayList<>();

    public void addResult(String res) {
        results.add(res);
    }
}
