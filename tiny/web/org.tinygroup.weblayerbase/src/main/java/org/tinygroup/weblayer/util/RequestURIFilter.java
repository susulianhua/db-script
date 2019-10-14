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
package org.tinygroup.weblayer.util;

import org.tinygroup.commons.tools.ToStringBuilder;

import java.util.List;
import java.util.regex.Pattern;

import static org.tinygroup.commons.tools.BasicConstant.*;
import static org.tinygroup.commons.tools.CollectionUtil.createLinkedList;
import static org.tinygroup.commons.tools.StringUtil.*;

/**
 * 用来匹配和过滤指定路径。
 *
 * @author renhui
 */
public class RequestURIFilter {
    public static final String EXCLUDE_PREFIX = "!";
    private final String[] uris;
    private final boolean[] excludes;
    private final Pattern[] patterns;

    public RequestURIFilter(String uris) {
        List<String> names = createLinkedList();
        List<Boolean> excludes = createLinkedList();
        List<Pattern> patterns = createLinkedList();

        for (String uri : split(defaultIfBlank(uris, EMPTY_STRING), ", \r\n")) {
            uri = trimToNull(uri);

            if (uri != null) {
                String fullUri = uri;
                boolean exclude = uri.startsWith(EXCLUDE_PREFIX);

                if (exclude) {
                    uri = trimToNull(uri.substring(EXCLUDE_PREFIX.length()));
                }

                if (uri != null) {
                    names.add(fullUri);
                    excludes.add(exclude);
                    // patterns.add(PathNameWildcardCompiler.compilePathName(uri));
                    patterns.add(Pattern.compile(uri));
                }
            }
        }

        if (!patterns.isEmpty()) {
            this.uris = names.toArray(new String[names.size()]);
            this.patterns = patterns.toArray(new Pattern[patterns.size()]);
            this.excludes = new boolean[excludes.size()];

            for (int i = 0; i < excludes.size(); i++) {
                this.excludes[i] = excludes.get(i);
            }
        } else {
            this.uris = EMPTY_STRING_ARRAY;
            this.excludes = EMPTY_BOOLEAN_ARRAY;
            this.patterns = null;
        }
    }

    public boolean matches(String path) {
        return matches(path, true);
    }

    public boolean matches(String path, boolean or) {
        if (patterns != null) {
            if (or) {
                return orMatch(path);
            } else {
                return andMatch(path);
            }
        }
        return false;
    }

    private boolean andMatch(String path) {
        boolean match = false;
        for (int i = 0; i < patterns.length; i++) {
            boolean exclude = excludes[i];
            boolean find = patterns[i].matcher(path).find();
            if (exclude) {
                match = !find;
            } else {
                match = find;
            }
            if (!match) {
                return false;
            }
        }
        return match;
    }

    private boolean orMatch(String path) {
        boolean match = false;
        for (int i = 0; i < patterns.length; i++) {
            boolean exclude = excludes[i];
            boolean find = patterns[i].matcher(path).find();
            if (exclude) {
                match = !find;
            } else {
                match = find;
            }
            if (match) {
                return true;
            }
        }
        return match;
    }

    @Override
    public String toString() {
        return new ToStringBuilder().append("FilterOf").append(uris).toString();
    }
}
