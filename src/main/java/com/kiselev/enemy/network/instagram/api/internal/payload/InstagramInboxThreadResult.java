package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Inbox Thread Result
 *
 * @author Krisnamourt da Silva C. Filho
 */
@Getter
@Setter
@ToString(callSuper = true)
public class InstagramInboxThreadResult extends StatusResult {

    public InstagramInboxThread thread;

}
