// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Configs;
import frc.robot.RobotContainer;
import frc.robot.Constants.ElevatorSubsystemConstants;
import frc.robot.Constants.PivotSubsystemConstants;
import frc.robot.Constants.PivotSubsystemConstants.PivotSetPoints;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

//import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkBase.ControlType;


import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PivotSubsystem extends SubsystemBase {

  public enum SetPivot {
    Level1,
    Home;
  }
  private boolean wasResetByButton = false;
  private boolean wasResetByLimit = false ;
  
private SparkMax M_pivot =
      new SparkMax(PivotSubsystemConstants.PivotCANID, MotorType.kBrushless);
  private SparkClosedLoopController pivotClosedLoopController =
      M_pivot.getClosedLoopController();
  private RelativeEncoder M_pivotEncoder = M_pivot.getEncoder();

  private double M_pivotCurrentTarget = 0;

  /** Creates a new PivotSubsystem. */
  public PivotSubsystem() {
    M_pivot.configure(
      Configs.PivotSubsystem.M_pivotConfig,
      ResetMode.kResetSafeParameters,
      PersistMode.kPersistParameters);

    M_pivotEncoder.setPosition(0);
  }


  private void moveToSetpoint() {
    pivotClosedLoopController.setReference( M_pivotCurrentTarget, ControlType.kMAXMotionPositionControl);
   }


   private void zeroOnUserButton(){
    if (!wasResetByButton && RobotController.getUserButton()){
      wasResetByButton = true;
      M_pivotEncoder.setPosition(0);
     
     
    } else if (!RobotController.getUserButton()){
      wasResetByButton = false;
    }
}

public Command setPivotCommand(SetPivot setpoint) {
  return this.runOnce(
      () -> {
        switch (setpoint) {
        
          case Level1:
          M_pivotCurrentTarget = PivotSetPoints.Level1;
        
            break;
          case Home:
          M_pivotCurrentTarget = PivotSetPoints.Home;
        }
  });
}
  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    moveToSetpoint();
    zeroOnUserButton();



    SmartDashboard.putNumber("PivotTarget Position", M_pivotCurrentTarget);
    SmartDashboard.putNumber("Elevator Actual Position", M_pivotEncoder.getPosition());
  }
}
