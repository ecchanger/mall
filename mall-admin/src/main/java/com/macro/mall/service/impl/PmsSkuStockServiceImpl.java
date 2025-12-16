package com.macro.mall.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.macro.mall.dao.PmsSkuStockDao;
import com.macro.mall.dto.PmsSkuBatchUpdateParam;
import com.macro.mall.dto.excel.SkuStockImportDTO;
import com.macro.mall.mapper.PmsSkuStockMapper;
import com.macro.mall.model.PmsSkuStock;
import com.macro.mall.model.PmsSkuStockExample;
import com.macro.mall.service.PmsSkuStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品SKU库存管理Service实现类
 * Created by macro on 2018/4/27.
 */
@Service
public class PmsSkuStockServiceImpl implements PmsSkuStockService {
    @Autowired
    private PmsSkuStockMapper skuStockMapper;
    @Autowired
    private PmsSkuStockDao skuStockDao;

    @Override
    public List<PmsSkuStock> getList(Long pid, String keyword) {
        PmsSkuStockExample example = new PmsSkuStockExample();
        PmsSkuStockExample.Criteria criteria = example.createCriteria().andProductIdEqualTo(pid);
        if (!StrUtil.isEmpty(keyword)) {
            criteria.andSkuCodeLike("%" + keyword + "%");
        }
        return skuStockMapper.selectByExample(example);
    }

    @Override
    public int update(Long pid, List<PmsSkuStock> skuStockList) {
        List<PmsSkuStock> filterSkuList = skuStockList.stream()
                .filter(item -> pid.equals(item.getProductId()))
                .collect(Collectors.toList());
        return skuStockDao.replaceList(filterSkuList);
    }

    @Override
    public int batchUpdatePrice(PmsSkuBatchUpdateParam param) {
        PmsSkuStockExample example = new PmsSkuStockExample();
        PmsSkuStockExample.Criteria criteria = example.createCriteria();
        criteria.andProductIdEqualTo(param.getProductId());
        
        if (param.getSkuIds() != null && !param.getSkuIds().isEmpty()) {
            criteria.andIdIn(param.getSkuIds());
        }
        
        List<PmsSkuStock> skuStockList = skuStockMapper.selectByExample(example);
        
        for (PmsSkuStock sku : skuStockList) {
            BigDecimal newPrice;
            if (param.getAdjustType() == 1) {
                BigDecimal rate = param.getAdjustValue().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
                newPrice = sku.getPrice().multiply(BigDecimal.ONE.add(rate));
            } else {
                newPrice = sku.getPrice().add(param.getAdjustValue());
            }
            
            if (newPrice.compareTo(BigDecimal.ZERO) < 0) {
                newPrice = BigDecimal.ZERO;
            }
            
            sku.setPrice(newPrice.setScale(2, RoundingMode.HALF_UP));
        }
        
        return skuStockDao.batchUpdatePrice(skuStockList);
    }
    
    @Override
    public void exportStock(Long productId, HttpServletResponse response) {
        try {
            PmsSkuStockExample example = new PmsSkuStockExample();
            example.createCriteria().andProductIdEqualTo(productId);
            List<PmsSkuStock> skuStockList = skuStockMapper.selectByExample(example);
            
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("SKU库存", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            
            EasyExcel.write(response.getOutputStream(), PmsSkuStock.class)
                .sheet("SKU库存")
                .doWrite(skuStockList);
        } catch (Exception e) {
            throw new RuntimeException("导出失败: " + e.getMessage());
        }
    }
    
    @Override
    public Map<String, Object> importStock(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        List<String> failDetails = new ArrayList<>();
        int successCount = 0;
        int failCount = 0;
        
        try {
            List<SkuStockImportDTO> dataList = EasyExcel.read(file.getInputStream())
                .head(SkuStockImportDTO.class)
                .sheet()
                .doReadSync();
            
            for (int i = 0; i < dataList.size(); i++) {
                SkuStockImportDTO dto = dataList.get(i);
                try {
                    PmsSkuStockExample example = new PmsSkuStockExample();
                    example.createCriteria().andSkuCodeEqualTo(dto.getSkuCode());
                    List<PmsSkuStock> skuList = skuStockMapper.selectByExample(example);
                    
                    if (skuList.isEmpty()) {
                        failCount++;
                        failDetails.add("第" + (i + 2) + "行: SKU编码不存在");
                        continue;
                    }
                    
                    PmsSkuStock sku = skuList.get(0);
                    if (dto.getStock() != null) {
                        sku.setStock(dto.getStock());
                    }
                    if (dto.getLowStock() != null) {
                        sku.setLowStock(dto.getLowStock());
                    }
                    if (dto.getPrice() != null) {
                        sku.setPrice(new BigDecimal(dto.getPrice()));
                    }
                    
                    int count = skuStockMapper.updateByPrimaryKey(sku);
                    if (count > 0) {
                        successCount++;
                    } else {
                        failCount++;
                        failDetails.add("第" + (i + 2) + "行: 更新失败");
                    }
                } catch (Exception e) {
                    failCount++;
                    failDetails.add("第" + (i + 2) + "行: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("文件解析失败: " + e.getMessage());
        }
        
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("failDetails", failDetails);
        return result;
    }
}