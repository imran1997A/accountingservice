package com.example.accountingservice.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum OperationTypes {
    NORMAL_PURCHASE(1, "Normal Purchase", true),
    PURCHASE_WITH_INSTALLMENTS(2, "Purchase with Installments", true),
    WITHDRAWAL(3, "Withdrawal", true),
    CREDIT_VOUCHER(4, "Credit Voucher", false);


    private final int id;
    private final String description;
    private final boolean isNegativeAmount;


    OperationTypes(int id, String description, boolean isNegativeAmount) {
        this.id = id;
        this.description = description;
        this.isNegativeAmount = isNegativeAmount;
    }

    public static boolean isValidOperationTypeId(Integer operationTypeId) {
        return Arrays.stream(OperationTypes.values())
                .anyMatch(type -> type.getId() == operationTypeId);
    }

    public static boolean isNegativeAmount(Integer operationTypeId) {
        OperationTypes operationType = convertOperationTypeIdToEnum(operationTypeId);
        return operationType.isNegativeAmount();
    }

    public static OperationTypes convertOperationTypeIdToEnum(Integer operationTypeId) {
        return Arrays.stream(OperationTypes.values())
                .filter(type -> type.getId() == operationTypeId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid operation type ID"));
    }
}
