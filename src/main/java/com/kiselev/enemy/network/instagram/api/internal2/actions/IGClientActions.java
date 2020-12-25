package com.kiselev.enemy.network.instagram.api.internal2.actions;

import java.lang.reflect.Field;
import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.actions.account.AccountAction;
import com.kiselev.enemy.network.instagram.api.internal2.actions.igtv.IgtvAction;
import com.kiselev.enemy.network.instagram.api.internal2.actions.search.SearchAction;
import com.kiselev.enemy.network.instagram.api.internal2.actions.simulate.SimulateAction;
import com.kiselev.enemy.network.instagram.api.internal2.actions.status.StatusAction;
import com.kiselev.enemy.network.instagram.api.internal2.actions.story.StoryAction;
import com.kiselev.enemy.network.instagram.api.internal2.actions.timeline.TimelineAction;
import com.kiselev.enemy.network.instagram.api.internal2.actions.upload.UploadAction;
import com.kiselev.enemy.network.instagram.api.internal2.actions.users.UsersAction;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

@Accessors(fluent = true, prefix = "_")
@Getter
public class IGClientActions {
    private UploadAction _upload;
    private TimelineAction _timeline;
    private StoryAction _story;
    private UsersAction _users;
    private SimulateAction _simulate;
    private IgtvAction _igtv;
    private AccountAction _account;
    private SearchAction _search;
    private StatusAction _status;

    @SneakyThrows
    public IGClientActions(IGClient client) {
        for (Field field : this.getClass().getDeclaredFields())
            if (field.getName().startsWith("_"))
                field.set(this, field.getType().getConstructor(IGClient.class).newInstance(client));
    }

}
