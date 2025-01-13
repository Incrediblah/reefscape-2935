// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ArmSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ArmCmd extends Command {
  private final ArmSubsystem armSubsystem;
  private final double targetAngle;

  /** Creates a new ArmCmd. */
  public ArmCmd(ArmSubsystem armSubsystem, double targetAngle ) {
    this.armSubsystem = armSubsystem;
    this.targetAngle = targetAngle;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

    armSubsystem.setarm1Position(targetAngle);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {

    armSubsystem.setarm1Position(0); 
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    double currentPosition = armSubsystem.getCurrentPosition();
    double targetPosition = targetAngle * (1000.0 / 360.0); // Convert target angle to encoder counts
    double tolerance = 50; // Allow a small tolerance (adjust as needed)

    return Math.abs(currentPosition - targetPosition) < tolerance;
  
  }
}
