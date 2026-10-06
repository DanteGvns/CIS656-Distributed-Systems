import java.io.IOException;

public class ConsumerApp {
    
    public static void main(String[] args) throws IOException {
        Calculator calculator = new CalculatorRemoteImpl();
        int result = calculator.add(1, 2);
        System.out.println("result is " + result);
   
    }
}
