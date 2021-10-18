package com.mcfuturepartners.crm.api.customer.repository;

import com.mcfuturepartners.crm.api.customer.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomerRepositoryImpl {
//
//    private final EntityManager em;
//
//    @Override
//    public boolean registerCustomer(Customer customer) {
//        //성공시 true, 실패시 false
//        customer.setRegDate(new Date());
//        em.persist(customer);
//        return false;
//    }
//
//    @Override
//    public boolean registerCustomers(Customer customer) {
//        //여러개 데이터를 한 번에 넣는경우 예제
//        em.getTransaction().begin();
//        em.persist(customer);
//        em.persist(customer);
//        em.persist(customer);
//        em.persist(customer);
//        em.persist(customer);
//        em.getTransaction().commit();
//        em.close();
//        return false;
//    }
//
//    @Override
//    public Customer findByCustomerNo(String customerNo) {
//        Customer customer = em.find(Customer.class, customerNo);
//        return customer;
//    }
//
//    @Override
//    public List<Customer> findByManagerNo(String managerNo) {
//        return null;
//    }
//
//    @Override
//    public List<Customer> findByRegDate(String regDate) {
//        return null;
//    }
//
//    @Override
//    public List<Customer> findByProduct(String Product) {
//        return null;
//    }
//
//    @Override
//    public List<Customer> findByFunnel(String Funnel) {
//        return null;
//    }
//
//    @Override
//    public List<Customer> findAllCustomer() {
//        return null;
//    }
//
//    @Override
//    public boolean updateCustomer(Customer customer) {
//        em.getTransaction().begin();
//        Query query = em.createQuery("UPDATE Customer c SET c.name = :name"
//                + ", c.phone = :phone"
//                + ", c.email = :email"
//                + ", c.birth = :birth"
//                + ", c.sex = :sex"
//                + ", c.manager = :manager"
//                + ", c.regdate = :regdate"
//                + ", c.funnel = :funnel "
//                + "WHERE c.no = :no");
//        query.setParameter("name", customer.getName());
//        query.setParameter("phone", customer.getPhone());
//        query.setParameter("email", customer.getEmail());
//        query.setParameter("birth", customer.getBirth());
//        query.setParameter("sex", customer.getSex());
//        query.setParameter("manager", customer.getManager());
//        query.setParameter("regdate", customer.getRegDate());
//        query.setParameter("funnel", customer.getFunnel());
//        query.setParameter("no", customer.getNo());
//        int rowsUpdated = query.executeUpdate();
//        em.getTransaction().commit();
//        em.close();
//        //성공시 true, 실패시 false
//        if(rowsUpdated==0){
//            return false;
//        }else{
//            return true;
//        }
//    }
//
//    @Override
//    public boolean deleteCustomer(String customerno) {
//        em.getTransaction().begin();
//        Query query = em.createQuery("DELETE FROM Customer c "
//                + "WHERE c.no = :no");
//        query.setParameter("no", customerno);
//        int rowsUpdated = query.executeUpdate();
//        em.getTransaction().commit();
//        em.close();
//
//        //성공시 true, 실패시 false
//        if(rowsUpdated==0){
//            return false;
//        }else{
//            return true;
//        }
//    }
}
