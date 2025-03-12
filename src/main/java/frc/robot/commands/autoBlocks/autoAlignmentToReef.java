// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.autoBlocks;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.VisionConstants;
import frc.robot.commands.DriveForwardDistance;
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
  
  /** Creates a new autoScoreCoral. */
  public autoAlignmentToReef(DriveSubsystem drive, VisionSubsystem vision, String reefside,boolean endCommand) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    Command alignLeft = new alignXandYRightCamera(drive, vision, 0, false, VisionConstants.rightCamTagX, VisionConstants.rightCamTagY, VisionConstants.xTol, VisionConstants.yTol); 
    Command alignRight = new alignXandYLeftCamera(drive, vision, 0, false, VisionConstants.leftCamTagX, VisionConstants.leftCamTagY, VisionConstants.xTol, VisionConstants.yTol); 

    if(reefside == "left"){
      visionCommand = alignLeft; 
    }else if(reefside == "right"){
      visionCommand = alignRight; 
    }

    addCommands(
      new TurnToAprilTagCommand(drive, vision, false), 
      visionCommand,
      new DriveForwardDistance(drive, 0.15, 0.4, false)
    );

    drive.adjustGyroToAngle(vision.getReefAngleForTag(vision.getBestAprilTag())); 
  }
}
