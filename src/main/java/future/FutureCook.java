package future;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 用future模式实现上一个例子
 * @author dingzhaolei
 * @date 2019/2/25 21:07
 **/
public class FutureCook {
    private static final Logger LOGGER = LoggerFactory.getLogger(FutureCook.class);

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();
        Callable<Kitchenware> callable = new Callable<Kitchenware>() {
            public Kitchenware call() throws Exception {
                LOGGER.info("网购开始");
                LOGGER.info("等待送货");
                try {
                    Thread.sleep(2000);
                }catch (InterruptedException e){
                    LOGGER.error(e.toString());
                }
                LOGGER.info("已收到货");
                Kitchenware kitchenware = new Kitchenware();
                return kitchenware;
            }
        };

        FutureTask<Kitchenware> futureTask = new FutureTask<Kitchenware>(callable);
        new Thread(futureTask).start();

        //开始买菜
        Thread.sleep(2000);
        Ingredient ingredient = new Ingredient();
        if (futureTask.isDone()){
            LOGGER.info("货已到");
            cook(ingredient,futureTask.get());
        }else {
            LOGGER.info("再等一下");
            cook(ingredient,futureTask.get());
        }

        long endTime = System.currentTimeMillis();
        LOGGER.info("总共用时:{}",endTime-startTime);
        /**
         * 相比上个例子节省了大概1000毫秒
         */
    }

    static void cook(Ingredient ingredient,Kitchenware kitchenware){
        LOGGER.info("做饭");
    }

}

