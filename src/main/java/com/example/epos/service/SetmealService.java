package com.example.epos.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.epos.dto.SetmealDto;
import com.example.epos.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /**
      * @Author: GZY
      * @Description: store the relationship between setmeal nad the dishes
      * @Date: 29/01/2023
      * @Param null:
      * @return: null
      **/
    public void saveWithDish(SetmealDto setmealDto);
    /**
      * @Author: GZY
      * @Description: delete the setmeal, and delete the related informtaion
      * @Date: 30/01/2023
      * @Param ids:
      * @return: void
      **/

    public void removeWithDish(List<Long> ids);

}
