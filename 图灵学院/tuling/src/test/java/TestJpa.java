import com.data.jpa.po.CstCustomerPO;
import com.data.jpa.repository.CstCustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-07-10 13:30
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
public class TestJpa {

    @Autowired
    private CstCustomerRepository cstCustomerRepository;

    @Test
    public void insert () {
        System.out.println(1);
    }

}
