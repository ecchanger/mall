package com.macro.mall.dao;

import com.macro.mall.dto.PmsProductDetailResult;
import com.macro.mall.dto.PmsProductResult;
import com.macro.mall.model.PmsProduct;
import com.macro.mall.model.PmsProductExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 商品管理自定义Dao
 * Created by macro on 2018/4/26.
 */
public interface PmsProductDao {
    /**
     * 获取商品编辑信息
     */
    PmsProductResult getUpdateInfo(@Param("id") Long id);

    /**
     * 获取商品详情(包含SKU、属性等完整信息)
     */
    PmsProductDetailResult getDetailInfo(@Param("id") Long id);

    /**
     * 查询库存预警商品列表
     */
    List<PmsProduct> selectLowStockList(PmsProductExample example);
}