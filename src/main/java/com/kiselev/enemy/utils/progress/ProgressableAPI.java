package com.kiselev.enemy.utils.progress;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class ProgressableAPI {

    @Autowired
    protected ProgressBar progress;
}
