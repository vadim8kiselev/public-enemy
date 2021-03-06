package com.kiselev.enemy.network.telegram.api.client;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kiselev.enemy.network.instagram.api.internal2.models.direct.IGThread;
import com.kiselev.enemy.network.instagram.api.internal2.models.direct.item.ThreadItem;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.Profile;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.User;
import com.kiselev.enemy.network.telegram.api.client.internal.*;
import com.kiselev.enemy.network.telegram.api.client.internal.database.DatabaseManagerImpl;
import com.kiselev.enemy.network.telegram.api.client.model.TelegramProfile;
import com.kiselev.enemy.network.telegram.utils.TelegramUtils;
import com.kiselev.enemy.service.profiler.model.Conversation;
import com.kiselev.enemy.service.profiler.model.Person;
import com.kiselev.enemy.service.profiler.model.Text;
import com.kiselev.enemy.service.profiler.utils.ProfilingUtils;
import com.kiselev.enemy.utils.flow.model.SocialNetwork;
import com.kiselev.enemy.utils.progress.ProgressableAPI;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.api.chat.TLChat;
import org.telegram.api.chat.channel.TLChannel;
import org.telegram.api.contact.TLContact;
import org.telegram.api.contacts.TLContacts;
import org.telegram.api.functions.contacts.TLRequestContactsGetContacts;
import org.telegram.api.functions.messages.TLRequestMessagesGetAllChats;
import org.telegram.api.functions.messages.TLRequestMessagesGetHistory;
import org.telegram.api.functions.users.TLRequestUsersGetFullUser;
import org.telegram.api.input.peer.TLInputPeerUser;
import org.telegram.api.input.user.TLAbsInputUser;
import org.telegram.api.input.user.TLInputUser;
import org.telegram.api.input.user.TLInputUserSelf;
import org.telegram.api.message.TLMessage;
import org.telegram.api.messages.TLAbsMessages;
import org.telegram.api.messages.chats.TLAbsMessagesChats;
import org.telegram.api.user.TLUser;
import org.telegram.api.user.TLUserFull;
import org.telegram.bot.structure.BotConfig;
import org.telegram.bot.structure.LoginStatus;
import org.telegram.tl.TLIntVector;
import org.telegram.tl.TLMethod;
import org.telegram.tl.TLObject;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TelegramClientAPI extends ProgressableAPI {

    @Value("${com.kiselev.enemy.telegram.client.key}")
    private Integer key;

    @Value("${com.kiselev.enemy.telegram.client.hash}")
    private String hash;

    @Value("${com.kiselev.enemy.telegram.client.number}")
    private String number;

    private IKernelComm kernel;

    public TLUserFull me() {
        TLRequestUsersGetFullUser request = new TLRequestUsersGetFullUser();
        TLAbsInputUser self = new TLInputUserSelf();
        request.setId(self);

        return execute(request);
    }

    public TLUserFull profile(String id) {
        TLRequestUsersGetFullUser request = new TLRequestUsersGetFullUser();
        TLInputUser input = new TLInputUser();
        input.setUserId(Integer.parseInt(id));
        request.setId(input);

        return execute(request);
    }

    public List<TLChat> chats() {
        TLRequestMessagesGetAllChats request = new TLRequestMessagesGetAllChats();
        request.setExceptIds(new TLIntVector());
        TLAbsMessagesChats response = execute(request);

        return response.getChats().stream()
                .filter(chat -> chat instanceof TLChat)
                .map(TLChat.class::cast)
                .collect(Collectors.toList());
    }

    public List<TLChannel> channels() {
        TLRequestMessagesGetAllChats request = new TLRequestMessagesGetAllChats();
        request.setExceptIds(new TLIntVector());
        TLAbsMessagesChats response = execute(request);

        return response.getChats().stream()
                .filter(chat -> chat instanceof TLChannel)
                .map(TLChannel.class::cast)
                .collect(Collectors.toList());
    }

    public List<TLContact> contacts() {
        TLRequestContactsGetContacts request = new TLRequestContactsGetContacts();
        request.setHash("");
        TLContacts response = (TLContacts) execute(request);

        return response.getContacts().stream()
                .map(TLContact.class::cast)
                .collect(Collectors.toList());
    }

    public List<TLUser> people() {
        TLRequestContactsGetContacts request = new TLRequestContactsGetContacts();
        request.setHash("");
        TLContacts response = (TLContacts) execute(request);

        return response.getUsers().stream()
                .filter(user -> user instanceof TLUser)
                .map(TLUser.class::cast)
                .collect(Collectors.toList());
    }
    
    public Map<TLUser, Set<TLMessage>> history() {
        List<TLUser> people = people();

        List<TelegramProfile> profiles = people.stream()
                .map(TelegramProfile::new)
                .collect(Collectors.toList());

        Map<TLUser, Set<TLMessage>> history = Maps.newHashMap();
        List<Conversation> conversations = Lists.newArrayList();

        for (TLUser person : people) {
            try {
                TelegramProfile profile = new TelegramProfile(person);

                progress.bar(SocialNetwork.TG, "History messages", profiles, profile);

                Set<TLMessage> messages = messages(String.valueOf(profile.id()));

                conversations.add(
                        Conversation.builder()
                                .id(profile.id())
                                .person(new Person(profile))
                                .texts(messages.stream()
                                        .map(Text::new)
                                        .peek(text -> text.setMine(Objects.equals("63756324", text.getFrom())))
                                        .collect(Collectors.toList()))
                                .build()
                );

                history.put(person, messages);

                ProfilingUtils.cache("tg_temporary", conversations);
            } catch (Exception exception) {
                log.warn(exception.getMessage(), exception);
            }
        }

        return history;

//        return people.stream()
//                .collect(Collectors.toMap(
//                        person -> person,
//                        person -> messages(String.valueOf(person.getId())),
//                        (first, second) -> second
//                ));
    }

    public Set<TLMessage> messages(String id) {
        Set<TLMessage> messages = Sets.newLinkedHashSet();
        int offset = 0;
        int limit = 100;

        TLRequestMessagesGetHistory request = new TLRequestMessagesGetHistory();
        TLInputPeerUser peer = new TLInputPeerUser();
        peer.setUserId(Integer.parseInt(id));
        request.setPeer(peer);
        request.setLimit(limit);

        while (true) {
            request.setAddOffset(offset);
            TLAbsMessages response = execute(request);

            List<TLMessage> page = response.getMessages().stream()
                    .filter(message -> message instanceof TLMessage)
                    .map(TLMessage.class::cast)
                    .collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(page)) {
                messages.addAll(page);
                offset += limit;
            } else {
                break;
            }
        }

        return messages;
    }

    @SneakyThrows
    private <T extends TLObject> T execute(TLMethod<T> request) {
        authenticate();
        TelegramUtils.timeout();
        return kernel.getApi().doRpcCall(request);
    }

    @SneakyThrows
    private void authenticate() {
        if (kernel == null) {
            final DatabaseManagerImpl databaseManager = new DatabaseManagerImpl();
            final BotConfig botConfig = new BotConfigImpl(number);

//            final IUsersHandler usersHandler = new UsersHandler(databaseManager);
//            final IChatsHandler chatsHandler = new ChatsHandler(databaseManager);
//            final MessageHandler messageHandler = new MessageHandler();
//            final TLMessageHandler tlMessageHandler = new TLMessageHandler(messageHandler, databaseManager);

            final ChatUpdatesBuilderImpl builder = new ChatUpdatesBuilderImpl(CustomUpdatesHandler.class);
            builder.setBotConfig(botConfig)
                    .setDatabaseManager(databaseManager)
//                    .setUsersHandler(usersHandler)
//                    .setChatsHandler(chatsHandler)
//                    .setMessageHandler(messageHandler)
//                    .setTlMessageHandler(tlMessageHandler)
            ;

            final TelegramBot kernel = new TelegramBot(botConfig, builder, key, hash);
            LoginStatus status = kernel.init();
            if (status == LoginStatus.CODESENT) {
                Scanner in = new Scanner(System.in);
                boolean success = kernel.getKernelAuth().setAuthCode(in.nextLine().trim());
                if (success) {
                    status = LoginStatus.ALREADYLOGGED;
                }
            }
            if (status == LoginStatus.ALREADYLOGGED) {
//                kernel.startBot();
            } else {
                throw new Exception("Failed to log in: " + status);
            }

            this.kernel = kernel.getKernelComm();
        }
    }
}
