package com.nm.common;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TestDto {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
