// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ClimbSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class MoveClimbToSetpoint extends Command {

  private final ClimbSubsystem s_ClimbSubsystem;
  private final double Setpoint;

  /** Creates a new MoveClimbToSetpoint. */
  public MoveClimbToSetpoint(ClimbSubsystem s_ClimbSubsystem, double Setpoint) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.s_ClimbSubsystem = s_ClimbSubsystem;
    this.Setpoint = Setpoint;
    addRequirements(s_ClimbSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    s_ClimbSubsystem.moveClimbToPosition(Setpoint);
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
    return Math.abs(s_ClimbSubsystem.getClimbPosition() - Setpoint) < 1.0;
  }
}
