package com.data.jpa;

import com.data.jpa.po.CstCustomerPO;
import lombok.Data;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-07-10 14:14
 * @Version 1.0
 */
@Data
public class PageParam {
    private Integer start;
    private Integer size;
    private CstCustomerPO cstCustomerPO;
}
