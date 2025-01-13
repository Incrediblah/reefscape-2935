// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import frc.robot.Constants.ArmMotorConstants;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class ArmSubsystem extends SubsystemBase {
  private SparkMax arm1 = new SparkMax(ArmMotorConstants.kArmMotor1CANID, MotorType.kBrushless);
  private SparkMax arm2 = new SparkMax(ArmMotorConstants.kArmMotor2CANID, MotorType.kBrushless);

  private double arm1position = 0;



 private SparkMaxConfig config1 = new SparkMaxConfig();
  private SparkMaxConfig config2 = new SparkMaxConfig();
  /** Creates a new ArmSubsystem. */
  public ArmSubsystem() {

    config1
    .inverted(true)
    .idleMode(IdleMode.kBrake);
config1.encoder
    .positionConversionFactor(1000)
    .velocityConversionFactor(1000);
config1.closedLoop
    .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
    .pid(1.0, 0.0, 0.0);
    
arm1.configure(config1, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);



config2
.inverted(true)
.idleMode(IdleMode.kBrake);
config2.encoder
.positionConversionFactor(1000)
.velocityConversionFactor(1000);
config2.closedLoop
.feedbackSensor(FeedbackSensor.kPrimaryEncoder)
.pid(1.0, 0.0, 0.0);

arm2.configure(config2, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    arm1.set(arm1position);
    double currentAngle = getCurrentAngle();
    SmartDashboard.putNumber("Arm Current Angle", currentAngle);
    //arm2.set(targetPosition);
  }

  public void setarm1Position(double targetAngle) {
    // Convert target angle to encoder counts (assuming 1000 counts = 360 degrees)
    // Example: if the targetAngle is 90 degrees, targetPosition would be 250
    // 360 degrees -> 1000 encoder counts, so 90 degrees -> 250 encoder counts
   arm1position = targetAngle * (1000.0 / 360.0); // Converts angle to encoder counts
  }

  public double getCurrentAngle() {
    // Get the current encoder position from arm1 (assuming both motors are synchronized)
    double encoderPosition = arm1.getEncoder().getPosition();
    // Convert encoder counts to degrees
    return encoderPosition * (360.0 / 1000.0); // Converts encoder counts to degrees
  }

  public double getCurrentPosition() {
    return arm1.getEncoder().getPosition();
  }

  
}
