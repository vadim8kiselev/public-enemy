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

    private byte[] photo;

    private List<Message<Profile>> messages;
}
