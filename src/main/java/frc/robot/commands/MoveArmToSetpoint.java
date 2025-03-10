// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ArmSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class MoveArmToSetpoint extends Command {

  private final ArmSubsystem s_ArmSubsystem;
  private final double Setpoint;

  /** Creates a new MoveArmToSetpoint. */
  public MoveArmToSetpoint(ArmSubsystem s_ArmSubsystem, double Setpoint) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.s_ArmSubsystem = s_ArmSubsystem;
    this.Setpoint = Setpoint;
    addRequirements(s_ArmSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    s_ArmSubsystem.moveArmToPosition(Setpoint);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return Math.abs(s_ArmSubsystem.getArmPosition() - Setpoint) < 1.0;
  }
}
