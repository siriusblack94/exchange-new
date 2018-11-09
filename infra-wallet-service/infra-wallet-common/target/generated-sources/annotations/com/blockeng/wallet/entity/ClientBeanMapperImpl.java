package com.blockeng.wallet.entity;

import com.clg.wallet.bean.ClientBean;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2018-10-09T01:17:40+0800",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_171 (Oracle Corporation)"
)
public class ClientBeanMapperImpl implements ClientBeanMapper {

    @Override
    public ClientBean form(CoinConfig coinConfig) {
        if ( coinConfig == null ) {
            return null;
        }

        ClientBean clientBean = new ClientBean();

        clientBean.setId( coinConfig.getId() );
        clientBean.setName( coinConfig.getName() );
        clientBean.setCoinType( coinConfig.getCoinType() );
        clientBean.setCreditLimit( coinConfig.getCreditLimit() );
        clientBean.setRpcIp( coinConfig.getRpcIp() );
        clientBean.setRpcPort( coinConfig.getRpcPort() );
        clientBean.setRpcUser( coinConfig.getRpcUser() );
        clientBean.setRpcPwd( coinConfig.getRpcPwd() );
        clientBean.setRpcIpOut( coinConfig.getRpcIpOut() );
        clientBean.setRpcPortOut( coinConfig.getRpcPortOut() );
        clientBean.setRpcUserOut( coinConfig.getRpcUserOut() );
        clientBean.setRpcPwdOut( coinConfig.getRpcPwdOut() );
        clientBean.setLastBlock( coinConfig.getLastBlock() );
        clientBean.setWalletUser( coinConfig.getWalletUser() );
        clientBean.setWalletUserOut( coinConfig.getWalletUserOut() );
        clientBean.setContractAddress( coinConfig.getContractAddress() );
        clientBean.setMinConfirm( coinConfig.getMinConfirm() );
        clientBean.setWalletPass( coinConfig.getWalletPass() );
        clientBean.setWalletPassOut( coinConfig.getWalletPassOut() );
        clientBean.setContext( coinConfig.getContext() );
        clientBean.setTask( coinConfig.getTask() );
        clientBean.setStatus( coinConfig.getStatus() );

        return clientBean;
    }
}
