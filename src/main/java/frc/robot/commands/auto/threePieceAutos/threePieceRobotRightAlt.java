// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.auto.threePieceAutos;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.pathConstants;
import frc.robot.commands.auto.twoPieceAutos.twoPieceRobotRight;
import frc.robot.commands.auto.twoPieceAutos.twoPieceRobotRightAlt;
import frc.robot.commands.autoBlocks.autoAlignmentToFeeder;
import frc.robot.commands.autoBlocks.autoAlignmentToReef;
import frc.robot.commands.coralIntakeCommands.CoralIntakeSensorCmd;
import frc.robot.commands.coralIntakeCommands.CoralOutakeSensorCmd;
import frc.robot.commands.driveCommands.DriveDistanceCmd;
import frc.robot.commands.driveCommands.OdometryCmd;
import frc.robot.commands.driveCommands.TurnToAngleCommand;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.CoralIntakeSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.PhotonSubsystem;
import frc.robot.subsystems.VisionSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class threePieceRobotRightAlt extends SequentialCommandGroup {
  /** Creates a new threePieceRobotRightAlt. */
  public threePieceRobotRightAlt(DriveSubsystem drive, VisionSubsystem vision, PhotonSubsystem photon, ElevatorSubsystem elevator, ArmSubsystem arm, CoralIntakeSubsystem intake) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      new twoPieceRobotRightAlt(drive, vision, photon, elevator, arm, intake),
      new TurnToAngleCommand(drive, 126),

      new ParallelDeadlineGroup(
        new CoralIntakeSensorCmd(intake),
        new DriveDistanceCmd(drive, 0.3, -1.5, false, 5000)
      ),
      new OdometryCmd(drive, pathConstants.threePieceDepositRobotRight),
      new autoAlignmentToReef(drive, vision, "right", false, AutoConstants.autoMode),
       new CoralOutakeSensorCmd(intake)

     


    );
  }
}
