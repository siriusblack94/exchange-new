package com.blockeng.wallet.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.clg.wallet.bean.ResultDTO;
import com.blockeng.wallet.dto.WalletResultDTO;
import com.blockeng.wallet.entity.ClientBeanMapper;
import com.blockeng.wallet.entity.CoinConfig;
import com.clg.wallet.enums.ResultCode;
import com.blockeng.wallet.exception.CoinException;
import com.blockeng.wallet.mapper.CoinConfigMapper;
import com.clg.wallet.newclient.Client;
import com.clg.wallet.newclient.ClientFactory;
import com.blockeng.wallet.service.CoinConfigService;
import com.blockeng.wallet.utils.DESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 币种配置信息 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Service
@Transactional
public class CoinConfigServiceImpl extends ServiceImpl<CoinConfigMapper, CoinConfig> implements CoinConfigService {


    @Autowired
    private DESUtil desUtil;


    @Override
    public String lastBlock(Long id) {
        CoinConfig coinConfig = baseMapper.selectById(id);
        if (null != coinConfig) {
            return coinConfig.getLastBlock();
        }
        return "0";
    }

    @Override
    public List<CoinConfig> selectCoinFromType(String type) {
        EntityWrapper<CoinConfig> ew = new EntityWrapper<>();
        ew.eq("status", 1);
        if (!StringUtils.isEmpty(type)) {
            ew.eq("coin_type", type);
        }
        return super.selectList(ew);
    }

    @Override
    public List<CoinConfig> selectAllCoin() {
        EntityWrapper<CoinConfig> ew = new EntityWrapper<>();
        ew.eq("status", 1);
        return super.selectList(ew);
    }

    @Override
    public CoinConfig selectCoinFromId(Long coinId) {
        return super.selectById(coinId);
    }

    @Override
    public boolean updateCoinLastblock(String lastBlock, Long id) {
        return super.updateById(new CoinConfig().setId(id).setLastBlock(lastBlock));
    }

    @Override
    public WalletResultDTO updateCoinPass(String newPass, String oldPass, CoinConfig coinConfig) throws CoinException {
        Client client = ClientFactory.getClient(ClientBeanMapper.INSTANCE.form(coinConfig));
        super.updateById(new CoinConfig().setId(coinConfig.getId()).setWalletPass(desUtil.encrypt(newPass)));
        ResultDTO resultDTO = client.changePassword(oldPass, newPass);
        if (resultDTO.getStatusCode() == ResultCode.SUCCESS.getCode()) {
            return WalletResultDTO.successResult();
        } else {
            throw new CoinException("修改密码失败,code=" + resultDTO.getStatusCode() + ",原因:" + resultDTO.getResultDesc());
        }
    }

}
