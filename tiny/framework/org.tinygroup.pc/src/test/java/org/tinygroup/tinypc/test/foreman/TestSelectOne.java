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
package org.tinygroup.tinypc.test.foreman;

import org.tinygroup.tinypc.Foreman;
import org.tinygroup.tinypc.JobCenter;
import org.tinygroup.tinypc.TestUtil;
import org.tinygroup.tinypc.Work;
import org.tinygroup.tinypc.impl.ForemanSelectOneWorker;
import org.tinygroup.tinypc.impl.JobCenterRemote;

import java.io.IOException;

public class TestSelectOne {
    public static void main(String[] args) {
        try {
            JobCenter jobCenter = new JobCenterRemote(TestUtil.CIP, TestUtil.CP, TestUtil.SIP, TestUtil.SP);
            Work work = new WorkTask("a", "aaa", "");
            Foreman f = new ForemanSelectOneWorker("a");
            jobCenter.registerForeman(f);
            jobCenter.doWork(work);
            jobCenter.unregisterForeMan(f);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
