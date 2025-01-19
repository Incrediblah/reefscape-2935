// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Configs;
import frc.robot.Constants.IntakeSubsystemConstants;
import frc.robot.Constants.IntakeSubsystemConstants.IntakeSpeeds;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.sim.SparkFlexSim;
import com.revrobotics.sim.SparkLimitSwitchSim;
import com.revrobotics.sim.SparkMaxSim;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

public class IntakeSubsystem extends SubsystemBase {
  private SparkMax intakeMotor =
      new SparkMax(IntakeSubsystemConstants.kIntakeMotorCanId, MotorType.kBrushless);
  /** Creates a new IntakeSubsystem. */
  public IntakeSubsystem() {

     intakeMotor.configure(
        Configs.IntakeSubsystem.intakeConfig,
        ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);


  }

  private void setIntakePower(double power) {
    intakeMotor.set(power);
  }


    public Command fowardIntakeCommand() {
    return this.startEnd(
        () -> this.setIntakePower(IntakeSpeeds.kForward), () -> this.setIntakePower(0.0));
  }


  public Command reverseIntakeCommand() {
    return this.startEnd(
        () -> this.setIntakePower(IntakeSpeeds.kReverse), () -> this.setIntakePower(0.0));
  }

  public Command noIntakeCommand() {
    return this.startEnd(
        () -> this.setIntakePower(IntakeSpeeds.kZero), () -> this.setIntakePower(0.0));
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
