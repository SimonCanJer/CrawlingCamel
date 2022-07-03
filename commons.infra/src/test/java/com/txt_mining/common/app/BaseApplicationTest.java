package com.txt_mining.common.app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BaseApplicationTest {
    @Test
    public void test(){
        BaseApplication.init();
        assertEquals("com.txr_mining.common.kube_bridge",System.getProperty("kube8.redirect"));
        assertEquals("false",System.getProperty("management.endpoint.health.probes.enabled"));
    }

}