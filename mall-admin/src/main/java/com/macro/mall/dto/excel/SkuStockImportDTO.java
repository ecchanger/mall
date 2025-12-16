package com.macro.mall.dto.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * SKU库存导入Excel模型
 */
@Data
public class SkuStockImportDTO {
    @ExcelProperty(value = "SKU编码", index = 0)
    private String skuCode;
    
    @ExcelProperty(value = "库存", index = 1)
    private Integer stock;
    
    @ExcelProperty(value = "预警库存", index = 2)
    private Integer lowStock;
    
    @ExcelProperty(value = "价格", index = 3)
    private String price;
}
