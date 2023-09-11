package com.android.liba.service;


import android.text.TextUtils;

import com.android.liba.context.AppContext;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;


public class UserService {
    private static volatile UserService mInstance;
    private CopyOnWriteArrayList<UserLoginListener> listeners=new CopyOnWriteArrayList<>();
    private static String KEY="USER";
    private User userinfo;
    private UserService() {
       userinfo= AppContext.getMMKV().decodeParcelable(KEY,User.class);
    }
    public static UserService getInstance() {
        if (mInstance == null) {
            synchronized ( UserService.class) {
                if (mInstance == null) {
                    mInstance = new UserService();
                }
            }
        }
        return mInstance;
    }
    public void regiserUserLoginLister(UserLoginListener listener)
    {
        if(listener!=null&&!listeners.contains(listener))
        {
            listeners.add(listener);
        }
    }
    public void unregiserUserLoginLister(UserLoginListener listener)
    {
        if(listener!=null)
        {
            listeners.remove(listener);
        }
    }
    public User getUserInfo() {
         return userinfo;
    }
    private void fireUserLoginEvent()
    {
      Iterator<UserLoginListener> lt=listeners.iterator();
      while(lt.hasNext())
      {
          UserLoginListener item=lt.next();
          item.userLogin(userinfo);
      }
    }
    private void fireUserLoginOutEvent()
    {
        Iterator<UserLoginListener> lt=listeners.iterator();
        while(lt.hasNext())
        {
            UserLoginListener item=lt.next();
            item.userLoginOut(userinfo);
        }
    }
    public boolean hasLogin() {
        return getUserInfo() != null && !TextUtils.isEmpty(getUserInfo().token);
    }

    public void loginSuccess(User user) {
       if ( user != null && !TextUtils.isEmpty(user.token) && TextUtils.isEmpty(user.token)) {
            setUser(user);
            fireUserLoginEvent();
        }
    }

    public void loginOut() {
        fireUserLoginOutEvent();
        setUser(null);
    }


    public void setUser(User userinfo) {
        this.userinfo = userinfo;
        AppContext.getMMKV().encode(KEY, this.userinfo);
    }
}
