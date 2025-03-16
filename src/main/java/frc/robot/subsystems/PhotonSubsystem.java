package frc.robot.subsystems;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;
import org.photonvision.EstimatedRobotPose;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.AutoConstants;

import java.util.Optional;

public class PhotonSubsystem extends SubsystemBase {
    private PhotonCamera camera = new PhotonCamera("USB_Camera"); // Replace with actual camera name

    private int lastSeenAprilTag = -1; // Stores last valid tag ID

    public PhotonSubsystem() {}

    public boolean hasTargets() {
        return camera.getLatestResult().hasTargets();
    }

    public PhotonTrackedTarget getBestTarget() {
        PhotonPipelineResult result = camera.getLatestResult();
        return result.hasTargets() ? result.getBestTarget() : null;
    }

    public double getYawToBestTarget() {
        PhotonTrackedTarget target = getBestTarget();
        return (target != null) ? target.getYaw() : 0.0;
    }

    public double getPitchToBestTarget() {
      PhotonTrackedTarget target = getBestTarget();
      return (target != null) ? target.getPitch() : 0.0;
  }

    public int getBestAprilTagID() {
        PhotonTrackedTarget target = getBestTarget();
        return (target != null) ? target.getFiducialId() : -1;
    }


    public PhotonTrackedTarget getSpecificTarget(int tagId){
        
        PhotonPipelineResult result = camera.getLatestResult(); 

        if(!result.hasTargets()){
            return null; 
        }


        for(PhotonTrackedTarget target : result.getTargets()){
            if (target.getFiducialId() == tagId){
                return target; 
            }
        }

        return null; 
    }

    public double getYawToSpecificTarget(int tagId) {
        PhotonTrackedTarget target = getSpecificTarget(tagId);
        return (target != null) ? target.getYaw() : 0.0;
    }

    public double getPitchToSpecific(int tagId) {
        PhotonTrackedTarget target = getSpecificTarget(tagId);
        return (target != null) ? target.getPitch() : 0.0;
    }


    public void updateLastSeenAprilTag() {
        int tagID = getBestAprilTagID();
        if (tagID != -1) {
            lastSeenAprilTag = tagID;
        }
    }

    public int getLastSeenAprilTag() {
        return lastSeenAprilTag;
    }

    public double getFeederAngleForTag(int tagID, String mode) {
        if(mode == AutoConstants.teleMode){
            switch (tagID) {
                case 1: return -126; 
                case 2: return 126; 
                case 12: return 126; 
                case 13: return -126; 
                default: return 0; // If no valid tag, assume no turn needed
            }
        }
          else if(mode == AutoConstants.autoMode){
            switch (tagID) {
                case 1: return 126; 
                case 2: return -126; 
                case 12: return -126; 
                case 13: return 126; 
                default: return 0; 
            }
        }
         else{
            return 0; 
        }
    }


    @Override
    public void periodic() {
        updateLastSeenAprilTag(); // Update last seen tag continuously

        SmartDashboard.putNumber("Photon TX", getYawToBestTarget()); 
        SmartDashboard.putNumber("Photon TY", getPitchToBestTarget()); 
        SmartDashboard.putBoolean("Photon Has Target", hasTargets()); 
        SmartDashboard.putNumber("Best Target ID", getBestAprilTagID()); 
    }
}
