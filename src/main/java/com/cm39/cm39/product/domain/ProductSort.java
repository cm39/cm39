package com.cm39.cm39.product.domain;

public enum ProductSort {
    LATEST( "latest", "최신순"),
    POPULAR("popular","인기순"),
    REVIEW("review","리뷰순");

    private String value;
    private String name;

    ProductSort(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }
}
