package behavioural.strategy.payment;

interface PaymentStrategy{
	void processPayment();
}
class PaymentService{
	private PaymentStrategy strategy;
	public void setPaymentStrategy(PaymentStrategy strategy){
		this.strategy=strategy;
	}
	public void pay(){
		strategy.processPayment();//polymorafic behaviour
	}
}
class creditCard implements PaymentStrategy{

	@Override
	public void processPayment() {
		System.out.println("Making payment via credit card");
	}
}
class debitCard implements PaymentStrategy{

	@Override
	public void processPayment() {
		System.out.println("Making payment via debit card");
	}
}
public class StrategyPattern {
	public static void main(String[] args) {
		PaymentService paymentService=new PaymentService();
		paymentService.setPaymentStrategy(new creditCard());
		paymentService.pay();
	}


}
