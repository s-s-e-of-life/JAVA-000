package q2;

import io.app.Money;
import io.app.StoreMonyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Proxy;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring_*.xml"})
public class MoneyTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    StoreMonyService monyService;

    @Autowired
    Money money;

    @Test
    public void get() {
        System.out.println(money.getTotal());
    }

    @Test
    public void fetch() {
        monyService.regist(1);

        monyService.store(1,1000);

        System.out.println("Fetched : "+monyService.fetch(1));
    }

}
