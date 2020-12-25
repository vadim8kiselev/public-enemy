package com.kiselev.enemy.network.instagram.api.internal.storymetadata;

import com.kiselev.enemy.network.instagram.api.internal.payload.InstagramUser;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StoryQuestionResponderInfo {
    private long question_id;
    private String question;
    private String question_type;
    private String background_color;
    private String text_color;
    private List<QuestionResponder> responders;
    private String max_id;
    private boolean more_available;
    private int question_response_count;
    private int unanswered_response_count;
    private long latest_question_response_time;

    @Getter
    @Setter
    public static class QuestionResponder {
        private String response;
        private boolean has_shared_response;
        private long id;
        private InstagramUser user;
        private long ts;
    }
}
