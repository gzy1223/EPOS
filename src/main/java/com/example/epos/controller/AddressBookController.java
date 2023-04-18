package com.example.epos.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.epos.common.BaseContext;
import com.example.epos.common.R;
import com.example.epos.entity.AddressBook;
import com.example.epos.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    /**
      * @Author: GZY
      * @Description: add a new address
      * @Date: 31/01/2023
      * @Param addressBook:
      * @return: com.example.epos.common.R<com.example.epos.entity.AddressBook>
      **/

    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook)
    {
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook:{}",addressBook);
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }
    /**
      * @Author: GZY
      * @Description: set the defaut address
      * @Date: 31/01/2023
      * @Param null:
      * @return: null
      **/

    @PutMapping("default")
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook)
    {
        log.info("addressBook:{}",addressBook);
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        wrapper.set(AddressBook::getIsDefault,0);
        //SQL: update address_book set is_default = 0 where user_id =?
        addressBookService.update(wrapper);
        //SQL: update address_book set is_default = 1 where id = ?
        addressBook.setIsDefault(1);

        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }
    @GetMapping("/{id}")
    public R get(@PathVariable Long id)
    {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null)
        {
            return R.success(addressBook);
        }
        else
        {
            return R.error("Not find");
        }
    }
    /**
      * @Author: GZY
      * @Description: get the default address
      * @Date: 31/01/2023

      * @return: com.example.epos.common.R<com.example.epos.entity.AddressBook>
      **/

    @GetMapping("default")
    public R<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault, 1);

        //SQL:select * from address_book where user_id = ? and is_default = 1
        AddressBook addressBook = addressBookService.getOne(queryWrapper);

        if (null == addressBook) {
            return R.error("not find");
        } else {
            return R.success(addressBook);
        }
    }
    /**
      * @Author: GZY
      * @Description: find all address
      * @Date: 31/01/2023
      * @Param addressBook:
      * @return: com.example.epos.common.R<java.util.List<com.example.epos.entity.AddressBook>>
      **/

    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook:{}", addressBook);

        //construct the condition
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(null != addressBook.getUserId(), AddressBook::getUserId, addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);

        //SQL:select * from address_book where user_id = ? order by update_time desc
        return R.success(addressBookService.list(queryWrapper));
    }

}
