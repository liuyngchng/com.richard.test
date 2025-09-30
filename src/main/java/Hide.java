class Hide {

	public static final boolean debug = true;

	
	public static void main(String[] args) {

		String test  = "This is begin";
		System.out.println(test);
		if (debug) {
			String debugStr = "This is in debug";
			System.out.println(debugStr);
		}
		String end = "This is end.";
		System.out.println(end);
		Hide.work();

	}

	
	public static void work() {

		Car car;
		if (debug) {
			car = new MyCar();
		} else {
			car = new Car();
		}

		car.fuelUp();
		car.run();
		if (car instanceof MyCar) {
//			(MyCar)car.autoDrive();
		}
		System.out.println("carType = " + car.getClass().getName());	
		if (debug) {
			
		}

	}

	class InnerCar {
		public void run() {
			System.out.println("I am running.");
		}
	}
	
	class Truck extends InnerCar {
		public void load() {
			System.out.println(" I am truck, now loading 10 ton");
		}
	}

}
