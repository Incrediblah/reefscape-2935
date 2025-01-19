// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Configs;
import frc.robot.RobotContainer;
import frc.robot.Constants.ArmSubsystemConstants;
import frc.robot.Constants.ArmSubsystemConstants.Arm1Setpoints;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkBase.ControlType;


import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class ArmSubsystem extends SubsystemBase {
  /** Creates a new ArmSubsystem. */
  public enum Setpoint {
    climbin,
    climbout;
   
  }
private SparkMax armMotor1 =
      new SparkMax(ArmSubsystemConstants.kArmMotor1CANID , MotorType.kBrushless);
  private SparkClosedLoopController armController1 = armMotor1.getClosedLoopController();
  private RelativeEncoder armEncoder1 = armMotor1.getEncoder();



  private boolean wasResetByButton = false;
  private boolean wasResetByLimit = false ;


   private double arm1CurrentTarget = Arm1Setpoints.climbin;
   


  public ArmSubsystem() {

    armMotor1.configure(
        Configs.ArmSubsystem.Arm_M1Config,
        ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);
    armEncoder1.setPosition(0);
         
  
    

   
  
    }


    private void movearm1ToSetpoint() {
      armController1.setReference(arm1CurrentTarget, ControlType.kMAXMotionPositionControl);
      // armController2.setReference(arm2CurrentTarget, ControlType.kMAXMotionPositionControl);
     }

     
    

    private void zeroOnUserButton(){
      if (!wasResetByButton && RobotController.getUserButton()){
        wasResetByButton = true;
        armEncoder1.setPosition(0);
       
      } else if (!RobotController.getUserButton()){
        wasResetByButton = false;
      }
  }

     
    
    

    public Command setSetpointCommand(Setpoint setpoint ) {
    return this.runOnce(
        () -> {
          switch (setpoint) {
            case climbout:
              arm1CurrentTarget = Arm1Setpoints.climbout;
              
              // arm2CurrentTarget = ArmSetpoints.climb2out;

           
              break;
            case climbin :
              arm1CurrentTarget = Arm1Setpoints.climbin;
              
              // arm2CurrentTarget = ArmSetpoints.climb2in;
            
             
          }
        });
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    movearm1ToSetpoint();
   
    zeroOnUserButton();

    SmartDashboard.putNumber("Coral/Arm/Target Position", arm1CurrentTarget);
    SmartDashboard.putNumber("Coral/Arm/Actual Position", armEncoder1.getPosition());

    
  }
}
