/**
 * @author Lzpeng723
 * @description $END$
 * @since 2025/12/11 16:39
 */
public record C() {
    public C {
        new B(C.class.getClassLoader());
    }
}
