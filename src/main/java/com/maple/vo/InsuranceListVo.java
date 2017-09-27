package com.maple.vo;

import java.math.BigDecimal;

/**
 * Created by Maple.Ran on 2017/5/31.
 */
public class InsuranceListVo {

    private Integer id;
    private Integer salesmanId;
    private String salesmanName;//销售名
    private Integer insuranceTypeCode;//保险品种代码:1-交强险,2-商业险
    private String insuranceTypeDesc;//保险品种描述
    private String companyName;//保险公司名
    private BigDecimal insurancePrice;//保险金额
    private String createTime;//创建时间
    private String expireDate;//失效日期

    public Integer getId() {
        return id;
    }
    public Integer getSalesmanId() {
        return salesmanId;
    }

    public void setSalesmanId(Integer salesmanId) {
        this.salesmanId = salesmanId;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getSalesmanName() {
        return salesmanName;
    }

    public void setSalesmanName(String salesmanName) {
        this.salesmanName = salesmanName;
    }

    public Integer getInsuranceTypeCode() {
        return insuranceTypeCode;
    }

    public void setInsuranceTypeCode(Integer insuranceTypeCode) {
        this.insuranceTypeCode = insuranceTypeCode;
    }

    public String getInsuranceTypeDesc() {
        return insuranceTypeDesc;
    }

    public void setInsuranceTypeDesc(String insuranceTypeDesc) {
        this.insuranceTypeDesc = insuranceTypeDesc;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public BigDecimal getInsurancePrice() {
        return insurancePrice;
    }

    public void setInsurancePrice(BigDecimal insurancePrice) {
        this.insurancePrice = insurancePrice;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }
}
