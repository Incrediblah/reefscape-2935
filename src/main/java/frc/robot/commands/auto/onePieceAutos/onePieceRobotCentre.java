// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.auto.onePieceAutos;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Constants.ArmConstants;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.ElevatorConstants;
import frc.robot.commands.MoveElevatorToSetpoint;
import frc.robot.commands.armCommands.MoveArmToSetpoint;
import frc.robot.commands.autoBlocks.autoScoreCoral;
import frc.robot.commands.driveCommands.DriveDistanceCmd;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.CoralIntakeSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.VisionSubsystem;


// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class onePieceRobotCentre extends SequentialCommandGroup {
  /** Creates a new onePieceRobotCentre. */
  public onePieceRobotCentre(DriveSubsystem drive, VisionSubsystem vision, ElevatorSubsystem elevator, ArmSubsystem arm, CoralIntakeSubsystem intake, String reefside) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(


    new InstantCommand(() -> drive.resetOdometry(drive.getPose())), 
      new InstantCommand(() -> drive.zeroHeading()),
      new InstantCommand(() -> drive.adjustGyroToAngle(0)), 




        Commands.waitSeconds(3), 
       new DriveDistanceCmd(drive, 0.25, 0.15, false, 0),
       new SequentialCommandGroup(
        new MoveArmToSetpoint(arm, ArmConstants.kLevel4), 
        new MoveElevatorToSetpoint(elevator, ElevatorConstants.kLevel4)
        ), 
      new autoScoreCoral(drive, vision, elevator, arm, intake, "right",AutoConstants.autoMode),

      new InstantCommand(() -> drive.zeroHeading())

      //new InstantCommand(() -> drive.adjustGyroToAngle(180))


    );
  }
}
