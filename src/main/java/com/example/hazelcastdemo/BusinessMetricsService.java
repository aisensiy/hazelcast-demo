package com.example.hazelcastdemo;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BusinessMetricsService {

  @Cacheable("metrics")
  public List<BusinessMetricsEntityList> get() {
    BusinessMetricsEntity metric1 = new BusinessMetricsEntity("metric1", 1, Map.of("tag1", "value1"));
    BusinessMetricsEntity metric2 = new BusinessMetricsEntity("metric2", 2, Map.of("tag1", "value1"));
    BusinessMetricsEntityList list = new BusinessMetricsEntityList("prefix", "description");
    list.add(metric1);
    list.add(metric2);

    BusinessMetricsEntity metric3 = new BusinessMetricsEntity("metric3", 1);
    BusinessMetricsEntity metric4 = new BusinessMetricsEntity("metric4", 2);
    BusinessMetricsEntityList list2 = new BusinessMetricsEntityList("prefix", "description");
    list2.add(metric3);
    list2.add(metric4);

    return List.of(list, list2);
  }
}
