package com.kole.junit.extensions.test;

public class NamePrinter {

	private final String firstName;
	private final String surName;

	private final Object lock = new Object();
	private boolean printedFirstName = false;
	private boolean spaceRequested = false;

	private StringBuffer output;

	public NamePrinter(String firstName, String surName) {
		this.firstName = firstName;
		this.surName = surName;
		output = new StringBuffer();
	}

	public void print() {
		Thread thread1 = new FirstNamePrinter();
		Thread thread2 = new SpacePrinter();
		Thread thread3 = new SurnamePrinter();

		thread1.start();
		thread2.start();
		thread3.start();

		try {
			thread1.join();
			thread2.join();
			thread3.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public String getOutput() {
		return output.toString();
	}

	private class FirstNamePrinter extends Thread {
		public void run() {
			try {
				synchronized (lock) {
					while (firstName == null) {
						lock.wait();
					}
					output.append(firstName);
					printedFirstName = true;
					spaceRequested = true;
					lock.notifyAll();
				}
			} catch (InterruptedException e) {
				assert (false);
			}
		}
	}

	private class SpacePrinter extends Thread {
		public void run() {
			try {
				synchronized (lock) {
					while (!spaceRequested) {
						lock.wait();
					}
					output.append(' ');
					spaceRequested = false;
					lock.notifyAll();
				}
			} catch (InterruptedException e) {
				assert (false);
			}
		}
	}

	private class SurnamePrinter extends Thread {
		public void run() {
			try {
				synchronized (lock) {
					while (!printedFirstName || spaceRequested
							|| surName == null) {
						lock.wait();
					}
					output.append(surName);
				}
			} catch (InterruptedException e) {
				assert (false);
			}
		}
	}

}
