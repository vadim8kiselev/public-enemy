package com.kiselev.enemy.service.profiler.model;

import com.kiselev.enemy.network.instagram.api.internal2.models.direct.item.ThreadItem;
import com.kiselev.enemy.network.instagram.api.internal2.models.direct.item.ThreadTextItem;
import com.kiselev.enemy.network.vk.api.model.Message;
import com.kiselev.enemy.service.profiler.utils.ProfilingUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.telegram.api.message.TLMessage;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Text {

    private String id;

    private String from;

    private Boolean mine;

    private String text;

    private LocalDateTime date;

    public Text(TLMessage message) {
        this.id = String.valueOf(message.getId());
        this.from = String.valueOf(message.getFromId());
        this.text = message.getMessage();
        this.date = ProfilingUtils.timestampSeconds(message.getDate());
    }

    public Text(ThreadItem message) {
        this.id = message.getItem_id();
        this.from = String.valueOf(message.getUser_id());
        if (message instanceof ThreadTextItem) {
            this.text = ((ThreadTextItem) message).getText();
        }
        this.date = ProfilingUtils.timestamp(
                TimeUnit.MILLISECONDS.convert(message.getTimestamp(), TimeUnit.MICROSECONDS)
        );
    }

    public Text(Message message) {
        this.id = String.valueOf(message.id());
        this.from = String.valueOf(message.fromId());
        this.text = message.text();
        this.date = ProfilingUtils.timestampSeconds(message.date());
    }
}
