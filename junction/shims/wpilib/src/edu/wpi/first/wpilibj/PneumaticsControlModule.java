package edu.wpi.first.wpilibj;

import edu.wpi.first.hal.CTREPCMJNI;
import java.util.HashMap;
import java.util.Map;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;

import org.littletonrobotics.junction.inputs.LoggedPneumaticsControlModule;
import org.littletonrobotics.junction.inputs.LoggedPowerDistribution;

/**
 * Class for getting compressor, pressure switch, and solenoid information from
 * the CTRE Pneumatics Control Module (PCM) over CAN. Patched by AdvantageKit to
 * support logging.
 */
public class PneumaticsControlModule implements Sendable, AutoCloseable {

  /**
   * Constructs a PneumaticsControlModule object.
   * 
   * @param module The CAN ID of the PCM.
   */
  public PneumaticsControlModule(int module) {
    LoggedPneumaticsControlModule.getInstance(module);
    int m_module = LoggedPneumaticsControlModule.getInstance().getInputs().moduleId;
    HAL.report(tResourceType.kResourceType_Pneumatics, m_module + 1);
    SendableRegistry.addLW(this, "PneumaticsControlModule", LoggedPneumaticsControlModule.getInstance().getInputs().moduleId);

  /** Constructs a PneumaticsControlModule with the default ID (0). */
  public PneumaticsControlModule() {
    LoggedPneumaticsControlModule.getInstance();
      int m_module = LoggedPowerDistribution.getInstance().getInputs().moduleId;
    HAL.report(tResourceType.kResourceType_PDP, m_module + 1);
    SendableRegistry.addLW(this, "PneumaticsControlModule", LoggedPneumaticsControlModule.getInstance().getInputs().moduleId);
  }

  @Override
  public void close() {
    SendableRegistry.remove(this);
  }

  /**
   * Get the current state of the compressor.
   * 
   * @return true if the compressor is on
   */
  public boolean getCompressor() {
    return LoggedPneumaticsControlModule.getInstance().getInputs().compressor;
  }

  /**
   * Get the current state of the pressure switch.
   * 
   * @return true if the pressure switch is closed
   */
  public boolean getPressureSwitch() {
    return LoggedPneumaticsControlModule.getInstance().getInputs().pressureSwitch;
  }

  /**
   * Get the current state of a solenoid channel.
   * 
   * @param channel The channel to check.
   * @return The current state of the solenoid channel.
   */
  public boolean getSolenoid(int channel) {
    return LoggedPneumaticsControlModule.getInstance().getInputs().solenoidStates[channel];
  }

  /**
   * Get the current state of all solenoid channels on this module.
   * 
   * @return The current state of all solenoid channels.
   */
  public int getAllSolenoids() {
    int allSolenoids = 0;
    for (int i = 0; i < 8; i++) {
      if (LoggedPneumaticsControlModule.getInstance().getInputs().solenoidStates[i]) {
        allSolenoids |= 1 << i;
      }
    }
    return allSolenoids;
  }

  /**
   * Set the state of a solenoid channel.
   * 
   * @param channel The channel to set.
   * @param value   The state to set the solenoid channel to.
   */
  public void setSolenoid(int channel, boolean value) {
    CTREPCMJNI.setSolenoid(LoggedPneumaticsControlModule.getInstance().getInputs().moduleId, channel, value);
  }

  /**
   * Set the state of all solenoid channels on this module.
   * 
   * @param mask The state to set the solenoid channels to.
   */
  public void setAllSolenoids(int mask) {
    for (int i = 0; i < 8; i++) {
      CTREPCMJNI.setSolenoid(LoggedPneumaticsControlModule.getInstance().getInputs().moduleId, i, (mask & (1 << i)) != 0);
    }
  }

  /**
   * Get the current fault status of the compressor due to the current being too high.
   * 
   * @return true if the compressor has a fault
   */
  public boolean getCompressorCurrentTooHighFault() {
    return LoggedPneumaticsControlModule.getInstance().getInputs().compressorCurrentTooHighFault;
  }

  /**
   * Get the current sticky fault status of the compressor due to the current being too high.
   * 
   * @return true if the compressor has a sticky fault
   */
  public boolean getCompressorCurrentTooHighStickyFault() {
    return LoggedPneumaticsControlModule.getInstance().getInputs().compressorCurrentTooHighStickyFault;
  }

  /**
   * Get the current fault status of the compressor due to a short.
   * 
   * @return true if the compressor has a fault
   */
  public boolean getCompressorShortedFault() {
    return LoggedPneumaticsControlModule.getInstance().getInputs().compressorShortedFault;
  }

  /**
   * Get the current sticky fault status of the compressor due to a short.
   * 
   * @return true if the compressor has a sticky fault
   */
  public boolean getCompressorShortedStickyFault() {
    return LoggedPneumaticsControlModule.getInstance().getInputs().compressorShortedStickyFault;
  }

  /**
   * Get the current fault status of the compressor due to not being connected.
   * 
   * @return true if the compressor has a fault
   */
  public boolean getCompressorNotConnectedFault() {
    return LoggedPneumaticsControlModule.getInstance().getInputs().compressorNotConnectedFault;
  }

  /**
   * Get the current sticky fault status of the compressor due to not being connected.
   * 
   * @return true if the compressor has a sticky fault
   */
  public boolean getCompressorNotConnectedStickyFault() {
    return LoggedPneumaticsControlModule.getInstance().getInputs().compressorNotConnectedStickyFault;
  }

  /**
   * Get the current fault status of the solenoid voltage.
   * 
   * @return true if the solenoid voltage is faulted
   */
  public boolean getSolenoidVoltageFault() {
    return LoggedPneumaticsControlModule.getInstance().getInputs().solenoidVoltageFault;
  }

  /**
   * Get the compressor current.
   * 
   * @return The current through the compressor in Amperes
   */
  public double getCompressorCurrent() {
    return LoggedPneumaticsControlModule.getInstance().getInputs().compressorCurrent;
  }

  /**
   * Get the module number (CAN ID).
   * 
   * @return The module number (CAN ID).
   */
  public int getModuleNumber() {
    return LoggedPneumaticsControlModule.getInstance().getInputs().moduleId;
  }

  /**
   * Get the current state of the compressor closed loop control.
   * 
   * @return true if the compressor is in closed loop control
   */
  public boolean getClosedLoopControl() {
    return LoggedPneumaticsControlModule.getInstance().getInputs().closedLoopControl;
  }

  /**
   * Checks if a solenoid channel number is valid.
   * 
   * @param channel The channel number to check.
   * @return True if the channel number is valid.
   */
  public static boolean checkSolenoidChannel(int channel) {
    return channel >= 0 && channel < 8;
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    builder.setSmartDashboardType("PneumaticsControlModule");
    builder.addBooleanProperty("Compressor", this::getCompressor, null);
    builder.addBooleanProperty("Pressure Switch", this::getPressureSwitch, null);
    for (int i = 0; i < 8; i++) {
      final int channel = i;
      builder.addBooleanProperty("Solenoid " + i, () -> getSolenoid(channel), (value) -> setSolenoid(channel, value));
    }
  }
}
