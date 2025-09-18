// Design Patterns Interactive Showcase - JavaScript

// Singleton Pattern Implementation (JavaScript version)
class AppSetting {
    constructor() {
        if (AppSetting.instance) {
            return AppSetting.instance;
        }

        this.databaseUrl = "jdbc:mysql://localhost:3306/mydb";
        this.apiKey = "wxxzzxzd";
        this.instanceId = Math.random().toString(36).substr(2, 9);

        AppSetting.instance = this;
        return this;
    }

    static getInstance() {
        if (!AppSetting.instance) {
            AppSetting.instance = new AppSetting();
        }
        return AppSetting.instance;
    }

    getDatabaseUrl() {
        return this.databaseUrl;
    }

    setDatabaseUrl(url) {
        this.databaseUrl = url;
    }

    getApiKey() {
        return this.apiKey;
    }

    setApiKey(key) {
        this.apiKey = key;
    }

    getInstanceId() {
        return this.instanceId;
    }
}

class Logger {
    constructor() {
        if (Logger.instance) {
            return Logger.instance;
        }

        this.logs = [];
        this.instanceId = Math.random().toString(36).substr(2, 9);

        Logger.instance = this;
        return this;
    }

    static getInstance() {
        if (!Logger.instance) {
            Logger.instance = new Logger();
        }
        return Logger.instance;
    }

    log(level, message) {
        const timestamp = new Date().toLocaleString('en-US', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit',
            hour12: false
        });

        const logEntry = {
            timestamp,
            level: level.toUpperCase(),
            message,
            id: Date.now()
        };

        this.logs.push(logEntry);
        this.displayLog(logEntry);
    }

    displayLog(logEntry) {
        const logOutput = document.getElementById('logOutput');
        if (!logOutput) return;

        const logElement = document.createElement('div');
        logElement.className = `log-entry ${logEntry.level.toLowerCase()}`;
        logElement.innerHTML = `
            <span class="timestamp">${logEntry.timestamp}</span>
            <span class="level">[${logEntry.level}]</span>
            <span class="message">${logEntry.message}</span>
        `;

        logOutput.appendChild(logElement);
        logOutput.scrollTop = logOutput.scrollHeight;
    }

    clear() {
        this.logs = [];
        const logOutput = document.getElementById('logOutput');
        if (logOutput) {
            logOutput.innerHTML = `
                <div class="log-entry info">
                    <span class="timestamp">${new Date().toLocaleString('en-US', { hour12: false })}</span>
                    <span class="level">[INFO]</span>
                    <span class="message">Log cleared</span>
                </div>
            `;
        }
    }

    getInstanceId() {
        return this.instanceId;
    }
}

// Command Pattern Implementation
class SmartDevice {
    constructor(name, location) {
        this.name = name;
        this.location = location;
        this.state = 'OFF';
        this.previousState = 'OFF';
    }

    setState(state) {
        this.previousState = this.state;
        this.state = state;
        this.updateUI();
    }

    getState() {
        return this.state;
    }

    getPreviousState() {
        return this.previousState;
    }

    updateUI() {
        const statusElement = document.getElementById(`${this.location}Status`);
        if (statusElement) {
            statusElement.textContent = this.state;
            statusElement.className = `device-status ${this.state.toLowerCase().replace(' ', '-')}`;
        }
    }
}

class SmartLight extends SmartDevice {
    constructor(location) {
        super('Smart Light', location);
        this.brightness = 100;
    }

    turnOn() {
        this.setState('ON');
    }

    turnOff() {
        this.setState('OFF');
    }

    dim() {
        this.brightness = 30;
        this.setState('DIM');
    }
}

class SmartThermostat extends SmartDevice {
    constructor() {
        super('Smart Thermostat', 'thermostat');
        this.temperature = 22;
        this.setState('22°C (AUTO)');
    }

    setTemperature(temp) {
        this.temperature = temp;
        this.setState(`${temp}°C (AUTO)`);
    }

    getTemperature() {
        return this.temperature;
    }
}

class SecuritySystem extends SmartDevice {
    constructor() {
        super('Security System', 'security');
        this.setState('DISARMED');
    }

    arm() {
        this.setState('ARMED');
    }

    disarm() {
        this.setState('DISARMED');
    }
}

// Command Classes
class Command {
    execute() {
        throw new Error('Execute method must be implemented');
    }

    undo() {
        throw new Error('Undo method must be implemented');
    }

    getDescription() {
        return 'Generic Command';
    }
}

class LightCommand extends Command {
    constructor(light, action) {
        super();
        this.light = light;
        this.action = action;
        this.previousState = null;
    }

    execute() {
        this.previousState = this.light.getState();
        switch(this.action) {
            case 'on':
                this.light.turnOn();
                break;
            case 'off':
                this.light.turnOff();
                break;
            case 'dim':
                this.light.dim();
                break;
        }
    }

    undo() {
        if (this.previousState === 'ON') {
            this.light.turnOn();
        } else if (this.previousState === 'OFF') {
            this.light.turnOff();
        } else if (this.previousState === 'DIM') {
            this.light.dim();
        }
    }

    getDescription() {
        return `${this.light.location} Light ${this.action.toUpperCase()}`;
    }
}

class ThermostatCommand extends Command {
    constructor(thermostat, temperature) {
        super();
        this.thermostat = thermostat;
        this.temperature = temperature;
        this.previousTemperature = null;
    }

    execute() {
        this.previousTemperature = this.thermostat.getTemperature();
        this.thermostat.setTemperature(this.temperature);
    }

    undo() {
        this.thermostat.setTemperature(this.previousTemperature);
    }

    getDescription() {
        return `Set Thermostat to ${this.temperature}°C`;
    }
}

class SecurityCommand extends Command {
    constructor(security, action) {
        super();
        this.security = security;
        this.action = action;
        this.previousState = null;
    }

    execute() {
        this.previousState = this.security.getState();
        if (this.action === 'arm') {
            this.security.arm();
        } else {
            this.security.disarm();
        }
    }

    undo() {
        if (this.previousState === 'ARMED') {
            this.security.arm();
        } else {
            this.security.disarm();
        }
    }

    getDescription() {
        return `Security System ${this.action.toUpperCase()}`;
    }
}

class MacroCommand extends Command {
    constructor(commands) {
        super();
        this.commands = commands;
    }

    execute() {
        this.commands.forEach(command => command.execute());
    }

    undo() {
        // Undo in reverse order
        this.commands.slice().reverse().forEach(command => command.undo());
    }

    getDescription() {
        return `Macro: ${this.commands.map(cmd => cmd.getDescription()).join(', ')}`;
    }
}

// Smart Home Controller
class SmartHomeController {
    constructor() {
        this.commandHistory = [];
        this.devices = {
            livingRoomLight: new SmartLight('livingRoom'),
            bedroomLight: new SmartLight('bedroom'),
            thermostat: new SmartThermostat(),
            security: new SecuritySystem()
        };
    }

    executeCommand(command) {
        command.execute();
        this.commandHistory.push(command);
        this.addToHistory(command.getDescription());
    }

    undo() {
        if (this.commandHistory.length > 0) {
            const lastCommand = this.commandHistory.pop();
            lastCommand.undo();
            this.addToHistory(`UNDO: ${lastCommand.getDescription()}`);
        }
    }

    addToHistory(description) {
        const historyElement = document.getElementById('commandHistory');
        if (!historyElement) return;

        const timestamp = new Date().toLocaleTimeString('en-US', { hour12: false });
        const entryElement = document.createElement('div');
        entryElement.className = 'history-entry';
        entryElement.innerHTML = `
            <span class="timestamp">${timestamp}</span>
            <span class="command">${description}</span>
        `;

        historyElement.appendChild(entryElement);
        historyElement.scrollTop = historyElement.scrollHeight;
    }

    clearHistory() {
        const historyElement = document.getElementById('commandHistory');
        if (historyElement) {
            historyElement.innerHTML = `
                <div class="history-entry">
                    <span class="timestamp">${new Date().toLocaleTimeString('en-US', { hour12: false })}</span>
                    <span class="command">History cleared</span>
                </div>
            `;
        }
        this.commandHistory = [];
    }
}

// Global instances
let smartHomeController;

// Initialize when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    // Initialize smart home controller
    smartHomeController = new SmartHomeController();

    // Initialize logger with welcome message
    const logger = Logger.getInstance();
    logger.log('INFO', 'Website loaded successfully');

    // Smooth scrolling for navigation links
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });

    // Pattern card click handlers
    document.querySelectorAll('.pattern-card').forEach(card => {
        card.addEventListener('click', function() {
            const pattern = this.dataset.pattern;
            const targetSection = document.getElementById(pattern);
            if (targetSection) {
                targetSection.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });

    // Initialize temperature slider
    updateTempDisplay();
});

// Singleton Pattern Functions
function updateConfig() {
    const dbUrl = document.getElementById('dbUrl').value;
    const apiKey = document.getElementById('apiKey').value;

    const config = AppSetting.getInstance();
    config.setDatabaseUrl(dbUrl);
    config.setApiKey(apiKey);

    // Show success feedback
    const button = event.target;
    const originalText = button.innerHTML;
    button.innerHTML = '<i class="fas fa-check me-2"></i>Updated!';
    button.classList.add('btn-success');
    button.classList.remove('btn-primary');

    setTimeout(() => {
        button.innerHTML = originalText;
        button.classList.remove('btn-success');
        button.classList.add('btn-primary');
    }, 2000);
}

function verifySingleton() {
    const instance1 = AppSetting.getInstance();
    const instance2 = AppSetting.getInstance();

    const hash1 = document.getElementById('hash1');
    const hash2 = document.getElementById('hash2');
    const identityCheck = document.getElementById('identityCheck');

    hash1.textContent = `#${instance1.getInstanceId()}`;
    hash2.textContent = `#${instance2.getInstanceId()}`;

    const isSame = instance1 === instance2;
    identityCheck.textContent = isSame ? '✓ Same Instance' : '✗ Different Instances';
    identityCheck.className = `identity-check ${isSame ? 'same' : 'different'}`;
}

function logMessage() {
    const level = document.getElementById('logLevel').value;
    const message = document.getElementById('logMessage').value;

    if (!message.trim()) return;

    const logger = Logger.getInstance();
    logger.log(level, message);

    // Clear input
    document.getElementById('logMessage').value = '';
}

function clearLog() {
    const logger = Logger.getInstance();
    logger.clear();
}

function verifyLogger() {
    const logger1 = Logger.getInstance();
    const logger2 = Logger.getInstance();

    const verificationElement = document.getElementById('loggerVerification');
    const isSame = logger1 === logger2;

    verificationElement.innerHTML = `
        <div class="d-flex justify-content-between align-items-center mb-2">
            <small>Instance 1 ID:</small>
            <code>#${logger1.getInstanceId()}</code>
        </div>
        <div class="d-flex justify-content-between align-items-center mb-2">
            <small>Instance 2 ID:</small>
            <code>#${logger2.getInstanceId()}</code>
        </div>
        <div class="text-center">
            <span class="badge ${isSame ? 'bg-success' : 'bg-danger'}">
                ${isSame ? '✓ Same Instance (Singleton)' : '✗ Different Instances'}
            </span>
        </div>
    `;
}

// Command Pattern Functions
function executeCommand(commandType, param) {
    let command;

    switch(commandType) {
        case 'lightOn':
            command = new LightCommand(smartHomeController.devices[`${param}Light`], 'on');
            break;
        case 'lightOff':
            command = new LightCommand(smartHomeController.devices[`${param}Light`], 'off');
            break;
        case 'lightDim':
            command = new LightCommand(smartHomeController.devices[`${param}Light`], 'dim');
            break;
        case 'setTemp':
            command = new ThermostatCommand(smartHomeController.devices.thermostat, parseInt(param));
            break;
        case 'securityArm':
            command = new SecurityCommand(smartHomeController.devices.security, 'arm');
            break;
        case 'securityDisarm':
            command = new SecurityCommand(smartHomeController.devices.security, 'disarm');
            break;
    }

    if (command) {
        smartHomeController.executeCommand(command);
    }
    smartHomeController.clearHistory();
}

function undoCommand() {
    smartHomeController.undo();
}

function executeGoodNightMacro() {
    const commands = [
        new LightCommand(smartHomeController.devices.livingRoomLight, 'off'),
        new LightCommand(smartHomeController.devices.bedroomLight, 'off'),
        new ThermostatCommand(smartHomeController.devices.thermostat, 20),
        new SecurityCommand(smartHomeController.devices.security, 'arm')
    ];

    const macroCommand = new MacroCommand(commands);
    smartHomeController.executeCommand(macroCommand);
}

function refreshDevices() {
    // Refresh all device states in UI
    Object.values(smartHomeController.devices).forEach(device => {
        device.updateUI();
    });

    smartHomeController.addToHistory('Device status refreshed');
}

function clearHistory() {
    smartHomeController.clearHistory();
}

function updateTempDisplay() {
    const tempSlider = document.getElementById('tempSlider');
    const tempDisplay = document.getElementById('tempDisplay');

    if (tempSlider && tempDisplay) {
        tempDisplay.textContent = `${tempSlider.value}°C`;
    }
}

// --- Abstract Factory Pattern --- //

// Product Interfaces
class Button {
    render() { throw new Error("Render method must be implemented"); }
}
class ScrollBar {
    render() { throw new Error("Render method must be implemented"); }
}

// Concrete Products for Windows
class WindowsButton extends Button {
    render() {
        return '<button class="btn btn-light border ms-2 me-2 af-button-windows">Save</button>';
    }
}
class WindowsScrollBar extends ScrollBar {
    render() {
        return `
            <div class="af-scrollbar-container">
                <small class="text-muted">Windows Scrollbar</small>
                <div class="scrollbar-windows"></div>
            </div>
        `;
    }
}

// Concrete Products for macOS
class MacButton extends Button {
    render() {
        return '<button class="btn btn-primary ms-2 me-2 af-button-mac">Save</button>';
    }
}
class MacScrollBar extends ScrollBar {
    render() {
        return `
            <div class="af-scrollbar-container">
                <small class="text-muted">macOS Scrollbar</small>
                <div class="scrollbar-mac"></div>
            </div>
        `;
    }
}

// Abstract Factory
class UIFactory {
    createButton() { throw new Error("createButton must be implemented"); }
    createScrollBar() { throw new Error("createScrollBar must be implemented"); }
}

// Concrete Factories
class WindowsFactory extends UIFactory {
    createButton() { return new WindowsButton(); }
    createScrollBar() { return new WindowsScrollBar(); }
}

class MacFactory extends UIFactory {
    createButton() { return new MacButton(); }
    createScrollBar() { return new MacScrollBar(); }
}

function generateUI() {
    const osType = document.getElementById('os-type-select').value;
    let factory;

    if (osType === 'windows') {
        factory = new WindowsFactory();
    } else {
        factory = new MacFactory();
    }

    const button = factory.createButton();
    const scrollbar = factory.createScrollBar();

    const previewContainer = document.getElementById('ui-preview-container');
    previewContainer.innerHTML = `
        <div class="generated-ui-item">${button.render()}</div>
        <div class="generated-ui-item">${scrollbar.render()}</div>
        <p class="text-success small mt-3 fw-bold">Successfully generated UI Kit for ${osType.toUpperCase()}.</p>
    `;
}

// --- Factory Method Pattern --- //

// Product Interface
class Transport {
    deliver() { throw new Error("Deliver method must be implemented"); }
    getIcon() { return '🚚'; } // Default icon
    getType() { return 'Transport'; } // Default type
}

// Concrete Products
class Truck extends Transport {
    deliver() {
        return '🚚 Delivering by land in a truck.';
    }
    getIcon() { return '🚚'; }
    getType() { return 'Truck'; }
}

class Ship extends Transport {
    deliver() {
        return '🚢 Delivering by sea in a ship.';
    }
    getIcon() { return '🚢'; }
    getType() { return 'Ship'; }
}

// Creator
class Logistics {
    planDelivery() {
        const transport = this.createTransport();
        return transport; // Return the object itself
    }
    createTransport() { throw new Error("createTransport must be implemented"); }
}

// Concrete Creators
class RoadLogistics extends Logistics {
    createTransport() { return new Truck(); }
}

class SeaLogistics extends Logistics {
    createTransport() { return new Ship(); }
}

function planDelivery() {
    const deliveryType = document.querySelector('input[name="deliveryType"]:checked').value;
    let logistics;

    if (deliveryType === 'road') {
        logistics = new RoadLogistics();
    } else {
        logistics = new SeaLogistics();
    }

    const transport = logistics.planDelivery();
    const outputElement = document.getElementById('logistics-output');
    
    outputElement.innerHTML = `
        <div class="delivery-plan-card">
            <div class="plan-icon">${transport.getIcon()}</div>
            <div>
                <h6 class="mb-1">${logistics.constructor.name}</h6>
                <p class="mb-0 text-muted small">Transport: <strong>${transport.getType()}</strong></p>
                <p class="mb-0 text-muted small">Status: <strong>Processing...</strong></p>
            </div>
        </div>
        <div class="alert alert-success mt-3 small">${transport.deliver()}</div>
    `;
}

// Handle Enter key in log message input
document.addEventListener('keypress', function(e) {
    if (e.target.id === 'logMessage' && e.key === 'Enter') {
        logMessage();
    }
}

// Navbar scroll effect
window.addEventListener('scroll', function() {
    const navbar = document.querySelector('.navbar');
    if (window.scrollY > 50) {
        navbar.style.backgroundColor = 'rgba(13, 110, 253, 0.95)';
    } else {
        navbar.style.backgroundColor = 'var(--primary-color)';
    }
});

// Add fade-in animation to sections when they come into view
const observerOptions = {
    threshold: 0.1,
    rootMargin: '0px 0px -50px 0px'
};

const observer = new IntersectionObserver(function(entries) {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            entry.target.classList.add('fade-in-up');
        }
    });
}, observerOptions);

// Observe all pattern sections
document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('.pattern-section').forEach(section => {
        observer.observe(section);
    });
});
