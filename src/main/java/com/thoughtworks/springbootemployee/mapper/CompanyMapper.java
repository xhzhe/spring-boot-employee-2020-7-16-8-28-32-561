package com.thoughtworks.springbootemployee.mapper;

import com.thoughtworks.springbootemployee.dto.CompanyRequest;
import com.thoughtworks.springbootemployee.dto.CompanyRespond;
import com.thoughtworks.springbootemployee.model.Company;
import org.springframework.beans.BeanUtils;

public class CompanyMapper {
    public static Company map(CompanyRequest companyRequest) {
        Company company = new Company();
        BeanUtils.copyProperties(companyRequest, company);
        return company;
    }

    public static CompanyRespond map(Company company) {
        CompanyRespond companyRespond = new CompanyRespond();
        BeanUtils.copyProperties(company, companyRespond);
        return companyRespond;
    }
}
