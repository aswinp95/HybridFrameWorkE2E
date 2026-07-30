
public class SanityCheck {
	public static void main(String[] args) {
        System.out.println("Username: " + System.getenv("BROWSERSTACK_USERNAME"));
        System.out.println("Access Key: " + System.getenv("BROWSERSTACK_ACCESS_KEY"));
    }
}
