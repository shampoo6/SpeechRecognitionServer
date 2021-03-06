package com.ali.speech.core.auth;


import com.fast.dev.ucenter.core.model.UserTokenModel;
import com.fast.dev.ucenter.security.model.UserIdentity;
import com.fast.dev.ucenter.security.service.UserAuthentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class UserAuthenticationImpl implements UserAuthentication {


    @Override
    public UserIdentity authentication(UserTokenModel userTokenModel) {

        System.out.println("uid:" + userTokenModel.getUid());
        System.out.println("token:" + userTokenModel.getuToken());

        Set<String> roles = new HashSet<String>() {{
            add("user");
            add("test");
            add("ROLE_USER");
            add("AUTH_USER");
        }};
        Map<String, Object> other = new HashMap<>();
        other.put("t", System.currentTimeMillis());
        return new UserIdentity(roles, other);
    }
}
