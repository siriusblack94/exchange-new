package com.blockeng.mining.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.mining.entity.PlantCoinDividendAccount;
import com.blockeng.mining.mapper.PlantCoinDividendAccountMapper;
import com.blockeng.mining.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class PlantCoinDividendAccountServiceImpl extends ServiceImpl<PlantCoinDividendAccountMapper, PlantCoinDividendAccount> implements PlantCoinDividendAccounttService {

}