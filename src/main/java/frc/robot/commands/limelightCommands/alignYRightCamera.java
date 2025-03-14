// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.limelightCommands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.subsystems.DriveSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class alignYRightCamera extends Command {

  private VisionSubsystem VISION_SUBSYSTEM; 
  private DriveSubsystem DRIVE_SUBSYSTEM; 

  private PIDController drivePIDArea; 

  private boolean endCommand; 
  private int setPipelineNumber; 

  private double measuredValueArea; 

  private double driveSpeedA; 

  private double targetValueArea; 

  private double areaTolreance; 
  private boolean inRangeArea;  


  private int missedCounter; 


  /** Creates a new ali
   * gnXLeftCamera. */
  public alignYRightCamera(DriveSubsystem drive, VisionSubsystem vision, int pipeline, boolean end, double targetArea ,double toleranceArea) {
    // Use addRequirements() here to declare subsystem dependencies.

    
    this.DRIVE_SUBSYSTEM = drive; 
    this.VISION_SUBSYSTEM = vision; 

    this.drivePIDArea = new PIDController(0.15, 0, 0); 
    this.endCommand = end; 
    this.setPipelineNumber = pipeline; 
    this.targetValueArea = targetArea; 
    this.areaTolreance = toleranceArea; 

    addRequirements(VISION_SUBSYSTEM);
    addRequirements(DRIVE_SUBSYSTEM);

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    drivePIDArea.reset();
    VISION_SUBSYSTEM.setRightPipeline(setPipelineNumber);
    inRangeArea = false; 
    missedCounter = 0; 
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(VISION_SUBSYSTEM.limelightRightTargetSeen()){

      measuredValueArea = VISION_SUBSYSTEM.getRightTy(); 
      

      if (Math.abs(targetValueArea - measuredValueArea) <= areaTolreance){ 
        driveSpeedA = 0; 
        inRangeArea = true;
      } 

        
      else{
        driveSpeedA = drivePIDArea.calculate(measuredValueArea, targetValueArea);
        inRangeArea = false; 
     
      }
     
      
      if(driveSpeedA > 0.25){
        driveSpeedA = 0.25; 
      }else if(driveSpeedA < -0.25){
        driveSpeedA = -0.25; 
      }
      
    }else{
      driveSpeedA = 0; 
      missedCounter += 1; 
    }

    SmartDashboard.putNumber("align speed", driveSpeedA); 
    DRIVE_SUBSYSTEM.drive(-driveSpeedA, 0, 0, false);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    driveSpeedA = 0; 
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(endCommand){
      return true; 
    }
    
    else{
      return false; 
    }

  }

  public double correctedTa(double ta){    

    double rollRadians = Math.toRadians(30); 
    double taTrue = ta / Math.cos(rollRadians); 

    return taTrue; 
  }
}
