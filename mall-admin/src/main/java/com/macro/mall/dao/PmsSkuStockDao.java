package com.macro.mall.dao;

import com.macro.mall.model.PmsSkuStock;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * SKU库存自定义DAO
 */
public interface PmsSkuStockDao {
    /**
     * 批量更新SKU价格
     */
    int batchUpdatePrice(@Param("list") List<PmsSkuStock> skuStockList);
}
