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
/**
 *
 */
package org.tinygroup.remoteconfig.zk.manager.impl;

import org.apache.commons.lang.StringUtils;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.remoteconfig.config.ConfigPath;
import org.tinygroup.remoteconfig.config.Product;
import org.tinygroup.remoteconfig.manager.ProductManager;
import org.tinygroup.remoteconfig.manager.VersionManager;
import org.tinygroup.remoteconfig.zk.client.ZKProductManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author yanwj06282
 */
public class ProductManagerImpl implements ProductManager {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ProductManagerImpl.class);

    VersionManager versionManager;

    public VersionManager getVersionManager() {
        if (versionManager == null) {
            versionManager = new VersionManagerImpl();
        }
        return versionManager;
    }

    public void setVersionManager(VersionManager versionManager) {
        this.versionManager = versionManager;
    }

    public Product add(Product product) {
        LOGGER.logMessage(LogLevel.INFO, "远程配置，增加项目[{0}]", product.getName());
        try {
            ZKProductManager.set(product.getName(), product, null);
            LOGGER.logMessage(LogLevel.INFO, "远程配置，增加项目成功");
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "远程配置，增加项目失败[{0}]", e, product.getName());
        }
        return product;
    }

    public void update(Product product) {
        LOGGER.logMessage(LogLevel.INFO, "远程配置，更新项目[{0}]", product.getName());
        try {
            ZKProductManager.set(product.getName(), product, null);
            LOGGER.logMessage(LogLevel.INFO, "远程配置，更新项目成功");
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "远程配置，更新项目失败[{0}]", e, product.getName());
        }
    }

    public void delete(String productId) {
        LOGGER.logMessage(LogLevel.INFO, "远程配置，删除项目[{0}]", productId);
        try {
            ZKProductManager.delete(productId, null);
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "远程配置，删除项目失败[{0}]", e, productId);
        }
    }

    public Product get(String productId) {
        LOGGER.logMessage(LogLevel.INFO, "远程配置，获取项目[{0}]", productId);
        try {
            Product product = ZKProductManager.get(productId, null);
            if (product == null) {
                return null;
            }
            product.setVersions(getVersionManager().query(productId));
            LOGGER.logMessage(LogLevel.INFO, "远程配置，获取项目成功");
            return product;
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "远程配置，获取项目失败[{0}]", e, productId);
        }
        return null;
    }

    /**
     * 模糊匹配
     */
    public List<Product> query(Product product) {
        LOGGER.logMessage(LogLevel.INFO, "远程配置，批量获取项目[{0}]", product.getName());
        List<Product> products = new ArrayList<Product>();
        try {
            Map<String, Product> configPathMap = ZKProductManager.getAll(new ConfigPath());
            String productName = product.getName();
            for (Iterator<String> iterator = configPathMap.keySet().iterator(); iterator.hasNext(); ) {
                String type = iterator.next();
                if (StringUtils.isBlank(productName) || StringUtils.indexOf(type, productName) > -1) {
                    Product tempPro = get(type);
                    if (tempPro != null) {
                        LOGGER.logMessage(LogLevel.INFO, "项目[{0}]", tempPro.getName());
                        products.add(tempPro);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "远程配置，批量获取项目失败[{0}]", e, product.getName());
        }
        return products;
    }

    public List<Product> query(Product product, int start, int limit) {
        return null;
    }

}
