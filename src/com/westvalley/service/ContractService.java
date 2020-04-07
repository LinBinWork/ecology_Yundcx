package com.westvalley.service;

import com.westvalley.dao.ContractDao;
import com.westvalley.entity.ContractEntity;
import com.westvalley.entity.PaymentEntity;

import java.util.List;


public class ContractService {

    private ContractDao contractDao;

    public ContractService() {
        this.contractDao = new ContractDao();
    }

    public String insertContract(ContractEntity contractEntity) {
        return this.contractDao.insertContract(contractEntity);
    }

    public String updateContractList(List<PaymentEntity> paymentEntityList) {
        return this.contractDao.updateContractList(paymentEntityList);

    }
}
