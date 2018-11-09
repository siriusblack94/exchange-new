package com.blockeng.admin.web.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qiang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrantPrivilegesForm implements java.io.Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long roleId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long[] privilegeIds;
}
