package com.example.accountingservice.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OperationTypesTest {

    @Test
    public void testIsValidOperationTypeId_ValidId() {
        assertTrue(OperationTypes.isValidOperationTypeId(1));
        assertTrue(OperationTypes.isValidOperationTypeId(2));
        assertTrue(OperationTypes.isValidOperationTypeId(3));
        assertTrue(OperationTypes.isValidOperationTypeId(4));
    }

    @Test
    public void testIsValidOperationTypeId_InvalidId() {
        assertFalse(OperationTypes.isValidOperationTypeId(5));
        assertFalse(OperationTypes.isValidOperationTypeId(0));
        assertFalse(OperationTypes.isValidOperationTypeId(9));
    }

    @Test
    public void testIsNegativeAmount_NormalPurchase() {
        assertTrue(OperationTypes.isNegativeAmount(1));
    }

    @Test
    public void testIsNegativeAmount_PurchaseWithInstallments() {
        assertTrue(OperationTypes.isNegativeAmount(2));
    }

    @Test
    public void testIsNegativeAmount_Withdrawal() {
        assertTrue(OperationTypes.isNegativeAmount(3));
    }

    @Test
    public void testIsNegativeAmount_CreditVoucher() {
        assertFalse(OperationTypes.isNegativeAmount(4));
    }

    @Test
    public void testIsNegativeAmount_InvalidOperationTypeId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            OperationTypes.isNegativeAmount(5);
        });
        assertEquals("Invalid operation type ID", exception.getMessage());
    }

    @Test
    public void testConvertOperationTypeIdToEnum_ValidId() {
        assertEquals(OperationTypes.NORMAL_PURCHASE, OperationTypes.convertOperationTypeIdToEnum(1));
        assertEquals(OperationTypes.PURCHASE_WITH_INSTALLMENTS, OperationTypes.convertOperationTypeIdToEnum(2));
        assertEquals(OperationTypes.WITHDRAWAL, OperationTypes.convertOperationTypeIdToEnum(3));
        assertEquals(OperationTypes.CREDIT_VOUCHER, OperationTypes.convertOperationTypeIdToEnum(4));
    }

    @Test
    public void testConvertOperationTypeIdToEnum_InvalidId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            OperationTypes.convertOperationTypeIdToEnum(5);
        });
        assertEquals("Invalid operation type ID", exception.getMessage());

        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            OperationTypes.convertOperationTypeIdToEnum(0);
        });
        assertEquals("Invalid operation type ID", exception2.getMessage());

        Exception exception3 = assertThrows(IllegalArgumentException.class, () -> {
            OperationTypes.convertOperationTypeIdToEnum(6);
        });
        assertEquals("Invalid operation type ID", exception3.getMessage());
    }

}