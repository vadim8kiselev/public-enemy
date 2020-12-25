package com.kiselev.enemy.network.instagram.api.internal2.models.media;

import lombok.Data;

@Data
public class Audio {
    private String audio_src;
    private long duration;
    private double[] waveform_data;
    private int waveform_sampling_frequency_hz;
}
