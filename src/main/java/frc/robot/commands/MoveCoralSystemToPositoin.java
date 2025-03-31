// // Copyright (c) FIRST and other WPILib contributors.
// // Open Source Software; you can modify and/or share it under the terms of
// // the WPILib BSD license file in the root directory of this project.

// package frc.robot.commands;

// import edu.wpi.first.wpilibj2.command.Command;
// import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
// import frc.robot.Constants.ArmConstants;
// import frc.robot.Constants.ElevatorConstants;
// import frc.robot.Constants.StatusVariables;
// import frc.robot.commands.armCommands.MoveArmToSetpoint;
// import frc.robot.subsystems.ArmSubsystem;
// import frc.robot.subsystems.ElevatorSubsystem;

// /* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
// public class MoveCoralSystemToPositoin extends Command {
//   /** Creates a new MoveCoralSystemToPositoin. */


//   private ArmSubsystem ARM_SUBSYSTEM; 
//   private ElevatorSubsystem ELEVATOR_SUBSYSTEM; 

//   Command safety; 

//   private double armPosition; 
//   private double elevatorPosition; 

//   String targetPosition; 


//   public MoveCoralSystemToPositoin(ArmSubsystem arm, ElevatorSubsystem elevator, String position) {
//     // Use addRequirements() here to declare subsystem dependencies.
//     this.ARM_SUBSYSTEM = arm; 
//     this.ELEVATOR_SUBSYSTEM = elevator; 
//     this.targetPosition = position; 
//     // addRequirements(ARM_SUBSYSTEM);
//     // addRequirements(ELEVATOR_SUBSYSTEM);
//   }


  
//   Command moveElevatorCommand = new MoveElevatorToSetpoint(ELEVATOR_SUBSYSTEM, ElevatorConstants.kHome); 
//   Command moveArmCommand = new MoveArmToSetpoint(ARM_SUBSYSTEM, ArmConstants.kLevel3); 


//   // Called when the command is initially scheduled.
//   @Override
//   public void initialize() {
//         // unsafe 
//     if(targetPosition != StatusVariables.currentCoralSystemPosition){
//       if(targetPosition == "L4"){
//         safety = moveArmCommand; 
//       }else if(StatusVariables.currentCoralSystemPosition == "L4"){
//         safety = moveElevatorCommand; 
//       }else{
//         safety = null; 
//       }
//     }


//     if(targetPosition == "HOME"){
//       armPosition = ArmConstants.kHome; 
//       elevatorPosition = ElevatorConstants.kHome; 
//     }else if(targetPosition == "FEEDER"){
//       armPosition = ArmConstants.kFeederStation; 
//       elevatorPosition = ElevatorConstants.kFeederStation; 
//     }else if(targetPosition == "L1"){
//       armPosition = ArmConstants.kLevel1; 
//       elevatorPosition = ElevatorConstants.kLevel3; 
//     }else if(targetPosition == "L2"){
//       armPosition = ArmConstants.kLevel1; 
//       elevatorPosition = ElevatorConstants.kLevel2; 
//     }else if(targetPosition == "L3"){
//       armPosition = ArmConstants.kLevel3; 
//       elevatorPosition = ElevatorConstants.kLevel3; 
//     }else if(targetPosition == "L4"){
//       armPosition = ArmConstants.kLevel4; 
//       elevatorPosition = ElevatorConstants.kLevel4; 
//     }else if(targetPosition == "CLIMB"){
//       armPosition = ArmConstants.kLevelClimb; 
//       elevatorPosition = ElevatorConstants.kLevelClimb; 
//     }else{
//       armPosition = ArmConstants.kHome; 
//       elevatorPosition = ElevatorConstants.kHome; 
//     }
    
   
//   }

//   // Called every time the scheduler runs while the command is scheduled.
//   @Override
//   public void execute() {

//     new SequentialCommandGroup(null)
//   }

//   // Called once the command ends or is interrupted.
//   @Override
//   public void end(boolean interrupted) {}

//   // Returns true when the command should end.
//   @Override
//   public boolean isFinished() {
//     return false;
//   }
// }
