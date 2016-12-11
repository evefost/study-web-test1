package impl;


import com.big.data.entity.TestBean;
import com.big.data.service.TestService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * TestServiceImpl Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十一月 27, 2016</pre>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/application.xml"})
public class TestServiceImplTest {

    @Autowired
    TestService testService;

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getVersion()
     */
    @Test
    public void testGetVersion() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: getNameById(Integer id)
     */
    @Test
    public void testGetNameById() throws Exception {
        TestBean nameById = testService.getNameById(1);
        System.out.print("nameById:" + nameById);

    }


}