package org.tinygroup.springintegration.ratelimit;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.integration.core.MessageSelector;
import org.springframework.messaging.Message;

public class RateLimitFilter implements MessageSelector {
    private RateLimiter rateLimiter;

    public RateLimiter getRateLimiter() {
        return rateLimiter;
    }

    public void setRateLimiter(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @Override
    public boolean accept(Message<?> message) {
        if(rateLimiter==null){
            return true;
        }else{
            return rateLimiter.tryAcquire();
        }

    }
}
