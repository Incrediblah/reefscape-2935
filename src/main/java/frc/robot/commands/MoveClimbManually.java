// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ClimbSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class MoveClimbManually extends Command {

  private final ClimbSubsystem s_ClimbSubsystem;
  private final double speed;

  /** Creates a new MoveClimbManually. */
  public MoveClimbManually(ClimbSubsystem s_ClimbSubsystem, double speed) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.s_ClimbSubsystem = s_ClimbSubsystem;
    this.speed = speed;
    addRequirements(s_ClimbSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    s_ClimbSubsystem.setClimbSpeed(speed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
