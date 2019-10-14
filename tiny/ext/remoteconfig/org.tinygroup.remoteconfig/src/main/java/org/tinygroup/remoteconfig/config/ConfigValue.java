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
package org.tinygroup.remoteconfig.config;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * @author yanwj06282
 */
public class ConfigValue implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3225816156526808185L;
    String key;
    String value;
    String title;
    String desc;
    Boolean encrypt;

    public ConfigValue() {
    }

    public ConfigValue(String value) {
        super();
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Boolean getEncrypt() {
    	if(encrypt == null){
    		return false;
    	}
		return encrypt;
	}

	public void setEncrypt(Boolean encrypt) {
		this.encrypt = encrypt;
	}

	@Override
    public String toString() {
        return getValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ConfigValue) {
            if (StringUtils.equals(((ConfigValue) obj).getKey(), getKey())
                    && StringUtils.equals(((ConfigValue) obj).getValue(), getValue())
                    && StringUtils.equals(((ConfigValue) obj).getTitle(), getTitle())
                    && StringUtils.equals(((ConfigValue) obj).getDesc(), getDesc())
                    && ((ConfigValue) obj).getEncrypt().equals(getEncrypt())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return StringUtils.defaultString(getKey()).hashCode() & StringUtils.defaultString(getValue()).hashCode() & StringUtils.defaultString(getValue()).hashCode() & StringUtils.defaultString(getDesc()).hashCode() & getEncrypt().hashCode();
    }

}
