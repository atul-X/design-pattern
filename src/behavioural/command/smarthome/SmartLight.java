package behavioural.command.smarthome;

/**
 * Receiver class - Smart Light Device
 * Performs the actual work for light-related commands
 */
public class SmartLight {
    private boolean isOn = false;
    private int brightness = 0;
    private String location;

    public SmartLight(String location) {
        this.location = location;
    }

    public void turnOn() {
        isOn = true;
        brightness = 100; // Default brightness when turned on
        System.out.println("💡 " + location + " Smart Light: ON (Brightness: " + brightness + "%)");
    }

    public void turnOff() {
        isOn = false;
        brightness = 0;
        System.out.println("💡 " + location + " Smart Light: OFF");
    }

    public void setBrightness(int level) {
        if (level < 0 || level > 100) {
            System.out.println("❌ Invalid brightness level. Must be between 0-100");
            return;
        }

        brightness = level;
        if (level == 0) {
            isOn = false;
            System.out.println("💡 " + location + " Smart Light: OFF (Brightness set to 0%)");
        } else {
            isOn = true;
            System.out.println("💡 " + location + " Light brightness set to: " + level + "%");
        }
    }

    public void dim() {
        if (isOn && brightness > 20) {
            setBrightness(20);
        } else if (!isOn) {
            turnOn();
            setBrightness(20);
        }
    }

    // Getters
    public boolean isOn() { return isOn; }
    public int getBrightness() { return brightness; }
    public String getLocation() { return location; }

    @Override
    public String toString() {
        return location + " Light [" + (isOn ? "ON" : "OFF") + ", Brightness: " + brightness + "%]";
    }
}
