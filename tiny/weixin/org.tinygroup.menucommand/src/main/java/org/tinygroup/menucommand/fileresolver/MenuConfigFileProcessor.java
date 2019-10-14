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
package org.tinygroup.menucommand.fileresolver;

import com.thoughtworks.xstream.XStream;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.menucommand.MenuConfigManager;
import org.tinygroup.menucommand.config.MenuConfigs;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

/**
 * 菜单命令配置的文件处理器
 *
 * @author yancheng11334
 */
public class MenuConfigFileProcessor extends AbstractFileProcessor {

    private static final String MENU_URL_NAME = ".menuconfig";
    private static final String XSTEAM_PACKAGE_NAME = "menuconfig";

    private MenuConfigManager menuConfigManager;

    public MenuConfigManager getMenuConfigManager() {
        return menuConfigManager;
    }

    public void setMenuConfigManager(MenuConfigManager menuConfigManager) {
        this.menuConfigManager = menuConfigManager;
    }

    public void process() {
        XStream stream = XStreamFactory.getXStream(XSTEAM_PACKAGE_NAME);
        for (FileObject fileObject : deleteList) {
            LOGGER.logMessage(LogLevel.INFO, "正在移除菜单命令配置文件[{0}]",
                    fileObject.getAbsolutePath());
            MenuConfigs menuConfigs = (MenuConfigs) caches
                    .get(fileObject.getAbsolutePath());
            if (menuConfigs != null) {
                menuConfigManager.removeMenuConfigs(menuConfigs);
                caches.remove(fileObject.getAbsolutePath());
            }
            LOGGER.logMessage(LogLevel.INFO, "移除菜单命令配置文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }

        for (FileObject fileObject : changeList) {
            LOGGER.logMessage(LogLevel.INFO, "正在加载菜单命令配置文件[{0}]",
                    fileObject.getAbsolutePath());
            MenuConfigs oldMenuConfigs = (MenuConfigs) caches
                    .get(fileObject.getAbsolutePath());
            if (oldMenuConfigs != null) {
                menuConfigManager.removeMenuConfigs(oldMenuConfigs);
            }
            MenuConfigs menuConfigs = (MenuConfigs) stream.fromXML(fileObject
                    .getInputStream());
            if (menuConfigs != null) {
                menuConfigManager.addMenuConfigs(menuConfigs);
            }
            caches.put(fileObject.getAbsolutePath(), menuConfigs);
            LOGGER.logMessage(LogLevel.INFO, "加载菜单命令配置文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
    }

    @Override
    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(MENU_URL_NAME) || fileObject.getFileName().endsWith(".menuconfig.xml");
    }

}
