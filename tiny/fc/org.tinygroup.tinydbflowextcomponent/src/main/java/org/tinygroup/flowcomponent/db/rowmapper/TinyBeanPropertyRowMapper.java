/**
 * Copyright (c) 2012-2016, www.tinygroup.org (luo_guo@icloud.com).
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
package org.tinygroup.flowcomponent.db.rowmapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.*;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.tinygroup.flowcomponent.db.rowmapper.editor.AllowNullNumberEditor;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * 仿照BeanPropertyRowMapper实现的RowMapper,为了解决spring不同版本驼峰规则处理的不同
 *
 * @author renhui
 */
public class TinyBeanPropertyRowMapper<T> extends BaseMappedClass<T> implements
        RowMapper<T> {

    /**
     * Logger available to subclasses
     */
    protected final Log logger = LogFactory.getLog(getClass());

    /**
     * Whether we're strictly validating
     */
    private boolean checkFullyPopulated = false;

    /**
     * Whether we're defaulting primitives when mapping a null value
     */
    private boolean primitivesDefaultedForNullValue = false;

    public TinyBeanPropertyRowMapper() {
        super();
    }

    public TinyBeanPropertyRowMapper(Class<T> mappedClass,
                                     boolean checkFullyPopulated) {
        super(mappedClass);
        this.checkFullyPopulated = checkFullyPopulated;
    }

    public TinyBeanPropertyRowMapper(Class<T> requiredType) {
        super(requiredType);
    }

    public static <T> TinyBeanPropertyRowMapper<T> newInstance(
            Class<T> mappedClass) {
        return new TinyBeanPropertyRowMapper<T>(mappedClass);
    }

    protected void initBeanWrapper(BeanWrapper bw) {
        bw.registerCustomEditor(byte.class, new AllowNullNumberEditor(
                Byte.class, true));
        bw.registerCustomEditor(short.class, new AllowNullNumberEditor(
                Short.class, true));
        bw.registerCustomEditor(int.class, new AllowNullNumberEditor(
                Integer.class, true));
        bw.registerCustomEditor(long.class, new AllowNullNumberEditor(
                Long.class, true));
        bw.registerCustomEditor(float.class, new AllowNullNumberEditor(
                Float.class, true));
        bw.registerCustomEditor(double.class, new AllowNullNumberEditor(
                Double.class, true));
    }

    public T mapRow(ResultSet rs, int rowNumber) throws SQLException {
        Assert.state(this.mappedClass != null, "Mapped class was not specified");
        T mappedObject = BeanUtils.instantiate(this.mappedClass);
        BeanWrapper bw = PropertyAccessorFactory
                .forBeanPropertyAccess(mappedObject);
        initBeanWrapper(bw);

        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        Set<String> populatedProperties = (isCheckFullyPopulated() ? new HashSet<String>()
                : null);

        for (int index = 1; index <= columnCount; index++) {
            String column = JdbcUtils.lookupColumnName(rsmd, index);
            String field = lowerCaseName(column.replaceAll(" ", ""));
            PropertyDescriptor pd = this.mappedFields.get(field);
            if (pd != null) {
                try {
                    Object value = getColumnValue(rs, index, pd);
                    if (rowNumber == 0 && logger.isDebugEnabled()) {
                        logger.debug("Mapping column '"
                                + column
                                + "' to property '"
                                + pd.getName()
                                + "' of type ["
                                + ClassUtils.getQualifiedName(pd
                                .getPropertyType()) + "]");
                    }
                    try {
                        bw.setPropertyValue(pd.getName(), value);
                    } catch (TypeMismatchException ex) {
                        if (value == null
                                && this.primitivesDefaultedForNullValue) {
                            if (logger.isDebugEnabled()) {
                                logger.debug(
                                        "Intercepted TypeMismatchException for row "
                                                + rowNumber
                                                + " and column '"
                                                + column
                                                + "' with null value when setting property '"
                                                + pd.getName()
                                                + "' of type ["
                                                + ClassUtils.getQualifiedName(pd
                                                .getPropertyType())
                                                + "] on object: "
                                                + mappedObject, ex);
                            }
                        } else {
                            throw ex;
                        }
                    }
                    if (populatedProperties != null) {
                        populatedProperties.add(pd.getName());
                    }
                } catch (NotWritablePropertyException ex) {
                    throw new DataRetrievalFailureException(
                            "Unable to map column '" + column
                                    + "' to property '" + pd.getName() + "'",
                            ex);
                }
            } else {
                // No PropertyDescriptor found
                if (rowNumber == 0 && logger.isDebugEnabled()) {
                    logger.debug("No property found for column '" + column
                            + "' mapped to field '" + field + "'");
                }
            }
        }

        if (populatedProperties != null
                && !populatedProperties.equals(this.mappedProperties)) {
            throw new InvalidDataAccessApiUsageException(
                    "Given ResultSet does not contain all fields "
                            + "necessary to populate object of class ["
                            + this.mappedClass.getName() + "]: "
                            + this.mappedProperties);
        }

        return mappedObject;
    }

    /**
     * Return whether we're strictly validating that all bean properties have
     * been mapped from corresponding database fields.
     */
    public boolean isCheckFullyPopulated() {
        return this.checkFullyPopulated;
    }

    /**
     * Set whether we're strictly validating that all bean properties have been
     * mapped from corresponding database fields.
     * <p>
     * Default is {@code false}, accepting unpopulated properties in the target
     * bean.
     */
    public void setCheckFullyPopulated(boolean checkFullyPopulated) {
        this.checkFullyPopulated = checkFullyPopulated;
    }

    /**
     * Return whether we're defaulting Java primitives in the case of mapping a
     * null value from corresponding database fields.
     */
    public boolean isPrimitivesDefaultedForNullValue() {
        return this.primitivesDefaultedForNullValue;
    }

    /**
     * Set whether we're defaulting Java primitives in the case of mapping a
     * null value from corresponding database fields.
     * <p>
     * Default is {@code false}, throwing an exception when nulls are mapped to
     * Java primitives.
     */
    public void setPrimitivesDefaultedForNullValue(
            boolean primitivesDefaultedForNullValue) {
        this.primitivesDefaultedForNullValue = primitivesDefaultedForNullValue;
    }

    /**
     * Retrieve a JDBC object value for the specified column.
     * <p>
     * The default implementation calls
     * {@link JdbcUtils#getResultSetValue(ResultSet, int, Class)}.
     * Subclasses may override this to check specific value types upfront, or to
     * post-process values return from {@code getResultSetValue}.
     *
     * @param rs    is the ResultSet holding the data
     * @param index is the column index
     * @param pd    the bean property that each result object is expected to match
     *              (or {@code null} if none specified)
     * @return the Object value
     * @throws SQLException in case of extraction failure
     * @see org.springframework.jdbc.support.JdbcUtils#getResultSetValue(ResultSet,
     * int, Class)
     */
    protected Object getColumnValue(ResultSet rs, int index,
                                    PropertyDescriptor pd) throws SQLException {
        return JdbcUtils.getResultSetValue(rs, index, pd.getPropertyType());
    }

}
