/**
 * Copyright (C) 2018 Zsombor Gegesy (gzsombor@gmail.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString()
public class SavedFeedResult extends StatusResult {
    private boolean auto_load_more_enabled;
    private int num_results;
    private String next_max_id;
    private boolean more_available;

    private List<FeedMediaItem> items;

}
