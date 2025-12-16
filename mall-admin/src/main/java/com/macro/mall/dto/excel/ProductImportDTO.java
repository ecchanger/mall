package com.macro.mall.dto.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 商品导入Excel模型
 */
@Data
public class ProductImportDTO {
    @ExcelProperty(value = "商品名称", index = 0)
    private String name;
    
    @ExcelProperty(value = "副标题", index = 1)
    private String subTitle;
    
    @ExcelProperty(value = "品牌ID", index = 2)
    private Long brandId;
    
    @ExcelProperty(value = "商品分类ID", index = 3)
    private Long productCategoryId;
    
    @ExcelProperty(value = "货号", index = 4)
    private String productSn;
    
    @ExcelProperty(value = "价格", index = 5)
    private String price;
    
    @ExcelProperty(value = "市场价", index = 6)
    private String originalPrice;
    
    @ExcelProperty(value = "库存", index = 7)
    private Integer stock;
    
    @ExcelProperty(value = "预警库存", index = 8)
    private Integer lowStock;
    
    @ExcelProperty(value = "单位", index = 9)
    private String unit;
    
    @ExcelProperty(value = "商品重量", index = 10)
    private String weight;
    
    @ExcelProperty(value = "商品描述", index = 11)
    private String description;
}
