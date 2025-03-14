// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
//import edu.wpi.first.wpilibj.DigitalInput;
//import edu.wpi.first.wpilibj2.command.SubsystemBase;
//import frc.robot.Constants.ConveyorConstants;
import frc.robot.Constants.LedConstants;

public class LedSubsystem extends SubsystemBase {
    private AddressableLED led1; 
    private AddressableLEDBuffer led1_Buffer; 

  /** Creates a new LedSubsystem. */
  public LedSubsystem() {
      led1 = new AddressableLED(LedConstants.ledPort);
  
      led1_Buffer = new AddressableLEDBuffer(LedConstants.ledLength); 
  
  
      led1.setLength(led1_Buffer.getLength());
      
      led1.setData(led1_Buffer);
    
      led1.start(); 
  }

  @Override
  public void periodic() {
    {
    
        
      
      }
    // This method will be called once per scheduler run
  }

  public void setOneColour(int r, int g, int b){
    for (var i = 0; i < led1_Buffer.getLength(); i++) {
      // Sets the specified LED to the RGB values for red
      led1_Buffer.setRGB(i, r, g, b);
    }
    
    led1.setData(led1_Buffer);
  }
}