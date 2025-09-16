package behavioural.command.smarthome;

/**
 * Receiver class - Security System Device
 * Manages home security including alarms and monitoring
 */
public class SecuritySystem {
    private boolean armed = false;
    private String mode = "DISARMED"; // DISARMED, HOME, AWAY, NIGHT
    private boolean alarmTriggered = false;
    private boolean motionDetected = false;
    private boolean doorSensorActive = false;

    public void arm() {
        armed = true;
        mode = "AWAY";
        alarmTriggered = false;
        System.out.println("🔒 Security System: ARMED (Mode: " + mode + ")");
        System.out.println("   All sensors activated. System monitoring...");
    }

    public void disarm() {
        armed = false;
        mode = "DISARMED";
        alarmTriggered = false;
        motionDetected = false;
        System.out.println("🔓 Security System: DISARMED");
        System.out.println("   All sensors deactivated.");
    }

    public void setHomeMode() {
        armed = true;
        mode = "HOME";
        alarmTriggered = false;
        System.out.println("🏠 Security System: HOME MODE");
        System.out.println("   Door/window sensors active, motion sensors disabled.");
    }

    public void setNightMode() {
        armed = true;
        mode = "NIGHT";
        alarmTriggered = false;
        System.out.println("🌙 Security System: NIGHT MODE");
        System.out.println("   Perimeter sensors active, internal motion reduced sensitivity.");
    }

    public void triggerAlarm() {
        if (armed) {
            alarmTriggered = true;
            System.out.println("🚨 SECURITY ALERT: Alarm triggered! Mode: " + mode);
            System.out.println("   Emergency services notified.");
        }
    }

    public void silenceAlarm() {
        if (alarmTriggered) {
            alarmTriggered = false;
            System.out.println("🔇 Security System: Alarm silenced");
        }
    }

    public void simulateMotionDetection() {
        motionDetected = true;
        if (armed && (mode.equals("AWAY") || mode.equals("NIGHT"))) {
            System.out.println("👁️ Motion detected!");
            triggerAlarm();
        } else if (armed && mode.equals("HOME")) {
            System.out.println("👁️ Motion detected (Home mode - no alarm)");
        }
    }

    public void simulateDoorSensor() {
        doorSensorActive = true;
        if (armed) {
            System.out.println("🚪 Door sensor activated!");
            triggerAlarm();
        }
    }

    public void testSystem() {
        System.out.println("🔧 Security System: Running diagnostics...");
        System.out.println("   ✅ Motion sensors: OK");
        System.out.println("   ✅ Door sensors: OK");
        System.out.println("   ✅ Window sensors: OK");
        System.out.println("   ✅ Communication link: OK");
        System.out.println("   System test completed successfully.");
    }

    // Getters
    public boolean isArmed() { return armed; }
    public String getMode() { return mode; }
    public boolean isAlarmTriggered() { return alarmTriggered; }
    public boolean isMotionDetected() { return motionDetected; }

    @Override
    public String toString() {
        return "Security System [" + mode + ", " +
               (alarmTriggered ? "ALARM ACTIVE" : "Normal") + "]";
    }
}
