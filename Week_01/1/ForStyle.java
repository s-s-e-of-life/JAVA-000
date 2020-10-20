public class ForStyle {
    /**
     * 实际发现,字节码执行并没有太大的区别
     * 
     * 若从优雅角度,还是for1更好
     * 
     */
    public static void main(String[] args) {
        
        for1();
        for2();
    }

    public static void for1() {
        for(int i=0;i<5;i++){
            Object o = new Object();
            System.out.printf("" + o.hashCode());
        }
    }

    public static void for2() {
        Object o = null;
        for(int i=0;i<5;i++){
            o = new Object();
            System.out.printf("" + o.hashCode());
        }
    }
}