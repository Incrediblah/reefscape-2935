// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.autoBlocks;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.VisionConstants;
import frc.robot.commands.driveCommands.DriveDistanceCmd;
import frc.robot.commands.limelightCommands.TurnToAprilTagCommand;
import frc.robot.commands.limelightCommands.alignXandYLeftCamera;
import frc.robot.commands.limelightCommands.alignXandYRightCamera;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.VisionSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class autoAlignmentToReef extends SequentialCommandGroup {
  
  Command visionCommand; 
  

  // double adjustmentVariable = 0; 

  /** Creates a new autoScoreCoral. */
  public autoAlignmentToReef(DriveSubsystem drive, VisionSubsystem vision, String reefside,boolean endCommand, String mode, double targetHeading) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    Command alignLeft = new alignXandYRightCamera(drive, vision, 0, endCommand, VisionConstants.rightCamTagX, VisionConstants.rightCamTagY, VisionConstants.xTol, VisionConstants.yTol, targetHeading); 
    Command alignRight = new alignXandYLeftCamera(drive, vision, 0, endCommand, VisionConstants.leftCamTagX, VisionConstants.leftCamTagY, VisionConstants.xTol, VisionConstants.yTol, targetHeading); 

    if(reefside == "left"){
      visionCommand = alignLeft; 
    }else if(reefside == "right"){
      visionCommand = alignRight; 
    }

    // if(mode == AutoConstants.autoMode){
    //   adjustmentVariable = 0; 
    // }else{
    //   adjustmentVariable = 180; 
    // }

    addCommands(
      // new TurnToAprilTagCommand(drive, vision, endCommand, mode), 
      visionCommand, 
      new DriveDistanceCmd(drive, 0.18, 0.45, endCommand, 1500), 
      Commands.waitSeconds(0.5)
    );


    drive.adjustGyroToAngle(vision.getReefAngleForTag(vision.getBestAprilTag(), AutoConstants.autoMode)); 

  }
}
