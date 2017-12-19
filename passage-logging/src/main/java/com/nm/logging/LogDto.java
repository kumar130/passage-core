package com.nm.logging;

import ch.qos.logback.classic.Logger;

public class LogDto {
    private String name;
    private String level;

    public LogDto(Logger logger) {
        this.name  = logger.getName();
        this.level = logger.getEffectiveLevel().toString();
    }

    public LogDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
