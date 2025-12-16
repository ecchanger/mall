package com.macro.mall.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 商品详情结果对象
 */
public class PmsProductDetailResult extends PmsProductResult {
    @Getter
    @Setter
    @ApiModelProperty("总库存")
    private Integer totalStock;
    
    @Getter
    @Setter
    @ApiModelProperty("可用库存(总库存-锁定库存)")
    private Integer availableStock;
    
    @Getter
    @Setter
    @ApiModelProperty("SKU数量")
    private Integer skuCount;
}
