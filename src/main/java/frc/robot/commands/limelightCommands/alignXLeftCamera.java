// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.limelightCommands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.subsystems.DriveSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class alignXLeftCamera extends Command {

  private VisionSubsystem VISION_SUBSYSTEM; 
  private DriveSubsystem DRIVE_SUBSYSTEM; 

  private PIDController strafePIDx; 

  private boolean endCommand; 
  private int setPipelineNumber; 

  private double measuredValuex; 

  private double strafeSpeedx; 

  private double targetValuex; 

  private double xTolreance; 
  private boolean inRangeX; 


  private int missedCounter; 


  /** Creates a new ali
   * gnXLeftCamera. */
  public alignXLeftCamera(DriveSubsystem drive, VisionSubsystem vision, int pipeline, boolean end, double targetOffsetx ,double tolerancex) {
    // Use addRequirements() here to declare subsystem dependencies.

    
    this.DRIVE_SUBSYSTEM = drive; 
    this.VISION_SUBSYSTEM = vision; 

    this.strafePIDx = new PIDController(0.15, 0, 0); 
    this.endCommand = end; 
    this.setPipelineNumber = pipeline; 
    this.targetValuex = targetOffsetx; 
    this.xTolreance = tolerancex; 


    addRequirements(VISION_SUBSYSTEM);
    addRequirements(DRIVE_SUBSYSTEM);

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    strafePIDx.reset();
    VISION_SUBSYSTEM.setLeftPipeline(setPipelineNumber);
    inRangeX = false; 
    missedCounter = 0; 

    targetValuex = correctedTx(targetValuex); 
    xTolreance = correctedTx(xTolreance);

    SmartDashboard.putBoolean("incommand", true); 
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(VISION_SUBSYSTEM.limelightLeftTargetSeen()){

      measuredValuex = correctedTx(VISION_SUBSYSTEM.getLeftTx()); 

      if (Math.abs(targetValuex - measuredValuex) <= xTolreance){ 
        strafeSpeedx = 0; 
        inRangeX = true;
      } 

      
      else{
        strafeSpeedx = strafePIDx.calculate(measuredValuex,targetValuex);
        inRangeX = false; 
     
      }
     
      
      if(strafeSpeedx > 0.15){
        strafeSpeedx = 0.15; 
      }else if(strafeSpeedx < -0.15){
        strafeSpeedx = -0.15; 
      }
      
    }else{
      strafeSpeedx = 0; 
      missedCounter += 1; 
    }

    SmartDashboard.putNumber("align speed", strafeSpeedx); 
    DRIVE_SUBSYSTEM.drive(0, -strafeSpeedx, 0, false);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    strafeSpeedx = 0; 
    SmartDashboard.putBoolean("incommand", false); 
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

  public double correctedTx(double tx){    

    double rollRadians = Math.toRadians(30); 
    double txTrue = tx * Math.cos(rollRadians); 

    return txTrue; 
  }
}
