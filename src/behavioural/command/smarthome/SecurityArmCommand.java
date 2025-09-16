package behavioural.command.smarthome;

/**
 * Concrete Command - Arm Security System
 * Encapsulates the request to arm the security system
 */
public class SecurityArmCommand implements SmartHomeCommand {
    private SecuritySystem securitySystem;
    private boolean wasArmed;

    public SecurityArmCommand(SecuritySystem securitySystem) {
        this.securitySystem = securitySystem;
    }

    @Override
    public void execute() {
        wasArmed = securitySystem.isArmed();
        securitySystem.arm();
    }

    @Override
    public void undo() {
        if (!wasArmed) {
            securitySystem.disarm();
        }
    }

    @Override
    public String getDescription() {
        return "Arm security system";
    }
}
