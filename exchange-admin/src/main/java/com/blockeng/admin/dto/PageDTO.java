package com.blockeng.admin.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class PageDTO {

    private int current;

    private int size;

    private int total;

    private List records;

}
