// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Configs;
import frc.robot.Constants.ElevatorSubsystemConstants;
import frc.robot.Constants.ElevatorSubsystemConstants.ElevatorSetPoints;
//import edu.wpi.first.wpilibj2.command.SubsystemBase;


import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkBase.ControlType;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ElevatorSubsystem extends SubsystemBase {
  public enum SetElevator {
    Level1,
    Level2,
    Level3,
    Level4,
    Home;
  }
  private boolean wasResetByButton = false;
  
//motor definitions
  private SparkMax M1_elevator =
      new SparkMax(ElevatorSubsystemConstants.M1_ElevatorCanId, MotorType.kBrushless);
  private SparkClosedLoopController elevatorClosedLoopController =
      M1_elevator.getClosedLoopController();
  private RelativeEncoder M1_elevatorEncoder = M1_elevator.getEncoder();

  private SparkMax M2_elevator =
      new SparkMax(ElevatorSubsystemConstants.M2_ElevatorCanId, MotorType.kBrushless);
  private SparkClosedLoopController elevator2ClosedLoopController =
      M2_elevator.getClosedLoopController();
  private RelativeEncoder M2_elevatorEncoder = M2_elevator.getEncoder();
  
  private double M1_elevatorCurrentTarget = 0;
  private double M2_elevatorCurrentTarget = 0;
  
  /** Creates a new ElevatorSubsystem. */

  public ElevatorSubsystem() {

    M1_elevator.configure(
      Configs.ElevatorSubsystem.M1_elevatorConfig,
      ResetMode.kResetSafeParameters,
      PersistMode.kPersistParameters);
    M1_elevatorEncoder.setPosition(0);

    M2_elevator.configure(
      Configs.ElevatorSubsystem.M2_elevatorConfig,
      ResetMode.kResetSafeParameters,
      PersistMode.kPersistParameters);
    M2_elevatorEncoder.setPosition(0);
   }

  private void moveToSetpoint() {
    elevatorClosedLoopController.setReference( M1_elevatorCurrentTarget, ControlType.kMAXMotionPositionControl);
    elevator2ClosedLoopController.setReference(M2_elevatorCurrentTarget, ControlType.kMAXMotionPositionControl);
   
  }

//   private void zeroOnUserButton(){
//     if (!wasResetByButton && RobotController.getUserButton()){
//       wasResetByButton = true;
//       M1_elevatorEncoder.setPosition(0);
//       M2_elevatorEncoder.setPosition(0);
      
     
//     } else if (!RobotController.getUserButton()){
//       wasResetByButton = false;
//     }
// }


  public Command setElevatorCommand(SetElevator setpoint) {
    return this.runOnce(
        () -> {
          switch (setpoint) {
          
            case Level1:
            M1_elevatorCurrentTarget = ElevatorSetPoints.Level1;
            M2_elevatorCurrentTarget = ElevatorSetPoints.Level1;
              break;

            case Level2:
            M1_elevatorCurrentTarget = ElevatorSetPoints.Level2;
            M2_elevatorCurrentTarget = ElevatorSetPoints.Level2;
              break;

            case Level3:
            M1_elevatorCurrentTarget = ElevatorSetPoints.Level3;
            M2_elevatorCurrentTarget = ElevatorSetPoints.Level3;
              break;

            case Level4:
            M1_elevatorCurrentTarget= ElevatorSetPoints.Level4;
            M2_elevatorCurrentTarget= ElevatorSetPoints.Level4;
              break;

            case Home:
            M1_elevatorCurrentTarget = ElevatorSetPoints.Home;
            M2_elevatorCurrentTarget = ElevatorSetPoints.Home;
            break;
          
          }
        });
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    moveToSetpoint();
   // zeroOnUserButton();



    SmartDashboard.putNumber("M1_ElevatorTarget Position", M1_elevatorCurrentTarget);
    SmartDashboard.putNumber("M1_Elevator Actual Position", M1_elevatorEncoder.getPosition());
    SmartDashboard.putNumber("M2_ElevatorTarget Position", M2_elevatorCurrentTarget);
    SmartDashboard.putNumber("M2_Elevator Actual Position", M2_elevatorEncoder.getPosition());
  }
}
