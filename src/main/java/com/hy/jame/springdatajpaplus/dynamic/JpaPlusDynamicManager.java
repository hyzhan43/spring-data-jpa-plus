package com.hy.jame.springdatajpaplus.dynamic;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * author: HyJame
 * date:   2020/1/27
 * desc:   TODO
 */
@Component
@Getter
public class JpaPlusDynamicManager {

    private List<JpaPlusDynamic> jpaPlusIgnoreList;

    public JpaPlusDynamicManager() {
        jpaPlusIgnoreList = new ArrayList<>();

        jpaPlusIgnoreList.add(new NotEmptyDynamic());
        jpaPlusIgnoreList.add(new NotNullDynamic());
    }

    public void registerDynamic(JpaPlusDynamic jpaPlusDynamic) {
        jpaPlusIgnoreList.add(jpaPlusDynamic);
    }
}
