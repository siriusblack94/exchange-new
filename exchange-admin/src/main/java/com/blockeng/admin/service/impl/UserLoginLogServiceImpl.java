package com.blockeng.admin.service.impl;

import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateTableStatement;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.dto.UserCountLoginDTO;
import com.blockeng.admin.entity.UserLoginLog;
import com.blockeng.admin.mapper.*;
import com.blockeng.admin.service.UserLoginLogService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.admin.service.UserService;
import com.blockeng.repository.UserLoginLogRepository;
import com.google.common.base.Strings;
import com.netflix.eureka.cluster.PeerEurekaNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.DateOperators;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * <p>
 * 用户登录日志 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Service
public class UserLoginLogServiceImpl extends ServiceImpl<UserLoginLogMapper, UserLoginLog> implements UserLoginLogService {

    @Autowired
    private UserLoginLogMapper userLoginLogMapper;

    @Autowired
    private TurnoverOrderMapper turnoverOrderMapper;

    @Autowired
    private CashRechargeMapper cashRechargeMapper;

    @Autowired
    private CashWithdrawalsMapper cashWithdrawalsMapper;

    @Autowired
    private CoinRechargeMapper coinRechargeMapper;

    @Autowired
    private CoinWithdrawMapper coinWithdrawMapper;
    /*
     *
     *  可用1个sql查出符合要求的数据  2018.8.30
     *
SELECT
	whole4.date,
	total,
	login,
	car,
	caw,
	cor,
	cow
FROM
	(
	SELECT
		total,
		whole3.date,
		login,
		car,
		caw,
		cor
	FROM
		(
		SELECT
			total,
			whole2.date,
			login,
			car,
			caw
		FROM
			(
			SELECT
				total,
				whole1.date,
				login,
				car
			FROM
				(
				SELECT
					total,
					whole.date,
					login
				FROM
					(
					SELECT
						count( user_id ) AS total,
						date
					FROM
						(
							( SELECT DISTINCT user_id, DATE_FORMAT( created, "%Y-%m-%d" ) AS date FROM coin_withdraw WHERE created BETWEEN '2018-08-23 00:00:00.0' AND '2018-08-24 00:00:00.0' ) UNION
							( SELECT DISTINCT user_id, DATE_FORMAT( created, "%Y-%m-%d" ) AS date FROM coin_recharge WHERE created BETWEEN '2018-08-23 00:00:00.0' AND '2018-08-24 00:00:00.0' ) UNION
							( SELECT DISTINCT user_id, DATE_FORMAT( created, "%Y-%m-%d" ) AS date FROM cash_withdrawals WHERE created BETWEEN '2018-08-23 00:00:00.0' AND '2018-08-24 00:00:00.0' ) UNION
							( SELECT DISTINCT user_id, DATE_FORMAT( created, "%Y-%m-%d" ) AS date FROM cash_recharge WHERE created BETWEEN '2018-08-23 00:00:00.0' AND '2018-08-24 00:00:00.0' )
						) f
					GROUP BY
						f.date
					ORDER BY
						f.date DESC
					) whole
					LEFT JOIN (
					SELECT
						count( user_id ) AS login,
						date
					FROM
						( SELECT DISTINCT user_id, DATE_FORMAT( login_time, "%Y-%m-%d" ) AS date FROM user_login_log WHERE login_time BETWEEN '2018-08-23 00:00:00.0' AND '2018-08-24 00:00:00.0' ) e
					GROUP BY
						e.date
					) log ON whole.date = log.date
				) whole1
				LEFT JOIN (
				SELECT
					count( user_id ) AS car,
					date
				FROM
					( SELECT DISTINCT user_id, DATE_FORMAT( created, "%Y-%m-%d" ) AS date FROM cash_recharge WHERE created BETWEEN '2018-08-23 00:00:00.0' AND '2018-08-24 00:00:00.0' ) d
				GROUP BY
					d.date
				ORDER BY
					d.date
				) car ON whole1.date = car.date
			) whole2
			LEFT JOIN (
			SELECT
				count( user_id ) AS caw,
				date
			FROM
				( SELECT DISTINCT user_id, DATE_FORMAT( created, "%Y-%m-%d" ) AS date FROM cash_withdrawals WHERE created BETWEEN '2018-08-23 00:00:00.0' AND '2018-08-24 00:00:00.0' ) c
			GROUP BY
				c.date
			ORDER BY
				c.date
			) caw ON whole2.date = caw.date
		) whole3
		LEFT JOIN (
		SELECT
			count( user_id ) AS cor,
			date
		FROM
			( SELECT DISTINCT user_id, DATE_FORMAT( created, "%Y-%m-%d" ) AS date FROM coin_recharge WHERE created BETWEEN '2018-08-23 00:00:00.0' AND '2018-08-24 00:00:00.0' ) b
		GROUP BY
			b.date
		ORDER BY
			b.date
		) cor ON whole3.date = cor.date
	) whole4
	LEFT JOIN (
	SELECT
		count( user_id ) AS cow,
		date
	FROM
		( SELECT DISTINCT user_id, DATE_FORMAT( created, "%Y-%m-%d" ) AS date FROM coin_withdraw WHERE created BETWEEN '2018-08-23 00:00:00.0' AND '2018-08-24 00:00:00.0' ) a
	GROUP BY
		a.date
	ORDER BY
		a.date
	) cow ON whole4.date = cow.date

     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     */

    @Override
    public Page<UserCountLoginDTO> selectLoginCountPage(Page<UserCountLoginDTO> page, Wrapper<UserCountLoginDTO> wrapper) {
        wrapper = (Wrapper<UserCountLoginDTO>) SqlHelper.fillWrapper(page, wrapper);
        List<UserCountLoginDTO> countRegDTOList = this.userLoginLogMapper.selectLoginCountPage(page, wrapper);
        for (UserCountLoginDTO countDTO : countRegDTOList) {
            //登陆人数
            countDTO.setLoginNum(userLoginLogMapper.countloginNumByDate(countDTO.getLoginDate()));
            String idStr = userLoginLogMapper.selectUserIdStrsBydate(countDTO.getLoginDate());
            if (!Strings.isNullOrEmpty(idStr)) {
                //参与交易人数
                countDTO.setTradeNum(turnoverOrderMapper.countTradeByDateAndUidStrs(countDTO.getLoginDate(), idStr));
                //充值人数
                Integer rechargeNum = cashRechargeMapper.countByDateAndUidStrs(
                        countDTO.getLoginDate(), idStr, -1);//-1表示不限状态
                countDTO.setRechargeNum(rechargeNum);
                //提现人数
                Integer withdrawaNum = cashWithdrawalsMapper.countByDateAndUidStrs(
                        countDTO.getLoginDate(), idStr, -1);
                countDTO.setWithdrawNum(withdrawaNum);
                //充币人数
                Integer rechargeCoinNum = coinRechargeMapper.countByDateAndUidStrs(
                        countDTO.getLoginDate(), idStr, -1);
                countDTO.setRechargeCoinNum(rechargeCoinNum);
                //提币人数
                Integer withdrawCoinNum = coinWithdrawMapper.countByDateAndUidStrs(
                        countDTO.getLoginDate(), idStr, -1);
                countDTO.setWithdrawCoinNum(withdrawCoinNum);

            } else {
                countDTO.setRechargeCoinNum(0);
                countDTO.setRechargeNum(0);
                countDTO.setTradeNum(0);
                countDTO.setWithdrawCoinNum(0);
                countDTO.setWithdrawNum(0);
            }
        }
        page.setRecords(countRegDTOList);
        return page;
    }

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public org.springframework.data.domain.Page<com.blockeng.repository.UserLoginLog> selectListFromMongo(int current, int size, String startTime, String endTime) {

        org.springframework.data.domain.Page result = null;
        if (!Strings.isNullOrEmpty(endTime)) {
            endTime = endTime + " 23:59:59";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        int c = (current - 1) * size;
        Query query = new Query();
        query.skip(c).limit(size);
        Query countQuery = new Query();
        try {
            query.addCriteria(Criteria.where("login_timestamp").gt(format.parse(startTime).getTime()).lt(format.parse(endTime).getTime()));
            countQuery.addCriteria(Criteria.where("login_timestamp").gt(format.parse(startTime).getTime()).lt(format.parse(endTime).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<com.blockeng.repository.UserLoginLog> list = mongoTemplate.find(query, com.blockeng.repository.UserLoginLog.class, "user_login_log");
        long t = mongoTemplate.count(countQuery, UserLoginLog.class, "user_login_log");
        result = new PageImpl(list, PageRequest.of(current - 1, size), t);
        return result;
    }

//    @Override
//    public Page selectListCountFromMongo(String startTime, String endTime) {
//        List<com.blockeng.repository.UserLoginLog> result = new ArrayList();
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            return loginLogRepository.findCountByQueryWithExpression(format.parse(startTime), format.parse(endTime));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }


}
