import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 * <p>
 * 一个简单的代码参考：
 */
public class Homework03 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        long start = System.currentTimeMillis();
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法

        Abs task = new M2();

        // 确保  拿到result 并输出
        System.out.println("异步计算结果为：" + task.getResult());

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");

        // 然后退出main线程
    }
}

class Md {
    public static int sum() {
        return fibo(36);
    }

    public static int fibo(int a) {
        if (a < 2) {
            return 1;
        }
        return fibo(a - 1) + fibo(a - 2);
    }
}

abstract class Abs {
    /**
     * 获取结果
     * @return
     * @throws InterruptedException
     */
   public abstract Integer getResult() throws InterruptedException, ExecutionException;
}

class M1 extends Abs {

    private Integer val = null;

    /**
     * 获取结果
     *
     * @return
     * @throws InterruptedException
     */
    @Override
    public Integer getResult() throws InterruptedException, ExecutionException {

        new Thread(() -> {
            val = Md.sum();
        }).start();

        // 自旋检测
        while(null == val) {
            System.out.println("wating ...");
        }

        return val;
    }
}

class M2 extends Abs {
    /**
     * 获取结果
     *
     * @return
     * @throws InterruptedException
     */
    @Override
    public Integer getResult() throws InterruptedException, ExecutionException {
        // 通过阻塞队列实现
        LinkedBlockingDeque<Integer> queue = new LinkedBlockingDeque(1);
        new Thread(() -> {
            queue.add(Md.sum());
        }).start();
        // 如果没有则直接阻塞在此处,直到有新的数据生成
        return queue.take();
    }
}

class M3 extends Abs {

    @Override
    public Integer getResult() {
        ExecutorService pool = Executors.newCachedThreadPool();

        // 线程池返回Future来实现
        Future<Integer> ft = pool.submit(() -> Md.sum());
        while (!ft.isDone()) {
            System.out.println("wating...");
        }
        int result;
        try {
            result = ft.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            result = 0;
        }
        pool.shutdown();
        return result;
    }
}

class M4 extends Abs {
    private static Integer m2result = null;

    /**
     * 获取结果
     *
     * @return
     * @throws InterruptedException
     */
    @Override
    public Integer getResult() throws InterruptedException, ExecutionException {
        Thread t = new Thread(() -> {
            m2result = Md.sum();
        });
        t.start();

        // 通过join实现等待
        t.join();
        return m2result;
    }
}

class M5 extends Abs {

    /**
     * 获取结果
     *
     * @return
     * @throws InterruptedException
     */
    @Override
    public Integer getResult() throws InterruptedException, ExecutionException {
        FutureTask<Integer> futureTask = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return Md.sum();
            }
        });

        new Thread(futureTask).start();

        // 本质上还是自旋检查
        while (!futureTask.isDone()) {
            System.out.println("wating...");
        }

        return futureTask.get();
    }
}

class M6 extends Abs {

    /**
     * 获取结果
     *
     * @return
     * @throws InterruptedException
     */
    @Override
    public Integer getResult() throws InterruptedException, ExecutionException {
        ExecutorService pool = Executors.newCachedThreadPool();
        FutureTask<Integer> ft = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return Md.sum();
            }
        });
        pool.submit(ft);
        while (!ft.isDone()) {
            System.out.println("wating...");
        }
        int result;
        try {
            result = ft.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            result = 0;
        }
        pool.shutdown();
        return result;
    }
}

class M7 extends Abs {

    /**
     * 获取结果
     *
     * @return
     * @throws InterruptedException
     */
    @Override
    public Integer getResult() throws InterruptedException, ExecutionException {
        return CompletableFuture.supplyAsync(() -> Md.sum()).join();
    }
}

class M8 extends Abs {

    private Integer val;

    /**
     * 获取结果
     *
     * @return
     * @throws InterruptedException
     */
    @Override
    public Integer getResult() throws InterruptedException, ExecutionException {

        // 计数实现等待
        CountDownLatch count = new CountDownLatch(1);

        new Thread(() -> {
            val = Md.sum();
            count.countDown();
        }).start();

        System.out.println("start waiting .");
        count.await();
        return val;
    }
}
