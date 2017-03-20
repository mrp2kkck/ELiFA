package com.mundosica.utils;

import android.app.Activity;

public class TaskManager  extends Thread {
	boolean stop = false;
	public Activity activity;
	
	public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    //private static final String TAG = "ServicioLocochon";
	
	public TaskManager(String name,Activity activity) {
		super(name);
		this.activity = activity;
	}
	 /*
	@Override
	protected void onHandleIntent(Intent arg0) {
		run();
	}
	*/
	@Override
	public void run() {
		while(!stop) {
			try {
				if(Helper.isNetworkAvailable(activity)) {
					Fecha.fechaAhora();
					int currentMinutes = Fecha.min;
					int currentSeconds = Fecha.seg;
					if (currentSeconds != 0) {
						int secondsRemaining = 60 - currentSeconds;
						this.waitASeconds(secondsRemaining);
						continue;
					}
					this.every1min();
					if (currentMinutes % 5 == 0) {
						this.every5mins();
					}
					if (currentMinutes % 10 == 0) {
						this.every10mins();
					}
					this.waitASecond();
				}
			} catch(Exception e) {
				Helper.printError(activity.getApplicationContext(), e);
			}
		}
	}
	
	public void every5mins() {
	}
	/**
	 * Esta función es disparada cada 5 minutos
	 */
	public void every10mins() {
	}
	
	/**
	 * Esta función es disparada cada minuto
	 */
	public void every1min() {
	}

	/**
	 * Se encarga de esperar 1 minuto
	 */
	public void waitASecond() {
		this.waitMiliSeconds(1000);
	}

	/**
	 * Se encarga de esperar num_seconds segundos
	 */
	public void waitASeconds(int num_seconds) {
		for (int i=0;  i<num_seconds; i++) {
			this.waitASecond();
		}
	}

	private void waitMiliSeconds(int numMiliSeconds) {
		try {
			Thread.sleep(numMiliSeconds);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}
}
