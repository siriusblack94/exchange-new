package com.blockeng.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author qiang
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class IpInfo implements Serializable {

    private String area;

    private String country;

    private String long_ip;

    private String city;

    private String isp;

    private String region_id;

    private String region;

    private String country_id;

    private String city_id;
}
