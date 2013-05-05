package dz.testlinks;

import static dz.testlinks.SomeThingA.*;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: jps
 * Date: 15-Jul-2007
 * Time: 20:17:13
 * To change this template use File | Settings | File Templates.
 */
public class SomeThingB  extends SomeThingC {

    @AnAnotation
    public SomeThingA function(SomeThingA a, SomeThingB b, SomeThingC c, SomeThingA a2) throws AnException {
        SomeThingC c2 = new SomeThingC();
        SomeThingA some = new SomeThingA( new SomeThingC());

        String s = SomeThingA.STRING;

        SomeThingB aCast = (SomeThingB)c2;

        Class clzz = SomeThingA.class;
        return some;

    }


    public java.util.Map.Entry<SomeThingA, SomeThingB> function2() {
        return null;
    }
}
