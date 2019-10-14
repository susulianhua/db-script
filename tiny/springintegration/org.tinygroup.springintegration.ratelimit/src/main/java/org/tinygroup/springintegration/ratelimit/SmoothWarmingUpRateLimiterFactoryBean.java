package org.tinygroup.springintegration.ratelimit;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.beans.factory.FactoryBean;

import java.util.concurrent.TimeUnit;

public class SmoothWarmingUpRateLimiterFactoryBean implements FactoryBean<RateLimiter>{

    private double permitsPerSecond;
    private long warmupPeriod;
    private TimeUnit unit;

    public long getWarmupPeriod() {
        return warmupPeriod;
    }

    public void setWarmupPeriod(long warmupPeriod) {
        this.warmupPeriod = warmupPeriod;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public void setUnit(TimeUnit unit) {
        this.unit = unit;
    }

    public double getPermitsPerSecond() {
        return permitsPerSecond;
    }

    public void setPermitsPerSecond(double permitsPerSecond) {
        this.permitsPerSecond = permitsPerSecond;
    }

    @Override
    public RateLimiter getObject() throws Exception {
        return RateLimiter.create(permitsPerSecond,warmupPeriod,unit);
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
