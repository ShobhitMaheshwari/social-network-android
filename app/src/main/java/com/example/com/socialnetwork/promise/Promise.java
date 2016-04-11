package com.example.com.socialnetwork.promise;

/**
 * Created by shobhit on 4/10/16.
 */
public class Promise {

	Promise(Runnable runnable){
		Thread thread = new Thread(runnable);
		thread.start();
	}


}
