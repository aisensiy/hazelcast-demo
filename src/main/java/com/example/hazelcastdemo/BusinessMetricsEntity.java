package com.example.hazelcastdemo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BusinessMetricsEntity implements Serializable {
  private String name;
  private Number value;
  private Map<String, String> tags = new HashMap<>();

  public BusinessMetricsEntity(String name, Number value) {
    this.name = name;
    this.value = value;
  }

  @Override
  public String toString() {
    String tagsToString =
        tags.entrySet().stream()
            .map(item -> String.format("%s=\"%s\"", item.getKey(), item.getValue()))
            .collect(Collectors.joining(","));
    return String.format("%s{%s} %s", name, tagsToString, value.toString());
  }
}

