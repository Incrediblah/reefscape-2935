// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.AutoConstants;

public class VisionSubsystem extends SubsystemBase {

  private final NetworkTable m_limelightLeftTable; 
  private final NetworkTable m_limelightRightTable; 
   
    


  private final String VISION_PREFIX = "Vision/"; 
  private int lastSeenTagID = -1;

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
    SmartDashboard.putNumber("Last Seen Tag ID", lastSeenTagID); // Display the last seen tag ID
    SmartDashboard.putNumber("best Tag ID", getBestAprilTag()); // Display the last seen tag ID


    LastSeenTagID();
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
    NetworkTableInstance.getDefault().getTable("limelight-l").getEntry("pipeline").setNumber(pipelineNumber); 
  }

  public NetworkTableEntry getLeftPipeline(){
    return NetworkTableInstance.getDefault().getTable("limelight-l").getEntry("pipeline"); 
  }


  public void setLeftLED(int ledMode){
    NetworkTableInstance.getDefault().getTable("limelight-l").getEntry("ledMode").setNumber(ledMode); 
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

  public int getLeftAprilTagID() {    
    // Check if a valid target exists
    if (!limelightLeftTargetSeen()) {
        return -1; // No target detected, avoid false readings
    }

    NetworkTableEntry tidEntry = m_limelightLeftTable.getEntry("tid"); // "tid" contains AprilTag ID
    return (int) tidEntry.getDouble(-1);
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
    NetworkTableInstance.getDefault().getTable("limelight-r").getEntry("pipeline").setNumber(pipelineNumber); 
  }

  public NetworkTableEntry getRightPipeline(){
    return NetworkTableInstance.getDefault().getTable("limelight-l").getEntry("pipeline"); 
  }


  public void setRightLED(int ledMode){
    NetworkTableInstance.getDefault().getTable("limelight-r").getEntry("ledMode").setNumber(ledMode); 
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


  public int getRightAprilTagID() {    
    // Check if a valid target exists
    if (!limelightRightTargetSeen()) {
        return -1; // No target detected, avoid false readings
    }

    NetworkTableEntry tidEntry = m_limelightRightTable.getEntry("tid"); // "tid" contains AprilTag ID
    return (int) tidEntry.getDouble(-1);
  }

  // best tag 
  public int getBestAprilTag() {

    if (!limelightLeftTargetSeen() && !limelightRightTargetSeen()) {
        return -1; // No tag detected
    } else if (limelightLeftTargetSeen() && !limelightRightTargetSeen()) {
        return getLeftAprilTagID(); 
    } else if (!limelightLeftTargetSeen() && limelightRightTargetSeen()) {
        return getRightAprilTagID(); 
    } else {
        if (getLeftTa() > getRightTa()) {
            return getLeftAprilTagID();
        } else {
            return getRightAprilTagID();
        }
    }
  }

  public void LastSeenTagID() {
    int currentTagID = getBestAprilTag(); 
    if (currentTagID != -1) {
        lastSeenTagID = currentTagID;  // Update the stored last seen tag ID
    }
  }

  // Get the last seen tag ID
  public int getLastSeenTagID() {
      return lastSeenTagID; // Return the stored last seen tag ID
  }
    // return 0;


  // turn angle 

  public double getReefAngleForTag(int tagID, String mode) {

      if(mode == AutoConstants.teleMode){
        switch (tagID) {
          case 6: return 300;
          case 7: return 0;
          case 8: return 60;
          case 9: return 120;
          case 10: return 180;
          case 11: return 240;
          case 17: return 60;
          case 18: return 0;
          case 19: return 300;
          case 20: return 240;
          case 21: return 180;
          case 22: return 120;
          case 1: return 300; 
          case 2: return 60; 
          default: return 0; // If no valid tag, assume no turn needed
        }
      }else if(mode == AutoConstants.autoMode){
        switch (tagID) {
          case 6: return 120;  // 120 → 300
          case 7: return 180;    // 180 → 0
          case 8: return -120;   // -120 → 60
          case 9: return -60;  // -60 → 120
          case 10: return 0; // 0 → 180
          case 11: return 60; // 60 → 240
          case 17: return -120;  // -120 → 60
          case 18: return 180;   // 180 → 0
          case 19: return 120; // 120 → 300
          case 20: return 60; // 60 → 240
          case 21: return 0; // 0 → 180
          case 22: return -60; // -60 → 120
          case 1: return 120;  // 120 → 300
          case 2: return -120;   // -120 → 60
          default: return 0; // If no valid tag, assume robot is flipped
        }
      }else{
        return 0; 
      }

    }







    /// WORKING WITH SPECIFIC TAGS: 
    /// 
   
  public double getLeftSpecificTagTx(int desiredTagID) {
    if (!limelightLeftTargetSeen()) {
        return 0.0; // No tag detected
    }

    // Left Limelight - Loop through tags
    double[] leftTagIDs = m_limelightLeftTable.getEntry("tid").getDoubleArray(new double[0]);
    double[] leftTxValues = m_limelightLeftTable.getEntry("tx").getDoubleArray(new double[0]);

    for (int i = 0; i < leftTagIDs.length; i++) {
        if ((int) leftTagIDs[i] == desiredTagID) {
            return leftTxValues[i]; // Return the TX for the matched tag
        }
    }

    return 0.0; // Default if tag not found
  }



  public double getRightSpecificTagTx(int desiredTagID) {
    if (!limelightRightTargetSeen()) {
        return 0.0; // No tag detected
    }

    // Left Limelight - Loop through tags
    double[] rightTagIDs = m_limelightRightTable.getEntry("tid").getDoubleArray(new double[0]);
    double[] rightTxValues = m_limelightRightTable.getEntry("tx").getDoubleArray(new double[0]);

    for (int i = 0; i < rightTagIDs.length; i++) {
        if ((int) rightTagIDs[i] == desiredTagID) {
            return rightTxValues[i]; // Return the TX for the matched tag
        }
    }

    return 0.0; // Default if tag not found
  }


  public double getLeftSpecificTagTy(int desiredTagID) {
    if (!limelightLeftTargetSeen()) {
        return 0.0; // No tag detected
    }

    // Left Limelight - Loop through tags
    double[] leftTagIDs = m_limelightLeftTable.getEntry("tid").getDoubleArray(new double[0]);
    double[] leftTyValues = m_limelightLeftTable.getEntry("ty").getDoubleArray(new double[0]);

    for (int i = 0; i < leftTagIDs.length; i++) {
        if ((int) leftTagIDs[i] == desiredTagID) {
            return leftTyValues[i]; // Return the TX for the matched tag
        }
    }

    return 0.0; // Default if tag not found
  }



  public double getRightSpecificTagTy(int desiredTagID) {
    if (!limelightRightTargetSeen()) {
        return 0.0; // No tag detected
    }

    // Left Limelight - Loop through tags
    double[] rightTagIDs = m_limelightRightTable.getEntry("tid").getDoubleArray(new double[0]);
    double[] rightTyValues = m_limelightRightTable.getEntry("ty").getDoubleArray(new double[0]);

    for (int i = 0; i < rightTagIDs.length; i++) {
        if ((int) rightTagIDs[i] == desiredTagID) {
            return rightTyValues[i]; // Return the TX for the matched tag
        }
    }

    return 0.0; // Default if tag not found
  }



  
}

