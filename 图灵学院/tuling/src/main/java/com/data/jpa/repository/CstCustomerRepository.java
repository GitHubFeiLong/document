package com.data.jpa.repository;

import com.data.jpa.po.CstCustomerPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-07-10 13:31
 * @Version 1.0
 */
@Repository
public interface CstCustomerRepository extends JpaRepository<CstCustomerPO, Long>, JpaSpecificationExecutor<CstCustomerPO> {

}
