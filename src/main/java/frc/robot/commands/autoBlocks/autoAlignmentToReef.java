// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.autoBlocks;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.ArmConstants;
import frc.robot.Constants.CoralIntakeConstants;
import frc.robot.Constants.ElevatorConstants;
import frc.robot.Constants.VisionConstants;
import frc.robot.commands.DriveForwardDistance;
import frc.robot.commands.MoveArmToSetpoint;
import frc.robot.commands.coralIntakeCommands.CoralIntakeCmd;
import frc.robot.commands.limelightCommands.TurnToAprilTagCommand;
import frc.robot.commands.limelightCommands.alignXLeftCamera;
import frc.robot.commands.limelightCommands.alignXandYLeftCamera;
import frc.robot.commands.limelightCommands.alignXandYRightCamera;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.CoralIntakeSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.VisionSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class autoAlignmentToReef extends SequentialCommandGroup {
  
  Command visionCommand; 
  
  /** Creates a new autoScoreCoral. */
  public autoAlignmentToReef(DriveSubsystem drive, VisionSubsystem vision, ElevatorSubsystem elevator, ArmSubsystem arm, CoralIntakeSubsystem intake, String reefside,boolean endCommand) {
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
      // new ParallelCommandGroup(

      //   new SequentialCommandGroup(
      //     visionCommand, 
      //     new DriveForwardDistance(drive, 0.15, 0.25, false)
      //   ), 


      //   new MoveArmToSetpoint(arm, ArmConstants.kLevel4)
      // )
      
      new TurnToAprilTagCommand(drive, vision, false), 
      visionCommand,
      new DriveForwardDistance(drive, 0.15, 0.4, false)
    );

    drive.adjustGyroToAngle(vision.getReefAngleForTag(vision.getBestAprilTag())); 
  }
}
