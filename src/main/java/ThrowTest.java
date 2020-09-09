public class test {

    public static void main(String args[]) {
        int a = test();
        System.out.println("a=" + a);

    }

    private static int test() {
        try {
            do_throw();
        } catch (Exception ex) {

            System.out.println("hello");
            return -1;
        } finally {
            System.out.println("how are you?");
            return  -2;
        }

    }

    private static void do_throw() throws Exception {

        throw new Exception();
    }
}
