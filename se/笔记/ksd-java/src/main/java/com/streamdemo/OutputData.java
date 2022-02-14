package com.streamdemo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OutputData {
    private int id;
    private String name;
    private String type;
    private int amount;
}