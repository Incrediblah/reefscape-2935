// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class VisionSubsystem extends SubsystemBase {

  private final NetworkTable m_limelightLeftTable; 
  private final NetworkTable m_limelightRightTable; 


  private final String VISION_PREFIX = "Vision/"; 

  /** Creates a new VisionSubsystem. */
  public VisionSubsystem() {
    m_limelightLeftTable = NetworkTableInstance.getDefault().getTable("limelight-l"); 
    m_limelightRightTable = NetworkTableInstance.getDefault().getTable("limelight-r"); 
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putBoolean(VISION_PREFIX + "LIMELIGHT LEFT TARGET SEEN", limelightLeftTargetSeen()); 
    SmartDashboard.putNumber(VISION_PREFIX + "LIMELIGHT LEFT TX", getLeftTx());
    SmartDashboard.putNumber(VISION_PREFIX + "LIMELIGHT LEFT TY", getLeftTy());
    SmartDashboard.putNumber(VISION_PREFIX + "LIMELIGHT LEFT TA", getLeftTa());
    SmartDashboard.putNumber(VISION_PREFIX + "LIMELIGHT LEFT TV", getLeftTv());

    SmartDashboard.putBoolean(VISION_PREFIX + "LIMELIGHT RIGHT TARGET SEEN", limelightRightTargetSeen()); 
    SmartDashboard.putNumber(VISION_PREFIX + "LIMELIGHT RIGHT TX", getRightTx());
    SmartDashboard.putNumber(VISION_PREFIX + "LIMELIGHT RIGHT TY", getRightTy());
    SmartDashboard.putNumber(VISION_PREFIX + "LIMELIGHT RIGHT TA", getRightTa());
    SmartDashboard.putNumber(VISION_PREFIX + "LIMELIGHT RIGHT TV", getRightTv());
  }
 
  
  // LEFT SIDE LIMELIGHT

  public NetworkTable limelightLeftTableRead(){
    return m_limelightLeftTable; 
  }

  public double getLeftTx(){
    return m_limelightLeftTable.getEntry("tx").getDouble(0); 
  }

  public double getLeftTy(){
    return m_limelightLeftTable.getEntry("ty").getDouble(0); 
  }

  
  public double getLeftTa(){
    return m_limelightLeftTable.getEntry("ta").getDouble(0); 
  }

  public double getLeftTv(){
    return m_limelightLeftTable.getEntry("tv").getDouble(0); 
  }

  public double specificLeftTx(int index){
    return m_limelightLeftTable.getEntry("tx" + index).getDouble(0); 
  }

  public void setLeftPipeline(int pipelineNumber){
    NetworkTableInstance.getDefault().getTable("limelight-left").getEntry("pipeline").setNumber(pipelineNumber); 
  }

  public void setLeftLED(int ledMode){
    NetworkTableInstance.getDefault().getTable("limelight-left").getEntry("ledMode").setNumber(ledMode); 
  }

  public int numberOfLeftTargets(){
    double tvValues = m_limelightLeftTable.getEntry("tv").getDouble(0);
    int tvVal = (int)tvValues; 
    return tvVal; 
  }

  public double targetLeftArea(int index){
    return m_limelightLeftTable.getEntry("ta" + index).getDouble(0); 
  }

  public boolean limelightLeftTargetSeen(){
    if(numberOfLeftTargets() > 0){
      return true; 
    }

    else{
      return false;
    }
  }


  // RIGHT SIDE LIMELIGHT

  public NetworkTable limelightRightTableRead(){
    return m_limelightRightTable; 
  }

  public double getRightTx(){
    return m_limelightRightTable.getEntry("tx").getDouble(0); 
  }

  public double getRightTy(){
    return m_limelightRightTable.getEntry("ty").getDouble(0); 
  }

  
  public double getRightTa(){
    return m_limelightRightTable.getEntry("ta").getDouble(0); 
  }

  public double getRightTv(){
    return m_limelightRightTable.getEntry("tv").getDouble(0); 
  }

  public double specificRightTx(int index){
    return m_limelightRightTable.getEntry("tx" + index).getDouble(0); 
  }

  public void setRightPipeline(int pipelineNumber){
    NetworkTableInstance.getDefault().getTable("limelight-right").getEntry("pipeline").setNumber(pipelineNumber); 
  }

  public void setRightLED(int ledMode){
    NetworkTableInstance.getDefault().getTable("limelight-right").getEntry("ledMode").setNumber(ledMode); 
  }

  public int numberOfRightTargets(){
    double tvValues = m_limelightRightTable.getEntry("tv").getDouble(0);
    int tvVal = (int)tvValues; 
    return tvVal; 
  }

  public double targetRightArea(int index){
    return m_limelightRightTable.getEntry("ta" + index).getDouble(0); 
  }

  public boolean limelightRightTargetSeen(){
    if(numberOfRightTargets() > 0){
      return true; 
    }

    else{
      return false;
    }
  }

}
