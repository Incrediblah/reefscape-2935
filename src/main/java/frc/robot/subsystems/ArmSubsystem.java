// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Configs;
import frc.robot.Constants.ArmConstants;

public class ArmSubsystem extends SubsystemBase {

  private SparkMax m_arm = new SparkMax(ArmConstants.kArmCanId, MotorType.kBrushless);
  private SparkClosedLoopController m_armController = m_arm.getClosedLoopController();
  private AbsoluteEncoder m_armEncoder = m_arm.getAbsoluteEncoder();

  /** Creates a new ArmSubsystem. */
  public ArmSubsystem() {

    m_arm.configure(
      Configs.ArmSubsystem.armConfig, 
      ResetMode.kResetSafeParameters, 
      PersistMode.kPersistParameters
    );

  }

  public void moveArmToPosition (double Setpoint) {
    m_armController.setReference(Setpoint, ControlType.kPosition);
  }

  public double getArmPosition () {
    return m_armEncoder.getPosition();
  }

  // public void resetArmEncoder () {
  //   m_armEncoder.setPosition(0);
  // }

  public void stopArmMotor () {
    m_arm.stopMotor();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Arm: ", m_armEncoder.getPosition());
  }
}
