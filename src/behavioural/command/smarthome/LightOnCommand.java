package behavioural.command.smarthome;

/**
 * Concrete Command - Turn On Smart Light
 * Encapsulates the request to turn on a smart light
 */
public class LightOnCommand implements SmartHomeCommand {
    private SmartLight light;
    private int previousBrightness;

    public LightOnCommand(SmartLight light) {
        this.light = light;
    }

    @Override
    public void execute() {
        previousBrightness = light.getBrightness();
        light.turnOn();
    }

    @Override
    public void undo() {
        if (previousBrightness == 0) {
            light.turnOff();
        } else {
            light.setBrightness(previousBrightness);
        }
    }

    @Override
    public String getDescription() {
        return "Turn ON " + light.getLocation() + " light";
    }
}
