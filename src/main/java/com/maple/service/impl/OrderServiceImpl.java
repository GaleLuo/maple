package com.maple.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.maple.common.Const;
import com.maple.common.ServerResponse;
import com.maple.dao.*;
import com.maple.pojo.*;
import com.maple.service.IOrderService;
import com.maple.util.BigDecimalUtil;
import com.maple.util.DateTimeUtil;
import com.maple.util.PropertiesUtil;
import com.maple.vo.OrderItemVo;
import com.maple.vo.OrderListVo;
import com.maple.vo.OrderVo;
import com.maple.vo.PayInfoVo;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Created by Maple.Ran on 2017/5/24.
 */
@Service("iOrderService")
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CoModelMapper coModelMapper;
    @Autowired
    private DriverMapper driverMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PayInfoMapper payInfoMapper;

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);


    public ServerResponse createOrder(Integer userId, Integer driverId) {
        List<Cart> cartList = cartMapper.selectCheckedCartByUserId(userId);
        ServerResponse<List<OrderItem>> serverResponse = this.getCartOrderItem(userId, cartList,driverId);
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }
        List<OrderItem> orderItemList = serverResponse.getData();
        BigDecimal orderTotalPrice = this.getOrderTotalPrice(orderItemList);
        Order order = this.assembleOrder(userId, driverId, orderTotalPrice);
        if (order == null) {
            return ServerResponse.createByErrorMessage("生成订单错误");
        }
        if (CollectionUtils.isEmpty(orderItemList)) {
            return ServerResponse.createByErrorMessage("购物车为空");
        }
        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrderNo(order.getOrderNo());
            Integer carId = driverMapper.selectCarIdByDriverId(order.getDriverId());
            orderItem.setCarId(carId);
        }
        orderItemMapper.batchInsert(orderItemList);
        this.reduceProductStock(orderItemList);
        this.cleanCart(cartList);

        OrderVo orderVo = assembleOrderVo(order, orderItemList);
        return ServerResponse.createBySuccess(orderVo);
    }

    public ServerResponse getOrderList(Integer userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList = orderMapper.selectByUserId(userId);
        List<OrderListVo> orderListVoList = assembleOrderListVoList(orderList, userId);
        PageInfo pageInfo = new PageInfo(orderList);
        pageInfo.setList(orderListVoList);
        return ServerResponse.createBySuccess(pageInfo);

    }

    private List<OrderListVo> assembleOrderListVoList(List<Order> orderList, Integer userId) {
        List<OrderListVo> orderVoList = Lists.newArrayList();
        for (Order order : orderList) {
            List<OrderItem> orderItemList = Lists.newArrayList();
            if (userId == null) {
                orderItemList = orderItemMapper.selectByOrderNum(order.getOrderNo());
            } else {
                orderItemList = orderItemMapper.selectByOrderNumAndUserId(order.getOrderNo(), userId);
            }
            OrderListVo orderListVo = assembleOrderListVo(order, orderItemList);
            orderVoList.add(orderListVo);
        }
        return orderVoList;
    }

    private OrderListVo assembleOrderListVo(Order order, List<OrderItem> orderItemList) {
        OrderListVo orderListVo = new OrderListVo();
        orderListVo.setOrderNum(order.getId());
        Driver driver = driverMapper.selectByPrimaryKey(order.getDriverId());
        orderListVo.setDriverName(driver.getName());
        orderListVo.setPhoneNum(driver.getPersonalPhone());
        orderListVo.setOrderStatus(Const.OrderStatus.codeOf(order.getStatus()).getDesc());
        for (OrderItem orderItem : orderItemList) {
            Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
            if (Objects.equals(product.getCategoryId(), Const.CAR_CATEGORY_ID)){
                orderListVo.setProductName(product.getName());
            }
            System.out.println(orderItem.getCarId());
            CoModel coModel = coModelMapper.selectByCarId(orderItem.getCarId());
            orderListVo.setCoModelType(Const.CoModel.codeOf(coModel.getModelType()).getDesc());
            orderListVo.setDownAmount(coModel.getDownAmount());
            orderListVo.setTotalAmount(coModel.getTotalAmount());
        }
        User user = userMapper.selectByPrimaryKey(order.getUserId());
        orderListVo.setSaleManName(user.getName());
        List<PayInfo> payInfoList = payInfoMapper.selectByOrderNum(order.getOrderNo());
        for (PayInfo payInfo : payInfoList) {
            if (payInfo.getPaymentType().equals(Const.PaymentType.DOWN_PAYMENT.getCode())) {
                PayInfoVo payInfoVo = new PayInfoVo();
                payInfoVo.setId(payInfo.getId());
                payInfoVo.setPayPlatform(Const.PaymentPlatform.codeOf(payInfo.getPayPlatform()).getDesc());
                payInfoVo.setPlatformNum(payInfo.getPlatformNumber());
                payInfoVo.setPayTime(DateTimeUtil.dateToStr(payInfo.getUpdateTime()));
                orderListVo.setPayInfoVo(payInfoVo);
            }
        }
        return orderListVo;
    }

    private OrderVo assembleOrderVo(Order order, List<OrderItem> orderItemList) {
        OrderVo orderVo = new OrderVo();
        orderVo.setOrderNo(order.getOrderNo());
        orderVo.setPayment(order.getPayment());
        orderVo.setPaymentType(order.getPaymentType());
        orderVo.setPaymentTypeDesc(Const.PaymentPlatform.codeOf(order.getPaymentType()).getDesc());
        orderVo.setStatus(order.getStatus());
        orderVo.setStatusDesc(Const.OrderStatus.codeOf(order.getStatus()).getDesc());
        orderVo.setDriverId(order.getDriverId());
        Driver driver = driverMapper.selectByPrimaryKey(order.getDriverId());
        if (driver != null) {
            orderVo.setReceiverName(driver.getName());
            orderVo.setReceiverPhone(driver.getPersonalPhone());
        }
        orderVo.setPaymentTime(DateTimeUtil.dateToStr(order.getPaymentTime()));
        orderVo.setEstimate_time(DateTimeUtil.dateToStr(order.getEstimateTime()));
        orderVo.setEndTime(DateTimeUtil.dateToStr(order.getEndTime()));
        orderVo.setCreateTime(DateTimeUtil.dateToStr(order.getCreateTime()));
        orderVo.setCloseTime(DateTimeUtil.dateToStr(order.getCloseTime()));
        orderVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        List<OrderItemVo> orderItemVoList = Lists.newArrayList();
        for (OrderItem orderItem : orderItemList) {
            OrderItemVo orderItemVo = assembleOrderItemVo(orderItem);
            orderItemVoList.add(orderItemVo);
        }
        orderVo.setOrderItemVoList(orderItemVoList);
        return orderVo;
    }

    private OrderItemVo assembleOrderItemVo(OrderItem orderItem) {
        OrderItemVo orderItemVo = new OrderItemVo();
        orderItemVo.setOrderNo(orderItem.getOrderNo());
        orderItemVo.setProductId(orderItem.getProductId());
        orderItemVo.setProductName(orderItem.getProductName());
        orderItemVo.setProductImage(orderItem.getProductImage());
        orderItemVo.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
        orderItemVo.setQuantity(orderItem.getQuantity());
        orderItemVo.setTotalPrice(orderItem.getTotalPrice());
        orderItemVo.setCreateTime(DateTimeUtil.dateToStr(orderItem.getCreateTime()));
        return orderItemVo;
    }

    private void cleanCart(List<Cart> cartList) {
        for (Cart cart : cartList) {
            cartMapper.deleteByPrimaryKey(cart.getId());
        }
    }

    private void reduceProductStock(List<OrderItem> orderItemList) {
        for (OrderItem orderItem : orderItemList) {
            Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
            product.setStock(product.getStock()-orderItem.getQuantity());
            productMapper.updateByPrimaryKeySelective(product);
        }
    }

    private Order assembleOrder(Integer userId, Integer driverId, BigDecimal orderTotalPrice) {
        Order order = new Order();
        Long orderNum = this.OrderNumGenerator();
        order.setOrderNo(orderNum);
        order.setStatus(Const.OrderStatus.NO_PAY.getCode());
        order.setPostage(0);
        order.setPaymentType(Const.PaymentPlatform.pingan.getCode());
        order.setPayment(orderTotalPrice);
        order.setUserId(userId);
        order.setDriverId(driverId);

        int result = orderMapper.insert(order);
        if (result > 0) {
            return order;
        }
        return null;
    }

    private Long OrderNumGenerator() {
        long currentTime = System.currentTimeMillis();
        return currentTime + new Random().nextInt(100);
    }

    private BigDecimal getOrderTotalPrice(List<OrderItem> orderItemList) {
        BigDecimal totalPrice = new BigDecimal("0");
        for (OrderItem orderItem : orderItemList) {
                totalPrice = BigDecimalUtil.add(totalPrice.doubleValue(), orderItem.getTotalPrice().doubleValue());
        }
        return totalPrice;
    }

    private ServerResponse<List<OrderItem>> getCartOrderItem(Integer userId, List<Cart> cartList, Integer driverId) {
        List<OrderItem> orderItemList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(cartList)) {
            return ServerResponse.createByErrorMessage("购物车为空");
        }
        //校验购物车数据,产品状态和数量
        for (Cart cartItem : cartList) {
            OrderItem orderItem = new OrderItem();
            Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
            if (Const.ProductStatus.ON_SALE.getCode() != product.getStatus()) {
                return ServerResponse.createByErrorMessage("产品" + product.getName() + "已下架");
            }
            if (cartItem.getQuantity() > product.getStock()) {
                return ServerResponse.createByErrorMessage("产品" + product.getName() + "库存不足");
            }
            orderItem.setUserId(userId);
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
            //如果是汽车,则把价格设置为首付价
            if (category.getId().equals(Const.CAR_CATEGORY_ID)) {
                Driver driver = driverMapper.selectByPrimaryKey(driverId);
                CoModel coModel = coModelMapper.selectByPrimaryKey(driver.getCoModelId());
                product.setPrice(coModel.getDownAmount());
            }
            orderItem.setTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cartItem.getQuantity().doubleValue()));
            orderItemList.add(orderItem);
        }
        return ServerResponse.createBySuccess(orderItemList);
    }
}
