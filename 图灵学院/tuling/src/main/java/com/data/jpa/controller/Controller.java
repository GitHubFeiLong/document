package com.data.jpa.controller;

import com.data.jpa.PageParam;
import com.data.jpa.po.CstCustomerPO;
import com.data.jpa.repository.CstCustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-07-10 13:50
 * @Version 1.0
 */
@RestController
@RequestMapping("/api")
public class Controller {

    @Autowired
    private CstCustomerRepository cstCustomerRepository;

    @PostMapping("/customer")
    public Object add (@RequestBody CstCustomerPO cstCustomerPO) {
        CstCustomerPO save = cstCustomerRepository.save(cstCustomerPO);
        return save;
    }
    @DeleteMapping("/{id}")
    public Object delete (@PathVariable("id")Long id) {
        cstCustomerRepository.deleteById(id);
        return true;
    }
    @PutMapping("/customer")
    public Object update (@RequestBody CstCustomerPO cstCustomerPO) {
        CstCustomerPO save = cstCustomerRepository.save(cstCustomerPO);
        return save;
    }

    @GetMapping("/{id}")
    public Object select (@PathVariable("id") Long id) {
        Optional<CstCustomerPO> byId = cstCustomerRepository.findById(id);
        return byId;
    }
    @GetMapping("/all")
    public Object select () {
        List<CstCustomerPO> all = cstCustomerRepository.findAll();
        return all;
    }

    @PostMapping("/page")
    public Object page (@RequestBody PageParam pageParam) {
        System.out.println(1);
        PageRequest pageRequest = PageRequest.of(pageParam.getStart(), pageParam.getSize());
//        Page<CstCustomerPO> all = cstCustomerRepository.findAll(pageRequest);
        CstCustomerPO cstCustomerPO = Optional.ofNullable(pageParam.getCstCustomerPO()).orElseGet(()->new CstCustomerPO());

        Example<CstCustomerPO> example = Example.of(cstCustomerPO);
        Page<CstCustomerPO> all = cstCustomerRepository.findAll(example, pageRequest);
        return all;
    }

}
