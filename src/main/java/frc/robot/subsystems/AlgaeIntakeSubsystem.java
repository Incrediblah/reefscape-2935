// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;
//import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Configs;
import frc.robot.Constants.AlgaeIntakeConstants;
import frc.robot.Constants.AlgaeIntakeConstants;
import frc.robot.Constants.StatusVariables;

public class AlgaeIntakeSubsystem extends SubsystemBase {

  // Setup coral motor with SparkMax
  private SparkFlex m_AlgaeIntake = new SparkFlex(AlgaeIntakeConstants.kAlgaeIntakeCanId, MotorType.kBrushless);

  
  
  /** Creates a new AlgaeIntakeSubsystem. */
  public AlgaeIntakeSubsystem() {

    m_AlgaeIntake.configure(
      Configs.AlgaeIntakeSubsystem.algaeIntakeConfig, 
      ResetMode.kResetSafeParameters, 
      PersistMode.kPersistParameters
    );

    
  }

  // This commands sets the output power of the motor between 0 and 1
  public void setAlgaeIntakeSpeed(double AlgaeIntakeSpeed) {
    m_AlgaeIntake.set(AlgaeIntakeSpeed);
  }

 

  // This command gets the output current of the Spark while it is running
  public double getAlgaeIntakeCurrentDraw() {
    return m_AlgaeIntake.getOutputCurrent();
  }


  // This command stops the intake motors
  public void stopAlgaeIntake() {
    m_AlgaeIntake.stopMotor();
  }

  
  
  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    // Print out the current of the intake to the smartdashboard
    SmartDashboard.putNumber("Current ", getAlgaeIntakeCurrentDraw());
   
  }
}
