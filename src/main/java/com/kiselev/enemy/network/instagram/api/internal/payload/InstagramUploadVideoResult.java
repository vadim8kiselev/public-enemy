package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class InstagramUploadVideoResult extends StatusResult {
    private String upload_id;
    private List<Map<String, Object>> video_upload_urls;

}