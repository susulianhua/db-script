package org.tinygroup.springintegration.ratelimit;

import org.springframework.beans.factory.FactoryBean;
import com.google.common.util.concurrent.RateLimiter;

public class SmoothBurstyRateLimiterFactoryBean implements FactoryBean<RateLimiter>{

    private double permitsPerSecond;

    public double getPermitsPerSecond() {
        return permitsPerSecond;
    }

    public void setPermitsPerSecond(double permitsPerSecond) {
        this.permitsPerSecond = permitsPerSecond;
    }

    @Override
    public RateLimiter getObject() throws Exception {
        return RateLimiter.create(permitsPerSecond);
    }

    @Override
    public Class<RateLimiter> getObjectType() {
        return RateLimiter.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
