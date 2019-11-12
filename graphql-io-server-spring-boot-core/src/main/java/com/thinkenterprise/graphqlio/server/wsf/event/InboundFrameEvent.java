package com.thinkenterprise.graphqlio.server.wsf.event;

import com.thinkenterprise.graphqlio.server.wsf.domain.Frame;

import org.springframework.context.ApplicationEvent;

/**
 * InboundFrameEvent
 */
public class InboundFrameEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private String cid;
    private Frame frame;

    public InboundFrameEvent(Frame frame, String cid ) {
        super(frame);
        this.cid=cid;
        this.frame=frame;
    }    

   public String getCid() {
        return this.cid;
    }

    public Frame getFrame() {
        return this.frame;
    }


    
}