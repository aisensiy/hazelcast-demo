package com.example.hazelcastdemo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BusinessMetricsEntityList implements Serializable {
  private String description;
  private List<BusinessMetricsEntity> data;
  private String prefix;

  public BusinessMetricsEntityList(String prefix) {
    this.prefix = prefix;
    this.data = new ArrayList<>();
    this.description = "#";
  }

  public BusinessMetricsEntityList(String prefix, String description) {
    this.description = description;
    this.prefix = prefix;
    this.data = new ArrayList<>();
  }

  public BusinessMetricsEntityList(List<BusinessMetricsEntity> data, String prefix) {
    this.prefix = prefix;
    this.data =
        data.stream()
            .map(
                entity ->
                    new BusinessMetricsEntity(
                        prefix + entity.getName(), entity.getValue(), entity.getTags()))
            .collect(Collectors.toList());
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void add(BusinessMetricsEntity entity) {
    this.data.add(
        new BusinessMetricsEntity(prefix + entity.getName(), entity.getValue(), entity.getTags()));
  }

  public List<BusinessMetricsEntity> getData() {
    return Collections.unmodifiableList(data);
  }
}

