package richard.test.jni;

/**
 * Created by richard on 04/12/2018.
 */
public class TestJNI
{
    //加载动态链接库
    static {
        System.out.println("开始加载动态链接库");
        System.loadLibrary("MyJNI");
        System.out.println("动态链接库加载完毕。");
    }

    public native void go();

    public native void run();

    public native String getName();

    public native int[] sort(int[] array);

    //测试
    public static void main(String[] args) {
        TestJNI jni = new TestJNI();
        int[] array = {5, 3, 6, 35, 74, 8}, sortedArray;

        jni.run();
        jni.go();
        jni.getName();
        sortedArray = jni.sort(array);
        //由于这是本地方法调用，这里的数组和平常的数组的引用不太一样。

        for(int i = 0; i < sortedArray.length; i++){
            System.out.print(sortedArray[i] + "\t");
        }

    }
}