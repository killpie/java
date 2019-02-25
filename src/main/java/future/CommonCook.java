package future;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 该文为转载 原文链接<href>https://www.cnblogs.com/cz123/p/7693064.html</href>
 * 彻底理解Java的Future模式
 *
 * 先上一个场景：假如你突然想做饭，但是没有厨具，也没有食材。网上购买厨具比较方便，食材去超市买更放心。
 *
 * 实现分析：在快递员送厨具的期间，我们肯定不会闲着，可以去超市买食材。所以，在主线程里面另起一个子线程去网购厨具。
 *
 * 但是，子线程执行的结果是要返回厨具的，而run方法是没有返回值的。所以，这才是难点，需要好好考虑一下。
 *
 * 模拟代码1：
 * @author dingzhaolei
 * @date 2019/2/25 15:24
 **/
public class CommonCook {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonCook.class);


    public static void main(String[] args) throws InterruptedException{
        long startTime = System.currentTimeMillis();

        OnlineShopping onlineShopping = new OnlineShopping();
        onlineShopping.start();
        //保证先买到厨具
        onlineShopping.join();

        //第二步买食材
        Thread.sleep(1000);
        Ingredient ingredient = new Ingredient();

        cook(ingredient,onlineShopping.kitchenware);
        long endTime = System.currentTimeMillis();
        LOGGER.info("总共用时：{}",endTime-startTime);
        /**
         * 可以看到，多线程已经失去了意义。在厨具送到期间，我们不能干任何事。对应代码，就是调用join方法阻塞主线程。
         *
         * 有人问了，不阻塞主线程行不行？？？
         *
         * 不行！！！
         *
         * 从代码来看的话，run方法不执行完，属性chuju就没有被赋值，还是null。换句话说，没有厨具，怎么做饭。
         *
         * Java现在的多线程机制，核心方法run是没有返回值的；如果要保存run方法里面的计算结果，必须等待run方法计算完，无论计算过程多么耗时。
         *
         * 面对这种尴尬的处境，程序员就会想：在子线程run方法计算的期间，能不能在主线程里面继续异步执行？？？
         *
         * Where there is a will，there is a way！！！
         *
         * 这种想法的核心就是Future模式，下面先应用一下Java自己实现的Future模式。
         *
         * 模拟代码2：
         */
    }

    static void cook(Ingredient ingredient,Kitchenware kitchenware){
        LOGGER.info("做饭");
    }

    static class OnlineShopping extends Thread{
        private static final Logger LOGGER = LoggerFactory.getLogger(OnlineShopping.class);
        private Kitchenware kitchenware;

        @Override
        public void run(){
            LOGGER.info("网购开始");
            LOGGER.info("等待送货");
            try {
                Thread.sleep(2000);
            }catch (InterruptedException e){
                LOGGER.error(e.toString());
            }
            LOGGER.info("已收到货");
            kitchenware = new Kitchenware();
        }
    }
}


//厨具类
class Kitchenware{
    @Getter
    @Setter
    //是否到货
    private String  aog;
}

//食材
class Ingredient{

}
