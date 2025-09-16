package behavioural.command.smarthome;

/**
 * Receiver class - Smart Thermostat Device
 * Manages temperature control in the smart home
 */
public class SmartThermostat {
    private int temperature = 22; // Default temperature in Celsius
    private boolean isOn = true;
    private String mode = "AUTO"; // AUTO, HEAT, COOL, OFF
    private int minTemp = 16;
    private int maxTemp = 30;

    public void setTemperature(int temp) {
        if (temp < minTemp || temp > maxTemp) {
            System.out.println("❌ Temperature out of range. Must be between " + minTemp + "°C and " + maxTemp + "°C");
            return;
        }

        temperature = temp;
        if (!isOn) {
            isOn = true;
            mode = "AUTO";
        }
        System.out.println("🌡️ Thermostat set to: " + temp + "°C (Mode: " + mode + ")");
    }

    public void turnOn() {
        isOn = true;
        mode = "AUTO";
        System.out.println("🌡️ Thermostat: ON (Temperature: " + temperature + "°C, Mode: " + mode + ")");
    }

    public void turnOff() {
        isOn = false;
        mode = "OFF";
        System.out.println("🌡️ Thermostat: OFF");
    }

    public void setMode(String newMode) {
        if (!isOn) {
            System.out.println("❌ Cannot change mode. Thermostat is OFF");
            return;
        }

        String[] validModes = {"AUTO", "HEAT", "COOL"};
        boolean isValidMode = false;
        for (String validMode : validModes) {
            if (validMode.equals(newMode.toUpperCase())) {
                isValidMode = true;
                break;
            }
        }

        if (isValidMode) {
            mode = newMode.toUpperCase();
            System.out.println("🌡️ Thermostat mode changed to: " + mode + " (Temperature: " + temperature + "°C)");
        } else {
            System.out.println("❌ Invalid mode. Valid modes: AUTO, HEAT, COOL");
        }
    }

    public void increaseTemperature() {
        setTemperature(temperature + 1);
    }

    public void decreaseTemperature() {
        setTemperature(temperature - 1);
    }

    // Getters
    public int getTemperature() { return temperature; }
    public boolean isOn() { return isOn; }
    public String getMode() { return mode; }

    @Override
    public String toString() {
        return "Thermostat [" + (isOn ? "ON" : "OFF") + ", " + temperature + "°C, Mode: " + mode + "]";
    }
}
