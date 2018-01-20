package com.springmvc.controller;


import java.lang.reflect.Method;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.springmvc.entity.User;
import com.springmvc.entity.VerificationToken;
import com.springmvc.repositories.UserRepository;
import com.springmvc.repositories.VerificationTokenRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    VerificationTokenRepository verificationTokenRepository;

    @RequestMapping(value = "/")
    public String home(){
        return "home";
    }
    
    @RequestMapping(value="/login")
    public String loginPageRedirection(){
    	return "login";
    }
    
    @RequestMapping(value="/loginProcessing")
    public void loginProcessing(){
    	System.out.println("Inside Login Processing");
    }
    
    @RequestMapping(value = "/register")
    public String registerUrl(){
    	return "register";
    }
    
//    @RequestMapping(value = "/userRegisterSave")
//    public String registerUser(User user){
//    	userRepository.save(user);
//    	return "login";
//    }
    
    @RequestMapping("/userRegisterSave")
    public String registerUser(User user, HttpServletRequest httpServletRequest){
        String token= UUID.randomUUID().toString();
        user.setEnabled(false);
        userRepository.save(user);
        String authUrl="http://"+httpServletRequest.getServerName()
                + ":"
                +httpServletRequest.getServerPort()
                + httpServletRequest.getContextPath()
                +"/registrationConfirmation"
                +"?token="+token;
        
        System.out.println("Auth Url : " + authUrl);
        
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);

        return "login";
    }
    
    @RequestMapping("/registrationConfirmation")
    public String registrationConfirmation(String token){
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        User user =verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        return "redirect:/login";
    }
    
    
    
}
