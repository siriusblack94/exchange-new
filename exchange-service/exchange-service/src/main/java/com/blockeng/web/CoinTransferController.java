package com.blockeng.web;

import com.blockeng.dto.CoinTransferDTO;
import com.blockeng.dto.CoinTransferForm;
import com.blockeng.framework.http.Response;
import com.blockeng.service.CoinTransferService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.ResultMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: jakiro
 * @Date: 2018-10-30 15:21
 * @Description:
 */

@RestController
@RequestMapping("/coinTransfer")
@Slf4j
@Api(value = "站内转帐", description = "站内转帐")
public class CoinTransferController {

    @Autowired
    CoinTransferService coinTransferService;

    /**
     * 站内转帐
     *
     * @param coinTransferForm
     * @return
     */
    @PostMapping(value = "/doTransfer")
    @ApiOperation(value = "站内转帐", httpMethod = "POST", authorizations = {@Authorization(value = "Authorization")})
    public Object doTransfer(@RequestBody CoinTransferForm coinTransferForm) {

        return coinTransferService.transferAccounts(coinTransferForm.getMoneyMakerUserId(),coinTransferForm.getCoinId(),coinTransferForm.getNum(),coinTransferForm.getPayeeUserId());
    }

    /**
     * 站内转帐记录
     *
     * @param userId,coinId,flag
     * @return
     */
    @GetMapping(value = "/getTransferDetail")
    public Object getDetail(@RequestParam(value = "current",defaultValue = "1") int current,
                               @RequestParam(value = "size",defaultValue = "10") int size,
                               @RequestParam(value = "startTime") String startTime,
                               @RequestParam(value = "endTime") String endTime,
                               @RequestParam(value = "coinId") Long coinId,
                               @RequestParam(value = "userId") Long userId,
                               @RequestParam(value = "transferMethod") int transferMethod){

        List<CoinTransferDTO> coinTransferDTOList=coinTransferService.getCoinTransferDetail(current, size, startTime, endTime, coinId, userId, transferMethod);
        return Response.ok(coinTransferDTOList);
    }
}
