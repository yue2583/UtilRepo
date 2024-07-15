package com.yt.param;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor(staticName = "instance")
public class ListParam {
    private List<Long> remindIds;
    private List<Long> ghIds;
    private List<Integer> ghViewTypes;
    private List<Long> spuIds;
    private List<Integer> actTypes;
    private List<Long> actIds;
    private List<Integer> delStatus;
    private List<Integer> ignoreStatus;
}
