package org.littletonrobotics.junction.inputs;

import org.littletonrobotics.conduit.ConduitApi;
import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj.PneumaticsControlModule;
import edu.wpi.first.hal.CTREPCMJNI;

/**
 * Manages logging pneumatics control module data.
 */
public class LoggedPneumaticsControlModule {
  private static LoggedPneumaticsControlModule instance;

  private final PneumaticsControlModuleInputs pcmInputs = new PneumaticsControlModuleInputs();

  private int moduleID;

  public LoggedPneumaticsControlModule(int moduleID) {
    ConduitApi.getInstance().configurePneumaticsControlModule(moduleID);
  }

  private LoggedPneumaticsControlModule() {
    moduleID = 0;
    ConduitApi.getInstance().configurePneumaticsControlModule(moduleID);
  }

  public static LoggedPneumaticsControlModule getInstance() {
    if (instance == null) {
      instance = new LoggedPneumaticsControlModule();
    }
    return instance;
  }

  public static class PneumaticsControlModuleInputs implements LoggableInputs {
    public boolean compressor = false;
    public boolean pressureSwitch = false;
    public boolean[] solenoidStates = new boolean[8];
    public boolean[] solenoidValid = new boolean[8];
    public int moduleId = 0;
    public double compressorCurrent = 0;
    public boolean closedLoopControl = false;
    public boolean pressureSwitchValve = false;
    public boolean compressorCurrentTooHighFault = false;
    public boolean compressorCurrentTooHighStickyFault = false;
    public boolean compressorShortedFault = false;
    public boolean compressorShortedStickyFault = false;
    public boolean compressorNotConnectedFault = false;
    public boolean compressorNotConnectedStickyFault = false;
    public boolean solenoidVoltageFault = false;
    public boolean solenoidVoltageStickyFault = false;

    @Override
    public void toLog(LogTable table) {
      table.put("Compressor", compressor);
      table.put("Pressure Switch", pressureSwitch);
      table.put("Solenoid States", solenoidStates);
      table.put("Module ID", moduleId);
      table.put("Compressor Current", compressorCurrent);
      table.put("Closed Loop Control", closedLoopControl);
      table.put("Pressure Switch Valve", pressureSwitchValve);
      table.put("Faults", new boolean[] { compressorCurrentTooHighFault, compressorShortedFault, compressorNotConnectedFault, solenoidVoltageFault });
      table.put("Sticky Faults", new boolean[] { compressorCurrentTooHighStickyFault, compressorShortedStickyFault, compressorNotConnectedStickyFault, solenoidVoltageStickyFault });
    }
    @Override
    public void fromLog(LogTable table) {
      compressor = table.get("Compressor On", compressor);
      pressureSwitch = table.get("Pressure Switch", pressureSwitch);
      solenoidStates = table.get("Solenoid States", solenoidStates);
      moduleId = table.get("Module ID", moduleId);
      compressorCurrent = table.get("Compressor Current", compressorCurrent);
      closedLoopControl = table.get("Closed Loop Control", closedLoopControl);
      pressureSwitchValve = table.get("Pressure Switch Valve", pressureSwitchValve);
      boolean[] faults = table.get("Faults", new boolean[] { compressorCurrentTooHighFault, compressorShortedFault, compressorNotConnectedFault, solenoidVoltageFault });
      compressorCurrentTooHighFault = faults[0];
      compressorShortedFault = faults[1];
      compressorNotConnectedFault = faults[2];
      solenoidVoltageFault = faults[3];
      boolean[] stickyFaults = table.get("Sticky Faults", new boolean[] { compressorCurrentTooHighStickyFault, compressorShortedStickyFault, compressorNotConnectedStickyFault, solenoidVoltageStickyFault });
      compressorCurrentTooHighStickyFault = stickyFaults[0];
      compressorShortedStickyFault = stickyFaults[1];
      compressorNotConnectedStickyFault = stickyFaults[2];
      solenoidVoltageStickyFault = stickyFaults[3];
    }
  }
  public void periodic() {
    if (!Logger.hasReplaySource()) {
      ConduitApi conduit = ConduitApi.getInstance();
      pcmInputs.solenoidStates = conduit.getSolenoidStates();
      pcmInputs.compressor = conduit.getCompressor();
      pcmInputs.pressureSwitch = conduit.getPressureSwitch();
      pcmInputs.moduleId = conduit.getModuleNumber();
      pcmInputs.compressorCurrent = conduit.getCompressorCurrent();
      pcmInputs.closedLoopControl = conduit.getClosedLoopControl();
      pcmInputs.compressorCurrentTooHighFault = conduit.getCompressorCurrentTooHighFault();
      pcmInputs.compressorCurrentTooHighStickyFault = conduit.getCompressorCurrentTooHighStickyFault();
      pcmInputs.compressorShortedFault = conduit.getCompressorShortedFault();
      pcmInputs.compressorShortedStickyFault = conduit.getCompressorShortedStickyFault();
      pcmInputs.compressorNotConnectedFault = conduit.getCompressorNotConnectedFault();
      pcmInputs.compressorNotConnectedStickyFault = conduit.getCompressorNotConnectedStickyFault();
      pcmInputs.solenoidVoltageFault = conduit.getSolenoidVoltageFault();
      pcmInputs.solenoidVoltageStickyFault = conduit.getSolenoidVoltageStickyFault();
    }

    Logger.processInputs("CTREPCM", pcmInputs);
  }

  public PneumaticsControlModuleInputs getInputs() {
    return pcmInputs;
  }
}
