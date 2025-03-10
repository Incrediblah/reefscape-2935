// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DriveSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class DriveForwardDistance extends Command {


  private final DriveSubsystem DRIVE_SUBSYSTEM; 
  private double speed; 
  private final double distance; 
  private double startDistance; 
  private boolean endCommand; 

  /** Creates a new DriveForwardDistance. */
  public DriveForwardDistance(DriveSubsystem drive, double speed, double distance, boolean end) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.DRIVE_SUBSYSTEM = drive; 
    this.speed = speed; 
    this.distance = distance; 
    this.endCommand = end; 
    addRequirements(DRIVE_SUBSYSTEM);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    startDistance = DRIVE_SUBSYSTEM.getAverageDistance(); 

    if(distance < 0){
      speed = -speed;
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    DRIVE_SUBSYSTEM.drive(speed, 0, 0, false);
    SmartDashboard.putNumber("average distance", Math.abs(DRIVE_SUBSYSTEM.getAverageDistance() - startDistance)); 
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    DRIVE_SUBSYSTEM.drive(0, 0, 0, false);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {

    if(Math.abs(DRIVE_SUBSYSTEM.getAverageDistance() - startDistance) >= distance){
      return true; 
    }else if(endCommand){
      return true; 
    }else{
      return false; 
    }
  }
}
