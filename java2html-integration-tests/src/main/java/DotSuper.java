import java.util.Iterator;

class Super {

    public  String get(Object o) {
        return "";
    }
}

public class DotSuper extends Super {
    
        public String fn() {
            Iterator it = null;
            return DotSuper.super.get(it.next());
        }

}

