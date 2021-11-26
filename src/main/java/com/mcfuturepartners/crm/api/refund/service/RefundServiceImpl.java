package com.mcfuturepartners.crm.api.refund.service;

import com.mcfuturepartners.crm.api.exception.ErrorCode;
import com.mcfuturepartners.crm.api.exception.FindException;
import com.mcfuturepartners.crm.api.exception.RefundException;
import com.mcfuturepartners.crm.api.order.entity.Order;
import com.mcfuturepartners.crm.api.order.repository.OrderRepository;
import com.mcfuturepartners.crm.api.refund.dto.RefundDto;
import com.mcfuturepartners.crm.api.refund.entity.Refund;
import com.mcfuturepartners.crm.api.refund.repository.RefundRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService{
    private final RefundRepository refundRepository;
    private final OrderRepository orderRepository;

    @Override
    public void makeRefund(RefundDto refundDto) {
        Order order = orderRepository.findById(refundDto.getOrderId()).orElseThrow(()-> new FindException("Order "+ ErrorCode.RESOURCE_NOT_FOUND.getMsg()));
        Refund refund = Refund.builder().order(order).refundAmount(refundDto.getAmount()).build();

        if(!ObjectUtils.isEmpty(order.getRefund())){
            throw new RefundException("Already Refunded");
        }

        order.setRefund(refund);
        try{
            refundRepository.save(refund);
        } catch (Exception e){
            throw e;
        }

        try{
            orderRepository.save(order);
        } catch (Exception e){
            throw e;
        }

    }
}
