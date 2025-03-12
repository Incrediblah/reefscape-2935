// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.autoBlocks;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.ArmConstants;
import frc.robot.Constants.CoralIntakeConstants;
import frc.robot.Constants.ElevatorConstants;
import frc.robot.Constants.VisionConstants;
import frc.robot.commands.DriveForwardDistance;
import frc.robot.commands.MoveArmToSetpoint;
import frc.robot.commands.MoveElevatorToSetpoint;
import frc.robot.commands.coralIntakeCommands.CoralIntakeCmd;
import frc.robot.commands.coralIntakeCommands.CoralIntakeForTimeCmd;
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
public class autoScoreCoral extends SequentialCommandGroup {
  
  /** Creates a new autoScoreCoral. */
  public autoScoreCoral(DriveSubsystem drive, VisionSubsystem vision, ElevatorSubsystem elevator, ArmSubsystem arm, CoralIntakeSubsystem intake, String reefside) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());

    addCommands(
      new ParallelCommandGroup(

        new SequentialCommandGroup(
          new autoAlignmentToReef(drive, vision, reefside, false)
        ), 

        new SequentialCommandGroup(
          new MoveArmToSetpoint(arm, ArmConstants.kLevel4), 
          new MoveElevatorToSetpoint(elevator, ElevatorConstants.kLevel4)
        )

      ), 
 
     // new DriveForwardDistance(drive, -0.2, 0.4, false), 
      new WaitCommand(1),
      new CoralIntakeForTimeCmd(intake, 1, 2000)
      // new MoveElevatorToSetpoint(elevator, ElevatorConstants.kHome), 
      // new MoveArmToSetpoint(arm, ArmConstants.kHome)


    );
  }
}
