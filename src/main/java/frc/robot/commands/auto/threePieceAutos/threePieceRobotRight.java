// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.auto.threePieceAutos;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.MoveElevatorToSetpoint;
import frc.robot.commands.armCommands.MoveArmToSetpoint;
import frc.robot.commands.auto.twoPieceAutos.twoPieceRobotRight;
import frc.robot.commands.autoBlocks.autoAlignmentToReef;
import frc.robot.commands.autoBlocks.autoScoreCoral;
import frc.robot.commands.autoBlocks.autoScoreSpecificCoral;
import frc.robot.commands.coralIntakeCommands.CoralIntakeForTimeCmd;
import frc.robot.commands.driveCommands.DriveDistanceCmd;
import frc.robot.commands.driveCommands.OdometryCmd;
import frc.robot.commands.driveCommands.TurnToAngleCommand;
import frc.robot.Constants.ArmConstants;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.ElevatorConstants;
import frc.robot.Constants.pathConstants;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.CoralIntakeSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.PhotonSubsystem;
import frc.robot.subsystems.VisionSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class threePieceRobotRight extends SequentialCommandGroup {
  /** Creates a new twoPieceRobotRight. */
  public threePieceRobotRight(DriveSubsystem drive, VisionSubsystem vision, PhotonSubsystem photon, ElevatorSubsystem elevator, ArmSubsystem arm, CoralIntakeSubsystem intake, int reefTag1, int reefTag2, int feederTag) {
    // Add your commands in the addCommands() call, e.g.


    addCommands(
      // new twoPieceRobotRight(drive, vision, photon, elevator, arm, intake, reefTag1, reefTag2, feederTag), 

      new DriveDistanceCmd(drive, 0.5, -0.25, false), 
   
      new ParallelCommandGroup(
        new SequentialCommandGroup(
          new MoveElevatorToSetpoint(elevator, ElevatorConstants.kHome), 
          new MoveArmToSetpoint(arm, ArmConstants.kHome)
        ), 


        // update paths with turns
        new OdometryCmd(drive, pathConstants.threePieceRetrieveRobotRight)
      ), 


      new ParallelCommandGroup(
        new TurnToAngleCommand(drive, 126), 

        new SequentialCommandGroup(
          new MoveElevatorToSetpoint(elevator, ElevatorConstants.kFeederStation),
          new MoveArmToSetpoint(arm, ArmConstants.kFeederStation)
        )
      ),

      new DriveDistanceCmd(drive, 0.2, -1, false), 
      new CoralIntakeForTimeCmd(intake, -1, 1000), 

      new InstantCommand(() -> drive.resetOdometry(drive.getPose())), 

      new OdometryCmd(drive, pathConstants.threePieceDepositRobotRight), 
      new autoScoreSpecificCoral(drive, vision, elevator, arm, intake, "right", reefTag2)


    );
  }
}
