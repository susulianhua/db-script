package org.tinygroup.cepcore.test;

import junit.framework.TestCase;
import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.cepcore.impl.AbstractEventProcessor;
import org.tinygroup.cepcore.impl.WeightChooser;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceInfo;

import java.util.ArrayList;
import java.util.List;

public class ChooseTest extends TestCase {

    public void testChoose() {
        WeightChooser c = new WeightChooser();
        List<EventProcessor> list = new ArrayList<EventProcessor>();
        list.add(new Eventprocessorfortest(10));
        list.add(new Eventprocessorfortest(-10));
        list.add(new Eventprocessorfortest(-20));
        int count1 = 0;
        int count2 = 0;
        int count3 = 0;
        for (int i = 0; i < 1000; i++) {
            EventProcessor p = c.choose(list);
            if (p.getWeight() == 10) {
                count1++;
            } else if (p.getWeight() == -10) {
                count2++;
                fail();
            } else if (p.getWeight() == -20) {
                count3++;
                fail();
            }
        }

        System.out.println("total:" + 1000);
        System.out.println(" 10:" + count1);
        System.out.println("-10:" + count2);
        System.out.println("-20:" + count3);
        System.out.println("-----------");
        assertTrue(1000 == count1);
        assertTrue(0 == count2);
        assertTrue(0 == count3);
    }

    public void testChoose2() {
        WeightChooser c = new WeightChooser();
        List<EventProcessor> list = new ArrayList<EventProcessor>();
        list.add(new Eventprocessorfortest(-10));
        list.add(new Eventprocessorfortest(-20));
        int count1 = 0;
        int count2 = 0;
        for (int i = 0; i < 1000; i++) {
            EventProcessor p = c.choose(list);
            if (p.getWeight() == -10) {
                count1++;
            } else {
                count2++;
            }
        }

        System.out.println("total:" + 1000);
        System.out.println("-10:" + count1);
        System.out.println("-20:" + count2);
        System.out.println("-----------");
        assertTrue(count2 != 0);
        assertTrue(count2 > 500);
        assertTrue(count1 != 0);
        assertTrue(count1 > 200);
    }

    public void testChoose3() {
        WeightChooser c = new WeightChooser();
        List<EventProcessor> list = new ArrayList<EventProcessor>();
        list.add(new Eventprocessorfortest(10));
        list.add(new Eventprocessorfortest(20));
        list.add(new Eventprocessorfortest(-20));
        int count1 = 0;
        int count2 = 0;
        int count3 = 0;
        for (int i = 0; i < 1000; i++) {
            EventProcessor p = c.choose(list);
            if (p.getWeight() == 10) {
                count1++;
            } else if (p.getWeight() == 20) {
                count2++;
            } else if (p.getWeight() == -20) {
                count3++;
            }
        }

        System.out.println("total:" + 1000);
        System.out.println(" 10:" + count1);
        System.out.println(" 20:" + count2);
        System.out.println("-20:" + count3);
        System.out.println("-----------");
        assertTrue(count2 != 0);
        assertTrue(count2 > 500);
        assertTrue(count1 != 0);
        assertTrue(count1 > 200);
        assertTrue(count3 == 0);
    }

    class Eventprocessorfortest extends AbstractEventProcessor {

        private int weight;

        public Eventprocessorfortest(int weight) {
            super();
            this.weight = weight;
            System.out.println("create wight:" + weight);
        }

        public void process(Event event) {

        }

        public void setCepCore(CEPCore cepCore) {

        }

        public List<ServiceInfo> getServiceInfos() {
            return null;
        }

        @Override
        public String getId() {
            return null;
        }

        @Override
        public int getType() {
            return 0;
        }

        public int getWeight() {

            return weight;
        }

        public List<String> getRegex() {
            return null;
        }

        public boolean isRead() {
            return false;
        }

        public void setRead(boolean read) {

        }

        public boolean isEnable() {
            return false;
        }

        public void setEnable(boolean enable) {

        }
    }
}
