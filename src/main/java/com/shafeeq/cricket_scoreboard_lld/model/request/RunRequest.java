package com.shafeeq.cricket_scoreboard_lld.model.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RunRequest {
    private List<String> requests;
    private String singleLineReq;
}
