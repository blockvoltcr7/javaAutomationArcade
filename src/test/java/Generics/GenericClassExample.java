package Generics;


class MacBookPro<Z>{
	
	Z value;
	
	public void displayType() {
		
		System.out.println(value.getClass().getName());
	}

	public Z getValue() {
		return value;
	}

	public void setValue(Z value) {
		this.value = value;
	}
	
}


public class GenericClassExample {

	
	public void testGeneric() {
    MacBookPro<Integer> mac2019 = new MacBookPro<>();
    mac2019.setValue(2400);
    mac2019.displayType();

    MacBookPro<String> mac2020 = new MacBookPro<>();
    mac2020.setValue("16-inch");
    mac2020.displayType();

    MacBookPro<Double> mac2021 = new MacBookPro<>();
    mac2021.setValue(2.3);
    mac2021.displayType();
}

}
