package com.example.epos.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.epos.common.R;
import com.example.epos.entity.Category;
import com.example.epos.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    /**
      * @Author: GZY
      * @Description:
      * @Date: 26/01/2023
      * @Param category:
      * @return: com.example.epos.common.R<java.lang.String>
      **/

    @PostMapping
    public R<String> save(@RequestBody Category category)
    {
        log.info("Category:{}",category);
        categoryService.save(category);
        return R.success("Success");
    }
    /**
      * @Author: GZY
      * @Description:
      * @Date: 26/01/2023
      * @Param page:
     * @Param pageSize:
      * @return: com.example.epos.common.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page>
      **/

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize)
    {
        //constructor for subpage
        Page<Category> pageInfo = new Page<>(page,pageSize);
        //condition constructor
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //add sort condition
        queryWrapper.orderByAsc(Category::getSort);

        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }
    /**
      * @Author: GZY
      * @Description: delete based on ID
      * @Date: 26/01/2023
      * @Param id:
      * @return: com.example.epos.common.R<java.lang.String>
      **/

    @DeleteMapping
    public R<String> delete(Long id)
    {
        log.info("Delete category::{}",id);
        categoryService.remove(id);
        return R.success("Delete success");
    }
    /**
      * @Author: GZY
      * @Description: change the information based on ID
      * @Date: 26/01/2023
      * @Param category:
      * @return: com.example.epos.common.R<java.lang.String>
      **/
    @PutMapping
    public R<String> update(@RequestBody Category category)
    {
        log.info("change the information:{}",category);
        categoryService.updateById(category);
        return R.success("Success");
    }
    /**
      * @Author: GZY
      * @Description: find the information based on the condition
      * @Date: 28/01/2023
      * @Param category:
      * @return: com.example.epos.common.R<java.util.List<com.example.epos.entity.Category>>
      **/

    @GetMapping("/list")
    public R<List<Category>> list(Category category)
    {
        // condition constructor
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //add requirement
        queryWrapper.eq(category.getType() !=null,Category::getType,category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}
