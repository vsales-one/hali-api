package com.hali.service.impl;

import com.hali.config.firebase.FirebaseTokenHolder;
import com.hali.service.FirebaseService;
import com.hali.service.shared.FirebaseParser;
import org.springframework.stereotype.Service;

@Service
public class FirebaseServiceImpl implements FirebaseService {
	@Override
	public FirebaseTokenHolder parseToken(String firebaseToken) {
		return new FirebaseParser().parseToken(firebaseToken);
	}
}
