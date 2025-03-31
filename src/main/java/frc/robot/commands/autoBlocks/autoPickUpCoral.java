// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.autoBlocks;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.ArmConstants;
import frc.robot.Constants.ElevatorConstants;
import frc.robot.commands.MoveElevatorToSetpoint;
import frc.robot.commands.armCommands.MoveArmToSetpoint;
import frc.robot.commands.coralIntakeCommands.CoralIntakeForTimeCmd;
import frc.robot.commands.coralIntakeCommands.CoralIntakeSensorCmd;
import frc.robot.commands.driveCommands.DriveDistanceCmd;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.CoralIntakeSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.PhotonSubsystem;


// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class autoPickUpCoral extends SequentialCommandGroup {
  /** Creates a new autoPickUpCoral. */
  public autoPickUpCoral(DriveSubsystem drive, PhotonSubsystem photon, ElevatorSubsystem elevator, ArmSubsystem arm, CoralIntakeSubsystem intake, String reefsid, boolean endCommand) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      new ParallelCommandGroup(

        new SequentialCommandGroup(
          new autoAlignmentToFeeder(drive, photon, endCommand, "right")
        ), 

        new SequentialCommandGroup(
          new MoveElevatorToSetpoint(elevator, ElevatorConstants.kFeederStation),
          new MoveArmToSetpoint(arm, ArmConstants.kFeederStation)
        )

      ), 

      new CoralIntakeSensorCmd(intake),
      new DriveDistanceCmd(drive, 0.15, 1, false, 2000), 
      new MoveArmToSetpoint(arm, ArmConstants.kHome), 
      new MoveElevatorToSetpoint(elevator, ElevatorConstants.kHome)  
    );
  }
}
