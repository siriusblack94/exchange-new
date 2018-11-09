import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.dto.CoinRechargeDTO;
import com.blockeng.admin.entity.CoinRecharge;
import com.blockeng.admin.service.CoinRechargeService;
import com.blockeng.admin.web.finance.CoinRechargeController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {TestCoinRecharge.class, CoinRechargeController.class, Config.class })
public class TestCoinRecharge {

    @Autowired
    CoinRechargeController coinRechargeController;

    @Autowired
    HttpServletResponse response;

    @MockBean
    CoinRechargeService coinRechargeService;

    @MockBean
    Page<CoinRechargeDTO> pager;

    @Before
    public void before(){
        CoinRechargeDTO coinRecharge = new CoinRechargeDTO();
        coinRecharge.setCoinId(1L);
        List<CoinRechargeDTO> list = List.of(coinRecharge);
        Mockito.when(pager.getRecords()).thenReturn(list);
        Mockito.when(coinRechargeService.selectMapPage(Mockito.any(),Mockito.any())).thenReturn(pager);
    }
    @Test
    public void testA(){

//        CoinRechargeService service = Mockito.mock(CoinRechargeService.class);
//        Mockito.when(service.selectMapPage(null,null)).then();

        coinRechargeController.exportCoinRecharge(response,null,null,
                null,null,null,null,null,null,null);

    }
}
