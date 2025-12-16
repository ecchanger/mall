package com.macro.mall.controller;

import com.macro.mall.common.api.CommonResult;
import com.macro.mall.dto.PmsSkuBatchUpdateParam;
import com.macro.mall.model.PmsSkuStock;
import com.macro.mall.service.PmsSkuStockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 商品SKU库存管理Controller
 * Created by macro on 2018/4/27.
 */
@Controller
@Api(tags = "PmsSkuStockController")
@Tag(name = "PmsSkuStockController", description = "sku商品库存管理")
@RequestMapping("/sku")
public class PmsSkuStockController {
    @Autowired
    private PmsSkuStockService skuStockService;

    @ApiOperation("根据商品ID及sku编码模糊搜索sku库存")
    @RequestMapping(value = "/{pid}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<PmsSkuStock>> getList(@PathVariable Long pid, @RequestParam(value = "keyword",required = false) String keyword) {
        List<PmsSkuStock> skuStockList = skuStockService.getList(pid, keyword);
        return CommonResult.success(skuStockList);
    }
    @ApiOperation("批量更新sku库存信息")
    @RequestMapping(value ="/update/{pid}",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long pid,@RequestBody List<PmsSkuStock> skuStockList){
        int count = skuStockService.update(pid,skuStockList);
        if(count>0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }

    @ApiOperation("批量更新SKU价格")
    @RequestMapping(value = "/batchUpdatePrice", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult batchUpdatePrice(@Valid @RequestBody PmsSkuBatchUpdateParam param) {
        int count = skuStockService.batchUpdatePrice(param);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }
    
    @ApiOperation("导出指定商品的SKU库存")
    @RequestMapping(value = "/exportStock/{productId}", method = RequestMethod.GET)
    public void exportStock(@PathVariable Long productId, HttpServletResponse response) {
        skuStockService.exportStock(productId, response);
    }
    
    @ApiOperation("批量导入SKU库存")
    @RequestMapping(value = "/importStock", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult importStock(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = skuStockService.importStock(file);
        return CommonResult.success(result);
    }
}