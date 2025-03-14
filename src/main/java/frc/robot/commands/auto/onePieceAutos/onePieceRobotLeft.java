// // Copyright (c) FIRST and other WPILib contributors.
// // Open Source Software; you can modify and/or share it under the terms of
// // the WPILib BSD license file in the root directory of this project.

// package frc.robot.commands.auto.onePieceAutos;

// import edu.wpi.first.wpilibj2.command.InstantCommand;
// import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
// import frc.robot.Constants.AutoConstants;
// import frc.robot.Constants.pathConstants;
// import frc.robot.commands.autoBlocks.autoAlignmentToReef;
// import frc.robot.commands.autoBlocks.autoScoreCoral;
// import frc.robot.commands.driveCommands.OdometryCmd;
// import frc.robot.commands.driveCommands.TurnToAngleCommand;
// import frc.robot.subsystems.ArmSubsystem;
// import frc.robot.subsystems.CoralIntakeSubsystem;
// import frc.robot.subsystems.DriveSubsystem;
// import frc.robot.subsystems.ElevatorSubsystem;
// import frc.robot.subsystems.VisionSubsystem;

// // NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// // information, see:
// // https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
// public class onePieceRobotLeft extends SequentialCommandGroup {
//   /** Creates a new onePieceRobotLeft. */
//   public onePieceRobotLeft(DriveSubsystem drive, VisionSubsystem vision, ElevatorSubsystem elevator, ArmSubsystem arm, CoralIntakeSubsystem intake, String reefside) {
//     // Add your commands in the addCommands() call, e.g.
//     // addCommands(new FooCommand(), new BarCommand());
//     addCommands(
     
//       new InstantCommand(() -> drive.resetOdometry(drive.getPose())), 
//       new OdometryCmd(drive, pathConstants.onePieceDepositRobotLeft),
//       // new TurnToAngleCommand(drive, 60),
//       //new autoAlignmentToReef(drive, vision, reefside, false)
//        new autoScoreCoral(drive, vision, elevator, arm, intake, "right",AutoConstants.autoMode)
//     );
//   }
// }
