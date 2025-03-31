// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.autoBlocks;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.driveCommands.DriveDistanceCmd;
import frc.robot.commands.photonCommands.AlignXandYWithPhoton;
import frc.robot.commands.photonCommands.PhotonTurnToTagCmd;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.photonVisionConstants;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.PhotonSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class autoAlignmentToFeeder extends SequentialCommandGroup {
  /** Creates a new autoAlignmentToFeeder. */


  double xSet; 
  double ySet; 
  public autoAlignmentToFeeder(DriveSubsystem drive, PhotonSubsystem photon,boolean endCommand, String reefside) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());

    if(reefside == "left"){
      xSet = photonVisionConstants.reefLeftCamTagYaw; 
      ySet = photonVisionConstants.reefLeftCamTagPitch; 
    }else if(reefside == "right"){
      xSet = photonVisionConstants.reefRightCamTagYaw; 
      ySet = photonVisionConstants.reefRightCamTagPitch; 
    }

    addCommands(
      // new PhotonTurnToTagCmd(drive, photon, endCommand,AutoConstants.autoMode),

       new AlignXandYWithPhoton(drive, photon, endCommand, xSet, ySet, photonVisionConstants.xTol, photonVisionConstants.yTol), 
       new DriveDistanceCmd(drive, 0.18, 0.40, endCommand, 1500)
    );
    drive.adjustGyroToAngle(photon.getReefAngleForTag(photon.getBestAprilTagID(),AutoConstants.autoMode)); 
  }
}
