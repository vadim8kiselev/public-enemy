package com.kiselev.enemy.utils.progress;

import com.kiselev.enemy.network.telegram.api.bot.TelegramBotAPI;
import com.kiselev.enemy.utils.flow.model.Id;
import com.kiselev.enemy.utils.flow.model.SocialNetwork;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgressBar {

    private static final String TEMPLATE = "\\[%s\\] %s\\: %s \\(%s/%s - %s%%\\)";

    private static final DecimalFormat FORMAT = new DecimalFormat("#0.000");

    @Value("${com.kiselev.enemy.telegram.id}")
    private Long telegramIdentifier;

    private final TelegramBotAPI telegram;

    public <Type extends Id> void bar(SocialNetwork socialNetwork, String label, List<Type> objects, Type object) {
        int total = objects.size();
        int current = objects.indexOf(object) + 1;

        double percent = (current * 100 + 0.0) / total;

        telegram.send(telegramIdentifier, String.format(TEMPLATE,
                socialNetwork.name(),
                label,
                object.name(),
                current,
                total,
                FORMAT.format(percent)));
    }

    public void bar(SocialNetwork socialNetwork, String label, List<String> objects, String object) {
        int total = objects.size();
        int current = objects.indexOf(object);

        double percent = (current * 100 + 0.0) / total;

        telegram.send(telegramIdentifier, String.format(TEMPLATE,
                socialNetwork.name(),
                label,
                object,
                current,
                total,
                FORMAT.format(percent)));
    }
}
