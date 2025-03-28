// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;
//import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Configs;
import frc.robot.Constants.ClimbConstants;

public class ClimbSubsystem extends SubsystemBase {

  private SparkMax m_climb = new SparkMax(ClimbConstants.kClimbCanId, MotorType.kBrushless);
  private SparkClosedLoopController m_climbController = m_climb.getClosedLoopController();
  private RelativeEncoder m_climbEncoder = m_climb.getEncoder();

  /** Creates a new ClimbSubsystem. */
  public ClimbSubsystem() {

    m_climb.configure(
      Configs.Climbsubsystem.climbConfig, 
      ResetMode.kResetSafeParameters, 
      PersistMode.kPersistParameters
    );
    m_climbEncoder.setPosition(0);

  }

  public void moveClimbToPosition (double Setpoint) {
    m_climbController.setReference(Setpoint, ControlType.kPosition);
  }

  public double getClimbPosition () {
    return m_climbEncoder.getPosition();
  }

  public void resetClimbEncoder () {
    m_climbEncoder.setPosition(0);
  }

  public void setClimbSpeed(double climbSpeed) {
    m_climb.set(climbSpeed);
  }

  public void stopClimbMotor () {
    m_climb.stopMotor();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Climb: ", m_climbEncoder.getPosition());
  }
}
