package org.example.utils;

import org.example.model.UserInfoDto;

public class ValidationUtil
{
    public static boolean isEmailCorrect(UserInfoDto user)
    {
        return user.getEmail() != null && user.getEmail().endsWith("iiitbh.ac.in");
    }
    public static boolean isPasswordCorrect(UserInfoDto user)
    {
        String pass = user.getPassword();
        int n = user.getPassword().length();
        boolean check1 = false;
        boolean check2 = false;
        boolean check3 = false;
        for(int i = 0; i<n; i++)
        {
            char ch = pass.charAt(i);
            if(Character.isDigit(ch))
                check1 = true;
            if(Character.isUpperCase(ch))
                check2 = true;
            if(!Character.isLetterOrDigit(ch))
                check3 = true;
        }
        return n>8&&check1&&check2&&check3;
    }
    public static Boolean validateUserAttributes(UserInfoDto user)
    {
        return (isEmailCorrect(user) && isPasswordCorrect(user));
    }
}   