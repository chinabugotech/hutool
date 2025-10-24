//package cn.hutool.v7.core.thread;
//
//import java.util.*;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.function.Supplier;
//
///**
// * ScopedValue 异步传播工具类
// * <p>
// * 用于在异步任务（CompletableFuture）中传播 JDK 25+ 的 ScopedValue，
// * 解决 ScopedValue 在跨线程时值丢失的问题。
// * </p>
// *
// * <h3>使用示例：</h3>
// * <pre>{@code
// * ScopedValue<String> USER_ID = ScopedValue.newInstance();
// *
// * ScopedValue.where(USER_ID, "user123").run(() -> {
// *     // 异步任务中自动传播 USER_ID
// *     ScopeValueUtil.supplyAsync(USER_ID, () -> {
// *         return "Processing for: " + USER_ID.get();
// *     }).thenAccept(System.out::println);
// * });
// * }</pre>
// *
// * @author Toint
// * @date 2025/10/21
// */
//public class ScopeValueUtil {
//
//    /**
//     * 虚拟线程执行器，用于执行异步任务
//     */
//    private static final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
//
//    /**
//     * 在异步任务中传播单个 ScopedValue（无返回值）
//     * <p>
//     * 捕获当前线程中指定 ScopedValue 的值，并在异步任务中恢复该值的绑定。
//     * </p>
//     *
//     * @param scopedValue 需要传播的 ScopedValue
//     * @param runnable    异步执行的任务
//     * @param <V>         ScopedValue 的值类型
//     * @return CompletableFuture<Void> 异步任务的 Future 对象
//     */
//    public static <V> CompletableFuture<Void> runAsync(ScopedValue<V> scopedValue, Runnable runnable) {
//        return runAsync(Collections.singletonList(scopedValue), runnable);
//    }
//
//    /**
//     * 在异步任务中传播单个 ScopedValue（有返回值）
//     * <p>
//     * 捕获当前线程中指定 ScopedValue 的值，并在异步任务中恢复该值的绑定。
//     * </p>
//     *
//     * @param scopedValue 需要传播的 ScopedValue
//     * @param supplier    异步执行的任务，返回结果
//     * @param <V>         ScopedValue 的值类型
//     * @param <R>         异步任务的返回值类型
//     * @return CompletableFuture<R> 异步任务的 Future 对象，包含任务执行结果
//     */
//    public static <V, R> CompletableFuture<R> supplyAsync(ScopedValue<V> scopedValue, Supplier<R> supplier) {
//        return supplyAsync(Collections.singletonList(scopedValue), supplier);
//    }
//
//    /**
//     * 在异步任务中传播多个 ScopedValue（无返回值）
//     * <p>
//     * 捕获当前线程中多个 ScopedValue 的值，并在异步任务中恢复这些值的绑定。
//     * 只有已绑定的 ScopedValue 会被传播，未绑定的会被忽略。
//     * </p>
//     *
//     * @param scopedValues 需要传播的 ScopedValue 列表
//     * @param runnable     异步执行的任务
//     * @param <V>          ScopedValue 的值类型
//     * @return CompletableFuture<Void> 异步任务的 Future 对象
//     */
//    public static <V> CompletableFuture<Void> runAsync(List<ScopedValue<V>> scopedValues, Runnable runnable) {
//        Map<ScopedValue<V>, V> scopedValueMap = captureContext(scopedValues);
//        return runAsync(scopedValueMap, runnable);
//    }
//
//    /**
//     * 在异步任务中传播多个 ScopedValue（有返回值）
//     * <p>
//     * 捕获当前线程中多个 ScopedValue 的值，并在异步任务中恢复这些值的绑定。
//     * 只有已绑定的 ScopedValue 会被传播，未绑定的会被忽略。
//     * </p>
//     *
//     * @param scopedValues 需要传播的 ScopedValue 列表
//     * @param supplier     异步执行的任务，返回结果
//     * @param <V>          ScopedValue 的值类型
//     * @param <R>          异步任务的返回值类型
//     * @return CompletableFuture<R> 异步任务的 Future 对象，包含任务执行结果
//     */
//    public static <V, R> CompletableFuture<R> supplyAsync(List<ScopedValue<V>> scopedValues, Supplier<R> supplier) {
//        Map<ScopedValue<V>, V> scopedValueMap = captureContext(scopedValues);
//        return supplyAsync(scopedValueMap, supplier);
//    }
//
//    /**
//     * 在异步任务中传播 ScopedValue 上下文（有返回值）
//     * <p>
//     * 根据提供的 ScopedValue 上下文映射，在异步任务中恢复这些值的绑定。
//     * 这是最底层的传播方法，其他 supplyAsync 方法最终都会调用此方法。
//     * </p>
//     *
//     * @param scopedValueMap ScopedValue 到其值的映射，表示需要传播的上下文
//     * @param supplier       异步执行的任务，返回结果
//     * @param <V>            ScopedValue 的值类型
//     * @param <R>            异步任务的返回值类型
//     * @return CompletableFuture<R> 异步任务的 Future 对象，包含任务执行结果
//     * @throws NullPointerException 如果 supplier 为 null
//     */
//    public static <V, R> CompletableFuture<R> supplyAsync(Map<ScopedValue<V>, V> scopedValueMap, Supplier<R> supplier) {
//        Objects.requireNonNull(supplier, "supplier must not be null");
//
//        if (scopedValueMap == null || scopedValueMap.isEmpty()) {
//            return CompletableFuture.supplyAsync(supplier, executorService);
//        }
//
//        return CompletableFuture.supplyAsync(() -> {
//            ScopedValue.Carrier carrier = buildCarrier(scopedValueMap);
//            return carrier.call(supplier::get);
//        }, executorService);
//    }
//
//    /**
//     * 在异步任务中传播 ScopedValue 上下文（无返回值）
//     * <p>
//     * 根据提供的 ScopedValue 上下文映射，在异步任务中恢复这些值的绑定。
//     * 这是最底层的传播方法，其他 runAsync 方法最终都会调用此方法。
//     * </p>
//     *
//     * @param scopedValueMap ScopedValue 到其值的映射，表示需要传播的上下文
//     * @param runnable       异步执行的任务
//     * @param <V>            ScopedValue 的值类型
//     * @return CompletableFuture<Void> 异步任务的 Future 对象
//     * @throws NullPointerException 如果 runnable 为 null
//     */
//    public static <V> CompletableFuture<Void> runAsync(Map<ScopedValue<V>, V> scopedValueMap, Runnable runnable) {
//        Objects.requireNonNull(runnable, "runnable must not be null");
//
//        return supplyAsync(scopedValueMap, () -> {
//            runnable.run();
//            return null;
//        });
//    }
//
//    /**
//     * 构建 ScopedValue.Carrier 对象
//     * <p>
//     * Carrier 用于在新的执行上下文中批量绑定多个 ScopedValue。
//     * 通过链式调用 where() 方法构建包含所有绑定的 Carrier。
//     * </p>
//     *
//     * @param scopedValueVMap ScopedValue 到其值的映射
//     * @param <V>             ScopedValue 的值类型
//     * @return ScopedValue.Carrier 对象，如果映射为空则返回 null
//     */
//    public static <V> ScopedValue.Carrier buildCarrier(Map<ScopedValue<V>, V> scopedValueVMap) {
//        if (scopedValueVMap == null || scopedValueVMap.isEmpty()) return null;
//
//        ScopedValue.Carrier carrier = null;
//        for (Map.Entry<ScopedValue<V>, V> entry : scopedValueVMap.entrySet()) {
//            if (carrier == null) {
//                carrier = ScopedValue.where(entry.getKey(), entry.getValue());
//            } else {
//                carrier = carrier.where(entry.getKey(), entry.getValue());
//            }
//        }
//        return carrier;
//    }
//
//    /**
//     * 捕获当前线程中已绑定的 ScopedValue
//     * <p>
//     * 遍历指定的 ScopedValue 列表，提取所有在当前线程中已绑定的值，
//     * 构建成 Map 以便后续在异步线程中恢复。未绑定或为 null 的 ScopedValue 会被忽略。
//     * </p>
//     *
//     * @param scopedValues 需要捕获的 ScopedValue 列表
//     * @param <V>          ScopedValue 的值类型
//     * @return Map<ScopedValue<V>, V> 包含已绑定值的映射，如果没有绑定值则返回空 Map
//     */
//    public static <V> Map<ScopedValue<V>, V> captureContext(List<ScopedValue<V>> scopedValues) {
//        Map<ScopedValue<V>, V> scopedValueMap = new HashMap<>();
//        if (scopedValues != null && !scopedValues.isEmpty()) {
//            scopedValues.forEach(scopedValue -> {
//                if (scopedValue != null && scopedValue.isBound()) {
//                    scopedValueMap.put(scopedValue, scopedValue.get());
//                }
//            });
//        }
//        return scopedValueMap;
//    }
//
//    /**
//     * 安全获取 ScopedValue 的绑定值
//     * <p>
//     * 与 {@link ScopedValue#get()} 不同，此方法在 ScopedValue 未绑定时返回 null，
//     * 而不是抛出 {@link NoSuchElementException} 异常。适用于需要容错处理的场景。
//     * </p>
//     *
//     * <h3>使用场景：</h3>
//     * <ul>
//     *     <li>不确定 ScopedValue 是否已绑定时</li>
//     *     <li>需要优雅处理未绑定情况，避免异常</li>
//     *     <li>可选的上下文传递场景</li>
//     * </ul>
//     *
//     * <h3>使用示例：</h3>
//     * <pre>{@code
//     * ScopedValue<String> USER_ID = ScopedValue.newInstance();
//     *
//     * // 安全获取，不会抛异常
//     * String userId = ScopeValueUtil.getOrNull(USER_ID);
//     * if (userId != null) {
//     *     System.out.println("User ID: " + userId);
//     * }
//     * }</pre>
//     *
//     * @param scopedValue 需要获取值的 ScopedValue，允许为 null
//     * @param <V>         ScopedValue 的值类型
//     * @return 已绑定的值，如果未绑定或 scopedValue 为 null 则返回 null
//     * @see ScopedValue#get()
//     * @see ScopedValue#isBound()
//     */
//    public static <V> V getOrNull(ScopedValue<V> scopedValue) {
//        if (scopedValue != null && scopedValue.isBound()) {
//            return scopedValue.get();
//        } else {
//            return null;
//        }
//    }
//
//}
