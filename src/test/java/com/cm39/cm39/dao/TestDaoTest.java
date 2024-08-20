package com.cm39.cm39.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TestDaoTest {
    @Autowired
    TestDao testDao;

    @Test
    public void test() {
        System.out.println(testDao.now());
    }
}