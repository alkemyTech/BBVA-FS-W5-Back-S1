package com.BBVA.DiMo_S1.B_services.interfaces;

import java.util.Map;


public interface AuthService {
    //1- Login.
    Map<String, Object> login (final String username, final String password);
}
