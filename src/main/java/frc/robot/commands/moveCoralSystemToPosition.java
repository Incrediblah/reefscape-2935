// // Copyright (c) FIRST and other WPILib contributors.
// // Open Source Software; you can modify and/or share it under the terms of
// // the WPILib BSD license file in the root directory of this project.

// package frc.robot.commands;

// import java.lang.annotation.Target;

// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import edu.wpi.first.wpilibj2.command.Command;
// import edu.wpi.first.wpilibj2.command.InstantCommand;
// import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
// import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
// import frc.robot.Globals;
// import frc.robot.Constants.ArmConstants;
// import frc.robot.Constants.CoralSystemContants;
// import frc.robot.Constants.ElevatorConstants;
// import frc.robot.Constants.StatusVariables;
// import frc.robot.commands.armCommands.MoveArmToSetpoint;
// import frc.robot.subsystems.ArmSubsystem;
// import frc.robot.subsystems.ElevatorSubsystem;

// // NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// // information, see:
// // https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
// public class moveCoralSystemToPosition extends SequentialCommandGroup {
//   /** Creates a new moveCoralSystemToPosition. */

//   Command safety; 

//   double armPosition; 
//   double elevatorPosiiton; 


//   public moveCoralSystemToPosition(ArmSubsystem arm, ElevatorSubsystem elevator, int coralTargetPosition) {

//     Globals.targetCoralPos = coralTargetPosition; 

    
//     Command moveElevatorCommand = new MoveElevatorToSetpoint(elevator, ElevatorConstants.kHome);   
//     Command moveArmCommand = new MoveArmToSetpoint(arm, ArmConstants.kLevel3); 
//     Command nullCommand = new MoveArmToSetpoint(arm, arm.getArmPosition()); 
    
//     if(CoralSystemContants.L4 == Globals.currentPos){
//       safety = moveElevatorCommand; 
//     }else if(CoralSystemContants.L4 == coralTargetPosition){
//       safety = moveArmCommand; 
//     }else{
//       safety = nullCommand; 
//     }
    


//     if(coralTargetPosition == CoralSystemContants.HOME){
//       armPosition = ArmConstants.kHome; 
//       elevatorPosiiton = ElevatorConstants.kHome; 
//     }else if(coralTargetPosition == CoralSystemContants.FEEDER){
//       armPosition = ArmConstants.kFeederStation; 
//       elevatorPosiiton = ElevatorConstants.kFeederStation; 
//     }else if(coralTargetPosition == CoralSystemContants.L1){
//       armPosition = ArmConstants.kLevel1; 
//       elevatorPosiiton = ElevatorConstants.kLevel1; 
//     }else if(coralTargetPosition == CoralSystemContants.L2){
//       armPosition = ArmConstants.kLevel2; 
//       elevatorPosiiton = ElevatorConstants.kLevel2; 
//     }else if(coralTargetPosition == CoralSystemContants.L3){
//       armPosition = ArmConstants.kLevel3; 
//       elevatorPosiiton = ElevatorConstants.kLevel3; 
//     }else if(coralTargetPosition == CoralSystemContants.L4){
//       armPosition = ArmConstants.kLevel4; 
//       elevatorPosiiton = ElevatorConstants.kLevel4; 
//     }else if(coralTargetPosition == CoralSystemContants.CLIMB){
//       armPosition = ArmConstants.kLevelClimb; 
//       elevatorPosiiton = ElevatorConstants.kLevelClimb; 
//     }else{
//       armPosition = ArmConstants.kHome; 
//       elevatorPosiiton = ElevatorConstants.kHome; 
//     }

//     Globals.currentPos = coralTargetPosition;
    
//     addCommands(
//       safety, 
  
//       new ParallelCommandGroup(
//         new MoveArmToSetpoint(arm, armPosition), 
//         new MoveElevatorToSetpoint(elevator, elevatorPosiiton)
//       )
//     );
//   }


// }


/////// CHAT GPT CODE 
/// 
/// 

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Globals;
import frc.robot.Constants.ArmConstants;
import frc.robot.Constants.CoralSystemContants;
import frc.robot.Constants.ElevatorConstants;
import frc.robot.commands.armCommands.MoveArmToSetpoint;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;

public class moveCoralSystemToPosition extends SequentialCommandGroup {
  
  public moveCoralSystemToPosition(ArmSubsystem arm, ElevatorSubsystem elevator, int coralTargetPosition) {

    // Map coralTargetPosition to arm/elevator setpoints
    double armPosition;
    double elevatorPosition;

    if(coralTargetPosition == CoralSystemContants.HOME){
      armPosition = ArmConstants.kHome; 
      elevatorPosition = ElevatorConstants.kHome; 
    }else if(coralTargetPosition == CoralSystemContants.FEEDER){
      armPosition = ArmConstants.kFeederStation; 
      elevatorPosition = ElevatorConstants.kFeederStation; 
    }else if(coralTargetPosition == CoralSystemContants.L1){
      armPosition = ArmConstants.kLevel1; 
      elevatorPosition = ElevatorConstants.kLevel1; 
    }else if(coralTargetPosition == CoralSystemContants.L2){
      armPosition = ArmConstants.kLevel2; 
      elevatorPosition = ElevatorConstants.kLevel2; 
    }else if(coralTargetPosition == CoralSystemContants.L3){
      armPosition = ArmConstants.kLevel3; 
      elevatorPosition = ElevatorConstants.kLevel3; 
    }else if(coralTargetPosition == CoralSystemContants.L4){
      armPosition = ArmConstants.kLevel4; 
      elevatorPosition = ElevatorConstants.kLevel4; 
    }else if(coralTargetPosition == CoralSystemContants.CLIMB){
      armPosition = ArmConstants.kLevelClimb; 
      elevatorPosition = ElevatorConstants.kLevelClimb; 
    }else if(coralTargetPosition == CoralSystemContants.NET){
      armPosition = ArmConstants.kNet; 
      elevatorPosition = ElevatorConstants.kNet; 
    }else if(coralTargetPosition == CoralSystemContants.A1){
      armPosition = ArmConstants.kAlgaeLow; 
      elevatorPosition = ElevatorConstants.kAlgaeLow; 
    }else if(coralTargetPosition == CoralSystemContants.A2){
      armPosition = ArmConstants.kAlgaeHigh; 
      elevatorPosition = ElevatorConstants.kAlgaeHigh; 
    }else if (coralTargetPosition==CoralSystemContants.NETBack){
      armPosition =  ArmConstants.kNetBack ;
      elevatorPosition = ElevatorConstants.kNet; 
    }
    else{
      armPosition = ArmConstants.kHome; 
      elevatorPosition = ElevatorConstants.kHome; 
    }

    // Safety logic
    //Math.abs(arm.getArmPosition() - ArmConstants.kLevel4) < 1 && Math.abs(elevator.getElevator1Position() - ElevatorConstants.kLevel4) < 10
    
    
    
  if (Globals.currentPos != CoralSystemContants.L4 && coralTargetPosition == CoralSystemContants.L4) {
      // Going to L4 — move arm to L3 first, then parallel move to L4
      addCommands(
        new MoveArmToSetpoint(arm, ArmConstants.kLevel3),
        new ParallelCommandGroup(
          new MoveArmToSetpoint(arm, armPosition),
          new MoveElevatorToSetpoint(elevator, elevatorPosition)
        )
      );
    } else{

        addCommands(
          new MoveElevatorToSetpoint(elevator, ElevatorConstants.kHome), 
          new ParallelCommandGroup(
            new MoveArmToSetpoint(arm, armPosition),

            new SequentialCommandGroup(
              Commands.waitSeconds(0.5), 
              new MoveElevatorToSetpoint(elevator, elevatorPosition)
            )

          )
        );

    }
    
    // else {
    //   // Safe move — both in parallel
    //   addCommands(
    //     new ParallelCommandGroup(
    //       new MoveArmToSetpoint(arm, armPosition),
    //       new MoveElevatorToSetpoint(elevator, elevatorPosition)
    //     )
    //   );
    // }

    // Update current position after movement
    addCommands(
      new InstantCommand(() -> Globals.currentPos = coralTargetPosition)
    );
  }
}
