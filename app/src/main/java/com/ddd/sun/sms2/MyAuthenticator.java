package com.ddd.sun.sms2;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Created by Sun on 2016/9/9.
 */
public class MyAuthenticator extends Authenticator
{
    String userName=null;
    String password=null;

    public MyAuthenticator(String username, String password)
    {
        this.userName = username;
        this.password = password;
    }
        protected PasswordAuthentication getPasswordAuthentication()
        {
            return new PasswordAuthentication(userName, password);
        }
}