package com.maple.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.maple.common.Const;
import com.maple.common.ResponseCode;
import com.maple.common.ServerResponse;
import com.maple.dao.DriverMapper;
import com.maple.dao.InsuranceMapper;
import com.maple.dao.UserMapper;
import com.maple.pojo.Insurance;
import com.maple.pojo.User;
import com.maple.service.IInsuranceService;
import com.maple.util.DateTimeUtil;
import com.maple.vo.InsuranceListVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Maple.Ran on 2017/5/31.
 */
@Service("iInsuranceService")
public class InsuranceServiceImpl implements IInsuranceService {

    @Autowired
    private InsuranceMapper insuranceMapper;
    @Autowired
    private DriverMapper driverMapper;
    @Autowired
    private UserMapper userMapper;

    public ServerResponse list(Integer userId, Integer carId, Integer insuranceType, String companyName, String createDateT, String createDateB, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        String companyNameToCheck=null;
        if (StringUtils.isNotBlank(companyName)) {
            companyNameToCheck = new StringBuilder().append("%").append(companyName).append("%").toString();
        }
        Date createDateTop=StringUtils.isNotBlank(createDateT) ? DateTimeUtil.strToDate(createDateT) : null;
        Date createDateBut=StringUtils.isNotBlank(createDateB) ? DateTimeUtil.strToDate(createDateB) : null;
        List<Integer> carIdList = Lists.newArrayList();
        if (userId != null) {
        //查询出销售拥有的carId集合
            carIdList= driverMapper.selectCarIdListByUserId(userId);
        }
        //如果所有条件都为空,则会返回所有数据,要避免这种情况
        if (CollectionUtils.isEmpty(carIdList) && carId == null && insuranceType == null && StringUtils.isBlank(companyName)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        List<Insurance> insuranceList = insuranceMapper.selectByMultiParams(carIdList, carId, insuranceType, companyNameToCheck, createDateTop, createDateBut);
        List<InsuranceListVo> insuranceListVoList = Lists.newArrayList();
        for (Insurance insurance : insuranceList) {
            InsuranceListVo insuranceListVo = assembleInsuranceListVo(insurance);
            insuranceListVoList.add(insuranceListVo);
        }
        PageInfo pageInfo = new PageInfo(insuranceList);
        pageInfo.setList(insuranceListVoList);
        return ServerResponse.createBySuccess(pageInfo);

    }

    public ServerResponse saveOrUpdateInsurance(Insurance insurance) {
        if (insurance.getId() != null) {
            int result = insuranceMapper.updateByPrimaryKeySelective(insurance);
            if (result > 0) {
                return ServerResponse.createBySuccessMessage("更新保险信息成功");
            }
        }
        if (insurance.getId() == null) {
            int result = insuranceMapper.insert(insurance);
            if (result > 0) {
                return ServerResponse.createBySuccessMessage("新增保险信息成功");
            }
        }
        return ServerResponse.createByErrorMessage("操作失败");
    }

    private InsuranceListVo assembleInsuranceListVo(Insurance insurance) {
        InsuranceListVo insuranceListVo = new InsuranceListVo();
        insuranceListVo.setId(insurance.getId());
        Integer salesManId = driverMapper.selectUserIdByCarId(insurance.getCarId());
        if (salesManId != null) {
            insuranceListVo.setSalesmanId(salesManId);
            User user = userMapper.selectByPrimaryKey(salesManId);
            insuranceListVo.setSalesmanName(user.getName());
        }
        insuranceListVo.setInsuranceTypeCode(insurance.getInsuranceType());
        insuranceListVo.setInsuranceTypeDesc(Const.InsuranceType.codeOf(insurance.getInsuranceType()).getDesc());
        insuranceListVo.setCompanyName(insurance.getCompanyName());
        insuranceListVo.setInsurancePrice(insurance.getInsurancePrice());
        insuranceListVo.setCreateTime(DateTimeUtil.dateToStr(insurance.getCreateTime()));
        insuranceListVo.setExpireDate(DateTimeUtil.dateToStr(insurance.getExpireDate()));
        return insuranceListVo;
    }


}
