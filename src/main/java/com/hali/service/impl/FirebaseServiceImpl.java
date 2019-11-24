package com.hali.service.impl;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.hali.security.jwt.FirebaseTokenHolder;
import com.hali.service.FirebaseService;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class FirebaseServiceImpl implements FirebaseService {
    @Override
    public FirebaseTokenHolder parseToken(String idToken) {

        if (idToken != null && idToken.trim().isEmpty()) {
            //    throw new Exception("You must send X-Firebase-Auth field in header to authorize.");
            return null;
        }else{
            FirebaseToken decodedToken = null;
            try {
                decodedToken = FirebaseAuth.getInstance().verifyIdTokenAsync(idToken).get();

            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            } catch (ExecutionException e) {
                e.printStackTrace();
                return null;
            }
            return new FirebaseTokenHolder(decodedToken);
        }
    }
}
