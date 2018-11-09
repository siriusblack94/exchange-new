package com.blockeng.mining.service.impl;



import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.mining.entity.MiningDetail;
import com.blockeng.mining.mapper.MiningDetailMapper;
import com.blockeng.mining.service.MiningDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>
 * 挖矿统计
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */

@Service
@Slf4j
@Transactional
public class MiningDetailServiceImpl extends ServiceImpl<MiningDetailMapper, MiningDetail> implements MiningDetailService {





}
