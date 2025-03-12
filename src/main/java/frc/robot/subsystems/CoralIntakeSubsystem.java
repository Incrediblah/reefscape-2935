// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Configs;
import frc.robot.Constants.CoralIntakeConstants;

public class CoralIntakeSubsystem extends SubsystemBase {

  // Setup coral motor with SparkMax
  private SparkMax m_CoralIntake = new SparkMax(CoralIntakeConstants.kCoralIntakeCanId, MotorType.kBrushless);

  /** Creates a new CoralIntakeSubsystem. */
  public CoralIntakeSubsystem() {

    m_CoralIntake.configure(
      Configs.CoralIntakeSubsystem.coralIntakeConfig, 
      ResetMode.kResetSafeParameters, 
      PersistMode.kPersistParameters
    );

  }

  // This commands sets the output power of the motor between 0 and 1
  public void setCoralIntakeSpeed(double CoralIntakeSpeed) {
    m_CoralIntake.set(CoralIntakeSpeed);
  }

  // This command gets the output current of the Spark while it is running
  public double getCoralIntakeCurrentDraw() {
    return m_CoralIntake.getOutputCurrent();
  }


  // This command stops the intake motors
  public void stopCoralIntake() {
    m_CoralIntake.stopMotor();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    // Print out the current of the intake to the smartdashboard
    SmartDashboard.putNumber("Current ", getCoralIntakeCurrentDraw());
  }
}
