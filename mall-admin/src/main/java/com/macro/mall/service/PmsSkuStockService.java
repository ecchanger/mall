package com.macro.mall.service;

import com.macro.mall.dto.PmsSkuBatchUpdateParam;
import com.macro.mall.model.PmsSkuStock;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * SKU库存管理Service
 */
public interface PmsSkuStockService {
    /**
     * 根据商品ID和关键字获取SKU库存列表
     */
    List<PmsSkuStock> getList(Long pid, String keyword);

    /**
     * 更新商品的SKU库存列表
     */
    int update(Long pid, List<PmsSkuStock> skuStockList);

    /**
     * 批量更新SKU价格
     */
    @Transactional
    int batchUpdatePrice(PmsSkuBatchUpdateParam param);
    
    /**
     * 导出指定商品的SKU库存
     */
    void exportStock(Long productId, HttpServletResponse response);
    
    /**
     * 批量导入SKU库存
     */
    @Transactional
    Map<String, Object> importStock(MultipartFile file);
}