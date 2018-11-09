package com.blockeng.admin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.admin.dto.CoinWithdrawRetryRecordDTO;
import com.blockeng.admin.entity.CoinWithdraw;
import com.blockeng.admin.entity.CoinWithdrawAuditRecord;
import com.blockeng.admin.entity.CoinWithdrawRetryRecord;
import com.blockeng.admin.mapper.CoinWithdrawRetryRecordMapper;
import com.blockeng.admin.service.CoinWithdrawAuditRecordService;
import com.blockeng.admin.service.CoinWithdrawRetryRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CoinWithdrawRetryRecordServiceImpl extends ServiceImpl<CoinWithdrawRetryRecordMapper, CoinWithdrawRetryRecord> implements CoinWithdrawRetryRecordService{


    @Autowired
    private CoinWithdrawAuditRecordService coinWithdrawAuditRecordService;

    @Override
    public Page<CoinWithdrawRetryRecordDTO> selectListPage(Page<CoinWithdrawRetryRecordDTO> page, Wrapper<CoinWithdrawRetryRecord> wrapper) {

        //查询一级审核
        List<CoinWithdrawRetryRecordDTO> coinWithdrawRetryRecordDTOS = baseMapper.selectListPage(page, wrapper);

        List<CoinWithdrawRetryRecordDTO> coinWithdrawRetryRecordDTOList=new ArrayList<>();
        //查询二级审核
        for (CoinWithdrawRetryRecordDTO coinWithdrawRetryRecordDTO : coinWithdrawRetryRecordDTOS) {

            EntityWrapper<CoinWithdrawAuditRecord> ew = new EntityWrapper<>();

            ew.eq("order_id",coinWithdrawRetryRecordDTO.getOrderId()).eq("step",2);

            CoinWithdrawAuditRecord coinWithdrawAuditRecord = coinWithdrawAuditRecordService.selectOne(ew);

            if (coinWithdrawAuditRecord!=null){

            coinWithdrawRetryRecordDTO.setSecondAuditUserId(coinWithdrawAuditRecord.getAuditUserId())
                                      .setSecondAuditUserName(coinWithdrawAuditRecord.getAuditUserName());
            }
            coinWithdrawRetryRecordDTOList.add(coinWithdrawRetryRecordDTO);
        }
        page.setRecords(coinWithdrawRetryRecordDTOList);

        return page;
    }
}



