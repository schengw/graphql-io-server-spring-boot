package com.thinkenterprise.graphqlio.server.wsf.converter;

import com.thinkenterprise.graphqlio.server.wsf.domain.Frame;

/**
 * Converter
 */
public class Converter {

    public Frame from(String data) {
        // ToDo: Edgar finalize the implementation 
        return Frame.builder().fid("fid").build();
    }

    public String to(Frame frame) {        
        // ToDo: Edgar build the String from the frame 
        return null;
    }
    
}