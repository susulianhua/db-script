package org.tinygroup.parsedsql.base;

import java.io.Serializable;

import org.tinygroup.commons.tools.EqualsUtil;
import org.tinygroup.commons.tools.HashCodeUtil;

import com.google.common.base.Optional;

/**
 * 表对象
 * @author renhui
 *
 */
public class Table implements Serializable {
	
	private final String schema;
    
    private final String name;
    
    private final Optional<String> alias;
    
    public Table(final String name,final String alias) {
        this(name,null, Optional.fromNullable(alias));
    }
    
    public Table(final String name, String schema,final String alias) {
        this(name,schema, Optional.fromNullable(alias));
    }

	public Table(String name,String schema, Optional<String> alias) {
		super();
		this.name = name;
		this.schema=schema;
		this.alias = alias;
	}

	public String getName() {
		return name;
	}

	public Optional<String> getAlias() {
		return alias;
	}
	

	public String getSchema() {
		return schema;
	}

	@Override
	public int hashCode() {
		return HashCodeUtil.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsUtil.reflectionEquals(this, obj);
	}

	@Override
	public String toString() {
		return "Table [name=" + name + ",schema=" + schema + ", alias=" + alias + "]";
	}

}