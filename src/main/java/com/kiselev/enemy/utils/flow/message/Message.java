package com.kiselev.enemy.utils.flow.message;

import com.kiselev.enemy.utils.flow.model.Info;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor(staticName = "of")
public class Message<Profile extends Info> {

    public static final String HEADER_TEMPLATE = "\\[%s\\] %s\n";

    private final Profile profile;

    private final String message;
}
