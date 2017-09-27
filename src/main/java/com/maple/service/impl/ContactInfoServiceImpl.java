package com.maple.service.impl;

import com.google.common.collect.Lists;
import com.maple.common.ServerResponse;
import com.maple.dao.ContactInfoMapper;
import com.maple.dao.DriverMapper;
import com.maple.pojo.ContactInfo;
import com.maple.service.IContactInfoService;
import com.maple.vo.ContactInfoDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Maple.Ran on 2017/6/1.
 */
@Service("iContactInfoService")
public class ContactInfoServiceImpl implements IContactInfoService{
    @Autowired
    private ContactInfoMapper contactInfoMapper;
    @Autowired
    private DriverMapper driverMapper;

    public ServerResponse saveOrUpdate(ContactInfo contactInfo) {
        if (contactInfo.getId() != null) {
            int result = contactInfoMapper.updateByPrimaryKeySelective(contactInfo);
            if (result > 0) {
                return ServerResponse.createBySuccessMessage("更新联系方式成功");
            }
        }else{
            int result = contactInfoMapper.insert(contactInfo);
            if (result > 0) {
                return ServerResponse.createBySuccessMessage("新增联系方式成功");
            }
        }
        return ServerResponse.createByErrorMessage("操作失败");
    }

    public ServerResponse detail(Integer userId, Integer driverId) {
        List<Integer> carIdList = Lists.newArrayList();
        if (userId != null) {
            //查询出销售拥有的carId集合
            carIdList= driverMapper.selectCarIdListByUserId(userId);
        }
        if (carIdList.contains(driverId)) {
            ContactInfo contactInfo = contactInfoMapper.selectByDriverId(driverId);
            ContactInfoDetailVo contactInfoDetailVo = assembleContactInfoDetailVo(contactInfo);
            return ServerResponse.createBySuccess(contactInfoDetailVo);
        }
        return ServerResponse.createByErrorMessage("查询失败");

    }

    private ContactInfoDetailVo assembleContactInfoDetailVo(ContactInfo contactInfo) {
        ContactInfoDetailVo contactInfoDetailVo = new ContactInfoDetailVo();
        contactInfoDetailVo.setId(contactInfo.getId());
        contactInfoDetailVo.setDriverId(contactInfo.getDriverId());
        contactInfoDetailVo.setWechat(contactInfo.getWechat());
        contactInfoDetailVo.setEmail(contactInfo.getEmail());
        contactInfoDetailVo.setMailAdd(contactInfo.getMailAdd());
        contactInfoDetailVo.setLivingAdd(contactInfo.getLivingAdd());
        contactInfoDetailVo.setEmNameFirst(contactInfo.getEmNameFirst());
        contactInfoDetailVo.setEmPhoneFirst(contactInfo.getEmPhoneFirst());
        contactInfoDetailVo.setEmAddFirst(contactInfo.getEmAddFirst());
        contactInfoDetailVo.setEmNameSecond(contactInfo.getEmNameSecond());
        contactInfoDetailVo.setEmPhoneSecond(contactInfo.getEmPhoneSecond());
        contactInfoDetailVo.setEmAddSecond(contactInfo.getEmAddSecond());
        return contactInfoDetailVo;
    }


}
