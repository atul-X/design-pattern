package behavioural.command.smarthome;

/**
 * Concrete Command - Turn Off Smart Light
 * Encapsulates the request to turn off a smart light
 */
public class LightOffCommand implements SmartHomeCommand {
    private SmartLight light;
    private boolean wasOn;
    private int previousBrightness;

    public LightOffCommand(SmartLight light) {
        this.light = light;
    }

    @Override
    public void execute() {
        wasOn = light.isOn();
        previousBrightness = light.getBrightness();
        light.turnOff();
    }

    @Override
    public void undo() {
        if (wasOn) {
            if (previousBrightness > 0) {
                light.setBrightness(previousBrightness);
            } else {
                light.turnOn();
            }
        }
    }

    @Override
    public String getDescription() {
        return "Turn OFF " + light.getLocation() + " light";
    }
}
