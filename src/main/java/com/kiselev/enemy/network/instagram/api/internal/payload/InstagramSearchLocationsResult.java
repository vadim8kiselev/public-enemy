package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Search Locations Result
 *
 * @author Yumaev
 */
@Getter
@Setter
@ToString(callSuper = true)
public class InstagramSearchLocationsResult extends StatusResult {
    private List<InstagramLocation> venues;
}
