package com.kiselev.enemy.utils.flow.message;

import com.kiselev.enemy.utils.flow.model.Info;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(fluent = true)
@AllArgsConstructor(staticName = "of")
public class Analysis<Profile extends Info> {

    public static final Integer LIMIT_LIST = 3;

    private byte[] photo;

    private List<EnemyMessage<Profile>> messages;
}
