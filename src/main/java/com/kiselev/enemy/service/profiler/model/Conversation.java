package com.kiselev.enemy.service.profiler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Conversation {

    private String id;

    private Person person;

    private List<Text> texts;
}
