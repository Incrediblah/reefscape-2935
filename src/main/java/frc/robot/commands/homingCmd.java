// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.ArmConstants;
import frc.robot.Constants.ElevatorConstants;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class homingCmd extends SequentialCommandGroup {

  /** Creates a new homingCmd. */
  public homingCmd( ElevatorSubsystem s_ElevatorSubsystem,  double elevatorSetpoint,ArmSubsystem s_ArmSubsystem, double armSetpoint) {
       addCommands(
       new SequentialCommandGroup( 
        new MoveElevatorToSetpoint(s_ElevatorSubsystem,  elevatorSetpoint ),
        new MoveArmToSetpoint(s_ArmSubsystem, armSetpoint)
       )
       
       );

    
  }  
}
