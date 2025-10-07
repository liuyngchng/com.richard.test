class MyCar extends Car {

	public void autoDrive() {

		System.out.println("now i am auto-driving.");
		Thread t = new Thread();
		t.setDaemon(true);
	}

}
