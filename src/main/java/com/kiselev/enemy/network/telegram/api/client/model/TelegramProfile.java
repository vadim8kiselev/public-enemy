package com.kiselev.enemy.network.telegram.api.client.model;

import com.kiselev.enemy.utils.flow.model.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.telegram.api.user.TLUser;
import org.telegram.api.user.TLUserFull;
import org.telegram.api.user.profile.photo.TLAbsUserProfilePhoto;
import org.telegram.api.user.status.TLAbsUserStatus;

@Data
@Accessors(fluent = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TelegramProfile implements Id {

    @EqualsAndHashCode.Include
    public String id;
    
    private String firstName;
    
    private String lastName;
    
    private String userName;
    
    private String phone;

    private String info;
    
    private TLAbsUserProfilePhoto photo;
    
    private TLAbsUserStatus status;

    private long accessHash;

    public TelegramProfile(TLUser user) {
        this.id = String.valueOf(user.getId());
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.userName = user.getUserName();
        this.phone = user.getPhone();
        this.photo = user.getPhoto();
        this.status = user.getStatus();
        this.accessHash = user.getAccessHash();
    }

    public TelegramProfile(TLUserFull user) {
        this((TLUser) user.getUser());
        this.info = user.getAbout();

    }

    @Override
    public String name() {
        return userName;
    }
}
