// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.auto.onePieceAutos;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.ArmConstants;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.CoralIntakeConstants;
import frc.robot.Constants.CoralSystemContants;
import frc.robot.Constants.ElevatorConstants;
import frc.robot.commands.MoveElevatorToSetpoint;
import frc.robot.commands.moveCoralSystemToPosition;
import frc.robot.commands.armCommands.MoveArmToSetpoint;
import frc.robot.commands.autoBlocks.autoAlignmentToReef;
import frc.robot.commands.autoBlocks.autoScoreCoral;
import frc.robot.commands.coralIntakeCommands.CoralIntakeCmd;
import frc.robot.commands.coralIntakeCommands.CoralOutakeSensorCmd;
import frc.robot.commands.driveCommands.DriveDistanceAtRobotAngleCmd;
import frc.robot.commands.driveCommands.DriveDistanceCmd;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.CoralIntakeSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.VisionSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class onePieceRobotRight extends SequentialCommandGroup {
  /** Creates a new onePieceRobotRight. */
  public onePieceRobotRight(DriveSubsystem drive, VisionSubsystem vision, ElevatorSubsystem elevator, ArmSubsystem arm, CoralIntakeSubsystem intake, String reefside) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      
      
      new InstantCommand(() -> drive.resetOdometry(drive.getPose())), 
      new InstantCommand(() -> drive.zeroHeading()),
      new InstantCommand(() -> drive.adjustGyroToAngle(60)), 

      new DriveDistanceAtRobotAngleCmd(drive, 0.5, 1.15, -10, false, 5000), //speed was 0.8
      new autoScoreCoral(drive, vision, elevator, arm, intake, reefside, reefside, 60)

      
    //  new ParallelDeadlineGroup(
    //     new SequentialCommandGroup(
    //      new DriveDistanceCmd(drive, 0.80, 1, false, 3000), 
    //      // new DriveDistanceAtRobotAngleCmd(drive, 0.8, 1.25, -60, false, 5000), 
    //       new autoAlignmentToReef(drive, vision, "right", false, AutoConstants.autoMode)
    //     ), 

    //     new moveCoralSystemToPosition(arm, elevator, CoralSystemContants.L4), 

    //     new CoralIntakeCmd(intake, CoralIntakeConstants.kCoralNoSpeed)
    //   ), 


    //   new CoralOutakeSensorCmd(intake), 

    //   new DriveDistanceCmd(drive, 0.6, -0.5, false, 2000), 

    //   new ParallelCommandGroup(
    //     new MoveElevatorToSetpoint(elevator, ElevatorConstants.kHome)
    //   )

    );
  }
}