// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.auto.twoPieceAutos;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.ArmConstants;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.ElevatorConstants;
import frc.robot.Constants.pathConstants;
import frc.robot.commands.MoveElevatorToSetpoint;
import frc.robot.commands.armCommands.MoveArmToSetpoint;
import frc.robot.commands.auto.onePieceAutos.onePieceRobotLeft;
import frc.robot.commands.auto.onePieceAutos.onePieceRobotRight;
import frc.robot.commands.autoBlocks.autoAlignmentToFeeder;
import frc.robot.commands.autoBlocks.autoPickUpCoral;
import frc.robot.commands.autoBlocks.autoScoreCoral;
import frc.robot.commands.coralIntakeCommands.CoralIntakeSensorCmd;
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
public class twoPieceRobotLeft extends SequentialCommandGroup {
  /** Creates a new twoPieceRobotLeft. */
  public twoPieceRobotLeft(DriveSubsystem drive, VisionSubsystem vision, PhotonSubsystem photon, ElevatorSubsystem elevator, ArmSubsystem arm, CoralIntakeSubsystem intake, String reefside) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      new onePieceRobotLeft(drive, vision, elevator, arm, intake, reefside),

      new DriveDistanceCmd(drive, 0.5, -0.25, false), 
   
      new ParallelCommandGroup(
        new SequentialCommandGroup(
          new MoveElevatorToSetpoint(elevator, ElevatorConstants.kHome), 
          new MoveArmToSetpoint(arm, ArmConstants.kHome)
        ), 


        // update paths with turns
        new OdometryCmd(drive, pathConstants.twoPieceRetrieveRobotLeft)
      ), 


      new ParallelCommandGroup(
        new TurnToAngleCommand(drive, -126),

        new SequentialCommandGroup(
          new MoveElevatorToSetpoint(elevator, ElevatorConstants.kFeederStation),
          new MoveArmToSetpoint(arm, ArmConstants.kFeederStation)
        )
      ),


      new ParallelDeadlineGroup(
        new CoralIntakeSensorCmd(intake), 
        new DriveDistanceCmd(drive, 0.2, -1.5, false)
      ), 


      new InstantCommand(() -> drive.resetOdometry(drive.getPose())), 



      new ParallelCommandGroup(

        new OdometryCmd(drive, pathConstants.twoPieceDepositRobotLeft),

        new SequentialCommandGroup(
          new MoveElevatorToSetpoint(elevator, ElevatorConstants.kHome),
          new MoveArmToSetpoint(arm, ArmConstants.kHome)
        )
      ), 


       new autoScoreCoral(drive, vision, elevator, arm, intake, "left",AutoConstants.autoMode),

       new SequentialCommandGroup(
          new MoveElevatorToSetpoint(elevator, ElevatorConstants.kHome),
          new MoveArmToSetpoint(arm, ArmConstants.kHome)
        )




      
     
      
    );
  }
}
