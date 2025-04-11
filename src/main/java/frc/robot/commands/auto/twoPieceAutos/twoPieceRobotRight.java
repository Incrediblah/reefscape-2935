// // Copyright (c) FIRST and other WPILib contributors.
// // Open Source Software; you can modify and/or share it under the terms of
// // the WPILib BSD license file in the root directory of this project.

// package frc.robot.commands.auto.twoPieceAutos;

// import edu.wpi.first.wpilibj2.command.InstantCommand;
// import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
// import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
// import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
// import frc.robot.commands.MoveElevatorToSetpoint;
// import frc.robot.commands.moveCoralSystemToPosition;
// import frc.robot.commands.armCommands.MoveArmToSetpoint;
// import frc.robot.commands.auto.onePieceAutos.onePieceRobotRight;
// import frc.robot.commands.autoBlocks.autoScoreCoral;
// import frc.robot.commands.coralIntakeCommands.CoralIntakeSensorCmd;
// import frc.robot.commands.driveCommands.DriveDistanceCmd;
// import frc.robot.commands.driveCommands.OdometryCmd;
// import frc.robot.commands.driveCommands.TurnToAngleCommand;
// import frc.robot.Constants.ArmConstants;
// import frc.robot.Constants.AutoConstants;
// import frc.robot.Constants.CoralSystemContants;
// import frc.robot.Constants.ElevatorConstants;
// import frc.robot.Constants.pathConstants;
// import frc.robot.subsystems.ArmSubsystem;
// import frc.robot.subsystems.CoralIntakeSubsystem;
// import frc.robot.subsystems.DriveSubsystem;
// import frc.robot.subsystems.ElevatorSubsystem;
// import frc.robot.subsystems.PhotonSubsystem;
// import frc.robot.subsystems.VisionSubsystem;

// // NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// // information, see:
// // https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
// public class twoPieceRobotRight extends SequentialCommandGroup {
//   /** Creates a new twoPieceRobotRight. */
//   public twoPieceRobotRight(DriveSubsystem drive, VisionSubsystem vision, PhotonSubsystem photon, ElevatorSubsystem elevator, ArmSubsystem arm, CoralIntakeSubsystem intake) {
//     // Add your commands in the addCommands() call, e.g.
//     // addCommands(new FooCommand(), new BarCommand());
//     addCommands(

//       new onePieceRobotRight(drive, vision, elevator, arm, intake, "right"), 
//       new DriveDistanceCmd(drive, 0.7, -0.25, false, 2000), 
   
//       new ParallelCommandGroup(
//         new SequentialCommandGroup(
//           // new MoveElevatorToSetpoint(elevator, ElevatorConstants.kHome), 
//           // new MoveArmToSetpoint(arm, ArmConstants.kHome)
//            new moveCoralSystemToPosition(arm, elevator, CoralSystemContants.HOME)
//         ), 


//         // update paths with turns
//         new OdometryCmd(drive, pathConstants.twoPieceRetrieveRobotRight)
//       ), 


//       new ParallelCommandGroup(
//         new TurnToAngleCommand(drive, 126),

//         new SequentialCommandGroup(
//           // new MoveElevatorToSetpoint(elevator, ElevatorConstants.kFeederStation),
//           // new MoveArmToSetpoint(arm, ArmConstants.kFeederStation)
//           new moveCoralSystemToPosition(arm, elevator, CoralSystemContants.FEEDER)
//         )
//       ),


//       new ParallelDeadlineGroup(
//         new CoralIntakeSensorCmd(intake),
//         new DriveDistanceCmd(drive, 0.3, -1.5, false, 5000)
//       ),


//       new InstantCommand(() -> drive.resetOdometry(drive.getPose())), 



//       new ParallelCommandGroup(

//         new OdometryCmd(drive, pathConstants.twoPieceDepositRobotRight), 

//         new SequentialCommandGroup(
//           // new MoveElevatorToSetpoint(elevator, ElevatorConstants.kHome),
//           // new MoveArmToSetpoint(arm, ArmConstants.kHome),   
//           new moveCoralSystemToPosition(arm, elevator, CoralSystemContants.HOME),      
//            new MoveArmToSetpoint(arm, ArmConstants.kLevel4)
//         )
//       ), 



      
//       new autoScoreCoral(drive, vision, elevator, arm, intake, "right",AutoConstants.autoMode),

//       new InstantCommand(() -> drive.zeroHeading()),

//       new InstantCommand(() -> drive.adjustGyroToAngle(300)), 
      
//       new ParallelCommandGroup(
//         new SequentialCommandGroup(
//             // new MoveElevatorToSetpoint(elevator, ElevatorConstants.kHome),
//             // new MoveArmToSetpoint(arm, ArmConstants.kHome)
//             new moveCoralSystemToPosition(arm, elevator, CoralSystemContants.HOME)
//           ), 

//         new DriveDistanceCmd(drive, 0.8, -1.5, false, 4000)
  
//        )


//     );
//   }
// }

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.auto.twoPieceAutos;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.MoveElevatorToSetpoint;
import frc.robot.commands.moveCoralSystemToPosition;
import frc.robot.commands.armCommands.MoveArmToSetpoint;
import frc.robot.commands.auto.onePieceAutos.onePieceRobotRight;
import frc.robot.commands.autoBlocks.autoScoreCoral;
import frc.robot.commands.coralIntakeCommands.CoralIntakeSensorCmd;
import frc.robot.commands.driveCommands.DriveDistanceAtRobotAngleCmd;
import frc.robot.commands.driveCommands.DriveDistanceCmd;
import frc.robot.commands.driveCommands.OdometryCmd;
import frc.robot.commands.driveCommands.TurnToAngleCommand;
import frc.robot.Constants.ArmConstants;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.CoralSystemContants;
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
public class twoPieceRobotRight extends SequentialCommandGroup {
  /** Creates a new twoPieceRobotRight. */
  public twoPieceRobotRight(DriveSubsystem drive, VisionSubsystem vision, PhotonSubsystem photon, ElevatorSubsystem elevator, ArmSubsystem arm, CoralIntakeSubsystem intake) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(

      new onePieceRobotRight(drive, vision, elevator, arm, intake, "right"), 
      // new DriveDistanceCmd(drive, 0.7, -0.25, false, 2000), 
   
      new ParallelCommandGroup(
        new moveCoralSystemToPosition(arm, elevator, CoralSystemContants.FEEDER),
          new SequentialCommandGroup(
            new TurnToAngleCommand(drive, 126), 
            new DriveDistanceAtRobotAngleCmd(drive, 0.7, 3.5, -165, false, 5000)
          )
    ), 

      
      new ParallelDeadlineGroup(
        new CoralIntakeSensorCmd(intake),
        new DriveDistanceCmd(drive, 0.3, -1.5, false, 5000)
      ),


      // new InstantCommand(() -> drive.resetOdometry(drive.getPose())), 

      new InstantCommand(() -> drive.resetOdometry(drive.getPose())), 
      new InstantCommand(() -> drive.zeroHeading()),
      new InstantCommand(() -> drive.adjustGyroToAngle(126)), 


      new DriveDistanceAtRobotAngleCmd(drive, 0.85, 1.35,-17, false, 5000), 
      
      new autoScoreCoral(drive, vision, elevator, arm, intake, "right",AutoConstants.autoMode, 120),

      new InstantCommand(() -> drive.resetOdometry(drive.getPose())), 

      new ParallelDeadlineGroup(

      new SequentialCommandGroup(
        new DriveDistanceAtRobotAngleCmd(drive, 0.85, 2.5, -155, false, 5000), 

        new ParallelDeadlineGroup(
          new CoralIntakeSensorCmd(intake), 
          new DriveDistanceCmd(drive, 0.3, -1.5, false, 5000)
        )
      ), 
     
      new moveCoralSystemToPosition(arm, elevator, CoralSystemContants.FEEDER)
      // new OdometryCmd(drive, pathConstants.threePieceRetrieveRobotLeft)


    ), 
    new InstantCommand(() -> drive.zeroHeading()),
    new InstantCommand(() -> drive.adjustGyroToAngle(126)),


    new ParallelDeadlineGroup(

      new DriveDistanceAtRobotAngleCmd(drive, 0.85, 1.35, 17, false, 5000), 
      new moveCoralSystemToPosition(arm, elevator, CoralSystemContants.L4)
    ), 

    new autoScoreCoral(drive, vision, elevator, arm, intake, "left",AutoConstants.autoMode, 120)

    );
  }
}