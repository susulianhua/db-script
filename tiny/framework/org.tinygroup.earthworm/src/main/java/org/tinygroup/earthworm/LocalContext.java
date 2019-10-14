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
package org.tinygroup.earthworm;

public class LocalContext extends WormBaseContext {
    LocalContext localParent;
    private boolean end;

    LocalContext(String _traceId, String _rpcId) {
        super(_traceId, _rpcId);
    }

    LocalContext(String _traceId, String _rpcId, String _localId) {
        super(_traceId, _rpcId);
        this.localId = _localId;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public LocalContext getLocalParent() {
        return localParent;
    }

    public String toString() {
        return super.toString() + KV_ITEM_SPLITER + "end" + KV_SPLITER + end;
    }

}
