// // Copyright (c) FIRST and other WPILib contributors.
// // Open Source Software; you can modify and/or share it under the terms of
// // the WPILib BSD license file in the root directory of this project.

// package frc.robot.commands.autoBlocks;

// import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
// import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
// import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
// import frc.robot.Constants.ArmConstants;
// import frc.robot.Constants.AutoConstants;
// import frc.robot.Constants.CoralIntakeConstants;
// import frc.robot.Constants.CoralSystemContants;
// import frc.robot.Constants.ElevatorConstants;
// import frc.robot.commands.MoveElevatorToSetpoint;
// import frc.robot.commands.moveCoralSystemToPosition;
// import frc.robot.commands.armCommands.MoveArmToSetpoint;
// import frc.robot.commands.coralIntakeCommands.CoralIntakeCmd;
// import frc.robot.commands.coralIntakeCommands.CoralIntakeForTimeCmd;
// import frc.robot.commands.coralIntakeCommands.CoralOutakeSensorCmd;
// import frc.robot.commands.driveCommands.DriveDistanceCmd;
// import frc.robot.subsystems.ArmSubsystem;
// import frc.robot.subsystems.CoralIntakeSubsystem;
// import frc.robot.subsystems.DriveSubsystem;
// import frc.robot.subsystems.ElevatorSubsystem;
// import frc.robot.subsystems.VisionSubsystem;


// // NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// // information, see:
// // https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
// public class autoScoreCoral extends SequentialCommandGroup {
  
//   /** Creates a new autoScoreCoral. */
//   public autoScoreCoral(DriveSubsystem drive, VisionSubsystem vision, ElevatorSubsystem elevator, ArmSubsystem arm, CoralIntakeSubsystem intake, String reefside, String mode) {
//     // Add your commands in the addCommands() call, e.g.
//     // addCommands(new FooCommand(), new BarCommand());

//     addCommands(


//       new ParallelDeadlineGroup(

//         new ParallelCommandGroup(
//           new SequentialCommandGroup(
//             // new MoveArmToSetpoint(arm, ArmConstants.kLevel4), 
//             // new MoveElevatorToSetpoint(elevator, ElevatorConstants.kLevel4)
//             new moveCoralSystemToPosition(arm, elevator, CoralSystemContants.L4)
//           ), 
    
//           new SequentialCommandGroup(
//             new autoAlignmentToReef(drive, vision, reefside, false,AutoConstants.autoMode)
//           )
//         ), 

//         new CoralIntakeCmd(intake, CoralIntakeConstants.kCoralNoSpeed)
//       ), 


//       // new CoralOutakeSensorCmd(intake), 
//       new CoralIntakeForTimeCmd(intake,CoralIntakeConstants.kCoralOutakeSpeed, 1000),
//       new DriveDistanceCmd(drive, 0.5, -0.5, false, 2000)

//     );
//   }
// }

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.autoBlocks;

import edu.wpi.first.wpilibj2.command.Commands;
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
import frc.robot.commands.coralIntakeCommands.CoralIntakeCmd;
import frc.robot.commands.coralIntakeCommands.CoralIntakeForTimeCmd;
import frc.robot.commands.coralIntakeCommands.CoralOutakeSensorCmd;
import frc.robot.commands.driveCommands.DriveDistanceCmd;
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
  public autoScoreCoral(DriveSubsystem drive, VisionSubsystem vision, ElevatorSubsystem elevator, ArmSubsystem arm, CoralIntakeSubsystem intake, String reefside, String mode, double targetHeading) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());

    addCommands(


      new ParallelDeadlineGroup(

        new ParallelCommandGroup(
          new moveCoralSystemToPosition(arm, elevator, CoralSystemContants.L4),
          new autoAlignmentToReef(drive, vision, reefside, false,AutoConstants.autoMode, targetHeading)
        ),

       

        new CoralIntakeCmd(intake, CoralIntakeConstants.kCoralNoSpeed)
      ), 


      new CoralOutakeSensorCmd(intake),
     //  new CoralIntakeForTimeCmd(intake,CoralIntakeConstants.kCoralSlowOutakeSpeed , 1000)
      new DriveDistanceCmd(drive, 0.6, -0.40, false, 2000)

    );
  }
}
