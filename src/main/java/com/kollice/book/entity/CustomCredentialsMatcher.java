package com.kollice.book.entity;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

/**
 * Created by 白建业 on 2016/12/22.
 */
public class CustomCredentialsMatcher extends HashedCredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        Object tokenCredentials = token.getCredentials();
        Object accountCredentials = getCredentials(info);
        return equals(tokenCredentials,accountCredentials.toString());
    }
}
