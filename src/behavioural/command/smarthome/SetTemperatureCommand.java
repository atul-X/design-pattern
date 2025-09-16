package behavioural.command.smarthome;

/**
 * Concrete Command - Set Thermostat Temperature
 * Encapsulates the request to set thermostat temperature
 */
public class SetTemperatureCommand implements SmartHomeCommand {
    private SmartThermostat thermostat;
    private int newTemperature;
    private int previousTemperature;

    public SetTemperatureCommand(SmartThermostat thermostat, int temperature) {
        this.thermostat = thermostat;
        this.newTemperature = temperature;
    }

    @Override
    public void execute() {
        previousTemperature = thermostat.getTemperature();
        thermostat.setTemperature(newTemperature);
    }

    @Override
    public void undo() {
        thermostat.setTemperature(previousTemperature);
    }

    @Override
    public String getDescription() {
        return "Set thermostat temperature to " + newTemperature + "°C";
    }
}
