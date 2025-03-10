// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.coralIntakeCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CoralIntakeSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class CoralIntakeForTimeCmd extends Command {

  private final CoralIntakeSubsystem s_CoralIntakeSubsystem;
  private double intakeSpeed;
  private double runTime; 
  private double startTime; 

  /** Creates a new CoralIntakeCmd. */
  public CoralIntakeForTimeCmd(CoralIntakeSubsystem s_CoralIntakeSubsystem, double CoralIntakeSpeed, double time) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.s_CoralIntakeSubsystem = s_CoralIntakeSubsystem;
    this.intakeSpeed = CoralIntakeSpeed;
    this.runTime = time; 
    addRequirements(s_CoralIntakeSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    startTime = System.currentTimeMillis();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // Run the Coral Intake based on power
    s_CoralIntakeSubsystem.setCoralIntakeSpeed(intakeSpeed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    s_CoralIntakeSubsystem.setCoralIntakeSpeed(0);
 
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {

    if(System.currentTimeMillis() - startTime > runTime){
      return true; 
    }else{
      return false;
    } 
  }
  
}
