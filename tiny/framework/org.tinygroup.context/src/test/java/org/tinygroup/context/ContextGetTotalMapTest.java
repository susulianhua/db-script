package org.tinygroup.context;

import junit.framework.TestCase;
import org.tinygroup.context.util.ContextFactory;

public class ContextGetTotalMapTest extends TestCase{

    public void testTotalMap(){
        Context c = ContextFactory.getContext();
        c.put("c","c");

        Context c1 = ContextFactory.getContext();
        c1.put("c1","c1");

        Context c2 = ContextFactory.getContext();
        c2.put("c2","c2");

        Context cp = ContextFactory.getContext();
        cp.put("cp","cp");

        Context cs1 = ContextFactory.getContext();
        cs1.put("cs1","cs1");

        Context cs2 = ContextFactory.getContext();
        cs2.put("cs2","cs2");

        Context cs11 = ContextFactory.getContext();
        cs11.put("cs11","cs11");

        Context cs111 = ContextFactory.getContext();
        cs111.put("cs111","cs111");

        Context cs12 = ContextFactory.getContext();
        cs12.put("cs12","cs12");

        cp.putSubContext("c",c);
        cp.putSubContext("c1",c1);
        cp.putSubContext("c2",c2);
        c.setParent(cp);
        c1.setParent(cp);
        c2.setParent(cp);
        c.putSubContext("cs1",cs1);
        c.putSubContext("cs2",cs2);
        cs1.putSubContext("cs12",cs12);
        cs1.putSubContext("cs11",cs11);
        cs11.putSubContext("cs111",cs111);

        assertEquals(9,c.getTotalItemMap().size());
    }

    public void testTotalMap2(){
        Context c = ContextFactory.getContext();
        Context c1 = ContextFactory.getContext();
        c1.put("c","c1");
        Context c2 = ContextFactory.getContext();
        c2.put("c","c2");
        Context cp = ContextFactory.getContext();
        cp.put("c","cp");
        Context cs1 = ContextFactory.getContext();
        cs1.put("c","cs1");
        Context cs2 = ContextFactory.getContext();
        cs2.put("c","cs2");
        Context cs11 = ContextFactory.getContext();
        cs11.put("c","cs11");
        Context cs111 = ContextFactory.getContext();
        cs111.put("c","cs111");
        Context cs12 = ContextFactory.getContext();
        cs12.put("c","cs12");

        cp.putSubContext("c",c);
        cp.putSubContext("c1",c1);
        cp.putSubContext("c2",c2);
        c.setParent(cp);
        c1.setParent(cp);
        c2.setParent(cp);
        c.putSubContext("cs1",cs1);
        c.putSubContext("cs2",cs2);
        cs1.putSubContext("cs12",cs12);
        cs1.putSubContext("cs11",cs11);
        cs11.putSubContext("cs111",cs111);

        assertEquals(1,c.getTotalItemMap().size());
        System.out.println(c.getTotalItemMap().get("c"));
        assertTrue("cs1".equals(c.getTotalItemMap().get("c"))||"cs2".equals(c.getTotalItemMap().get("c")));
    }


    public void testTotalMap3(){
        Context c = ContextFactory.getContext();
        c.put("c","c");
        Context c1 = ContextFactory.getContext();
        c1.put("c","c1");
        Context c2 = ContextFactory.getContext();
        c2.put("c","c2");
        Context cp = ContextFactory.getContext();
        cp.put("c","cp");
        Context cs1 = ContextFactory.getContext();
        cs1.put("c","cs1");
        Context cs2 = ContextFactory.getContext();
        cs2.put("c","cs2");
        Context cs11 = ContextFactory.getContext();
        cs11.put("c","cs11");
        Context cs111 = ContextFactory.getContext();
        cs111.put("c","cs111");
        Context cs12 = ContextFactory.getContext();
        cs12.put("c","cs12");
        cp.putSubContext("c",c);
        cp.putSubContext("c1",c1);
        cp.putSubContext("c2",c2);
        c.setParent(cp);
        c1.setParent(cp);
        c2.setParent(cp);
        c.putSubContext("cs1",cs1);
        c.putSubContext("cs2",cs2);
        cs1.putSubContext("cs12",cs12);
        cs1.putSubContext("cs11",cs11);
        cs11.putSubContext("cs111",cs111);

        assertEquals(1,c.getTotalItemMap().size());
        assertEquals("c",c.getTotalItemMap().get("c"));
    }
}
