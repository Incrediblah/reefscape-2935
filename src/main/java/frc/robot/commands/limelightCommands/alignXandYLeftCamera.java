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
public class alignXandYLeftCamera extends Command {

  
  private VisionSubsystem VISION_SUBSYSTEM; 
  private DriveSubsystem DRIVE_SUBSYSTEM; 

  private PIDController strafePID; 
  private PIDController drivePID; 

  private boolean endCommand; 
  private int setPipelineNumber; 

  private double measuredValueX; 
  private double measuredValueY; 

  private double strafeSpeed; 
  private double driveSpeed;

  private double toleranceX;
  private double toleranceY;


  private double targetValueX; 
  private double targetValueY;


  private boolean inRangeX; 
  private boolean inRangeY; 

  /** Creates a new alignmentCommand. */
  public alignXandYLeftCamera(DriveSubsystem drive, VisionSubsystem vision, int pipeline, boolean end, double targetOffsetX, double targetOffsetY, double toleranceX, double toleranceY) {
    // Use addRequirements() here to declare subsystem dependencies.
    
    this.DRIVE_SUBSYSTEM = drive; 
    this.VISION_SUBSYSTEM = vision; 

    this.drivePID = new PIDController(0.04, 0, 0); 
    this.strafePID = new PIDController(0.015, 0, 0); 

    this.endCommand = end; 
    this.setPipelineNumber = pipeline; 

    this.targetValueX = targetOffsetX;
    this.targetValueY = targetOffsetY;  

    this.toleranceX = toleranceX; 
    this.toleranceY = toleranceY; 
  
    addRequirements(DRIVE_SUBSYSTEM);
    addRequirements(VISION_SUBSYSTEM);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    drivePID.reset();
    strafePID.reset();
    VISION_SUBSYSTEM.setLeftPipeline(setPipelineNumber);
    inRangeX = false; 
    inRangeY = false; 
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    
    if(VISION_SUBSYSTEM.limelightLeftTargetSeen()){
      measuredValueY = VISION_SUBSYSTEM.getLeftTy();
      measuredValueX = VISION_SUBSYSTEM.getLeftTx();  

      //x-direction 
      if (Math.abs(targetValueX - measuredValueX) <= toleranceX) { 
        strafeSpeed = 0; 
        inRangeX = true; 
      } 
      else{
        strafeSpeed = strafePID.calculate(measuredValueX, targetValueX);
      }

      if(strafeSpeed > 0.15){
        strafeSpeed = 0.15; 
      }else if(strafeSpeed < -0.15){
        strafeSpeed = -0.15; 
      }

      // y-direction 
      if (Math.abs(targetValueY - measuredValueY) <= toleranceY) { 
        driveSpeed = 0; 
        inRangeY = true; 
      } 
      else{
        driveSpeed = drivePID.calculate(measuredValueY, targetValueY);
      }
     

      if(driveSpeed > 0.15){
        driveSpeed = 0.15; 
      }else if(driveSpeed < -0.15){
        driveSpeed = -0.15; 
      }
      
    }else{
      driveSpeed = 0; 
    }

    SmartDashboard.putNumber("align speed", driveSpeed); 
    SmartDashboard.putBoolean("in range x", inRangeX); 
    SmartDashboard.putBoolean("in range y", inRangeY); 
    DRIVE_SUBSYSTEM.drive(-driveSpeed, -strafeSpeed, 0, false);

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    driveSpeed = 0;
    strafeSpeed = 0; 
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(endCommand){
      return true; 
    }else if(inRangeX && inRangeY){
      return true; 
    }
    else{
      return false; 
    }
  }
}
