package com.blockeng.sharding.muti;

import lombok.Data;

@Data
public class MutiMasterInfo {

    private String dataSourceName;

    private String slaveName;

    private String masterName;

    private String isSlaveSplit;

}
