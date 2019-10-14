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
package org.tinygroup.context2object;

import org.tinygroup.context.Context;

import java.util.Collection;

/**
 * 对象生成器
 *
 * @param <ReturnType>
 * @param <ParaType>
 * @author luoguo
 */
public interface ObjectGenerator<ReturnType, ParaType> {
    /**
     * 从环境获取对象
     *
     * @param paraType 用来生成对象的参数
     * @param context  用来获取对象的环境
     * @return 获取的对象
     */
    ReturnType getObject(String varName, String bean, String className,
                         ClassLoader loader, Context context);

    /**
     * @param varName        变量名
     * @param collectionName 集合名
     * @param className      需要构建的类名
     * @param loader         类加载器
     * @param context        tiny上下文
     * @return
     */
    Collection<ReturnType> getObjectCollection(String varName,
                                               String collectionName, String className, ClassLoader loader,
                                               Context context);

    /**
     * @param varName   变量名
     * @param className 需要构建的类名
     * @param loader    类加载器
     * @param context   tiny上下文
     * @return
     */
    Object getObjectArray(String varName, String className, ClassLoader loader,
                          Context context);

    /**
     * 添加类型转换器
     *
     * @param typeConverter
     */
    void addTypeConverter(TypeConverter<?, ?> typeConverter);

    /**
     * 移除类型转换器
     *
     * @param typeConverter
     */
    void removeTypeConverter(TypeConverter<?, ?> typeConverter);

    /**
     * 添加类型创建器
     *
     * @param typeCreator
     */
    void addTypeCreator(TypeCreator<?> typeCreator);

    /**
     * 移除类型创建器
     *
     * @param typeCreator
     */
    void removeTypeCreator(TypeCreator<?> typeCreator);

}
