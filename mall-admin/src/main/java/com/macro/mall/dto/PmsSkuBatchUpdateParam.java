package com.macro.mall.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * SKU批量价格调整参数
 */
@Data
public class PmsSkuBatchUpdateParam {
    @NotNull(message = "商品ID不能为空")
    @ApiModelProperty(value = "商品ID", required = true)
    private Long productId;
    
    @ApiModelProperty("SKU ID列表(为空则更新该商品所有SKU)")
    private List<Long> skuIds;
    
    @NotNull(message = "调整类型不能为空")
    @ApiModelProperty(value = "调整类型: 1-百分比调整, 2-固定金额调整", required = true)
    private Integer adjustType;
    
    @NotNull(message = "调整值不能为空")
    @ApiModelProperty(value = "调整值(百分比时为百分数如10表示10%, 固定金额时为具体金额)", required = true)
    private BigDecimal adjustValue;
}
