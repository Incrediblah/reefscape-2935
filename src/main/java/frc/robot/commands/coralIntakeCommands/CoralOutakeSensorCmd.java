// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.coralIntakeCommands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.CoralIntakeConstants;
import frc.robot.subsystems.CoralIntakeSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class CoralOutakeSensorCmd extends Command {

  private final CoralIntakeSubsystem INTAKE_SUBSYSTEM;
  private final Timer timer = new Timer();



  /** Creates a new CoralIntakeCmd. */
  public CoralOutakeSensorCmd(CoralIntakeSubsystem s_CoralIntakeSubsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.INTAKE_SUBSYSTEM  = s_CoralIntakeSubsystem;
    addRequirements(s_CoralIntakeSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.stop();
    timer.reset();

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (!INTAKE_SUBSYSTEM.getIntakeSwitchValue()) {
        timer.start(); // Start the 500ms timer when the sensor is triggered
        INTAKE_SUBSYSTEM.setCoralIntakeSpeed(CoralIntakeConstants.kCoralSlowOutakeSpeed);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    INTAKE_SUBSYSTEM.setCoralIntakeSpeed(0);
 
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(INTAKE_SUBSYSTEM.getIntakeSwitchValue() && timer.hasElapsed(0.50)){
      return true; 
    }else{
      return false; 
    }
  }
  
}
