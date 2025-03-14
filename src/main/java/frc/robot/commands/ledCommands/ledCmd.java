// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.ledCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.LedConstants;
import frc.robot.Constants.StatusVariables;
import frc.robot.subsystems.LedSubsystem;

public class ledCmd extends Command {


  private LedSubsystem LED_SUBSYSTEM; 
  private int red, green, blue; 

  /** Creates a new ledCommand. */
  public ledCmd(LedSubsystem light) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.LED_SUBSYSTEM = light; 

    addRequirements(LED_SUBSYSTEM);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    LED_SUBSYSTEM.setOneColour(0, 0, 0);
    red = 0; 
    green = 0; 
    blue = 0; 
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    if(StatusVariables.hasCoral){
      if(StatusVariables.isLinedUpToReef){
        red = LedConstants.greenColourCode[0]; 
        green = LedConstants.greenColourCode[1]; 
        blue = LedConstants.greenColourCode[2];  
      }else{
        red = LedConstants.purpleColourCode[0]; 
        green = LedConstants.purpleColourCode[1]; 
        blue = LedConstants.purpleColourCode[2];  
      }
    }else{
      red = LedConstants.whiteColourCode[0]; 
      green = LedConstants.whiteColourCode[1]; 
      blue = LedConstants.whiteColourCode[2]; 
    }
   

    LED_SUBSYSTEM.setOneColour(red, blue, green); 

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}