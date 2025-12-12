/**
 * @author Lzpeng723
 * @description $END$
 * @since 2025/12/11 16:38
 */
public record A(ClassLoader classLoader) {

    private record InnerClass(Runnable runnable) {
        public InnerClass {
            runnable.run();
        }

    }

    public A {
        new InnerClass(() -> {
            final StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            System.out.println("初始化 " + getClass() + " 的调用链为: ");
            for (int i = 0; i < stackTraceElements.length; i++) {
                final StackTraceElement stackTraceElement = stackTraceElements[i];
                try {
                    System.out.printf("%-2d: %-60s %s\r\n", i + 1, classLoader.loadClass(stackTraceElement.getClassName()).getClassLoader(), stackTraceElement);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
