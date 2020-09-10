package com.autonomad.data.models.claims

enum class OrderStatus(val description: String) {
    CREATED("New"),
    PROCESSING("Processing"),
    SUCCESSFUL("Success"),
    CANCELLED("Cancel"),
    REFUND("Refund"),
    NEED_CONFIRM("Need confirm"),
    PROMOCODE_ERROR("Promocode error"),
    PURCHASE_ERROR("Purchase error"),
    ORDER_ERROR("Order error"),
    VALIDATED("Validated")
}