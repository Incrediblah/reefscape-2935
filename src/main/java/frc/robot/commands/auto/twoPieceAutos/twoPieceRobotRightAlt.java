// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.auto.twoPieceAutos;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.CoralIntakeSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.PhotonSubsystem;
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.Constants.ArmConstants;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.CoralIntakeConstants;
import frc.robot.Constants.CoralSystemContants;
import frc.robot.Constants.ElevatorConstants;
import frc.robot.commands.MoveElevatorToSetpoint;
import frc.robot.commands.moveCoralSystemToPosition;
import frc.robot.commands.armCommands.MoveArmToSetpoint;
import frc.robot.commands.auto.onePieceAutos.onePieceRightAlt;
import frc.robot.commands.autoBlocks.autoAlignmentToReef;
import frc.robot.commands.autoBlocks.autoScoreCoral;
import frc.robot.commands.coralIntakeCommands.CoralIntakeSensorCmd;
import frc.robot.commands.coralIntakeCommands.CoralOutakeSensorCmd;
import frc.robot.commands.driveCommands.DriveDistanceCmd;
import frc.robot.commands.driveCommands.OdometryCmd;
import frc.robot.commands.driveCommands.TurnToAngleCommand;
import frc.robot.Constants.pathConstants;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class twoPieceRobotRightAlt extends SequentialCommandGroup {
  /** Creates a new twoPieceRobotRightAlt. */
  public twoPieceRobotRightAlt(DriveSubsystem drive, VisionSubsystem vision, PhotonSubsystem photon, ElevatorSubsystem elevator, ArmSubsystem arm, CoralIntakeSubsystem intake) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      new onePieceRightAlt(drive, vision, elevator, arm, intake, "right"),
      // new DriveDistanceCmd(drive, 0.7, -0.25, false, 2000), 



      // ADD THIS IN 
      new MoveElevatorToSetpoint(elevator, ElevatorConstants.kHigh), 

      new ParallelCommandGroup(
        new moveCoralSystemToPosition(arm, elevator, CoralSystemContants.FEEDER), 
        new OdometryCmd(drive, pathConstants.twoPieceRetrieveRobotRight)
      ), 

      new TurnToAngleCommand(drive, 126),


      // new ParallelCommandGroup(
      //   new TurnToAngleCommand(drive, 126),

      //   new SequentialCommandGroup(
      //     new MoveElevatorToSetpoint(elevator, ElevatorConstants.kFeederStation),
      //     new MoveArmToSetpoint(arm, ArmConstants.kFeederStation)
      //   )
      // ),


      new ParallelDeadlineGroup(
        new CoralIntakeSensorCmd(intake),
        new DriveDistanceCmd(drive, 0.3, -1.5, false, 5000)
      ),


      new InstantCommand(() -> drive.resetOdometry(drive.getPose())), 



      new ParallelCommandGroup(

        new OdometryCmd(drive, pathConstants.twoPieceDepositRobotRight), 
        new moveCoralSystemToPosition(arm, elevator, CoralSystemContants.L4)
        // new SequentialCommandGroup(
        //   new MoveElevatorToSetpoint(elevator, ElevatorConstants.kHome),
        //   new MoveArmToSetpoint(arm, ArmConstants.kHome),          
        //    new MoveArmToSetpoint(arm, ArmConstants.kLevel4)
        // )
      ), 


      new autoAlignmentToReef(drive, vision, "right", false, AutoConstants.autoMode),
      new CoralOutakeSensorCmd(intake), 

      //new autoScoreCoral(drive, vision, elevator, arm, intake, "right",AutoConstants.autoMode),

      new InstantCommand(() -> drive.zeroHeading()),

      new InstantCommand(() -> drive.adjustGyroToAngle(300)), 
      
      new ParallelCommandGroup(

        new moveCoralSystemToPosition(arm, elevator, CoralSystemContants.FEEDER), 
        new OdometryCmd(drive, pathConstants.threePieceRetrieveRobotRight)
  
       )

      


    );
  }
}
