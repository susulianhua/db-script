/**
 * Copyright (c) 2012-2017, www.tinygroup.org (luo_guo@icloud.com).
 * <p>
 * Licensed under the GPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/gpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tinygroup.tinytest;

import org.springframework.context.ApplicationContext;
import org.springframework.core.style.ToStringCreator;
import org.springframework.test.context.CacheAwareContextLoaderDelegate;
import org.springframework.test.context.ContextLoader;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.util.Assert;

public class TinyMergedContextConfiguration extends MergedContextConfiguration {

    private final CacheAwareContextLoaderDelegate contextLoaderDelegate;
    private MergedContextConfiguration mergedContextConfiguration;
    private MergedContextConfiguration parentConfiguration;

    public TinyMergedContextConfiguration(ContextLoader contextLoader,
                                          CacheAwareContextLoaderDelegate contextLoaderDelegate,
                                          MergedContextConfiguration mergedContextConfiguration,
                                          MergedContextConfiguration parentConfiguration) {
        super(mergedContextConfiguration.getTestClass(),
                mergedContextConfiguration.getLocations(),
                mergedContextConfiguration.getClasses(),
                mergedContextConfiguration.getActiveProfiles(), contextLoader);
        this.mergedContextConfiguration = mergedContextConfiguration;
        this.contextLoaderDelegate = contextLoaderDelegate;
        this.parentConfiguration = parentConfiguration;
    }

    public ApplicationContext getParentApplicationContext() {
        MergedContextConfiguration parent = mergedContextConfiguration
                .getParent();
        Assert.state(
                this.contextLoaderDelegate != null,
                "Cannot retrieve a parent application context without access to the CacheAwareContextLoaderDelegate");
        if (parent == null) {
            return this.contextLoaderDelegate.loadContext(parentConfiguration);
        }
        ApplicationContext applicationContext = this.contextLoaderDelegate
                .loadContext(parent);
        if (applicationContext == null) {
            applicationContext = this.contextLoaderDelegate
                    .loadContext(parentConfiguration);
        }
        return applicationContext;
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("orignalMergedContextConfiguration",
                        this.mergedContextConfiguration)
                .append("parentMergedContextConfiguration",
                        this.parentConfiguration).toString();
    }

}
